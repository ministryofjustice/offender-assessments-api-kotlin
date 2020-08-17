package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.SspInterventionInSet
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

        fun from(sspObjIntervenePivots: Set<SspObjIntervenePivot?>?): Set<InterventionDto?> {
            return sspObjIntervenePivots?.mapNotNull{ from(it?.sspInterventionInSet) }?.toSet().orEmpty()
        }

        private fun from(sspInterventionInSet: SspInterventionInSet?): InterventionDto?{
            return InterventionDto(
                    sspInterventionInSet?.copiedForwardIndicator.ynToBoolean(),
                    sspInterventionInSet?.interventionComment,
                    RefElementDto.from(sspInterventionInSet?.timescaleForIntervention),
                    sspInterventionInSet?.intervention?.refElementCode,
                    sspInterventionInSet?.intervention?.refElementDesc,
                    WhoDoingWorkDto.from(sspInterventionInSet?.sspWhoDoWorkPivot),
                    InterventionMeasureDto.from(sspInterventionInSet?.sspInterventionMeasure))
        }
    }
}