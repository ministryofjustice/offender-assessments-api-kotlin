package uk.gov.justice.digital.oasys.api

import com.fasterxml.jackson.annotation.JsonProperty

data class PermissionsResponseDto(
  @JsonProperty("STATE")
  val state: String,
  @JsonProperty("DETAIL")
  val detail: PermissionsDetails
)

data class PermissionsDetails(
  @JsonProperty("Results")
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
