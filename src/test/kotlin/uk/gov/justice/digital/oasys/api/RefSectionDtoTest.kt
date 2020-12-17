package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.RefSection

@DisplayName("Reference Section DTO Tests")
class RefSectionDtoTest {

  @Test
  fun `Builds valid Reference Section DTOs from Entities`() {
    val sections = setupSectionCollection()
    val refSectionDtos = RefSectionDto.from(sections)

    assertThat(refSectionDtos).isEqualTo(setupValidDtos())
  }

  @Test
  fun `Builds valid Reference Section DTO from null entity`() {
    val refSectionDto = RefSectionDto.from(null)
    assertThat(refSectionDto).isEmpty()
  }

  private fun setupSectionCollection(): Collection<RefSection> {
    val section = setupSection()
    return listOf(section, section.copy(refSectionCode = "3"))
  }

  private fun setupSection(): RefSection {
    val sectionType = RefElement(refElementShortDesc = "Any String", refElementDesc = "Any String")
    return RefSection(
      crimNeedScoreThreshold = 12L,
      refSectionCode = "Any Code",
      sectionType = sectionType,
      scoredForOgp = "Y",
      scoredForOvp = "Y",
      refQuestions = emptyList()
    )
  }

  private fun setupValidDtos(): Collection<RefSectionDto?>? {
    val dto = setupValidDto()
    return setOf(dto, dto.copy(refSectionCode = "3"))
  }

  private fun setupValidDto(): RefSectionDto {
    return RefSectionDto(
      refSectionId = 12L,
      refSectionCode = "Any Code",
      shortDescription = "Any String",
      description = "Any String",
      refCrimNeedScoreThreshold = 12L,
      refScoredForOgp = true,
      refScoredForOvp = true,
      refQuestions = emptyList()
    )
  }
}
