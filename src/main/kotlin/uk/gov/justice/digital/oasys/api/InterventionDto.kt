package uk.gov.justice.digital.oasys.api

data class InterventionDto (
        val copiedForward: Boolean? = null,
        val interventionComment: String? = null,
        val timescale: RefElementDto? = null,
        val interventionCode: String? = null,
        val interventionDescription: String? = null,
        val whoDoingWork: Set<WhoDoingWorkDto?>? = null,
        val interventionMeasure: InterventionMeasureDto? = null
) {

}
