package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.RefSection

@DisplayName("Reference Section DTO Tests")
class RefSectionDtoTest {

    @Test
    fun `Builds valid Reference Section DTO from Entity`() {
        val section = setupSection()
        val dto = RefSectionDto.from(section)
        assertThat(dto).isEqualTo(setupValidDto())
    }

    private fun setupSection(): RefSection {
        val sectionType = RefElement(refElementShortDesc = "Any String", refElementDesc = "Any String")
        return RefSection(
                crimNeedScoreThreshold = 12L,
                refSectionCode = "Any Code",
                sectionType = sectionType,
                scoredForOgp = "Y",
                scoredForOvp = "Y",
                refQuestions = emptyList())
    }

    private fun setupValidDto(): RefSectionDto? {
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