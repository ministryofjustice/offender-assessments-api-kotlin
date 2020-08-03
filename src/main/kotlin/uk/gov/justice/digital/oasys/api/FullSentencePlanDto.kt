package uk.gov.justice.digital.oasys.api

import java.time.LocalDateTime

data class FullSentencePlanDto (
        var oasysSetId: Long? = null,
        var createdDate: LocalDateTime? = null,
        var completedDate: LocalDateTime? = null,
        var objectives: Set<ObjectiveDto?>? = null,
        var questions: Map<String?, QuestionDto?>? = null
) {


}