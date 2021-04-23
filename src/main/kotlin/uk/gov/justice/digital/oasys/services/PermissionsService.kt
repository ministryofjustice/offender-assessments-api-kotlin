package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentType
import uk.gov.justice.digital.oasys.api.PermissionsDetail
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto
import uk.gov.justice.digital.oasys.api.RoleNames
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.entities.OasysPermissions
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import uk.gov.justice.digital.oasys.services.exceptions.InvalidOasysPermissionsException
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
    offenderPk: Long?,
    oasysSetPk: Long?,
    assessmentType: AssessmentType?,
    roleNames: RoleNames?
  ): PermissionsDetailsDto {
    val permissions = permissionsRepository.getPermissions(
      userCode,
      roleChecks.map { it.name }.toList(),
      area,
      offenderPk,
      oasysSetPk
    )
    val oasysPermissionsResponse = objectMapper.readValue<OasysPermissions>(permissions)
    when (oasysPermissionsResponse.state) {
      "SUCCESS" -> {}
      "USER_FAIL"-> {}
      else -> { throw NotImplementedError()}


    }
    //TODO understand errors better
    //TODO 403 error if any of the permissions is not granted
    if (oasysPermissionsResponse.detail.results.isEmpty()) {
      throw InvalidOasysPermissions("Permissions not found for user with code $userCode and roleChecks ${roleChecks.joinToString()}")
    }
    val firstResult = oasysPermissionsResponse.detail.results[0]
    val permissionDetails = oasysPermissionsResponse.detail.results.map {
      PermissionsDetail(
        Roles.valueOf(it.checkCode),
        it.returnCode == "YES",
        it.returnMessage
      )
    }
    return PermissionsDetailsDto(
      firstResult.userCode,
      firstResult.offenderPK,
      firstResult.oasysSetPk,
      permissionDetails
    )
  }
}
