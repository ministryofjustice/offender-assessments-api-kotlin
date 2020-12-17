package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Objective
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspObjective
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectiveMeasure
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectivesInSet

@DisplayName("Objective DTO Tests")
class ObjectiveDtoTest {

  @Test
  fun `Build Objective DTO from SSP Objectives entity set`() {
    val objectivesInSet = setupObjectivesInSet()
    val objectives = ObjectiveDto.from(objectivesInSet)
    assertThat(objectives).isEqualTo(setupValidObjectiveDto())
  }

  @Test
  fun `Build empty DTO set from empty SSP Objectives entity set`() {
    val objectives = ObjectiveDto.from(emptySet())
    assertThat(objectives).isEqualTo(emptySet<ObjectiveDto>())
  }

  @Test
  fun `Build empty DTO set from null SSP Objectives entity set`() {
    val objectives = ObjectiveDto.from(null)
    assertThat(objectives).isEqualTo(emptySet<ObjectiveDto>())
  }

  private fun setupObjectivesInSet(): Set<SspObjectivesInSet> {
    val objective = SspObjectivesInSet(
      objectiveType = RefElement(
        refElementCode = "code 1",
        refElementShortDesc = "short desc 1",
        refElementDesc = "description 1"
      ),
      sspObjectiveMeasure = SspObjectiveMeasure(),
      sspObjIntervenePivots = emptySet(),
      sspObjective = (
        SspObjective(
          objectiveDesc = "Objective description",
          sspObjectivePk = 2L,
          objective = Objective(
            objectiveCode = "code",
            objectiveUk = 1L,
            objectiveDesc = "objective description",
            objectiveHeading = RefElement(
              refElementCode = null,
              refElementShortDesc = null,
              refElementDesc = "description 2"
            )
          )
        )
        ),
      sspObjectivesInSetPk = 2L
    )
    return setOf(objective)
  }

  private fun setupValidObjectiveDto(): Set<ObjectiveDto> {
    return setOf(
      ObjectiveDto(
        criminogenicNeeds = emptySet(),
        interventions = emptySet(),
        objectiveMeasure = ObjectiveMeasureDto(),
        objectiveType = RefElementDto(
          code = "code 1",
          shortDescription = "short desc 1",
          description = "description 1"
        ),
        objectiveCode = "code",
        objectiveDescription = "objective description",
        objectiveHeading = "description 2",
        objectiveComment = "Objective description",
        howMeasured = null,
        createdDate = null
      )
    )
  }
}
