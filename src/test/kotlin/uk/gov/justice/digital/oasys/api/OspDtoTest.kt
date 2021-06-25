package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.math.BigDecimal

class OspDtoTest {

  @Test
  fun `should build valid OSP DTO`() {
    val assessment = setupAssessment()
    val ospDto = OspDto.from(assessment)

    Assertions.assertThat(ospDto).isEqualTo(setupValidOsp())
  }

  @Test
  fun `should build valid null OSP DTO`() {
    val ospDto = OspDto.from(null)
    Assertions.assertThat(ospDto).isNull()
  }

  private fun setupAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 123,
      ospIndecentRiskRecon = RefElement(
        refElementCode = "L",
        refElementDesc = "Long",
        refElementShortDesc = "Long"
      ),
      ospIndecentPercentageScore = BigDecimal.valueOf(50.1234),
      ospContactRiskRecon = RefElement(
        refElementCode = "L",
        refElementDesc = "Long",
        refElementShortDesc = "Long"
      ),
      ospContactPercentageScore = BigDecimal.valueOf(50.1234)
    )
  }

  private fun setupValidOsp(): OspDto {
    return OspDto(
      ospIndecentRiskRecon = RefElementDto(
        code = "L",
        shortDescription = "Long",
        description = "Long"
      ),
      ospIndecentPercentageScore = BigDecimal.valueOf(50.1234),
      ospContactRiskRecon = RefElementDto(
        code = "L",
        shortDescription = "Long",
        description = "Long"
      ),
      ospContactPercentageScore = BigDecimal.valueOf(50.1234)
    )
  }
}
