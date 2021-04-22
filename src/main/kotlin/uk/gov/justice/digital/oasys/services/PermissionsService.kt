package uk.gov.justice.digital.oasys.services

import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentType
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto
import uk.gov.justice.digital.oasys.api.RoleNames
import uk.gov.justice.digital.oasys.api.Roles
import uk.gov.justice.digital.oasys.jpa.repositories.PermissionsRepository
import kotlin.streams.toList

@Service
class PermissionsService(private val permissionsRepository: PermissionsRepository) {
  fun getPermissions(
    userCode: String,
    roleChecks: Set<Roles>,
    area: String,
    offenderPk: Long?,
    oasysSetPk: Long?,
    assessmentType: AssessmentType?,
    roleNames: RoleNames?
  ): PermissionsResponseDto {
    throw permissionsRepository.getPermissions(
      userCode,
      roleChecks.stream().map { it.name }.toList(),
      area,
      offenderPk,
      oasysSetPk
    )
  }
}
