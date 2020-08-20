package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspInterventionMeasure

@DisplayName("Intervention Measure DTO Tests")
class InterventionMeasureDtoTest {

    @Test
    fun `Build valid Intervention Measure DTO from entity`() {
        val measure = setupInterventionMeasureEntity()
        val measureDto = (InterventionMeasureDto.from(measure))
        assertThat(measureDto).isEqualTo(setupValidMeasureDto())
    }

    @Test
    fun `Build null Intervention Measure DTO from null entity`() {
        val measureDto = (InterventionMeasureDto.from(null))
        assertThat(measureDto).isNull()
    }

    private fun setupInterventionMeasureEntity(): SspInterventionMeasure {
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