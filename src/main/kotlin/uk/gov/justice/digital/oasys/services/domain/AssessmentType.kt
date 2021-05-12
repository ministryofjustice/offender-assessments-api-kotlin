package uk.gov.justice.digital.oasys.services.domain

enum class AssessmentType(val value: String) {
  LAYER1("LAYER_1"),
  LAYER2("LAYER_2"),
  LAYER3("LAYER_3"),
  SARA("SARA");

  companion object {
    fun fromString(enumValue: String?): AssessmentType? {
      return values().firstOrNull { it.value == enumValue }
    }
  }
}
