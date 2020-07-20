package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal
import java.util.*

data class Ogrs3 (
    var ogrs3_1Year: BigDecimal? = null,
    var ogrs3_2Year: BigDecimal? = null,
    var reconvictionRisk: RefElementDto? = null
) {
    companion object {
        fun from(assessment: Assessment?): Ogrs3? {
            return if (Objects.isNull(assessment)) {
                null
            } else Ogrs3(
                    assessment?.ogrs31Year,
                    assessment?.ogrs32Year,
                    RefElementDto.from(assessment?.ogrs3RiskRecon)
            )
        }
    }
}