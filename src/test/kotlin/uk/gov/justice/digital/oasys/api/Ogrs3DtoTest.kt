package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.math.BigDecimal

@DisplayName("OGRS3 DTO Tests")
class Ogrs3DtoTest {

  @Test
  fun `should build valid OGRS3 DTO`() {
    val assessment = setupAssessment()
    val ogr = Ogrs3Dto.from(assessment)

    assertThat(ogr).isEqualTo(setupValidOgrs3())
  }

  @Test
  fun `should build valid null OGRS3 DTO`() {
    val dto = Ogrs3Dto.from(null)
    assertThat(dto).isNull()
  }

  private fun setupAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 123,
      ogrs31Year = BigDecimal.valueOf(4),
      ogrs32Year = BigDecimal.valueOf(5),
      ogrs3RiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L")
    )
  }

  private fun setupValidOgrs3(): Ogrs3Dto {
    return Ogrs3Dto(
      ogrs3_1Year = BigDecimal.valueOf(4),
      ogrs3_2Year = BigDecimal.valueOf(5),
      reconvictionRisk = RefElementDto(description = "Low", code = "L")
    )
  }
}
