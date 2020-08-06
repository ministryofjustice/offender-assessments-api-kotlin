package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectiveMeasure

class ObjectiveMeasureDtoTest {
    @Test
    fun shouldReturnObjectiveMeasureDtoForEntity() {
        val objectiveMeasure= setupSspObjectiveMeasure()
        val measure = ObjectiveMeasureDto.from(objectiveMeasure)
        Assertions.assertThat(measure).isEqualTo(setupValidObjectiveMeasureDto())
    }

    private fun setupSspObjectiveMeasure():SspObjectiveMeasure {
        return SspObjectiveMeasure(
                sspObjectiveMeasurePk = 1L,
                objectiveStatus = RefElement(
                        refElementCode = "R",
                        refElementDesc = "Ongoing",
                        refElementShortDesc = null),
                objectiveStatusComments = "Status Comments",
                sspObjectivesInSetPk = 2L)
    }
    private fun setupValidObjectiveMeasureDto(): ObjectiveMeasureDto{
        return ObjectiveMeasureDto(
                comments = "Status Comments",
                status = RefElementDto(
                        code = "R",
                        description = "Ongoing"))
    }
}