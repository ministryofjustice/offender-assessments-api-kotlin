package uk.gov.justice.digital.oasys

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.api.ApiTestContext.getSentencePlanSection
import uk.gov.justice.digital.oasys.api.ApiTestContext.setupLayer3AssessmentWithFullSentencePlan
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectivesInSet
import java.time.LocalDateTime

class FullSentencePlanDtoTest {
    @Test
    fun shouldReturnSentencePlanDtoFromOASysAssessment() {
        val assessment = setupLayer3AssessmentWithFullSentencePlan(123L)
        val section = getSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.oasysSetId).isEqualTo(123L)
        assertThat(sentencePlan?.objectives).hasSize(2)
        assertThat(sentencePlan?.questions).hasSize(3)
    }



    @Test
    fun shouldReturnNullWhenNoISPorRSPSectionFoundAndNoObjectives() {
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
    fun shouldUseCreatedDateForStartDate() {
        val assessment = setupLayer3AssessmentWithFullSentencePlan(123L)
        val section = getSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.createdDate).isEqualToIgnoringSeconds(LocalDateTime.now().minusDays(1))
    }

    @Test
    fun shouldReturnOASysSetCompletedDateAsSentencePlanCompletedDate() {
        val assessment = Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                assessmentStatus = "COMPLETED",
                oasysSections = emptySet(),
                sspObjectivesInSets = setOf(SspObjectivesInSet()),
                dateCompleted = LocalDateTime.now().minusDays(1),
                oasysSetPk= 123L)
        val section = getSentencePlanSection()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan?.oasysSetId).isEqualTo(123L)
        assertThat(sentencePlan?.completedDate).isEqualToIgnoringMinutes(LocalDateTime.now().minusDays(1))
    }

    @Test
    fun sentencePlanHasNullCompletedDateIfNotPresent() {
        val today = LocalDateTime.now()
        val assessment = Assessment(
                createDate = today,
                dateCompleted = null,
                oasysSections = emptySet(),
                sspObjectivesInSets = setOf(SspObjectivesInSet()))
        val section = getSentencePlanSection()
        val actual = FullSentencePlanDto.from(assessment, section)
        assertThat(actual?.completedDate).isNull()
    }

    @Test
    fun shouldReturnRefQuestionsForSPSectionsInAdditionToOASysQuestions() {
        val section = getSentencePlanSection()
        section.refSection.setRefQuestions(listOf(RefQuestion(
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