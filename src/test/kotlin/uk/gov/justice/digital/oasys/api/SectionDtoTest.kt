package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import uk.gov.justice.digital.oasys.jpa.entities.RefSection
import uk.gov.justice.digital.oasys.jpa.entities.Section

@DisplayName("Section DTO tests")
class SectionDtoTest {

    @Test
    fun `Builds valid Offender DTO from Entity`() {

        val section = setupSection()
        val sectionDto = SectionDto.from(setOf(section)).first()

        assertThat(sectionDto?.sectionId).isEqualTo(section.oasysSectionPk)
        assertThat(sectionDto?.assessmentId).isEqualTo(section.oasysSetPk)
        assertThat(sectionDto?.refAssessmentVersionCode).isEqualTo(section.refSection?.refAssVersionCode)
        assertThat(sectionDto?.refSectionVersionNumber).isEqualTo(section.refSection?.versionNumber)
        assertThat(sectionDto?.refSectionCode).isEqualTo(section.refSection?.refSectionCode)
        assertThat(sectionDto?.refSectionCrimNeedScoreThreshold).isEqualTo(section.refSection?.crimNeedScoreThreshold)
        assertThat(sectionDto?.status).isEqualTo(section.sectionStatusElm)
        assertThat(sectionDto?.sectionOgpWeightedScore).isEqualTo(section.sectOgpWeightedScore)
        assertThat(sectionDto?.sectionOgpRawScore).isEqualTo(section.sectOgpRawScore)
        assertThat(sectionDto?.sectionOvpWeightedScore).isEqualTo(section.sectOvpWeightedScore)
        assertThat(sectionDto?.sectionOvpRawScore).isEqualTo(section.sectOvpRawScore)
        assertThat(sectionDto?.sectionOtherWeightedScore).isEqualTo(section.sectOtherWeightedScore)
        assertThat(sectionDto?.sectionOtherRawScore).isEqualTo(section.sectOtherRawScore)
        assertThat(sectionDto?.lowScoreAttentionNeeded).isTrue()
        assertThat(sectionDto?.questions).hasSize(section.oasysQuestions!!.size)
    }

    @Test
    fun `Builds valid Offender DTO Low Score Indicator No`() {
        val section = setupSection().copy(lowScoreNeedAttnInd = "N")
        val sectionDto = SectionDto.from(setOf(section)).first()
        assertThat(sectionDto?.lowScoreAttentionNeeded).isFalse()
    }

    @Test
    fun `Builds valid Offender DTO Low Score Indicator Null`() {
        val section = setupSection().copy(lowScoreNeedAttnInd = null)
        val sectionDto = SectionDto.from(setOf(section)).first()
        assertThat(sectionDto?.lowScoreAttentionNeeded).isFalse()
    }

    @Test
    fun `Builds valid Section DTO Null`() {
        val sectionDto = SectionDto.from(null)
        assertThat(sectionDto).isEmpty()
    }

    @Test
    fun `Builds valid Section DTO Null in Set`() {
        val sectionDto = SectionDto.from(setOf(null))
        assertThat(sectionDto).isEmpty()
    }

    private fun setupSection(): Section {

        return Section(
                oasysSectionPk = 1L,
                oasysSetPk = 1L,
                sectionStatusElm = "COMPLETE",
                sectOgpWeightedScore = 100L,
                sectOgpRawScore = 101L,
                sectOvpWeightedScore = 200,
                sectOvpRawScore = 201,
                sectOtherWeightedScore = 300L,
                sectOtherRawScore = 301L,
                lowScoreNeedAttnInd = "Y",
                oasysQuestions = emptySet(),
                refSection = setupRefSection())
    }

    private fun setupRefSection(): RefSection {
        return RefSection(
                refSectionCode = "SC",
                refAssVersionCode = "1",
                crimNeedScoreThreshold = 10L)
    }
}