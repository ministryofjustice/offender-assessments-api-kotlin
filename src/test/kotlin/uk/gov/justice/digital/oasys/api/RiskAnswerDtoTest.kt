package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Risk DTO Tests")
class RiskAnswerDtoTest {

  @Test
  fun `build RiskAssessmentAnswers DTO from ROSHA answers`() {
    val riskAnswerDto = RiskAnswerDto.from(answerDto())
    assertThat(riskAnswerDto.refAnswerCode).isEqualTo("code")
    assertThat(riskAnswerDto.staticText).isEqualTo("answer text")
  }

  private fun answerDto(): AnswerDto {
    return AnswerDto(
      refAnswerCode = "code",
      staticText = "answer text"
    )
  }
}
