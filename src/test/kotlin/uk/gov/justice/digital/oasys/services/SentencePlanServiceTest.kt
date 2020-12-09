package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import uk.gov.justice.digital.oasys.api.AnswerDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanSummaryDto
import uk.gov.justice.digital.oasys.api.QuestionDto
import uk.gov.justice.digital.oasys.jpa.entities.*
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import java.time.LocalDate
import java.time.LocalDateTime

class SentencePlanServiceTest {

    private var assessmentRepository: AssessmentRepository = mockk()
    private var sectionService: SectionService = mockk()
    private var offenderService: OffenderService= mockk()

    private var service: SentencePlanService = SentencePlanService(offenderService, assessmentRepository, sectionService)

    private val oasysSetId = 1234L
    private val oasysType = "OASYS"
    private val sectionIds = setOf(SectionHeader.INITIAL_SENTENCE_PLAN, SectionHeader.REVIEW_SENTENCE_PLAN)
    private val identity = "1"
    private val offenderId = 1L
    private val dateCreated = LocalDateTime.of(2020,1,1,14,0)
    private val dateCompleted = LocalDateTime.of(2020,12,1,14,0)
    private val dateOpened = LocalDateTime.of(2020,2,1,14,0)


    @Test
    fun `return Basic Sentence Plans`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getAssessmentsForOffender(offenderId, null, null, null, null) } returns setOf(setupAssessment())

        val basicSentencePlan = service.getBasicSentencePlansForOffender(oasysType, identity).first()

        assertThat(basicSentencePlan.basicSentencePlanItems).hasSize(1)
        assertThat(basicSentencePlan.sentencePlanId).isEqualTo(oasysSetId)
        assertThat(basicSentencePlan.createdDate).isEqualTo(LocalDate.of(2020,1,1))

        verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(offenderId,null, null, null, null)  }
    }

    @Test
    fun `return Latest Basic Sentence Plans`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getLatestAssessmentForOffender(offenderId) } returns setupAssessment()

        val basicSentencePlan = service.getLatestBasicSentencePlanForOffender(oasysType, identity)

        assertThat(basicSentencePlan.sentencePlanId).isEqualTo(oasysSetId)
        assertThat(basicSentencePlan.createdDate).isEqualTo(LocalDate.of(2020,1,1))

        verify(exactly = 1) { assessmentRepository.getLatestAssessmentForOffender(offenderId)  }
    }

    @Test
    fun `throws not found exception when null assessment returned`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getLatestAssessmentForOffender(offenderId) } returns null

        assertThrows<EntityNotFoundException> { service.getLatestBasicSentencePlanForOffender(oasysType, identity) }
    }

    @Test
    fun `throws not found exception when null assessment has no basic sentence plan items`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getLatestAssessmentForOffender(offenderId) } returns Assessment()

        assertThrows<EntityNotFoundException> { service.getLatestBasicSentencePlanForOffender(oasysType, identity) }
    }

    @Test
    fun `return Full Sentence Plan`() {
        every { assessmentRepository.getAssessment(oasysSetId)} returns setupAssessment()
        every { sectionService.getSectionsForAssessment(oasysSetId = oasysSetId, sectionIds = sectionIds) } returns setOf(setupSection())

        val fullSentencePlan = service.getFullSentencePlan(oasysSetId)

        assertThat(fullSentencePlan).isEqualTo(setupValidFullSentencePlanDto())
    }

    @Test
    fun `return Full Sentence Plans for Offender`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getAssessmentsForOffender(offenderId, "groupStatus", "assessmentType", false, "assessmentStatus") } returns setOf(setupAssessment())
        every { sectionService.getSectionsForAssessment(oasysSetId = oasysSetId, sectionIds = sectionIds) } returns setOf(setupSection())

        val fullSentencePlans = service.getFullSentencePlansForOffender(oasysType, identity, "groupStatus", "assessmentType", false, "assessmentStatus")

        assertThat(fullSentencePlans).isEqualTo(setOf(setupValidFullSentencePlanDto()))
    }

    @Test
    fun `return Full Sentence Plan Summaries for Offender`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getAssessmentsForOffender(offenderId, "groupStatus", "assessmentType", false, "assessmentStatus") } returns setOf(setupAssessment())
        every { sectionService.getSectionsForAssessment(oasysSetId = oasysSetId, sectionIds = sectionIds) } returns setOf(setupSection())

        val fullSentencePlanSummaries = service.getFullSentencePlanSummariesForOffender(oasysType, identity, "groupStatus", "assessmentType", false, "assessmentStatus")

        assertThat(fullSentencePlanSummaries).isEqualTo(setOf(setupValidFullSentencePlanSummaryDto()))
    }

    @Test
    fun `throws not found exception when null assessment returned for Full Sentence Plan`() {
        every { assessmentRepository.getAssessment(oasysSetId)} returns null

        val exception = assertThrows<EntityNotFoundException> { service.getFullSentencePlan(oasysSetId) }
        assertThat(exception.message).isEqualTo("Full Sentence Plan $oasysSetId, not found!")
    }

    @Test
    fun `return empty Full Sentence Plan DTO set for Offender with no Assessments`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getAssessmentsForOffender(offenderId, "groupStatus", "assessmentType", false, "assessmentStatus") } returns emptySet()

        val fullSentencePlans = service.getFullSentencePlansForOffender(oasysType, identity, "groupStatus", "assessmentType", false, "assessmentStatus")

        assertThat(fullSentencePlans).isEqualTo(emptySet<FullSentencePlanDto>())
    }

    @Test
    fun `return empty Full Sentence Plan Summary set for Offender with no Assessments`() {
        every { offenderService.getOffenderIdByIdentifier(oasysType, identity) } returns offenderId
        every { assessmentRepository.getAssessmentsForOffender(offenderId, "groupStatus", "assessmentType", false, "assessmentStatus") } returns emptySet()

        val fullSentencePlans = service.getFullSentencePlanSummariesForOffender(oasysType, identity, "groupStatus", "assessmentType", false, "assessmentStatus")

        assertThat(fullSentencePlans).isEqualTo(emptySet<FullSentencePlanSummaryDto>())
    }

    private fun setupAssessment(): Assessment {
        return Assessment(oasysSetPk = oasysSetId,
                assessmentStatus = "STATUS",
                assessmentType = "LAYER_3",
                createDate = dateCreated,
                dateCompleted = dateCompleted,
                basicSentencePlanList = setOf(
                        BasicSentencePlanObj(
                                basicSentPlanObjPk = 1L,
                                includeInPlanInd = "Y",
                                offenceBehaviourLink = RefElement(refElementCode = "RefCode", refElementDesc = "Ref Description"),
                                objectiveText = "Objective text",
                                measureText = "Measure text",
                                whatWorkText = "What Work",
                                whoWillDoWorkText = "Who do work",
                                timescalesText = "Timescales",
                                dateOpened = dateOpened,
                                dateCompleted = dateCompleted,
                                problemAreaCompInd = "Y",
                                createDate = dateCreated)))
    }

    private fun setupSection(): Section {
        return Section(
                refSection = RefSection(
                        crimNeedScoreThreshold = (5L),
                        refSectionCode = ("10"),
                        scoredForOgp = ("Y"),
                        scoredForOvp = ("Y"),
                        sectionType = RefElement(
                                refElementCode = ("10"),
                                refElementShortDesc = "Emotional Wellbeing"),
                        refQuestions = listOf(RefQuestion(
                                refQuestionCode = "IP.1"
                        ))),
                sectOvpRawScore = (5L),
                sectOgpRawScore = (5L),
                lowScoreNeedAttnInd = ("YES"),
                sectOtherRawScore = (10L),
                oasysQuestions = setOf(OasysQuestion(
                        refQuestion = RefQuestion(
                                refQuestionCode = "IP.2"))))
    }
    
    private fun setupValidFullSentencePlanDto():FullSentencePlanDto{
         return FullSentencePlanDto(
                oasysSetId = oasysSetId,
                createdDate = dateCreated,
                completedDate = dateCompleted,
                objectives =  emptySet(),
                questions = mutableMapOf(
                        "IP.1" to QuestionDto(
                                refQuestionCode = "IP.1"),
                        "IP.2" to QuestionDto(
                                refQuestionCode = "IP.2",
                                answers = setOf(AnswerDto()))))
    }

    private fun setupValidFullSentencePlanSummaryDto():FullSentencePlanSummaryDto {
        return FullSentencePlanSummaryDto(
                oasysSetId = oasysSetId,
                createdDate = dateCreated,
                completedDate = dateCompleted)
    }
}