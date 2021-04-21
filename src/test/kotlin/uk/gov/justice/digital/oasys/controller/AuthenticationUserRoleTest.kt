package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.PermissionsDetail
import uk.gov.justice.digital.oasys.api.PermissionsDetails
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto
import uk.gov.justice.digital.oasys.api.Roles

@AutoConfigureWebTestClient
class AuthenticationUserRoleTest: IntegrationTest() {
  @Test
  fun `user can read an assessment`(){
    val userCode = "USERCODE"
    val areaCode = "AREA"
    val offenderPk= 123456L
    val oasysSetPk = 12L
    val permissions = PermissionsDto(setOf(Roles.ASSESSMENT_READ), areaCode, offenderPk, oasysSetPk)
    webTestClient.post().uri("/authentication/user/$userCode")
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

  fun readAssessmentPermissionsResponse(): PermissionsResponseDto{
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



}