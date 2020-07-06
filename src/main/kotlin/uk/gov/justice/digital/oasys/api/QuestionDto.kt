package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion
import java.util.*
import java.util.stream.Collectors

data class QuestionDto(

        var refQuestionId: Long? = null,
        val refQuestionCode: String? = null,
        val oasysQuestionId: Long? = null,
        val displayOrder: Long? = null,
        val displayScore: Long? = null,
        val questionText: String? = null,
        val answer: AnswerDto? = null

) {

    companion object {

        fun from(oasysQuestions: Collection<OasysQuestion?>?): Set<QuestionDto?>? {
            return oasysQuestions?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
        }

        fun from(question: OasysQuestion?): QuestionDto? {
            if (question == null) return null
            val refQuestion = question?.refQuestion
            return QuestionDto(
                    refQuestion?.refQuestionUk,
                    refQuestion?.refQuestionCode,
                    question.oasysQuestionPk,
                    refQuestion?.displaySort,
                    question?.displayScore,
                    refQuestion?.refSectionQuestion,
                    AnswerDto.from(question))
        }

        fun from(refQuestion: RefQuestion?): QuestionDto? {
            return if (refQuestion == null) {
                null
            } else QuestionDto( refQuestionId = refQuestion?.refQuestionUk,
                    refQuestionCode = refQuestion?.refQuestionCode,
                    displayOrder = refQuestion?.displaySort,
                    questionText = refQuestion?.refSectionQuestion)
        }

    }

}
