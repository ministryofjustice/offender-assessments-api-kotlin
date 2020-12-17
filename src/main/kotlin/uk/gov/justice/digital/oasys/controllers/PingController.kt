package uk.gov.justice.digital.oasys.controllers

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

@Configuration
@WebEndpoint(id = "ping")
class PingController {
  @ReadOperation(produces = [MediaType.TEXT_PLAIN_VALUE])
  fun ping(): String {
    return "pong"
  }
}
