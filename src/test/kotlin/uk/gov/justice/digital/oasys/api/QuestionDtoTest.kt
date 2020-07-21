package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

@DisplayName("Question DTO Tests")
class QuestionDtoTest {

    @Test
    fun `Builds Set of Question DTOs from Oasys Question Set`() {
        val oasysQuestions = setOf(setupQuestion1098(), setupQuestion1099(), setupFreeFormQuestion())
        val validQuestions = setOf("10.98", "10.99", "IP.1")
        val questionDtos = QuestionDto.from(oasysQuestions)
        assertThat(questionDtos?.map { q -> q?.refQuestionCode }).containsExactlyInAnyOrderElementsOf(validQuestions)
    }


    @Test
    fun `Builds Question DTO from Null Entity`() {
        val oasysQuestion : OasysQuestion? = null
        val questionDto = QuestionDto.from(oasysQuestion)
        assertThat(questionDto).isNull()
    }

    @Test
    fun `Builds Question DTO ignoring null objects`() {
        val oasysQuestion : OasysQuestion? = null
        val questionDto = QuestionDto.from(setOf(oasysQuestion))
        assertThat(questionDto).isEmpty()
    }

    @Test
    fun `Builds Question DTO with free form text if present`() {
        val oasysQuestions = setOf(setupFreeFormQuestion())
        val questionDto = QuestionDto.from(oasysQuestions)?.first()
        assertThat(questionDto?.answer?.freeFormText).isEqualTo("Free form answer")
    }

    @Test
    fun `Builds Question DTO with values`() {
        val oasysQuestions = setOf(setupQuestion1098())
        val questionDto = QuestionDto.from(oasysQuestions)?.first()
        assertThat(questionDto?.refQuestionId).isEqualTo(1L)
        assertThat(questionDto?.refQuestionCode).isEqualTo("10.98")
        assertThat(questionDto?.displayOrder).isEqualTo(1L)
        assertThat(questionDto?.questionText).isEqualTo("Question 10.98")
        assertThat(questionDto?.displayScore).isEqualTo(1L)
    }

    @Test
    fun `Builds Question DTO with Answer if present`() {
        val oasysQuestions = setOf(setupQuestion1099())
        val questionDto = QuestionDto.from(oasysQuestions)?.first()
        assertThat(questionDto?.answer?.staticText).isEqualTo("No")
        assertThat(questionDto?.answer?.freeFormText).isNull()
        assertThat(questionDto?.answer?.refAnswerCode).isEqualTo("NO")
        assertThat(questionDto?.answer?.ogpScore).isEqualTo(1L)
        assertThat(questionDto?.answer?.ovpScore).isEqualTo(2L)
        assertThat(questionDto?.answer?.qaRawScore).isEqualTo(3L)
        assertThat(questionDto?.answer?.displayOrder).isEqualTo(2L)
    }

    @Test
    fun `Builds Question DTO from Ref Question`() {
        val refQuestion = setupQuestion1098().refQuestion
        val questionDto = QuestionDto.from(refQuestion)

        assertThat(questionDto?.refQuestionCode).isEqualTo("10.98")
        assertThat(questionDto?.displayOrder).isEqualTo(1L)
        assertThat(questionDto?.refQuestionId).isEqualTo(1L)
        assertThat(questionDto?.questionText).isEqualTo("Question 10.98")
    }

    private fun setupQuestion1098() : OasysQuestion {

        val question1098 = OasysQuestion(
                oasysQuestionPk = 1L,
                displayScore = 1L,
                refQuestion = RefQuestion(
                        refQuestionUk = 1L,
                        displaySort = 1L,
                        refSectionQuestion = "Question 10.98",
                        refQuestionCode = "10.98"))

        val answer1098 = OasysAnswer(
                refAnswer = RefAnswer(
                        refAnswerCode = "YES",
                        defaultDisplayScore = 1L,
                        displaySort = 1L,
                        refSectionAnswer ="Yes"))

        question1098.oasysAnswer = answer1098
        answer1098.oasysQuestion = question1098

        return question1098
    }

    private fun setupQuestion1099() : OasysQuestion {

        val question1099 = OasysQuestion(
                oasysQuestionPk = 2L,
                displayScore =2L,
                refQuestion = RefQuestion(
                        refQuestionUk = 2L,
                        refSectionQuestion = "Question 10.99",
                        refQuestionCode = "10.99"))


        val answer1099 = OasysAnswer(
                refAnswer = RefAnswer(
                        refAnswerCode = "NO",
                        ogpScore = 1L,
                        ovpScore = 2L,
                        qaRawScore = 3L,
                        defaultDisplayScore = 2L,
                        displaySort = 2L,
                        refSectionAnswer ="No"))


        question1099.oasysAnswer = answer1099
        answer1099.oasysQuestion = question1099

        return question1099
    }

    private fun setupFreeFormQuestion() : OasysQuestion {
        return OasysQuestion(
                oasysQuestionPk = 3L,
                displayScore =3L,
                freeFormatAnswer = "Free form answer",
                additionalNote = "additional note",
                refQuestion = RefQuestion(
                        refQuestionUk = 3L,
                        refSectionQuestion = "Question IP.1",
                        refQuestionCode = "IP.1"))
    }


}