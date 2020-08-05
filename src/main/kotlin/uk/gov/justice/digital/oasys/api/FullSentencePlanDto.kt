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
            val sentencePlanFields = mutableMapOf<String?, QuestionDto?>()
            val questions: List<OasysQuestion>? = section?.oasysQuestions?.toMutableList()
                    ?.sortedWith(compareBy { it.refQuestion?.displaySort })
            questions?.forEach { sentencePlanFields[it.refQuestion?.refQuestionCode] = QuestionDto.from(it) }

            if (section?.refSection != null) {
                section.refSection?.refQuestions?.forEach { questionRef -> sentencePlanFields.putIfAbsent(questionRef.refQuestionCode, QuestionDto.from(questionRef)) }
            }
            return FullSentencePlanDto(
                    assessment?.oasysSetPk,
                    assessment?.createDate,
                    assessment?.dateCompleted,
                    ObjectiveDto.from(assessment?.sspObjectivesInSets),
                    sentencePlanFields)
        }
    }

}