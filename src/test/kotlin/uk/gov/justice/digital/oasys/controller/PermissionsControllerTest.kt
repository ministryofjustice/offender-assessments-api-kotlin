package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.PermissionsDetail
import uk.gov.justice.digital.oasys.api.PermissionsDetails
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto
import uk.gov.justice.digital.oasys.api.Roles

@AutoConfigureWebTestClient
class PermissionsControllerTest : IntegrationTest() {
  @Test
  fun `user can read an assessment`() {
    val permissions = PermissionsDto(setOf(Roles.ASSESSMENT_READ), "AREA", 123456L, 12L)
    webTestClient.post().uri("/authentication/user/USERCODE")
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsResponseDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.state).isEqualTo("SUCCESS")
        val result = response.detail.results[0]
        assertThat(result).isEqualTo(readAssessmentPermissionsResponse())
      }
  }

  @Test
  fun `user can edit an assessment`() {
    val permissions = PermissionsDto(setOf(Roles.ASSESSMENT_EDIT), "AREA", 123456L, 12L)
    webTestClient.post().uri("/authentication/user/USERCODE")
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsResponseDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.state).isEqualTo("SUCCESS")
        val result = response.detail.results[0]
        assertThat(result).isEqualTo(editAssessmentPermissionsResponse())
      }
  }

  fun readAssessmentPermissionsResponse(): PermissionsResponseDto {
    return PermissionsResponseDto(
      state = "SUCCESS",
      detail = PermissionsDetails(
        results = listOf(
          PermissionsDetail(
            checkCode = Roles.ASSESSMENT_READ,
            returnCode = "YES",
            offenderPK = 123456L,
            oasysSetPk = 12L,
            userCode = "USERCODE",
            checkDate = ""
          )
        )
      )
    )
  }

  fun editAssessmentPermissionsResponse(): PermissionsResponseDto {
    return PermissionsResponseDto(
      state = "SUCCESS",
      detail = PermissionsDetails(
        results = listOf(
          PermissionsDetail(
            checkCode = Roles.ASSESSMENT_EDIT,
            returnCode = "YES",
            offenderPK = 123456L,
            oasysSetPk = 12L,
            userCode = "USERCODE",
            checkDate = ""
          )
        )
      )
    )
  }
}
