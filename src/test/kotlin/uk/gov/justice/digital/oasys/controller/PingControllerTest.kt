package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.controllers.PingController

@AutoConfigureWebTestClient
class PingControllerTest : IntegrationTest() {

  @Test
  fun ping() {
    assertThat(PingController().ping()).isEqualTo("pong")
  }

  @Test
  fun `ping integration`() {
    webTestClient.get().uri("ping")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<String>()
      .consumeWith { (assertThat(it.responseBody).isEqualTo("pong")) }
  }
}
