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

        val oasysQuestion = OasysQuestion(refQuestion = RefQuestion(refQuestionCode = "1.1"))
        val oasysAnswer = OasysAnswer(
                refAnswer = RefAnswer(
                        refAnswerCode = "NO",
                        displaySort = 5L,
                        ogpScore = 1L,
                        ovpScore = 2L,
                        qaRawScore = 3L,
                        refSectionAnswer = "No"))

        oasysQuestion.oasysAnswer = oasysAnswer
        oasysAnswer.oasysQuestion = oasysQuestion

        every { questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetId, questions) } returns listOf(oasysQuestion)

        val assessmentQuestionDto = service.getAnswersForQuestions(oasysSetId, questions)

        assertThat(assessmentQuestionDto.assessmentId).isEqualTo(oasysSetId)
        assertThat(assessmentQuestionDto.questionAnswers).hasSize(1)
        val questionDto = assessmentQuestionDto.questionAnswers.first()
        assertThat(questionDto.refQuestionCode).isEqualTo("1.1")
        assertThat(questionDto.answer?.staticText).isEqualTo("No")
        assertThat(questionDto.answer?.freeFormText).isNull()
        assertThat(questionDto.answer?.refAnswerCode).isEqualTo("NO")
        assertThat(questionDto.answer?.ogpScore).isEqualTo(1L)
        assertThat(questionDto.answer?.ovpScore).isEqualTo(2L)
        assertThat(questionDto.answer?.qaRawScore).isEqualTo(3L)


        verify(exactly = 1) { questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetId, questions)  }
    }

}