package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentType
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto
import uk.gov.justice.digital.oasys.api.RoleNames
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import kotlin.streams.toList

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
  ): PermissionsResponseDto {
    val permissions = permissionsRepository.getPermissions(
      userCode,
      roleChecks.stream().map { it.name }.toList(),
      area,
      offenderPk,
      oasysSetPk
    )
    return objectMapper.readValue(permissions)
  }
}
