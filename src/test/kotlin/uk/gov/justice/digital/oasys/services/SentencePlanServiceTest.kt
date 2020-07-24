package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.BasicSentencePlanObj
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import java.time.LocalDate
import java.time.LocalDateTime


class SentencePlanServiceTest {

    var assessmentRepository: AssessmentRepository = mockk()
    var sectionService: SectionService = mockk()
    var offenderService: OffenderService= mockk()

    var service: SentencePlanService = SentencePlanService(offenderService, assessmentRepository, sectionService)

    @Test
    fun `return Basic Sentence Plans`() {
        every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns 1L
        every { assessmentRepository.getAssessmentsForOffender(1L, null, null, null, null) } returns setOf(setupAssessment())

        val basicSentencePlan = service.getBasicSentencePlansForOffender("OASYS", "1")?.first()

        assertThat(basicSentencePlan.basicSentencePlanItems).hasSize(1)
        assertThat(basicSentencePlan.sentencePlanId).isEqualTo(1234L)
        assertThat(basicSentencePlan.createdDate).isEqualTo(LocalDate.of(2020,1,1))

        verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(1L,null, null, null, null)  }
    }

    @Test
    fun `return Latest Basic Sentence Plans`() {
        every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns 1L
        every { assessmentRepository.getLatestAssessmentForOffender(1L) } returns setupAssessment()

        val basicSentencePlan = service.getLatestBasicSentencePlanForOffender("OASYS", "1")

        assertThat(basicSentencePlan.sentencePlanId).isEqualTo(1234L)
        assertThat(basicSentencePlan.createdDate).isEqualTo(LocalDate.of(2020,1,1))

        verify(exactly = 1) { assessmentRepository.getLatestAssessmentForOffender(1L)  }
    }

    private fun setupAssessment(): Assessment {
        val created = LocalDateTime.of(2020,1,1,14,0)
        val dateOpenend = LocalDateTime.of(2020,2,1,14,0)
        val dateCompleted = LocalDateTime.of(2020,12,1,14,0)
        return Assessment(oasysSetPk = 1234L,
                assessmentStatus = "STATUS",
                assessmentType = "LAYER_3",
                createDate = created,
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
                                dateOpened = dateOpenend,
                                dateCompleted = dateCompleted,
                                problemAreaCompInd = "Y",
                                createDate = created
                        )
                )
        )
    }

}