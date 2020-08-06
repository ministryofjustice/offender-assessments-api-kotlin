package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.*

@DisplayName("Objective DTO Tests")
class ObjectiveDtoTest {

    @Test
    fun `Build Objective DTO from SSP Objectives in Set`() {
        val objectivesInSet: Set<SspObjectivesInSet>? = setupObjectivesInSet()
        val objectives = ObjectiveDto.from(objectivesInSet)
        assertThat(objectives).isEqualTo(setupObjectiveDto())
    }
    
    private fun setupObjectiveDto():Set<ObjectiveDto>{
        return setOf( ObjectiveDto(
                criminogenicNeeds = emptySet(),
                interventions = emptySet(),
                objectiveMeasure = ObjectiveMeasureDto(
                        comments = null,
                        status = null),
                objectiveType = RefElementDto(
                        code = "code 1",
                        shortDescription = "short desc 1",
                        description = "description 1"),
                objectiveCode = "code",
                objectiveDescription = "objective description",
                objectiveHeading = "description 2",
                objectiveComment = "Objective description",
                howMeasured = null,
                createdDate = null))
    }

    private fun setupObjectivesInSet(): Set<SspObjectivesInSet> {
        val objective = SspObjectivesInSet(objectiveType = RefElement(refElementCode = "code 1", refElementShortDesc = "short desc 1", refElementDesc = "description 1"),
                sspObjectiveMeasure = SspObjectiveMeasure(),
                sspObjIntervenePivots = setOf(SspObjIntervenePivot()),
                sspObjective = (SspObjective(
                        objectiveDesc = "Objective description",
                        sspObjectivePk = 2L,
                        objective = Objective(
                                objectiveCode = "code",
                                objectiveUk = 1L,
                                objectiveDesc = "objective description",
                                objectiveHeading = RefElement(
                                        refElementCode = "code 2", refElementShortDesc = "short desc 2", refElementDesc = "description 2")))),
                sspObjectivesInSetPk = 2L)
        return setOf(objective)
    }
}