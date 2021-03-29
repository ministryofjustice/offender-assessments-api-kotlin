package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Risk DTO Tests")
class RiskAssessmentAnswersDtoTest {

  @Test
  fun `build RiskAssessmentAnswers DTO from ROSHA answers`() {
    val riskAssessmentAnswersDto = RiskAssessmentAnswersDto.fromRosha(roshaAnswers())
    assertThat(riskAssessmentAnswersDto.oasysSetId).isEqualTo(1111)
    assertThat(riskAssessmentAnswersDto.riskQuestions?.size).isEqualTo(10)
    assertThat(riskAssessmentAnswersDto.riskQuestions?.map { it.refQuestionCode })
      .containsExactlyInAnyOrder("SUM6.1.2", "SUM6.2.1", "SUM6.2.2", "SUM6.3.1", "SUM6.3.2", "SUM6.4.1", "SUM6.4.2", "SUM6.5.2", "FA31", "FA32")
  }

  @Test
  fun `build RiskAssessmentAnswers DTO from SARA answers`() {
    val riskAssessmentAnswersDto = RiskAssessmentAnswersDto.fromSara(saraAnswers())
    assertThat(riskAssessmentAnswersDto.oasysSetId).isEqualTo(3333)
    assertThat(riskAssessmentAnswersDto.riskQuestions?.map { it.refQuestionCode })
      .containsExactlyInAnyOrder("SR76.1.1", "SR77.1.1")
  }

  private fun roshaAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1111,
      questionAnswers = listOf(
        QuestionDto(refQuestionCode = "SUM6.1.2"),
        QuestionDto(refQuestionCode = "SUM6.2.1"),
        QuestionDto(refQuestionCode = "SUM6.2.2"),
        QuestionDto(refQuestionCode = "SUM6.3.1"),
        QuestionDto(refQuestionCode = "SUM6.3.2"),
        QuestionDto(refQuestionCode = "SUM6.4.1"),
        QuestionDto(refQuestionCode = "SUM6.4.2"),
        QuestionDto(refQuestionCode = "SUM6.5.2"),
        QuestionDto(refQuestionCode = "FA31"),
        QuestionDto(refQuestionCode = "FA32")
      )
    )
  }

  private fun saraAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 3333,
      questionAnswers = listOf(
        QuestionDto(refQuestionCode = "SR76.1.1"),
        QuestionDto(refQuestionCode = "SR77.1.1")
      )
    )
  }
}
