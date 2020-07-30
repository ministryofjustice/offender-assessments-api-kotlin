package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

@DisplayName("Reference Question DTO Tests")
class RefQuestionDtoTest {

    @Test
    fun `Builds valid Reference Question DTO from Entity`() {
        val question = setupQuestion()
        val dto = RefQuestionDto.from(question)
        assertThat(dto).isEqualTo(setupValidDto())
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

    private fun setupValidDto(): RefQuestionDto? {
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