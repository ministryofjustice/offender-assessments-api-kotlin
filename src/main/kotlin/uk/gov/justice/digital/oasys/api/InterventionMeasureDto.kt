package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspInterventionMeasure

data class InterventionMeasureDto(
  val comments: String? = null,
  val status: RefElementDto? = null
) {
  companion object {

    fun from(sspInterventionMeasure: SspInterventionMeasure?): InterventionMeasureDto? {
      return if (sspInterventionMeasure == null) {
        null
      } else InterventionMeasureDto(
        comments = sspInterventionMeasure.interventionStatusComments,
        status = RefElementDto.from(sspInterventionMeasure.interventionStatus)
      )
    }
  }
}
