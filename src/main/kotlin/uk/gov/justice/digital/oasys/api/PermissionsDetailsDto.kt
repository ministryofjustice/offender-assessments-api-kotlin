package uk.gov.justice.digital.oasys.api

data class PermissionsDetailsDto(
  val userCode: String,
  val offenderPk: Long? = null,
  val oasysSetPk: Long? = null,
  val permissions: List<PermissionsDetail>
)

data class PermissionsDetail(
  val checkCode: Roles,
  val authorised: Boolean,
  val returnMessage: String? = null
)
