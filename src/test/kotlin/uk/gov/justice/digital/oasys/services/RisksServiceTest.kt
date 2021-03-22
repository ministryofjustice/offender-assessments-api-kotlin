package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.api.QuestionDto
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.api.RiskAssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.RiskDto
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Predictor Service Tests")
class RisksServiceTest {

  private val answerService = mockk<AnswerService>()
  private val assessmentService: AssessmentService = mockk()
  private val risksService = RisksService(answerService, assessmentService)
  private val created = LocalDateTime.now()
  private val completed = created.plusMonths(3)
  private val voided = created.plusMonths(4)

  private val assessment = setupAssessment()
  private val assessmentSummary = setupAssessmentSummary()
  private val answers = setupAnswers()

  @Test
  fun `should get all Risks For Offender`() {
    every { assessmentService.getAssessmentsForOffender("OASYS", "1", null, null, null, null) } returns (listOf(assessmentSummary))
    every { answerService.getAnswersForQuestions(1234L, RisksService.riskQuestions) } returns(answers)

    val risks = risksService.getAllRisksForOffender("OASYS", "1")

    validateRisks(risks?.first())

    verify(exactly = 1) { assessmentService.getAssessmentsForOffender(any(), any(), null, null, null, null) }
    verify(exactly = 1) { answerService.getAnswersForQuestions(any(), any()) }

  }

  private fun validateRisks(riskDto: RiskDto?) {
      assertThat(riskDto?.oasysSetId).isEqualTo(1234L)
      assertThat(riskDto?.refAssessmentVersionCode).isEqualTo("Any Ref Version Code")
      assertThat(riskDto?.refAssessmentVersionNumber).isEqualTo("Any Version")
      assertThat(riskDto?.refAssessmentId).isEqualTo(1L)
      assertThat(riskDto?.assessmentCompleted).isEqualTo(true)
      assertThat(riskDto?.sara).isEqualTo(RiskAssessmentAnswersDto())
      assertThat(riskDto?.rosha).isEqualTo(null)

  }

  private fun setupAssessment(): AssessmentDto {
    return AssessmentDto(
      assessmentId = 1234L,
      refAssessmentVersionCode = "Any Ref Version Code",
      refAssessmentVersionNumber = "Any Version",
      refAssessmentId = 1L,
      completed = completed,
      voided = voided,
      assessmentStatus = "status",
    )
  }
  private fun setupAssessmentSummary(): AssessmentSummaryDto {
    return AssessmentSummaryDto(
      assessmentId = 1234L,
      refAssessmentVersionCode = "Any Ref Version Code",
      refAssessmentVersionNumber = "Any Version",
      refAssessmentId = 1L,
      completed = completed,
      voided = voided,
      assessmentStatus = "status",
    )
  }
  private fun setupAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1234L,
      questionAnswers = listOf(QuestionDto())
    )
  }

  private fun setupRiskAssessmentAnswers(): RiskAssessmentAnswersDto {
    return RiskAssessmentAnswersDto()
  }
}
