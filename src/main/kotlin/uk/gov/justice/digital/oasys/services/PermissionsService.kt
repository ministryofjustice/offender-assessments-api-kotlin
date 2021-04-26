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
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissions
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissionsDetails
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import uk.gov.justice.digital.oasys.services.exceptions.InvalidOasysRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsBadRequestException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsChecksFailedException

@Service
class PermissionsService(private val permissionsRepository: PermissionsRepository) {

  private val objectMapper: ObjectMapper = jacksonObjectMapper()

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
    roleNames: RoleNames? = null
  ): PermissionsDetailsDto {
    val permissions = permissionsRepository.getPermissions(
      userCode,
      roleChecks.map { it.name }.toSet(),
      area,
      offenderPk,
      oasysSetPk
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
        val errorDetailsDto = oasysPermissionsResponse.toErrorDetailsDto()
        throw UserPermissionsBadRequestException("Operation check failed", errors = errorDetailsDto)
      }
      else -> {
        throw InvalidOasysRequestException("")
      }
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

fun OasysPermissions.toErrorDetailsDto(): List<ErrorDetailsDto> {
  return this.detail.errors.map {
    ErrorDetailsDto(
      it.failureType.name, it.errorName, it.oasysErrorLogId, it.message
    )
  }.toList()
}

fun OasysPermissionsDetails.toPermissionsDetails(): List<PermissionsDetailDto> {
  return this.results.map {
    PermissionsDetailDto(
      Roles.valueOf(it.checkCode),
      it.returnCode == "YES",
      it.returnMessage
    )
  }
}
