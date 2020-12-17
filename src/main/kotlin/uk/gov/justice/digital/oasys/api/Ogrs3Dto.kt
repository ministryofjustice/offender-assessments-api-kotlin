package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.math.BigDecimal
import java.util.Objects

data class Ogrs3Dto(
  val ogrs3_1Year: BigDecimal? = null,
  val ogrs3_2Year: BigDecimal? = null,
  val reconvictionRisk: RefElementDto? = null
) {
  companion object {
    fun from(assessment: Assessment?): Ogrs3Dto? {
      return if (Objects.isNull(assessment)) {
        null
      } else Ogrs3Dto(
        assessment?.ogrs31Year,
        assessment?.ogrs32Year,
        RefElementDto.from(assessment?.ogrs3RiskRecon)
      )
    }
  }
}
