package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.stereotype.Repository

@Repository
class PermissionsRepository {
  fun getPermissions(
    userCode: String,
    roleChecks: List<String>,
    area: String,
    offenderPk: Long?,
    oasysSetPk: Long?
  ): Throwable {
    TODO("Not yet implemented")
  }
}
