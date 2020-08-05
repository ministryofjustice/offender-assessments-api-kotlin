package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.Section
import java.time.LocalDateTime

data class FullSentencePlanSummaryDto (
    var oasysSetId: Long? = null,
    var createdDate: LocalDateTime? = null,
    var completedDate: LocalDateTime? = null
){
    companion object {
        fun from(assessment: Assessment, section: Section?): FullSentencePlanSummaryDto? {
            return if (assessment.sspObjectivesInSets.isNullOrEmpty() && section == null) {
                null
            } else FullSentencePlanSummaryDto(
                    assessment.oasysSetPk,
                    assessment.createDate,
                    assessment.dateCompleted)
        }
    }
}
