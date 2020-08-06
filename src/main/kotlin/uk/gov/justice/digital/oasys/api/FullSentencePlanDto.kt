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
                    buildSentencePlanFields(section))
        }

        private fun buildSentencePlanFields(section: Section?): MutableMap<String?, QuestionDto?> {
            val questions = buildSortedQuestionsList(section?.oasysQuestions)
            val sentencePlanFields = mutableMapOf<String?, QuestionDto?>()
            populateFields(questions, sentencePlanFields)
            populateFields(section, sentencePlanFields)
            return sentencePlanFields
        }

        private fun buildSortedQuestionsList(oasysQuestions: Set<OasysQuestion>?): List<OasysQuestion>? {
            return oasysQuestions?.toMutableList()
                    ?.sortedWith(compareBy { it.refQuestion?.displaySort })
        }

        private fun populateFields(questions: List<OasysQuestion>?, sentencePlanFields: MutableMap<String?, QuestionDto?>): MutableMap<String?, QuestionDto?> {
            questions?.forEach {
                sentencePlanFields[it.refQuestion?.refQuestionCode] = QuestionDto.from(it)
            }
            return sentencePlanFields
        }

        private fun populateFields(section: Section?, sentencePlanFields: MutableMap<String?, QuestionDto?>): MutableMap<String?, QuestionDto?> {
            if (section?.refSection != null) {
                section.refSection?.refQuestions?.forEach { questionRef -> sentencePlanFields.putIfAbsent(questionRef.refQuestionCode, QuestionDto.from(questionRef)) }
            }
            return sentencePlanFields
        }
    }
}
