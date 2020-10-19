package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer

@DisplayName("Assessment Answers DTO Tests")
class AssessmentAnswersDtoTest {

    @Test
    fun `Builds valid AnswerDto with only free form text`() {

        val oasysSetPk: Long = 1
        val oasysQuestion1 = OasysQuestion(oasysQuestionPk = 1)
        val oasysAnswer1 = OasysAnswer(
                refAnswer = RefAnswer(refAnswerCode = "1"))

        oasysQuestion1.oasysAnswer = oasysAnswer1
        oasysAnswer1.oasysQuestion = oasysQuestion1

        val oasysQuestion2 = OasysQuestion(oasysQuestionPk = 2)
        val oasysAnswer2 = OasysAnswer(
                refAnswer = RefAnswer(refAnswerCode = "2"))

        oasysQuestion2.oasysAnswer = oasysAnswer2
        oasysAnswer2.oasysQuestion = oasysQuestion2

        val answers = AssessmentAnswersDto.from(oasysSetPk, setOf(oasysQuestion1, oasysQuestion2))
        Assertions.assertThat(answers.assessmentId).isEqualTo(1)
        Assertions.assertThat(answers.questionAnswers).hasSize(2)
    }

}