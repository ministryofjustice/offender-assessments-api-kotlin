package uk.gov.justice.digital.oasys.api

data class PermissionsDetailsDto(
  val userCode: String,
  val offenderPk: Long? = null,
  val oasysSetPk: Long? = null,
  val permissions: List<PermissionsDetailDto>
)

data class PermissionsDetailDto(
  val checkCode: Roles,
  val authorised: Boolean,
  val returnMessage: String? = null
)

data class ErrorDetailsDto(
  val failureType: String,
  val errorName: String,
  val oasysErrorLogId: Int,
  val message: String
)
