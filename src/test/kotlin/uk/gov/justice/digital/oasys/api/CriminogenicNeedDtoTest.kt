package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspCrimNeedObjPivot

@DisplayName("Criminogenic Need DTO tests")
class CriminogenicNeedDtoTest {

    @Test
    fun `Build Criminogenic Need DTO Set from Entity Set`() {
        val sspCrimNeedObjPivots = setupSspCrimNeedObjPivotEntity()
        val needs = CriminogenicNeedDto.from(sspCrimNeedObjPivots)
        assertThat(needs).isEqualTo(setupCriminogenicNeedDto())
    }

    @Test
    fun `Build empty Criminogenic Need DTO Set from null Entity Set`() {
        val sspCrimNeedObjPivots: Set<SspCrimNeedObjPivot>? = null
        val needs = CriminogenicNeedDto.from(sspCrimNeedObjPivots)
        assertThat(needs).isEmpty()
    }

    private fun setupSspCrimNeedObjPivotEntity(): Set<SspCrimNeedObjPivot> {
        val need1 = SspCrimNeedObjPivot(
                sspCrimNeedObjPivotPk = 1L,
                criminogenicNeed = RefElement(refElementCode = "I10", refElementDesc = "Need 1", refElementShortDesc = null),
                displaySort = 1L)
        val need2 = SspCrimNeedObjPivot(
                sspCrimNeedObjPivotPk = 2L,
                criminogenicNeed = RefElement(refElementCode = "I20", refElementDesc = "Need 2", refElementShortDesc = null),
                displaySort = 2L)
        return setOf(need1, need2)
    }

    private fun setupCriminogenicNeedDto(): Set<CriminogenicNeedDto>{
        return setOf(
                CriminogenicNeedDto(code = "I10", description = "Need 1", priority = 1L),
                CriminogenicNeedDto(code = "I20", description = "Need 2", priority = 2L)
        )
    }
}