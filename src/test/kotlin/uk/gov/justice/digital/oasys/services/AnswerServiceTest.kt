package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.QuestionAnswerDto
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion
import uk.gov.justice.digital.oasys.jpa.repositories.QuestionRepository
import uk.gov.justice.digital.oasys.services.domain.RoshMapping
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

@ExtendWith(MockKExtension::class)
@DisplayName("Question Service Tests")
class AnswerServiceTest {

  private val questionRepository: QuestionRepository = mockk()
  private val service = AnswerService(questionRepository)

  @Test
  fun `returns Assessment Answers from repository`() {
    val oasysSetId: Long = 1
    val questions = mapOf("1" to setOf("1.1"))

    every {
      questionRepository.getQuestionAnswersFromQuestionCodes(
        oasysSetId,
        questions
      )
    } returns listOf(oasysQuestion())

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

    verify(exactly = 1) { questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetId, questions) }
  }

  @Test
  fun `should get Rosh section answers for Assessment`() {
    val assessmentId = 3333L

    every {
      questionRepository.getQuestionAnswersFromQuestionCodes(
        assessmentId,
        RoshMapping.rosh(setOf(SectionHeader.ROSH_SUMMARY, SectionHeader.ROSH_SCREENING))
      )
    } returns listOf(oasysQuestion())

    val sectionAnswers = service.getRisksForAssessmentSections(
      assessmentId,
      setOf(SectionHeader.ROSH_SUMMARY, SectionHeader.ROSH_SCREENING)
    )

    assertThat(sectionAnswers.assessmentId).isEqualTo(assessmentId)
    assertThat(sectionAnswers.sections).hasSize(1)
  }

  @Test
  fun `should get Rosh section answers for Assessment rosh questions`() {
    val assessmentId = 3333L
    val questions = mapOf(
      "ROSH" to setOf("R2.1", "R2.2"),
      "ROSHSUM" to setOf(
        "SUM1",
        "SUM4",
        "SUM5",
        "SUM6"
      )
    )

    every {
      questionRepository.getQuestionAnswersFromQuestionCodes(
        assessmentId,
        questions
      )
    } returns
      listOf(
        oasysQuestion("R2.1", "ROSH"),
        oasysQuestion("SUM1", "ROSHSUM"),
        oasysQuestion("SUM6", "ROSHSUM")
      )

    val sectionAnswers = service.getSectionAnswersForQuestions(
      assessmentId,
      questions
    )

    assertThat(sectionAnswers.assessmentId).isEqualTo(assessmentId)
    assertThat(sectionAnswers.sections).hasSize(2)
    assertThat(sectionAnswers.sections["ROSH"]).containsExactly(
      QuestionAnswerDto(
        refQuestionCode = "R2.1",
        refAnswerCode = "NO",
        staticText = "No"
      )
    )
    assertThat(sectionAnswers.sections["ROSHSUM"]).containsExactly(
      QuestionAnswerDto(
        refQuestionCode = "SUM1",
        refAnswerCode = "NO",
        staticText = "No"
      ),
      QuestionAnswerDto(refQuestionCode = "SUM6", refAnswerCode = "NO", staticText = "No")
    )
  }

  private fun oasysQuestion(refQuestionCode: String? = "1.1", refSectionCode: String? = "ROSH"): OasysQuestion {
    val oasysAnswer = OasysAnswer(
      refAnswer = RefAnswer(
        refAnswerCode = "NO",
        displaySort = 5,
        ogpScore = 1,
        ovpScore = 2,
        qaRawScore = 3,
        refSectionAnswer = "No",
        refQuestionCode = refQuestionCode,
        refSectionCode = refSectionCode
      )
    )
    val oasysQuestion =
      OasysQuestion(refQuestion = RefQuestion(refQuestionCode = refQuestionCode, refSectionCode = refSectionCode))
    oasysQuestion.oasysAnswers = mutableSetOf(oasysAnswer)
    oasysAnswer.oasysQuestion = oasysQuestion

    return oasysQuestion
  }
}
