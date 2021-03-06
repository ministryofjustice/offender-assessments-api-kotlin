package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentType
import uk.gov.justice.digital.oasys.api.ErrorDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.RoleNames
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissionError
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissions
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissionsDetails
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import uk.gov.justice.digital.oasys.services.PermissionsService.Companion.authorisedReturnCode
import uk.gov.justice.digital.oasys.services.exceptions.InvalidOasysRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsBadRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsChecksFailedException

@Service
class PermissionsService(private val permissionsRepository: PermissionsRepository) {

  companion object {
    val authorisedReturnCode = "YES"
    val readAndEditAssessmentRoles = setOf(Roles.ASSESSMENT_READ, Roles.ASSESSMENT_EDIT)
    val createOffenderAssessmentRole = setOf(Roles.OFF_ASSESSMENT_CREATE)
    val objectMapper: ObjectMapper = jacksonObjectMapper()
  }

  init {
    objectMapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }

  fun getPermissions(
    userCode: String,
    roleChecks: Set<Roles>,
    area: String,
    offenderPk: Long? = null,
    oasysSetPk: Long? = null,
    assessmentType: AssessmentType? = null,
    roleNames: Set<RoleNames>? = emptySet()
  ): PermissionsDetailsDto {
    validateInputsForRoleChecks(roleChecks, userCode, area, roleNames, offenderPk, oasysSetPk, assessmentType)
    val permissions = permissionsRepository.getPermissions(
      userCode,
      roleChecks.map { it.name }.toSet(),
      area,
      offenderPk,
      oasysSetPk,
      assessmentType?.name,
      roleNames?.map { it.rbacName }?.toSet()
    )
    val oasysPermissionsResponse = objectMapper.readValue<OasysPermissions>(permissions)
    when (oasysPermissionsResponse.state) {
      "SUCCESS" -> {
        val permissionsDetailsDto = oasysPermissionsResponse.toPermissionsDetailsDto()
        if (permissionsDetailsDto.permissions.any { !it.authorised }) {
          throw UserPermissionsChecksFailedException("One of the permissions is Unauthorized", permissionsDetailsDto)
        }
        return permissionsDetailsDto
      }
      "USER_FAIL" -> {
        throw InvalidOasysRequestException("User not found in OASys for user with code $userCode, area $area")
      }
      "OFFENDER_FAIL" -> {
        throw InvalidOasysRequestException("Offender not found in OASys for offender $offenderPk")
      }
      "OASYS_SET_FAIL" -> {
        throw InvalidOasysRequestException("Assessment not found in OASys for oasys set pk $oasysSetPk")
      }
      "OPERATION_CHECK_FAIL" -> {
        val errorDetailsDto = oasysPermissionsResponse.toErrorDetailsDtos()
        throw UserPermissionsBadRequestException("Operation check failed", errors = errorDetailsDto)
      }
      else -> {
        throw InvalidOasysRequestException("Unknown Exception with oasys response $oasysPermissionsResponse")
      }
    }
  }

  private fun validateInputsForRoleChecks(
    roleChecks: Set<Roles>,
    userCode: String,
    area: String,
    roleNames: Set<RoleNames>?,
    offenderPk: Long?,
    oasysSetPk: Long?,
    assessmentType: AssessmentType?
  ) {
    if (roleChecks.isEmpty()) {
      throw UserPermissionsBadRequestException("roleChecks should not be empty for user with code $userCode, area $area")
    }
    if (roleChecks.contains(Roles.RBAC_OTHER) && roleNames.isNullOrEmpty()) {
      throw UserPermissionsBadRequestException("At least one RBAC name must be selected for user with code $userCode, area $area")
    }
    if (offenderPk == null && roleChecks.any(readAndEditAssessmentRoles::contains)) {
      throw UserPermissionsBadRequestException("Role checks $roleChecks, require parameter offenderPk")
    }
    if (oasysSetPk == null && roleChecks.any(readAndEditAssessmentRoles::contains)) {
      throw UserPermissionsBadRequestException("Role checks $roleChecks, require parameter oasysSetPk")
    }
    if (offenderPk == null && roleChecks.any(createOffenderAssessmentRole::contains)) {
      throw UserPermissionsBadRequestException("Role checks $roleChecks, require parameter offenderPk")
    }
    if (assessmentType == null && roleChecks.any(createOffenderAssessmentRole::contains)) {
      throw UserPermissionsBadRequestException("Role checks $roleChecks, require parameter assessmentType")
    }
  }
}

fun OasysPermissions.toPermissionsDetailsDto(): PermissionsDetailsDto {
  val firstResult = this.detail.results[0]
  return PermissionsDetailsDto(
    firstResult.userCode,
    firstResult.offenderPK,
    firstResult.oasysSetPk,
    this.detail.toPermissionsDetails()
  )
}

fun OasysPermissions.toErrorDetailsDtos(): List<ErrorDetailsDto> {
  return this.detail.errors.map {
    it.toErrorDetailsDto()
  }.toList()
}

fun OasysPermissionError.toErrorDetailsDto(): ErrorDetailsDto {
  return ErrorDetailsDto(
    failureType.name, errorName, message
  )
}

fun OasysPermissionsDetails.toPermissionsDetails(): List<PermissionsDetailDto> {
  return this.results.map {
    PermissionsDetailDto(
      Roles.valueOf(it.checkCode),
      it.returnCode == authorisedReturnCode,
      it.returnMessage,
      it.rbacName?.let { rbacName -> RoleNames.findByRbacName(rbacName.trimStart()) }
    )
  }
}
