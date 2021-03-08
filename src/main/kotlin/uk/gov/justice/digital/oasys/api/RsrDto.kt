package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal

data class RsrDto(
  val rsrPercentageScore: BigDecimal? = null,
  val rsrStaticOrDynamic: String? = null,
  val rsrAlgorithmVersion: Long? = null,
  val rsrRiskRecon: RefElementDto? = null
) {
  companion object {
    fun from(assessment: Assessment?): RsrDto? {
      return if (assessment == null)
        null
      else {
        return RsrDto(
          rsrPercentageScore = assessment.rsrPercentageScore,
          rsrStaticOrDynamic = assessment.rsrStaticOrDynamic,
          rsrAlgorithmVersion = assessment.rsrAlgorithmVersion,
          rsrRiskRecon = RefElementDto.from(assessment.rsrRiskRecon)
        )
      }
    }
  }
}
