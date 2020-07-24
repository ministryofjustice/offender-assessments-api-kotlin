package uk.gov.justice.digital.oasys.api

import java.time.LocalDateTime

data class ObjectiveDto(
        val criminogenicNeeds: Set<CriminogenicNeedDto?>? = null,
        val interventions: Set<InterventionDto?>? = null,
        val objectiveMeasure: ObjectiveMeasureDto? = null,
        val objectiveType: RefElementDto? = null,
        val objectiveCode: String? = null,
        val objectiveDescription: String? = null,
        val objectiveHeading: String? = null,
        val objectiveComment: String? = null,
        val howMeasured: String? = null,
        val createdDate: LocalDateTime? = null
) {

}
