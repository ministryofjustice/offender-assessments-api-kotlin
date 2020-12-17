package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModel

@ApiModel(description = "Offender Type Models")
enum class OffenderIdentifier(val value: String) {
  OASYS("oasysOffenderId"),
  CRN("crn"),
  PNC("pnc"),
  NOMIS("nomisId"),
  BOOKING("bookingId"),
  UNKNOWN("none");

  companion object {
    fun fromString(enumValue: String?): OffenderIdentifier? {
      return values().firstOrNull { it.value == enumValue }
    }
  }
}
