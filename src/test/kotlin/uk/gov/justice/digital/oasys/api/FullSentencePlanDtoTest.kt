package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.api.ApiTestContext.setupLayer3AssessmentWithFullSentencePlan
import uk.gov.justice.digital.oasys.api.ApiTestContext.setupSentencePlanSection
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectivesInSet
import java.time.LocalDateTime

@DisplayName("Full Sentence Plan DTO Tests")
class FullSentencePlanDtoTest {
    @Test
    fun `builds Full Sentence Plan DTO from OASys Assessment`() {
        val assessment = setupLayer3AssessmentWithFullSentencePlan(123L)
        val section = setupSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.oasysSetId).isEqualTo(123L)
        assertThat(sentencePlan?.objectives).hasSize(2)
        assertThat(sentencePlan?.questions).hasSize(3)
    }

    @Test
    fun `Builds null Full Sentence Plan DTO when no ISP or RSP Section and no Objectives`() {
        val assessment = Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                assessmentStatus = "COMPLETED",
                oasysSections = emptySet(),
                sspObjectivesInSets = emptySet(),
                dateCompleted = LocalDateTime.now().minusDays(1),
                oasysSetPk = (123L))
        val sentencePlan = FullSentencePlanDto.from(assessment, null)
        assertThat(sentencePlan).isNull()
    }

    @Test
    fun `Builds Full Sentence Plan with Created Date from Start Date`() {
        val assessment = setupLayer3AssessmentWithFullSentencePlan(123L)
        val section = setupSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.createdDate).isEqualToIgnoringSeconds(LocalDateTime.now().minusDays(1))
    }

    @Test
    fun `Builds Full Sentence Plan with Completed Date from Assessment Completed Date`() {
        val assessment = Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                assessmentStatus = "COMPLETED",
                oasysSections = emptySet(),
                sspObjectivesInSets = setOf(SspObjectivesInSet()),
                dateCompleted = LocalDateTime.now().minusDays(1),
                oasysSetPk= 123L)
        val section = setupSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.oasysSetId).isEqualTo(123L)
        assertThat(sentencePlan?.completedDate).isEqualToIgnoringMinutes(LocalDateTime.now().minusDays(1))
    }

    @Test
    fun `Builds Full Sentence Plan with null Completed Date if Assessment completed date not present`() {
        val today = LocalDateTime.now()
        val assessment = Assessment(
                createDate = today,
                dateCompleted = null,
                oasysSections = emptySet(),
                sspObjectivesInSets = setOf(SspObjectivesInSet()))
        val section = setupSentencePlanSection()
        val actual = FullSentencePlanDto.from(assessment, section)
        assertThat(actual?.completedDate).isNull()
    }

    @Test
    fun `Builds Full Sentence Plan with Section RefQuestions and Assessment Questions`() {
        val section = setupSentencePlanSection()
        section.refSection?.refQuestions = (listOf(RefQuestion(
                refQuestionUk = 1L,
                refQuestionCode = "IP.40",
                displaySort = 1L,
                refSectionQuestion = "Ref Question Text")))
        val assessment = Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                assessmentStatus = "COMPLETED",
                sspObjectivesInSets = setOf(SspObjectivesInSet()),
                dateCompleted = LocalDateTime.now().minusDays(1),
                oasysSections = setOf(section),
                oasysSetPk = 123L )
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.questions).hasSize(3)
        assertThat(sentencePlan?.questions).containsOnlyKeys("IP.40", "IP.1", "IP.2")
    }
}