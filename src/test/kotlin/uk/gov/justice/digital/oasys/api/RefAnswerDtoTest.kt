package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer

@DisplayName("Reference Answer DTO Tests")
class RefAnswerDtoTest {

    @Test
    fun `Builds valid Reference Answer DTO from Entity`() {
        val answers = setUpAnswers()
        val dto = RefAnswerDto.from(answers)
        Assertions.assertThat(dto).isEqualTo(setupValidDto())
    }

    private fun setupValidDto(): Collection<RefAnswerDto>? {
        return listOf(
                RefAnswerDto(refAnswerCode = "Any Code",
                        refAnswerId = 10L,
                        refDisplaySort = 20L ),
                RefAnswerDto(refAnswerCode = "Any Code",
                        refAnswerId = 30L,
                        refDisplaySort = 40L))
    }

    private fun setUpAnswers(): Collection<RefAnswer?>? {
        return listOf(
                RefAnswer(refAnswerCode = "Any Code",
                        refAnswerUk = 10L,
                        displaySort = 20L ),
                RefAnswer(refAnswerCode = "Any Code",
                        refAnswerUk = 30L,
                        displaySort = 40L))
    }
}