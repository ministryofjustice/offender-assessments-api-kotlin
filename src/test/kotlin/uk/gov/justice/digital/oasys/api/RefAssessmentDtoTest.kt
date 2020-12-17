package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion

@DisplayName("Reference Assessment DTO Tests")
class RefAssessmentDtoTest {

  @Test
  fun `Builds valid Reference Assessment DTO from Entity`() {
    val version = setupVersion()
    val dto = RefAssessmentDto.from(version)
    assertThat(dto).isEqualTo(setupValidDto())
  }

  @Test
  fun `Builds null DTO from null entity`() {
    val refAnswerDto = RefAssessmentDto.from(null)
    assertThat(refAnswerDto).isNull()
  }

  private fun setupVersion(): RefAssessmentVersion {
    return RefAssessmentVersion(
      refAssVersionUk = 12L,
      refAssVersionCode = "Any Code",
      versionNumber = "Any Version",
      oasysScoringAlgVersion = 2L,
      refModuleCode = "Any Module Code",
      refSections = emptyList()
    )
  }

  private fun setupValidDto(): RefAssessmentDto? {
    return RefAssessmentDto(
      refAssessmentVersionId = 12L,
      refAssVersionCode = "Any Code",
      versionNumber = "Any Version",
      oasysScoringAlgorithmVersion = 2L,
      refModuleCode = "Any Module Code",
      refSections = emptySet()
    )
  }
}
