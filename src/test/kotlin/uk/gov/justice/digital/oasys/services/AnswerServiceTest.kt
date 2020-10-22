package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion
import uk.gov.justice.digital.oasys.jpa.repositories.QuestionRepository

@ExtendWith(MockKExtension::class)
@DisplayName("Question Service Tests")
class AnswerServiceTest
{

    private val questionRepository: QuestionRepository = mockk()
    private val service = AnswerService(questionRepository)


    @Test
    fun `returns Assessment Answers from repository`() {
        val oasysSetId: Long = 1
        val questions = setOf("1.1")


        val oasysAnswer = OasysAnswer(
                refAnswer = RefAnswer(
                        refAnswerCode = "NO",
                        displaySort = 5L,
                        ogpScore = 1L,
                        ovpScore = 2L,
                        qaRawScore = 3L,
                        refSectionAnswer = "No"))
        val oasysQuestion = OasysQuestion(refQuestion = RefQuestion(refQuestionCode = "1.1"))
        oasysQuestion.oasysAnswers = mutableSetOf(oasysAnswer)
        oasysAnswer.oasysQuestion = oasysQuestion

        every { questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetId, questions) } returns listOf(oasysQuestion)

        val assessmentQuestionDto = service.getAnswersForQuestions(oasysSetId, questions)

        assertThat(assessmentQuestionDto.assessmentId).isEqualTo(oasysSetId)
        assertThat(assessmentQuestionDto.questionAnswers).hasSize(1)
        val questionDto = assessmentQuestionDto.questionAnswers.first()
        assertThat(questionDto.refQuestionCode).isEqualTo("1.1")
        assertThat(questionDto.answers.toList()[0].staticText).isEqualTo("No")
        assertThat(questionDto.answers.toList()[0].freeFormText).isNull()
        assertThat(questionDto.answers.toList()[0].refAnswerCode).isEqualTo("NO")
        assertThat(questionDto.answers.toList()[0].ogpScore).isEqualTo(1L)
        assertThat(questionDto.answers.toList()[0].ovpScore).isEqualTo(2L)
        assertThat(questionDto.answers.toList()[0].qaRawScore).isEqualTo(3L)


        verify(exactly = 1) { questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetId, questions)  }
    }

}