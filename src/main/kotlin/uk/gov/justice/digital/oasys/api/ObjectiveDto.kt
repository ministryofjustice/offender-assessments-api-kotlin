package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspObjectivesInSet
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
    companion object {
        fun from(sspObjectivesInSets: Set<SspObjectivesInSet?>?): Set<ObjectiveDto> {
            return if (sspObjectivesInSets.isNullOrEmpty()) {
                emptySet()
            } else sspObjectivesInSets.mapNotNull { from(it) }.toSet()
        }

        private fun from(sspObjectivesInSet: SspObjectivesInSet?): ObjectiveDto? {
            return if (sspObjectivesInSet == null) {
                null
            } else ObjectiveDto(
                    criminogenicNeeds = CriminogenicNeedDto.from(sspObjectivesInSet.sspCrimNeedObjPivots),
                    howMeasured = sspObjectivesInSet.howProgressMeasured,
                    interventions = InterventionDto.from(sspObjectivesInSet.sspObjIntervenePivots),
                    objectiveCode = sspObjectivesInSet.sspObjective?.objective?.objectiveCode,
                    objectiveDescription = sspObjectivesInSet.sspObjective?.objective?.objectiveDesc,
                    objectiveComment = sspObjectivesInSet.sspObjective?.objectiveDesc,
                    objectiveHeading = sspObjectivesInSet.sspObjective?.objective?.objectiveHeading?.refElementDesc,
                    objectiveMeasure = ObjectiveMeasureDto.from(sspObjectivesInSet.sspObjectiveMeasure),
                    objectiveType = RefElementDto.from(sspObjectivesInSet.objectiveType))
        }
    }
}
