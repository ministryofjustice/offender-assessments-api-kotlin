package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer

@DisplayName("Reference Answer DTO Tests")
class RefAnswerDtoTest {

  @Test
  fun `Builds valid Reference Answer DTOs from Entities`() {
    val answers = setUpAnswers()
    val dtos = RefAnswerDto.from(answers)
    assertThat(dtos).isEqualTo(setupValidDtos())
  }

  @Test
  fun `Builds valid Reference Answer DTO from null entity`() {
    val refAnswerDto = RefAnswerDto.from(null)
    assertThat(refAnswerDto).isEmpty()
  }

  private fun setUpAnswers(): Collection<RefAnswer?>? {
    val answer = setUpAnswer()
    return listOf(answer, answer.copy(refAnswerCode = "New Code"))
  }

  private fun setUpAnswer(): RefAnswer {
    return RefAnswer(
      refAnswerCode = "Any Code",
      refAnswerUk = 10L,
      displaySort = 20L
    )
  }

  private fun setupValidDtos(): Collection<RefAnswerDto>? {
    val dto = setupValidDto()
    return listOf(dto, dto.copy(refAnswerCode = "New Code"))
  }

  private fun setupValidDto(): RefAnswerDto {
    return RefAnswerDto(
      refAnswerCode = "Any Code",
      refAnswerId = 10L,
      refDisplaySort = 20L
    )
  }
}
