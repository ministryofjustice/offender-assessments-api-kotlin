package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

@DisplayName("Section Answers DTO tests")
class SectionAnswersDtoTest {
  @Test
  fun `Builds valid Section Answer DTO from Entity using free text if answers are empty`() {
    val oasysSetPk: Long = 1
    val oasysQuestion1 = OasysQuestion(
      oasysQuestionPk = 1L,
      refQuestion = RefQuestion(
        refQuestionUk = 3L,
        refSectionCode = "ROSH",
        refSectionQuestion = "Question text",
        refQuestionCode = "R2.5"
      )
    )
    val oasysAnswer1 = OasysAnswer(
      refAnswer = RefAnswer(refAnswerCode = "DK", refSectionAnswer = "Don't know", refQuestionCode = "R2.5")
    )
    oasysQuestion1.oasysAnswers = mutableSetOf(oasysAnswer1)
    oasysAnswer1.oasysQuestion = oasysQuestion1

    val oasysQuestion2 = OasysQuestion(
      oasysQuestionPk = 2L,
      freeFormatAnswer = "Free form answer",
      refQuestion = RefQuestion(
        refQuestionUk = 4L,
        refSectionCode = "ROSH",
        refSectionQuestion = "Question text",
        refQuestionCode = "R3.1"
      )
    )
    val oasysAnswer2 = OasysAnswer(
      refAnswer = RefAnswer(refAnswerCode = "2")
    )
    oasysQuestion2.oasysAnswers = mutableSetOf()
    oasysAnswer2.oasysQuestion = oasysQuestion2

    val sectionAnswers = SectionAnswersDto.from(oasysSetPk, setOf(oasysQuestion1, oasysQuestion2))
    assertThat(sectionAnswers.assessmentId).isEqualTo(1)
    assertThat(sectionAnswers.sections).hasSize(1)
    assertThat(sectionAnswers.sections["ROSH"])
      .containsExactly(
        QuestionAnswerDto(
          refQuestionCode = "R2.5",
          questionText = "Question text",
          refAnswerCode = "DK",
          staticText = "Don't know",
          freeFormText = null
        ),
        QuestionAnswerDto(
          refQuestionCode = "R3.1",
          questionText = "Question text",
          refAnswerCode = null,
          staticText = null,
          freeFormText = "Free form answer"
        )
      )
  }
}
