package uk.gov.justice.digital.oasys.services.domain

enum class AssessmentPurposeType(val value: String) {
  RSR_ONLY("620"),
  RISK_OF_HARM_ASSESSMENT("600"),
  RISK_REVIEW("580");

  companion object {
    fun fromString(enumValue: String?): AssessmentPurposeType? {
      return values().firstOrNull { it.value == enumValue }
    }
  }
}
