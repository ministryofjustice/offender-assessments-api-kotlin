package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.SspObjIntervenePivot

data class InterventionDto (
        val copiedForward: Boolean? = null,
        val interventionComment: String? = null,
        val timescale: RefElementDto? = null,
        val interventionCode: String? = null,
        val interventionDescription: String? = null,
        val whoDoingWork: Set<WhoDoingWorkDto?>? = null,
        val interventionMeasure: InterventionMeasureDto? = null
) {

    companion object {

        fun from(sspObjIntervenePivots: Set<SspObjIntervenePivot?>?): Set<InterventionDto?>? {
            return sspObjIntervenePivots?.mapNotNull{ it?.sspInterventionInSet}
                    ?.map {
                        InterventionDto(
                                it.copiedForwardIndicator.ynToBoolean(),
                                it.interventionComment,
                                RefElementDto.from(it.timescaleForIntervention),
                                it.intervention?.refElementCode,
                                it.intervention?.refElementDesc,
                                WhoDoingWorkDto.from(it.sspWhoDoWorkPivot),
                                InterventionMeasureDto.from(it.sspInterventionMeasure))
                    }?.toSet().orEmpty()
        }
    }
}
