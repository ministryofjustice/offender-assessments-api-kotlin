package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspInterventionInSet
import uk.gov.justice.digital.oasys.jpa.entities.SspObjIntervenePivot
import uk.gov.justice.digital.oasys.jpa.entities.SspWhoDoWorkPivot

@DisplayName("Intervention DTO Tests")
class InterventionDtoTest {

    @Test
    fun `Builds Intervention DTO from entity`(){
        val sspObjIntervenePivot = setupInterventionEntity()
        val interventionDto = InterventionDto.from(sspObjIntervenePivot)
        assertThat(interventionDto).isEqualTo(setupValidInterventionDto())
    }

    @Test
    fun `Builds valid empty Intervention DTO set from null entity`(){
        val interventionDto = InterventionDto.from(null)
        assertThat(interventionDto).isEqualTo(emptySet<InterventionDto>())
    }

    @Test
    fun `Builds valid empty Intervention DTO set from empty entity set`(){
        val interventionDto = InterventionDto.from(emptySet())
        assertThat(interventionDto).isEqualTo(emptySet<InterventionDto>())
    }

    private fun setupInterventionEntity(): Set<SspObjIntervenePivot>? {
        return setOf(SspObjIntervenePivot(
                sspObjectivesInSetPk = 1L,
                sspObjIntervenePivotPk = 1L,
                sspInterventionInSet = SspInterventionInSet(
                        copiedForwardIndicator = "Y",
                        sspInterventionInSetPk = 1L,
                        interventionComment = "Intervention Comment",
                        intervention = RefElement(refElementCode = "V1", refElementDesc = "Intervention 1", refElementShortDesc = "shortDescription"),
                        timescaleForIntervention = RefElement(refElementCode = ("ONE_MONTH"), refElementDesc = ("One Month")),
                        sspInterventionMeasure = null,
                        sspWhoDoWorkPivot = setOf(SspWhoDoWorkPivot()
                        ))))
    }

    private fun setupValidInterventionDto(): Set<InterventionDto> {
        return setOf(InterventionDto (
                copiedForward = true,
                interventionComment = "Intervention Comment",
                timescale = RefElementDto(code = "ONE_MONTH", description = "One Month", shortDescription = null),
                interventionCode = "V1",
                interventionDescription = "Intervention 1",
                whoDoingWork = setOf(WhoDoingWorkDto()),
                interventionMeasure = null))
    }
}