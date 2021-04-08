package uk.gov.justice.digital.oasys.jpa.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class ReferenceDataEntity(
  @JsonProperty("STATE")
  val state: String? = null,

  @JsonProperty("DETAIL")
  val referenceData: Map<String, List<FilteredRefDataEntity>>? = emptyMap(),
)

data class FilteredRefDataEntity(
  @JsonProperty("displayValue")
  val displayValue: String?,

  @JsonProperty("returnValue")
  val returnValue: String?
)

class ReferenceDataFailureEntity(
  @JsonProperty("DETAIL")
  val errorDetail: RefDataFailure? = null
)

class RefDataFailure(
  @JsonProperty("Failures")
  val failures: List<ValidationFailure>? = null
)

class ValidationFailure(
  @JsonProperty("sectionCode")
  val sectionCode: String? = null,

  @JsonProperty("errors")
  val errors: List<ValidationError> = emptyList()

)

data class ValidationError(
  @JsonProperty("failureType")
  val failureType: String? = null,

  @JsonProperty("errorName")
  val errorName: String? = null,

  @JsonProperty("messageCode")
  val messageCode: String? = null,

  @JsonProperty("message")
  val message: String? = null,

  @JsonProperty("oasysErrorLogId")
  val oasysErrorLogId: Long? = null
)
