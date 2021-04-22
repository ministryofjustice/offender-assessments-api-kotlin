package uk.gov.justice.digital.oasys.api

data class PermissionsResponseDto(
  val state: String,
  val detail: PermissionsDetails
)

data class PermissionsDetails(
  val results: List<PermissionsDetail>
)

data class PermissionsDetail(
  val checkCode: Roles,
  val returnCode: String,
  val returnMessage: String? = null,
  val areaCode: String? = null,
  val rbacName: String? = null,
  val checkDate: String,
  val userCode: String,
  val offenderPK: Long? = null,
  val oasysSetPk: Long? = null
)
