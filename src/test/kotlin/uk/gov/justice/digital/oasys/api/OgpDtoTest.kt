package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.math.BigDecimal


@DisplayName("OGP DTO Tests")
class OgpDtoTest {

    @Test
    fun `should build valid OGP DTO`() {
        val assessment = setupAssessment()
        val ogp = OgpDto.from(assessment)

        assertThat(ogp).isEqualTo(setupValidOgp())
    }

    @Test
    fun `should build valid null OGP DTO`() {
        val dto = OgpDto.from(null)
        assertThat(dto).isNull()
    }

    private fun setupAssessment(): Assessment {
        return Assessment(ogpStWesc = BigDecimal.valueOf(1),
                ogpDyWesc = BigDecimal.valueOf(2),
                ogpTotWesc = BigDecimal.valueOf(3),
                ogp1Year = BigDecimal.valueOf(4),
                ogp2Year = BigDecimal.valueOf(5),
                ogpRiskRecon = RefElement(refElementDesc = ("Low"), refElementCode = ("L"))
        )
    }
    private fun setupValidOgp(): OgpDto {
        return OgpDto(ogpStaticWeightedScore = BigDecimal.valueOf(1),
                ogpDynamicWeightedScore = BigDecimal.valueOf(2),
                ogpTotalWeightedScore = BigDecimal.valueOf(3),
                ogp1Year = BigDecimal.valueOf(4),
                ogp2Year = BigDecimal.valueOf(5),
                ogpRisk = RefElementDto(description = "Low", code = "L")
        )
    }
}