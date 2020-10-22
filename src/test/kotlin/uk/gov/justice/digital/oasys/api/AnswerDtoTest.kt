package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer

@DisplayName("Answer DTO Tests")
class AnswerDtoTest {

    @Test
    fun `Builds valid AnswerDto with no question free form text`() {

        val oasysQuestion = OasysQuestion()
        val oasysAnswer = OasysAnswer(
                refAnswer = RefAnswer(
                        refAnswerCode = "NO",
                        defaultDisplayScore = 2L,
                        displaySort = 5L,
                        ogpScore = 1L,
                        ovpScore = 2L,
                        qaRawScore = 3L,
                        refSectionAnswer = "No"))

        oasysQuestion.oasysAnswers = mutableSetOf(oasysAnswer)
        oasysAnswer.oasysQuestion = oasysQuestion

        val answer = AnswerDto.from(oasysQuestion).first()
        assertThat(answer.staticText).isEqualTo("No")
        assertThat(answer.freeFormText).isNull()
        assertThat(answer.refAnswerCode).isEqualTo("NO")
        assertThat(answer.ogpScore).isEqualTo(1L)
        assertThat(answer.ovpScore).isEqualTo(2L)
        assertThat(answer.qaRawScore).isEqualTo(3L)
        assertThat(answer.displayOrder).isEqualTo(5L)
        assertThat(answer.freeFormText).isNull()
    }

    @Test
    fun `Builds valid AnswerDto with only free form text`() {

        val oasysQuestion = OasysQuestion(freeFormatAnswer = "Free form answer")
        val answer = AnswerDto.from(oasysQuestion).first()
        assertThat(answer?.freeFormText).isEqualTo("Free form answer")
    }



}