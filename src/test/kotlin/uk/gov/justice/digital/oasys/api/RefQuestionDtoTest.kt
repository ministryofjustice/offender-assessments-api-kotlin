package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

@DisplayName("Reference Question DTO Tests")
class RefQuestionDtoTest {

  @Test
  fun `Builds valid Reference Question DTOs from Entities`() {
    val questions = setupQuestions()
    val referenceQuestionDtos = RefQuestionDto.from(questions)
    assertThat(referenceQuestionDtos).isEqualTo(setupValidDtos())
  }

  @Test
  fun `Builds valid Reference Question DTO from null entity`() {
    val refQuestionDto = RefQuestionDto.from(null)
    assertThat(refQuestionDto).isEmpty()
  }

  private fun setupQuestions(): Collection<RefQuestion> {
    val question = setupQuestion()
    return listOf(question, question.copy(refQuestionCode = "3"))
  }

  private fun setupQuestion(): RefQuestion {
    return RefQuestion(
      displaySort = 2L,
      mandatoryInd = "Y",
      qaWeighting = 12L,
      refQuestionCode = "Any Code",
      refQuestionUk = 24L,
      refSectionQuestion = "Any String",
      refAnswers = emptyList()
    )
  }

  private fun setupValidDtos(): Collection<RefQuestionDto> {
    val dto = setupValidDto()
    return listOf(dto, dto.copy(refQuestionCode = "3"))
  }

  private fun setupValidDto(): RefQuestionDto {
    return RefQuestionDto(
      refDisplaySort = 2L,
      refMandatoryIndicator = true,
      refQAWeighting = 12L,
      refQuestionCode = "Any Code",
      refQuestionId = 24L,
      refQuestionText = "Any String",
      refAnswers = emptyList()
    )
  }
}
