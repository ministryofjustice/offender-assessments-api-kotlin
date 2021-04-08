package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Risk DTO Tests")
class RiskQuestionDtoTest {

  @Test
  fun `build RiskAssessmentAnswers DTO from ROSHA answers`() {
    val riskQuestionDto = RiskQuestionDto.from(questionDto())
    assertThat(riskQuestionDto.refQuestionCode).isEqualTo("SUM6.1.2")
    assertThat(riskQuestionDto.questionText).isEqualTo("question text")
    assertThat(riskQuestionDto.currentlyHidden).isEqualTo(false)
    assertThat(riskQuestionDto.disclosed).isEqualTo(true)
    assertThat(riskQuestionDto.answers).containsExactly(RiskAnswerDto())
  }

  private fun questionDto(): QuestionDto {
    return QuestionDto(
      refQuestionCode = "SUM6.1.2",
      questionText = "question text",
      currentlyHidden = false,
      disclosed = true,
      answers = listOf(AnswerDto())
    )
  }
}
