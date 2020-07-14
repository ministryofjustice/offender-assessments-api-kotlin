package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal
import java.util.*

class Ovp (
    var ovpStaticWeightedScore: BigDecimal? = null,
    var ovpDynamicWeightedScore: BigDecimal? = null,
    var ovpTotalWeightedScore: BigDecimal? = null,
    var ovp1Year: BigDecimal? = null,
    var ovp2Year: BigDecimal? = null,
    var ovpRisk: RefElementDto? = null,
    var ovpPreviousWeightedScore: BigDecimal? = null,
    var ovpViolentWeightedScore: BigDecimal? = null,
    var ovpNonViolentWeightedScore: BigDecimal? = null,
    var ovpAgeWeightedScore: BigDecimal? = null,
    var ovpSexWeightedScore: BigDecimal? = null
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