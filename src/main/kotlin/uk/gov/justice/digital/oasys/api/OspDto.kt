package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal

data class OspDto(
  val ospIndecentPercentageScore: BigDecimal? = null,
  val ospContactPercentageScore: BigDecimal? = null,
  val ospIndecentRiskRecon: RefElementDto? = null,
  val ospContactRiskRecon: RefElementDto? = null
) {
  companion object {
    fun from(assessment: Assessment?): OspDto? {
      return if (assessment == null)
        null
      else {
        return OspDto(
          ospIndecentPercentageScore = assessment.ospIndecentPercentageScore,
          ospContactPercentageScore = assessment.ospContactPercentageScore,
          ospIndecentRiskRecon = RefElementDto.from(assessment.ospIndecentRiskRecon),
          ospContactRiskRecon = RefElementDto.from(assessment.ospContactRiskRecon)
        )
      }
    }
  }
}
