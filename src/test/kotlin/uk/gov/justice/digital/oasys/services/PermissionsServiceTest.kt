package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.ErrorDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.RoleNames
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import uk.gov.justice.digital.oasys.services.exceptions.InvalidOasysRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsBadRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsChecksFailedException

@ExtendWith(MockKExtension::class)
@DisplayName("Permissions Service Tests")
class PermissionsServiceTest {
  private val userCode = "USER_CODE"
  private val area = "AREA"
  private val offenderPk = 123456L
  private val oasysSetPk = 12L
  private val permissionsRepository: PermissionsRepository = mockk()
  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  private val service = PermissionsService(permissionsRepository)

  @Test
  fun `get permissions with empty roleChecks throws Exception`() {
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        setOf(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("roleChecks should not be empty for user with code $userCode, area $area")
  }

  @Test
  fun `user can read an assessment`() {
    val permissions = "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
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
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns permissions

    val permissionsDetails = service.getPermissions(userCode, roleChecks, area, offenderPk, oasysSetPk)

    assertThat(permissionsDetails).isEqualTo(
      readAssessmentPermissionsResponse()
    )
  }

  @Test
  fun `user does not have permissions to read an assessment`() {
    val permissions = "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
      "{" +
      "\"checkCode\":\"ASSESSMENT_READ\"" +
      ",\"returnCode\":\"NO\"" +
      ",\"offenderPK\":" + offenderPk + "" +
      ",\"oasysSetPk\":" + oasysSetPk + "" +
      ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
      ",\"userCode\":\"" + userCode + "\"" +
      ",\"returnMessage\":\"Assessment is read-only\"" +
      "}" +
      "]" +
      "}}"
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns permissions

    val exception = assertThrows<UserPermissionsChecksFailedException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    val permissionsDetails =
      objectMapper.readValue<PermissionsDetailsDto>(objectMapper.writeValueAsString(exception.permissions))
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
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns errorResponse

    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk
      )
    }
    val errorDetails =
      objectMapper.readValue<List<ErrorDetailsDto>>(objectMapper.writeValueAsString(exception.errors))
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

  @Test
  fun `user cant be found in OASys`() {
    val permissions = "{\"STATE\":\"USER_FAIL\",\"DETAIL\":{\"Results\":[]}}"
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns permissions

    val exception = assertThrows<InvalidOasysRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("User not found in OASys for user with code $userCode, area $area")
  }

  @Test
  fun `offender cant be found in OASys`() {
    val permissions = "{\"STATE\":\"OFFENDER_FAIL\",\"DETAIL\":{\"Results\":[]}}"
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns permissions

    val exception = assertThrows<InvalidOasysRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk
      )
    }
    assertThat(exception.message).isEqualTo("Offender not found in OASys for offender $offenderPk")
  }

  @Test
  fun `Assessment cant be found in OASys`() {
    val permissions = "{\"STATE\":\"OASYS_SET_FAIL\",\"DETAIL\":{\"Results\":[]}}"
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    } returns permissions

    val exception = assertThrows<InvalidOasysRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Assessment not found in OASys for oasys set pk $oasysSetPk")
  }

  @Test
  fun `At least one role name needed to get permissions for RBAC_OTHER role`() {
    val roleChecks = setOf(Roles.RBAC_OTHER)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("At least one RBAC name must be selected for user with code $userCode, area $area")
  }

  @Test
  fun `Read assessment requires offenderPk`() {
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        null,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter offenderPk")
  }

  @Test
  fun `Read assessment requires oasysSetPk`() {
    val roleChecks = setOf(Roles.ASSESSMENT_READ)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        null,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter oasysSetPk")
  }

  @Test
  fun `Edit assessment requires offenderPk`() {
    val roleChecks = setOf(Roles.ASSESSMENT_EDIT)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        null,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter offenderPk")
  }

  @Test
  fun `Create offender assessment requires offenderPk`() {
    val roleChecks = setOf(Roles.OFF_ASSESSMENT_CREATE)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        null,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter offenderPk")
  }

  @Test
  fun `Create offender assessment assessmentType`() {
    val roleChecks = setOf(Roles.OFF_ASSESSMENT_CREATE)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        oasysSetPk,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter assessmentType")
  }

  @Test
  fun `Edit assessment requires oasysSetPk`() {
    val roleChecks = setOf(Roles.ASSESSMENT_EDIT)
    val exception = assertThrows<UserPermissionsBadRequestException> {
      service.getPermissions(
        userCode,
        roleChecks,
        area,
        offenderPk,
        null,
        null,
        emptySet()
      )
    }
    assertThat(exception.message).isEqualTo("Role checks $roleChecks, require parameter oasysSetPk")
  }

  @Test
  fun `user does have permissions to create basic assessment for rbac_other`() {
    val permissions = "{\"STATE\":\"SUCCESS\",\"DETAIL\":{\"Results\":[" +
      "{" +
      "\"checkCode\":\"RBAC_OTHER\"" +
      ",\"returnCode\":\"YES\"" +
      ",\"areaCode\":\"WWS\"" +
      ",\"RBACName\":\"Create Basic Assessment\"" +
      ",\"checkDate\":\"21\\/04\\/2021 12:21:35\"" +
      ",\"userCode\":\"" + userCode + "\"" +
      "}" +
      "]" +
      "}}"
    val roleChecks = setOf(Roles.RBAC_OTHER)
    every {
      permissionsRepository.getPermissions(
        userCode,
        roleChecks.map { it.name }.toSet(),
        area,
        offenderPk,
        oasysSetPk,
        null,
        setOf(RoleNames.CREATE_BASIC_ASSESSMENT.rbacName)
      )
    } returns permissions

    val permissionsDetails = service.getPermissions(
      userCode,
      roleChecks,
      area,
      offenderPk,
      oasysSetPk,
      null,
      setOf(RoleNames.CREATE_BASIC_ASSESSMENT)
    )

    assertThat(permissionsDetails).isEqualTo(
      createRbacAssessmentPermissionsResponse()
    )
  }

  private fun createRbacAssessmentPermissionsResponse(): PermissionsDetailsDto {
    return PermissionsDetailsDto(
      userCode = userCode,
      permissions = listOf(
        PermissionsDetailDto(
          checkCode = Roles.RBAC_OTHER,
          authorised = true,
          rbacName = RoleNames.CREATE_BASIC_ASSESSMENT
        )
      )
    )
  }

  private fun readAssessmentPermissionsResponse(): PermissionsDetailsDto {
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
}
