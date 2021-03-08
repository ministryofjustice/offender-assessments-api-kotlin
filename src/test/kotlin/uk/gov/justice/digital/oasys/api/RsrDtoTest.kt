package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.math.BigDecimal

class RsrDtoTest {

  @Test
  fun `should build valid RSR DTO`() {
    val assessment = setupAssessment()
    val rsrDto = RsrDto.from(assessment)

    Assertions.assertThat(rsrDto).isEqualTo(validRsr())
  }

  @Test
  fun `should build valid null RSR DTO`() {
    val rsrDto = RsrDto.from(null)
    Assertions.assertThat(rsrDto).isNull()
  }

  private fun setupAssessment(): Assessment {
    return Assessment(
      rsrRiskRecon = RefElement(
        refElementCode = "L",
        refElementDesc = "Long",
        refElementShortDesc = "Long"
      ),
      rsrPercentageScore = BigDecimal.valueOf(50.1234),
      rsrStaticOrDynamic = "STATIC",
      rsrAlgorithmVersion = 11L
    )
  }

  private fun validRsr(): RsrDto {
    return RsrDto(
      rsrRiskRecon = RefElementDto(
        code = "L",
        shortDescription = "Long",
        description = "Long"
      ),
      rsrPercentageScore = BigDecimal.valueOf(50.1234),
      rsrStaticOrDynamic = "STATIC",
      rsrAlgorithmVersion = 11L
    )
  }
}
