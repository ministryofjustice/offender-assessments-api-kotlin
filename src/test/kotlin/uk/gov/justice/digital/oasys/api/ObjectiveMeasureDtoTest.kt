package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectiveMeasure

@DisplayName("Objective Measure DTO Tests")
class ObjectiveMeasureDtoTest {
    @Test
    fun `Builds valid Objective Measure DTO from entity`() {
        val objectiveMeasure = setupSspObjectiveMeasure()
        val measure = ObjectiveMeasureDto.from(objectiveMeasure)
        assertThat(measure).isEqualTo(setupValidObjectiveMeasureDto())
    }

    @Test
    fun `Builds valid null Objective Measure DTO from null entity`() {
        val measure = ObjectiveMeasureDto.from(null)
        assertThat(measure).isNull()
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