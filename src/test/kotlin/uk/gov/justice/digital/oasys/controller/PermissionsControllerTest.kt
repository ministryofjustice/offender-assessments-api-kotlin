package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.PermissionsDetail
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository

@AutoConfigureWebTestClient(timeout = "60000")
class PermissionsControllerTest : IntegrationTest() {
  private val userCode = "USER_CODE"
  private val area = "AREA"
  private val offenderPk = 123456L
  private val oasysSetPk = 12L

  @MockBean
  private lateinit var permissionsRepository: PermissionsRepository

  @Test
  fun `user can read an assessment`() {
    given(permissionsRepository.getPermissions(userCode, listOf("ASSESSMENT_READ"), area, offenderPk, oasysSetPk))
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"ASSESSMENT_READ\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          "]" +
          "}}"
      )
    val permissions = PermissionsDto(userCode, setOf(Roles.ASSESSMENT_READ), area, offenderPk, oasysSetPk)

    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsDetailsDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response).isEqualTo(readAssessmentPermissionsResponse())
      }
  }

  @Test
  fun `user can edit an assessment`() {
    given(permissionsRepository.getPermissions(userCode, listOf("ASSESSMENT_EDIT"), area, offenderPk, oasysSetPk))
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"ASSESSMENT_EDIT\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          "]" +
          "}}"
      )
    val permissions = PermissionsDto(userCode, setOf(Roles.ASSESSMENT_EDIT), area, offenderPk, oasysSetPk)
    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsDetailsDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response).isEqualTo(editAssessmentPermissionsResponse())
      }
  }

  @Test
  fun `user does not have permissions to read an assessment`() {
    given(permissionsRepository.getPermissions(userCode, listOf("ASSESSMENT_READ"), area, offenderPk, oasysSetPk))
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"ASSESSMENT_READ\"" +
          ",\"returnCode\":\"NO\"" +
          ",\"returnMessage\":\"Assessment is read-only\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          "]" +
          "}}"
      )
    val permissions = PermissionsDto(userCode, setOf(Roles.ASSESSMENT_READ), area, offenderPk, oasysSetPk)

    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isUnauthorized
      .expectBody<PermissionsDetailsDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response).isEqualTo(
          readAssessmentPermissionsResponse().copy(
            permissions = listOf(
              PermissionsDetail(
                checkCode = Roles.ASSESSMENT_READ,
                authorised = false,
                returnMessage = "Assessment is read-only"
              )
            )
          )
        )
      }
  }

  fun readAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      offenderPk = offenderPk,
      oasysSetPk = oasysSetPk,
      permissions = listOf(
        PermissionsDetail(
          checkCode = Roles.ASSESSMENT_READ,
          authorised = true
        )
      )
    )
  }

  fun editAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      offenderPk = offenderPk,
      oasysSetPk = oasysSetPk,
      permissions = listOf(
        PermissionsDetail(
          checkCode = Roles.ASSESSMENT_EDIT,
          authorised = true
        )
      )
    )
  }
}
