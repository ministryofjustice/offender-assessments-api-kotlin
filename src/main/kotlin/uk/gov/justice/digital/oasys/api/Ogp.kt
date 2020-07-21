package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal

data class Ogp(
    val ogpStaticWeightedScore: BigDecimal? = null,
    val ogpDynamicWeightedScore: BigDecimal? = null,
    val ogpTotalWeightedScore: BigDecimal? = null,
    val ogp1Year: BigDecimal? = null,
    val ogp2Year: BigDecimal? = null,
    val ogpRisk: RefElementDto? = null
) {

    companion object {
        fun from(assessment: Assessment?): Ogp? {
            return if (assessment == null) {
                null
            } else Ogp(
                    assessment.ogpStWesc,
                    assessment.ogpDyWesc,
                    assessment.ogpTotWesc,
                    assessment.ogp1Year,
                    assessment.ogp2Year,
                    RefElementDto.from(assessment.ogpRiskRecon)
            )
        }
    }
}