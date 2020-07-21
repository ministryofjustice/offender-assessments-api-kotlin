package uk.gov.justice.digital.oasys.api 

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.math.BigDecimal 

@DisplayName("OVP DTO Tests")
class OvpTest {

    @Test
    fun `should build valid OVP DTO`() {
        val assessment = setupAssessment() 
        val ovp = Ovp.from(assessment)
        val validOvp = setupValidOvp()
        assertThat(ovp).isEqualTo(validOvp)
    }

    @Test
    fun ` should build valid null OVP DTO`() {
        val dto = Ovp.from(null) 
        assertThat(dto).isNull() 
    }

    private fun setupAssessment():Assessment {
        return Assessment( ovp1Year = BigDecimal.valueOf(1), 
                ovp2Year = BigDecimal.valueOf(2),
                ovpAgeWesc = BigDecimal.valueOf(3),
                ovpDyWesc = BigDecimal.valueOf(4),
                ovpNonVioWesc = BigDecimal.valueOf(5),
                ovpPrevWesc = BigDecimal.valueOf(6),
                ovpSexWesc = BigDecimal.valueOf(7),
                ovpStWesc = BigDecimal.valueOf(8),
                ovpTotWesc = BigDecimal.valueOf(9),
                ovpVioWesc = BigDecimal.valueOf(10),
                ovpRiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L")
        )
    }

    private fun setupValidOvp():Ovp {
        return Ovp(ovp1Year = BigDecimal.valueOf(1),
                ovp2Year = BigDecimal.valueOf(2),
                ovpAgeWeightedScore = BigDecimal.valueOf(3),
                ovpDynamicWeightedScore = BigDecimal.valueOf(4),
                ovpNonViolentWeightedScore = BigDecimal.valueOf(5),
                ovpPreviousWeightedScore = BigDecimal.valueOf(6),
                ovpSexWeightedScore = BigDecimal.valueOf(7),
                ovpStaticWeightedScore = BigDecimal.valueOf(8),
                ovpTotalWeightedScore = BigDecimal.valueOf(9),
                ovpViolentWeightedScore = BigDecimal.valueOf(10),
                ovpRisk = RefElementDto(description = "Low", code = "L")
        )
    }
}