package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal

data class Ogp(
    var ogpStaticWeightedScore: BigDecimal? = null,
    var ogpDynamicWeightedScore: BigDecimal? = null,
    var ogpTotalWeightedScore: BigDecimal? = null,
    var ogp1Year: BigDecimal? = null,
    var ogp2Year: BigDecimal? = null,
    var ogpRisk: RefElementDto? = null
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