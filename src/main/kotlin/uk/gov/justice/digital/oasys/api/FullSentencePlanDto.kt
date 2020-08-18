package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.Section
import java.time.LocalDateTime

data class FullSentencePlanDto (
        var oasysSetId: Long? = null,
        var createdDate: LocalDateTime? = null,
        var completedDate: LocalDateTime? = null,
        var objectives: Set<ObjectiveDto?>? = null,
        var questions: MutableMap<String?, QuestionDto?>? = null
) {

    companion object {

        fun from(assessment: Assessment?, section: Section?): FullSentencePlanDto? {
            if (assessment?.sspObjectivesInSets.isNullOrEmpty() && section == null) {
                return null
            }
            return FullSentencePlanDto(
                    assessment?.oasysSetPk,
                    assessment?.createDate,
                    assessment?.dateCompleted,
                    ObjectiveDto.from(assessment?.sspObjectivesInSets),
                    getSentencePlanFieldsFrom(section))
        }

        private fun getSentencePlanFieldsFrom(section: Section?): MutableMap<String?, QuestionDto?> {
            val sentencePlanFields = getSentencePlanFieldsFrom(section?.oasysQuestions)
            if (section?.refSection != null) {
                section.refSection?.refQuestions?.forEach {refQuestion ->
                    sentencePlanFields.putIfAbsent(refQuestion.refQuestionCode, QuestionDto.from(refQuestion)) }
            }
            return sentencePlanFields
        }

        private fun getSentencePlanFieldsFrom(oasysQuestions: Set<OasysQuestion>?): MutableMap<String?, QuestionDto?> {
            val questions = getSortedQuestionsListFrom(oasysQuestions)
            val oasysQuestionFields = mutableMapOf<String?, QuestionDto?>()
            questions?.forEach {oasysQuestion ->
                oasysQuestionFields[oasysQuestion.refQuestion?.refQuestionCode] = QuestionDto.from(oasysQuestion)
            }
            return oasysQuestionFields
        }

        private fun getSortedQuestionsListFrom(oasysQuestions: Set<OasysQuestion>?): List<OasysQuestion>? {
            return oasysQuestions?.toMutableList()
                    ?.sortedWith(compareBy { it.refQuestion?.displaySort })
        }
    }
}
