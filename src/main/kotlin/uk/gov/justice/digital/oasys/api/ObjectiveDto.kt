package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspObjective
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
        fun from(sspObjectivesInSets: Set<SspObjectivesInSet?>?): Set<ObjectiveDto?>? {
            return if (sspObjectivesInSets.isNullOrEmpty()) {
                emptySet()
            } else sspObjectivesInSets.mapNotNull { from(it) }.toSet()
        }

        fun from(sspo: SspObjectivesInSet?): ObjectiveDto? {
            return if (sspo == null) {
                null
            } else ObjectiveDto(
                    criminogenicNeeds = CriminogenicNeedDto.from(sspo.sspCrimNeedObjPivots),
                    howMeasured = sspo.howProgressMeasured,
                    interventions = InterventionDto.from(sspo.sspObjIntervenePivots),
                    objectiveCode = objectiveCodeOf(sspo.sspObjective),
                    objectiveDescription = objectiveDescriptionOf(sspo.sspObjective),
                    objectiveComment = objectiveCommentOf(sspo.sspObjective),
                    objectiveHeading = objectiveHeadingOf(sspo.sspObjective),
                    objectiveMeasure = ObjectiveMeasureDto.from(sspo.sspObjectiveMeasure),
                    objectiveType = RefElementDto.from(sspo.objectiveType))
        }

        private fun objectiveDescriptionOf(sspObjective: SspObjective?): String? {
            return if (sspObjective?.objective == null) {
                null
            } else sspObjective.objective?.objectiveDesc
        }

        private fun objectiveHeadingOf(sspObjective: SspObjective?): String? {
            return if (sspObjective?.objective == null) {
                null
            } else {
                sspObjective.objective?.objectiveHeading?.refElementDesc
            }
        }

        private fun objectiveCommentOf(sspObjective: SspObjective?): String? {
            return sspObjective?.objectiveDesc
        }

        private fun objectiveCodeOf(sspObjective: SspObjective?): String? {
            return if (sspObjective?.objective == null) {
                null
            } else sspObjective.objective?.objectiveCode
        }
    }
}
