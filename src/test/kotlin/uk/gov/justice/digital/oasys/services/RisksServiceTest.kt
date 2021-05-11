package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.AnswerDto
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.QuestionDto
import uk.gov.justice.digital.oasys.api.RiskAssessmentAnswersDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException

@ExtendWith(MockKExtension::class)
@DisplayName("Risks Service Tests")
class RisksServiceTest {
  private val answerService = mockk<AnswerService>()
  private val assessmentRepository: AssessmentRepository = mockk()
  private val offenderService: OffenderService = mockk()
  private val risksService = RisksService(answerService, assessmentRepository, offenderService)

  @Test
  fun `should get All Risks For Offender with ROSHA`() {
    every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns (1111L)
    every { assessmentRepository.getAssessmentsForOffender(1111) } returns (listOf(roshAssessment()))
    every { answerService.getAnswersForQuestions(2222L, RisksService.riskQuestions) } returns (roshaAnswers())

    val risks = risksService.getAllRisksForOffender("OASYS", "1")
    with(risks?.first()!!) {
      assertThat(oasysSetId).isEqualTo(2222L)
      assertThat(sara).isEqualTo(null)
      assertThat(rosha).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 1111, riskQuestions = emptyList()))
    }

    verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(any(), any()) }
    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(any()) }
    verify(exactly = 1) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Test
  fun `should get All Risks For Offender with ROSHA and SARA`() {
    every { offenderService.getOffenderIdByIdentifier("OASYS", "44") } returns (55)
    every { assessmentRepository.getAssessmentsForOffender(55) } returns (listOf(assessment()))
    every { answerService.getAnswersForQuestions(1111, RisksService.riskQuestions) } returns (roshaAnswers())
    every { answerService.getAnswersForQuestions(3333, RisksService.riskQuestions) } returns (saraAnswers())

    val risks = risksService.getAllRisksForOffender("OASYS", "44")
    with(risks?.first()!!) {
      assertThat(oasysSetId).isEqualTo(1111)
      assertThat(rosha).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 1111, riskQuestions = emptyList()))
      assertThat(sara).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 3333, riskQuestions = emptyList()))
    }
    verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(any(), any()) }
    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(any()) }
    verify(exactly = 2) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Test
  fun `should get Risks For Assessment with SARA`() {
    every { assessmentRepository.getAssessment(3333) } returns (saraAssessment())
    every { answerService.getAnswersForQuestions(3333, RisksService.riskQuestions) } returns (saraAnswers())

    val risks = risksService.getRisksForAssessmentId(3333L)
    with(risks!!) {
      assertThat(oasysSetId).isEqualTo(3333L)
      assertThat(sara).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 3333, riskQuestions = emptyList()))
      assertThat(rosha).isEqualTo(null)
    }
    verify(exactly = 1) { assessmentRepository.getAssessment(any()) }
    verify(exactly = 1) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Test
  fun `should get Risks for Assessment with SARA and ROSHA`() {
    every { assessmentRepository.getAssessment(1111) } returns (assessment())
    every { answerService.getAnswersForQuestions(1111, RisksService.riskQuestions) } returns (roshaAnswers())
    every { answerService.getAnswersForQuestions(3333, RisksService.riskQuestions) } returns (saraAnswers())

    val risks = risksService.getRisksForAssessmentId(1111)

    with(risks!!) {
      assertThat(oasysSetId).isEqualTo(1111)
      assertThat(sara).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 3333, riskQuestions = emptyList()))
      assertThat(rosha).isEqualTo(RiskAssessmentAnswersDto(oasysSetId = 1111, riskQuestions = emptyList()))
    }
    verify(exactly = 1) { assessmentRepository.getAssessment(any()) }
    verify(exactly = 2) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Test
  fun `throws not found exception when no Assessments found`() {
    every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns (1111L)
    every { assessmentRepository.getAssessmentsForOffender(1111) } returns (null)

    assertThrows<EntityNotFoundException> { risksService.getAllRisksForOffender("OASYS", "1") }

    verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(any(), any()) }
    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(any()) }
    verify(exactly = 0) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Test
  fun `throws not found exception when Assessment ID not found`() {
    every { assessmentRepository.getAssessment(1111) } returns (null)

    assertThrows<EntityNotFoundException> { risksService.getRisksForAssessmentId(1111) }

    verify(exactly = 1) { assessmentRepository.getAssessment(any()) }
    verify(exactly = 0) { answerService.getAnswersForQuestions(any(), any()) }
  }

  @Nested
  @DisplayName("Child safeguarding tests")
  inner class ChildSafeguardingTest {

    @Test
    fun `should not return child safeguarding flag if ROSH section not present`() {
      every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns (1111L)
      every { assessmentRepository.getAssessmentsForOffender(1111) } returns (listOf(roshAssessment()))
      every { answerService.getAnswersForQuestions(2222L, RisksService.riskQuestions) } returns (roshaAnswers())

      val risks = risksService.getAllRisksForOffender("OASYS", "1")
      assertThat(risks?.size).isEqualTo(1)
      with(risks?.first()!!) {
        assertThat(childSafeguardingIndicated).isNull()
        assertThat(rosh).isNull()
      }
    }

    @Test
    fun `should not return child safeguarding flag if ROSH section has no answers`() {
      val assessment = assessment()
      every { assessmentRepository.getAssessment(2222L) } returns assessment
      every { answerService.getAnswersForQuestions(1111, RisksService.riskQuestions) } returns (emptyRoshAnswers())
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isNull()
      assertThat(risk?.rosh).isNull()
    }

    @Test
    fun `should return child safeguarding flag True if ROSH R2-1 is Positive`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "YES")))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isTrue
    }

    @Test
    fun `should return child safeguarding flag True if ROSH R2-1 is Positive and R2-2 is null`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "Y", "R2.2" to null)))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isTrue
    }

    @Test
    fun `should return child safeguarding flag True if ROSH R2-2 is Positive`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "N", "R2.2" to "Y")))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isTrue
    }

    @Test
    fun `should return child safeguarding flag True if both ROSH R2-1 and R2-2 are Positive`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "Y", "R2.2" to "Y")))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isTrue
    }

    @Test
    fun `should return child safeguarding flag False if ROSH R2-1 is Negative and R2-2 is null`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "N", "R2.2" to null)))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isFalse
    }

    @Test
    fun `should return child safeguarding flag False if both ROSH R2-1 and R2-2 are Negative`() {
      every { assessmentRepository.getAssessment(2222L) } returns assessment()
      every {
        answerService.getAnswersForQuestions(
          1111,
          RisksService.riskQuestions
        )
      } returns (roshAnswers(mapOf("R2.1" to "N", "R2.2" to "NO")))
      every { answerService.getAnswersForQuestions(3333L, RisksService.riskQuestions) } returns (emptyRoshAnswers())

      val risk = risksService.getRisksForAssessmentId(2222L)

      assertThat(risk?.childSafeguardingIndicated).isFalse
    }
  }

  private fun assessment(): Assessment {
    return Assessment(
      oasysSetPk = 1111,
      assessmentStatus = "COMPLETED",
      assessmentType = "LAYER_3",
      childAssessments = setOf(saraAssessment())
    )
  }

  private fun roshAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 2222L,
      assessmentStatus = "COMPLETED",
      assessmentType = "LAYER_3"
    )
  }

  private fun saraAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 3333L,
      assessmentStatus = "COMPLETED",
      assessmentType = "SARA"
    )
  }

  private fun roshaAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1111,
      questionAnswers = listOf(QuestionDto())
    )
  }

  private fun emptyRoshAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1111,
      questionAnswers = listOf(
        QuestionDto(refQuestionCode = "R2.1", answers = emptyList()),
        QuestionDto(refQuestionCode = "R2.2", answers = emptyList())
      )
    )
  }

  private fun roshAnswers(refQuestionCode: Map<String, String?>): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1111,
      questionAnswers = refQuestionCode.map {
        QuestionDto(refQuestionCode = it.key, answers = listOf(AnswerDto(it.value)))
      }.toList()
    )
  }

  private fun saraAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 3333,
      questionAnswers = listOf(QuestionDto())
    )
  }
}
