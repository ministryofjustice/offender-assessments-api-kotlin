package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("Risk DTO Tests")
class RiskDtoTest {

  private val created = LocalDateTime.now()
  private val completed = created.plusMonths(3)
  private val voided = created.plusMonths(4)

  @Test
  fun `build Risk DTO from Assessment DTO and AssessmentAnswers DTO`() {
    val assessment = setupAssessment()
    val answers = setupAnswers()
    val riskDto = RiskDto.from(assessment, answers)
    assertThat(riskDto.oasysSetId).isEqualTo(assessment.assessmentId)
    assertThat(riskDto.sara?.riskQuestions).isEmpty()
  }

  @Test
  fun `build Risk DTO from Assessment Summary DTO and AssessmentAnswers DTO`() {
    val assessmentSummary = setupAssessmentSummary()
    val answers = setupAnswers()
    val riskDto = RiskDto.from(assessmentSummary, answers)
    assertThat(riskDto.oasysSetId).isEqualTo(assessmentSummary.assessmentId)
    assertThat(riskDto.sara?.riskQuestions).isEmpty()
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

}
