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
import uk.gov.justice.digital.oasys.api.AssessmentType
import uk.gov.justice.digital.oasys.api.ErrorDetailsDto
import uk.gov.justice.digital.oasys.api.ErrorResponse
import uk.gov.justice.digital.oasys.api.PermissionsDetailDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.RoleNames
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
        oasysSetPk,
        null,
        emptySet()
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
        oasysSetPk,
        null,
        emptySet()
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
        oasysSetPk,
        null,
        emptySet()
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
  fun `user can create offender assessment`() {
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("OFF_ASSESSMENT_CREATE"),
        area,
        offenderPk,
        oasysSetPk,
        "SHORT_FORM_PSR",
        emptySet()
      )
    )
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"OFF_ASSESSMENT_CREATE\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"offenderPK\":" + offenderPk + "" +
          ",\"oasysSetPk\":" + oasysSetPk + "" +
          ",\"assessmentType\":\"SHORT_FORM_PSR\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          "]" +
          "}}"
      )
    val permissions = PermissionsDto(
      userCode,
      setOf(Roles.OFF_ASSESSMENT_CREATE),
      area,
      offenderPk,
      oasysSetPk,
      AssessmentType.SHORT_FORM_PSR
    )
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
        oasysSetPk,
        null,
        emptySet()
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
      ", \"Failures\": [" +
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
        oasysSetPk,
        null,
        emptySet()
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
            message = "Assessment type missing"
          ),
          ErrorDetailsDto(
            failureType = "PARAMETER_CHECK",
            errorName = "missing_set_pk",
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

  @Test
  fun `User can get all permissions for RBAC_OTHER role`() {
    val roleNames = setOf(
      RoleNames.CREATE_BASIC_ASSESSMENT, RoleNames.CREATE_FULL_ASSESSMENT, RoleNames.CREATE_STANDARD_ASSESSMENT,
      RoleNames.EDIT_SARA, RoleNames.EDIT_SIGN_AND_LOCK_THE_ASSESSMENT, RoleNames.OPEN_OFFENDER_RECORD,
      RoleNames.OPEN_SARA, RoleNames.CREATE_OFFENDER, RoleNames.CREATE_RISK_OF_HARM_ASSESSMENT
    )
    given(
      permissionsRepository.getPermissions(
        userCode,
        setOf("RBAC_OTHER"),
        area,
        offenderPk,
        oasysSetPk,
        roleNames = roleNames?.map { it.rbacName }?.toSet()
      )
    )
      .willReturn(
        "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
          "{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Create Basic Assessment\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ", \"RBACName\":\"Create Full Assessment\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Create Standard Assessment\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Edit SARA\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Edit Sign And Lock The Assessment\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Open Offender Record\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Open SARA\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"YES\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Create Offender\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}" +
          ",{" +
          "\"checkCode\":\"RBAC_OTHER\"" +
          ",\"returnCode\":\"NO\"" +
          ",\"areaCode\":\"" + area + "\"" +
          ",\"RBACName\":\"Create Risk of Harm Assessment\"" +
          ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
          ",\"userCode\":\"" + userCode + "\"" +
          "}]}}"
      )

    val permissions = PermissionsDto(
      userCode,
      setOf(Roles.RBAC_OTHER),
      area,
      offenderPk,
      oasysSetPk,
      roleNames = roleNames
    )
    webTestClient.post().uri("/authorisation/permissions")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(permissions)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isForbidden
      .expectBody<ErrorResponse>()
      .consumeWith {
        val response = it.responseBody
        assertThat(response.developerMessage).isEqualTo("One of the permissions is Unauthorized")
        val permissionsDetails =
          objectMapper.readValue<PermissionsDetailsDto>(objectMapper.writeValueAsString(response.payload))
        assertThat(permissionsDetails).isEqualTo(allRbacOtherPermissionsResponse())
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

  fun allRbacOtherPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      permissions = listOf(
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.CREATE_BASIC_ASSESSMENT
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.CREATE_FULL_ASSESSMENT
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.CREATE_STANDARD_ASSESSMENT
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.EDIT_SARA
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.EDIT_SIGN_AND_LOCK_THE_ASSESSMENT
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.OPEN_OFFENDER_RECORD
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.OPEN_SARA
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.CREATE_OFFENDER
        ),
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = false,
          rbacName = RoleNames.CREATE_RISK_OF_HARM_ASSESSMENT
        )
      )
    )
  }
}
