package uk.gov.justice.digital.oasys.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestData(excludeUris: String?) : HandlerInterceptor {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
    private val excludeUriRegex: Pattern = Pattern.compile(excludeUris)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute("startTime", LocalDateTime.now().toString())
        MDC.clear()
        MDC.put(CORRELATION_ID_HEADER, initialiseCorrelationId(request))
        MDC.put(USERNAME_HEADER, initialiseUserName(request))
        MDC.put(USER_ID_HEADER, initialiseUserId(request))

        if (excludeUriRegex.matcher(request.requestURI).matches()) {
            MDC.put(SKIP_LOGGING, "true")
        }
        if (log.isTraceEnabled && isLoggingAllowed) {
            log.trace("Request: ${request.method} ${request.requestURI}")
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        response.setHeader(USERNAME_HEADER, getUsername())
        response.setHeader(CORRELATION_ID_HEADER, getCorrelationId())

        val status = response.status
        val start = LocalDateTime.parse(
                request.getAttribute("startTime").toString())
        val duration = Duration.between(start, LocalDateTime.now()).toMillis()

        if (log.isTraceEnabled && isLoggingAllowed) {
            log.trace("Response: ${request.method} ${request.requestURI} - Status $status - Start ${start.format(formatter)}, Duration $duration ms")
        }

        MDC.put(REQUEST_DURATION, duration.toString())
        MDC.put(RESPONSE_STATUS, status.toString())
        MDC.clear()
    }

    private fun initialiseCorrelationId(request: HttpServletRequest): String {
        val correlationId: String? = request.getHeader(CORRELATION_ID_HEADER)
        return if (correlationId.isNullOrBlank()) UUID.randomUUID().toString() else correlationId
    }

    private fun initialiseUserName(request: HttpServletRequest): String {
        val username: String? = request.getHeader(USERNAME_HEADER)
        return if (username.isNullOrBlank()) ANONYMOUS else username
    }

    private fun initialiseUserId(request: HttpServletRequest): String? {
        val userId: String? = if (request.userPrincipal != null) request.userPrincipal.name else null
        return if (userId.isNullOrEmpty()) null else userId
    }

    private fun getCorrelationId(): String? {
        return MDC.get(CORRELATION_ID_HEADER)
    }

    private fun getUsername(): String? {
        return MDC.get(USERNAME_HEADER)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
        const val CORRELATION_ID_HEADER = "X-Correlation-Id"
        const val USERNAME_HEADER = "X-Auth-Username"
        private const val ANONYMOUS = "anonymous"
        const val SKIP_LOGGING = "skipLogging"
        const val REQUEST_DURATION = "duration"
        const val RESPONSE_STATUS = "status"
        const val USER_ID_HEADER = "userId"
        val isLoggingAllowed: Boolean = "true" != MDC.get(SKIP_LOGGING)
    }
}