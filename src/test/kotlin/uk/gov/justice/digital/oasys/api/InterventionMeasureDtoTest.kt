package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspInterventionMeasure

class InterventionMeasureDtoTest {

    @Test
    fun shouldReturnInterventionMeasureDtoForEntity() {
        val measure = setupInterventionMeasure()
        val measureDto = (InterventionMeasureDto.from(measure))
        assertThat(measureDto).isEqualTo(setupValidMeasureDto())
    }

    private fun setupInterventionMeasure(): SspInterventionMeasure {
        return SspInterventionMeasure(
                interventionStatus = RefElement(
                        refElementCode = "R",
                        refElementDesc = "Ongoing",
                        refElementShortDesc = null),
                interventionStatusComments = "Status Comments")
    }

    private fun setupValidMeasureDto(): InterventionMeasureDto {
        return InterventionMeasureDto(
                comments = "Status Comments",
                status = RefElementDto(
                        code = "R",
                        description = "Ongoing",
                        shortDescription = null))
    }
}