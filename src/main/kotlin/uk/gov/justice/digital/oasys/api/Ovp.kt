package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal
import java.util.*

data class Ovp (
    val ovpStaticWeightedScore: BigDecimal? = null,
    val ovpDynamicWeightedScore: BigDecimal? = null,
    val ovpTotalWeightedScore: BigDecimal? = null,
    val ovp1Year: BigDecimal? = null,
    val ovp2Year: BigDecimal? = null,
    val ovpRisk: RefElementDto? = null,
    val ovpPreviousWeightedScore: BigDecimal? = null,
    val ovpViolentWeightedScore: BigDecimal? = null,
    val ovpNonViolentWeightedScore: BigDecimal? = null,
    val ovpAgeWeightedScore: BigDecimal? = null,
    val ovpSexWeightedScore: BigDecimal? = null
) {

    companion object {
        fun from(assessment: Assessment?): Ovp? {
            return if (Objects.isNull(assessment)) {
                null
            } else Ovp(
                    assessment?.ovpStWesc,
                    assessment?.ovpDyWesc,
                    assessment?.ovpTotWesc,
                    assessment?.ovp1Year,
                    assessment?.ovp2Year,
                    RefElementDto.from(assessment?.ovpRiskRecon),
                    assessment?.ovpPrevWesc,
                    assessment?.ovpVioWesc,
                    assessment?.ovpNonVioWesc,
                    assessment?.ovpAgeWesc,
                    assessment?.ovpSexWesc
            )
        }
    }
}