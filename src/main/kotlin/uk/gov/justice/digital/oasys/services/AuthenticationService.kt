package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.microsoft.applicationinsights.TelemetryClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AuthenticationDto
import uk.gov.justice.digital.oasys.api.AuthorisationDto
import uk.gov.justice.digital.oasys.api.OffenderPermissionLevel
import uk.gov.justice.digital.oasys.api.OffenderPermissionResource
import uk.gov.justice.digital.oasys.api.RegionDto
import uk.gov.justice.digital.oasys.api.UserDto
import uk.gov.justice.digital.oasys.api.UserProfileDto
import uk.gov.justice.digital.oasys.jpa.entities.AuthenticationStatus
import uk.gov.justice.digital.oasys.jpa.entities.AuthorisationStatus
import uk.gov.justice.digital.oasys.jpa.entities.OasysUser
import uk.gov.justice.digital.oasys.jpa.repositories.AuthenticationRepository
import uk.gov.justice.digital.oasys.jpa.repositories.UserRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import uk.gov.justice.digital.oasys.services.exceptions.UserNotAuthorisedException
import java.io.IOException

@Service
class AuthenticationService(
  private var oasysUserRepository: UserRepository,
  private val oasysAuthenticationRepository: AuthenticationRepository,
  private val telemetryClient: TelemetryClient
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  init {
    objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
    objectMapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }

  @Cacheable("users")
  fun getUserByUserId(username: String?): UserProfileDto {
    if (username.isNullOrEmpty()) throw IllegalArgumentException("Username cannot be blank")
    val user = oasysUserRepository.findOasysUserByOasysUserCodeIgnoreCase(username)
      ?: throw EntityNotFoundException("User for username $username, not found")
    log.info("Found user with OASys username $username")
    return UserProfileDto.from(user, user.toRegions())
  }

  fun validateUserCredentials(username: String?, password: String?): AuthenticationDto {
    log.info("Attempting to authenticate user $username")
    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
      logAuthResult(username ?: "", false)
      throw UserNotAuthorisedException("User credentials cannot be empty")
    }

    val response = oasysAuthenticationRepository.validateCredentials(username, password)

    if (response != null) {
      try {
        val result: AuthenticationStatus = objectMapper.readValue(response)
        logAuthResult(username, result.isAuthenticated())
        if (!result.isAuthenticated()) throw UserNotAuthorisedException("Invalid username or password")
        return AuthenticationDto(result.isAuthenticated())
      } catch (e: IOException) {
        logAuthResult(username, false)
        log.error("Failed to parse OASys response for user $username response: $response")
        throw UserNotAuthorisedException("Failed to parse authentication check result from OASys")
      }
    }
    log.info("No response from OASys authentication function for user, $username")
    throw UserNotAuthorisedException("No response from OASys authentication function for user, $username")
  }

  fun userCanAccessOffenderRecord(
    oasysUserCode: String?,
    offenderId: Long?,
    sessionId: Long?,
    resource: OffenderPermissionResource?
  ): AuthorisationDto {

    log.info("Attempting to authorise user $oasysUserCode for offender $offenderId")

    if (oasysUserCode.isNullOrEmpty() || offenderId == null || resource == null) {
      logAuthFailure(oasysUserCode, "Null input", offenderId)
      return AuthorisationDto(offenderPermissionLevel = OffenderPermissionLevel.UNAUTHORISED)
    }

    val session: Long = sessionId
      ?: oasysUserRepository.findCurrentUserSessionForOffender(offenderId, oasysUserCode) ?: 0

    val response = authoriseSentencePlan(oasysUserCode, offenderId, session)

    if (response != null) {
      try {
        val authStatus: AuthorisationStatus = objectMapper.readValue(response)

        when (authStatus.state) {
          AuthorisationStatus.AuthorisationState.READ -> {
            logAuthSuccess(
              oasysUserCode,
              OffenderPermissionLevel.READ_ONLY,
              OffenderPermissionResource.SENTENCE_PLAN,
              offenderId
            )
            return AuthorisationDto(
              oasysUserCode,
              offenderId,
              OffenderPermissionLevel.READ_ONLY,
              OffenderPermissionResource.SENTENCE_PLAN
            )
          }
          AuthorisationStatus.AuthorisationState.EDIT -> {
            logAuthSuccess(
              oasysUserCode,
              OffenderPermissionLevel.WRITE,
              OffenderPermissionResource.SENTENCE_PLAN,
              offenderId
            )
            return AuthorisationDto(
              oasysUserCode,
              offenderId,
              OffenderPermissionLevel.WRITE,
              OffenderPermissionResource.SENTENCE_PLAN
            )
          }
          AuthorisationStatus.AuthorisationState.NO_ACCESS -> {
            logAuthFailure(oasysUserCode, "Unauthorised", offenderId)
            return AuthorisationDto(
              oasysUserCode,
              offenderId,
              OffenderPermissionLevel.UNAUTHORISED,
              OffenderPermissionResource.SENTENCE_PLAN
            )
          }
          else -> {
            logAuthFailure(oasysUserCode, "Invalid OASys Response", offenderId)
            log.error(
              "Failed to authorise user $oasysUserCode for offender $offenderId with status ${authStatus.state}",
              oasysUserCode,
              offenderId,
              authStatus.state
            )
            return AuthorisationDto(
              oasysUserCode,
              offenderId,
              OffenderPermissionLevel.UNAUTHORISED,
              OffenderPermissionResource.SENTENCE_PLAN
            )
          }
        }
      } catch (e: IOException) {
        logAuthFailure(oasysUserCode, "Parse Error", offenderId)
        log.error("Failed to parse OASys response for user $oasysUserCode response: $response")
        return AuthorisationDto(offenderPermissionLevel = OffenderPermissionLevel.UNAUTHORISED)
      }
    }
    logAuthFailure(oasysUserCode, "No response from OASys", offenderId)
    return AuthorisationDto(
      oasysUserCode,
      offenderId,
      OffenderPermissionLevel.UNAUTHORISED,
      OffenderPermissionResource.SENTENCE_PLAN
    )
  }

  private fun authoriseSentencePlan(oasysUserId: String, offenderId: Long, sessionId: Long): String? {
    return oasysAuthenticationRepository.validateUserSentencePlanAccessWithSession(oasysUserId, offenderId, sessionId)
  }

  private fun logAuthResult(username: String?, success: Boolean) {
    val event = if (success) "OASysAuthenticationSuccess" else "OASysAuthenticationFailure"
    telemetryClient.trackEvent(event, mapOf("username" to username), null)
  }

  private fun logAuthSuccess(
    username: String?,
    permissionLevel: OffenderPermissionLevel,
    resource: OffenderPermissionResource,
    offenderId: Long
  ) {
    telemetryClient.trackEvent(
      "OASysAuthorisationSuccess",
      mapOf(
        "username" to username,
        "accessGranted" to permissionLevel.toString(),
        "resource" to resource.toString(),
        "offender" to offenderId.toString()
      ),
      null
    )
  }

  private fun logAuthFailure(username: String?, reason: String?, offenderId: Long?) {
    telemetryClient.trackEvent(
      "OASysAuthorisationFailure",
      mapOf(
        "username" to username,
        "reason" to reason,
        "offender" to offenderId.toString()
      ),
      null
    )
  }

  fun getUserCodeByEmail(email: String?): UserDto {
    if (email.isNullOrEmpty()) throw IllegalArgumentException("Email cannot be blank")
    val user = oasysUserRepository.findOasysUserByEmailAddressIgnoreCase(email)
      ?: throw EntityNotFoundException("User for email $email, not found")
    log.info("Found user with email $email")

    return UserDto.from(user)
  }

  fun OasysUser.toRegions(): Set<RegionDto> {
    return this.oasysUserCode?.let { oasysUserRepository.findCtAreasEstByUserCode(it) }
      ?.map { area -> RegionDto(area.name, area.code) }
      ?.toSet() ?: emptySet()
  }
}
