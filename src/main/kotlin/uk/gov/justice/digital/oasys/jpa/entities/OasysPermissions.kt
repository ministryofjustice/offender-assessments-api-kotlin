package uk.gov.justice.digital.oasys.jpa.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class OasysPermissions(
  @JsonProperty("STATE")
  val state: String,
  @JsonProperty("DETAIL")
  val detail: OasysPermissionsDetails
)

data class OasysPermissionsDetails(
  @JsonProperty("Results")
  val results: List<OasysPermissionsDetail> = emptyList(),
  @JsonProperty("Errors")
  val errors: List<OasysPermissionError> = emptyList()
)

data class OasysPermissionsDetail(
  val checkCode: String,
  val returnCode: String,
  val returnMessage: String? = null,
  val areaCode: String? = null,
  val rbacName: String? = null,
  val checkDate: String,
  val userCode: String,
  val offenderPK: Long? = null,
  val oasysSetPk: Long? = null
)

data class OasysPermissionError(
  val failureType: FailureType,
  val errorName: String,
  val oasysErrorLogId: Int,
  val message: String
)

enum class FailureType {
  PARAMETER_CHECK
}
