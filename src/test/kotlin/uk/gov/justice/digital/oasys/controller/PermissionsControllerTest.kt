package uk.gov.justice.digital.oasys.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.ErrorDetailsDto
import uk.gov.justice.digital.oasys.api.ErrorResponse
import uk.gov.justice.digital.oasys.api.PermissionsDetailDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository

@AutoConfigureWebTestClient
class PermissionsControllerTest : IntegrationTest() {
  private val userCode = "USER_CODE"
  private val area = "AREA"
  private val offenderPk = 123456L
  private val oasysSetPk = 12L
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  @MockBean
  private lateinit var permissionsRepository: PermissionsRepository

  @Test
  fun `user can read an assessment`() {
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("ASSESSMENT_READ"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
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
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("ASSESSMENT_EDIT"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
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
  fun `user can read and edit an assessment`() {
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("ASSESSMENT_READ", "ASSESSMENT_EDIT"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"ASSESSMENT_READ\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}," +
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
    val permissions =
      PermissionsDto(userCode, setOf(Roles.ASSESSMENT_READ, Roles.ASSESSMENT_EDIT), area, offenderPk, oasysSetPk)

    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsDetailsDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response).isEqualTo(readAndEditAssessmentPermissionsResponse())
      }
  }

  @Test
  fun `user can crete offender assessment`() {
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("OFF_ASSESSMENT_CREATE"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"OFF_ASSESSMENT_CREATE\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"assessmentType\":\"LONG_FORM_PSR\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          "]" +
          "}}"
      )
    val permissions = PermissionsDto(userCode, setOf(Roles.OFF_ASSESSMENT_CREATE), area, offenderPk, oasysSetPk)
    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<PermissionsDetailsDto>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response).isEqualTo(createOffenderAssessmentPermissionsResponse())
      }
  }

  @Test
  fun `user does not have permissions to read an assessment`() {
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("ASSESSMENT_READ"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
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
      .expectStatus().isForbidden
      .expectBody<ErrorResponse>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.status).isEqualTo(403)
        assertThat(response.developerMessage).isEqualTo("One of the permissions is Unauthorized")
        val permissionsDetails =
          objectMapper.readValue<PermissionsDetailsDto>(objectMapper.writeValueAsString(response.payload))
        assertThat(permissionsDetails).isEqualTo(
          readAssessmentPermissionsResponse().copy(
            permissions = listOf(
              PermissionsDetailDto(
                checkCode = Roles.ASSESSMENT_READ,
                authorised = false,
                returnMessage = "Assessment is read-only"
              )
            )
          )
        )
      }
  }

  @Test
  fun `Operation check failed and a list of errors is returned`() {
    val errorResponse = "{\"STATE\":\"OPERATION_CHECK_FAIL\"" +
      ",\"DETAIL\":{\"Results\":[]" +
      ", \"Errors\": [" +
      "{" +
      "\"failureType\":\"PARAMETER_CHECK\"," +
      "\"errorName\":\"missing_assessment_type\"," +
      "\"oasysErrorLogId\":863," +
      "\"message\":\"Assessment type missing\"" +
      " }," +
      "{" +
      "\"failureType\":\"PARAMETER_CHECK\"," +
      "\"errorName\":\"missing_set_pk\"," +
      "\"oasysErrorLogId\":862," +
      "\"message\":\"OASYS SET PKmissing\"" +
      "}" +
      "]}}"
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("ASSESSMENT_READ"),
        area,
        offenderPk,
        oasysSetPk
      )
    )
      .willReturn(errorResponse)

    val permissions = PermissionsDto(userCode, setOf(Roles.ASSESSMENT_READ), area, offenderPk, oasysSetPk)
    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isBadRequest
      .expectBody<ErrorResponse>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.status).isEqualTo(400)
        assertThat(response.developerMessage).isEqualTo("Operation check failed")
        val errorDetails =
          objectMapper.readValue<List<ErrorDetailsDto>>(objectMapper.writeValueAsString(response.payload))
        assertThat(errorDetails).containsExactlyInAnyOrder(
          ErrorDetailsDto(
            failureType = "PARAMETER_CHECK",
            errorName = "missing_assessment_type",
            oasysErrorLogId = 863,
            message = "Assessment type missing"
          ),
          ErrorDetailsDto(
            failureType = "PARAMETER_CHECK",
            errorName = "missing_set_pk",
            oasysErrorLogId = 862,
            message = "OASYS SET PKmissing"
          )
        )
      }
  }

  @Test
  fun `get permissions with empty roleChecks throws Exception`() {
    val permissions = PermissionsDto(userCode, setOf(), area, offenderPk, oasysSetPk)
    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isBadRequest
      .expectBody<ErrorResponse>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.status).isEqualTo(400)
        assertThat(response.developerMessage).isEqualTo("roleChecks should not be empty for user with code $userCode, area $area")
      }
  }

  fun readAndEditAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      offenderPk = offenderPk,
      oasysSetPk = oasysSetPk,
      permissions = listOf(
        PermissionsDetailDto(
          checkCode = Roles.ASSESSMENT_READ,
          authorised = true
        ),
        PermissionsDetailDto(
          checkCode = Roles.ASSESSMENT_EDIT,
          authorised = true
        )
      )
    )
  }

  fun createOffenderAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      offenderPk = offenderPk,
      oasysSetPk = oasysSetPk,
      permissions = listOf(
        PermissionsDetailDto(
          checkCode = Roles.OFF_ASSESSMENT_CREATE,
          authorised = true
        )
      )
    )
  }
  fun readAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      offenderPk = offenderPk,
      oasysSetPk = oasysSetPk,
      permissions = listOf(
        PermissionsDetailDto(
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
        PermissionsDetailDto(
          checkCode = Roles.ASSESSMENT_EDIT,
          authorised = true
        )
      )
    )
  }
}
