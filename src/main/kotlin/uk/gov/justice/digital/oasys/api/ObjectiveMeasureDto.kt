package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspObjectiveMeasure

data class ObjectiveMeasureDto (
        val comments: String? = null,
        val status: RefElementDto? = null
) {

    companion object{

        fun from(sspObjectiveMeasure: SspObjectiveMeasure?): ObjectiveMeasureDto? {
            return if (sspObjectiveMeasure == null) {
                null
            } else ObjectiveMeasureDto(
                    comments = sspObjectiveMeasure.objectiveStatusComments,
                    status = RefElementDto.from(sspObjectiveMeasure.objectiveStatus))
        }
    }
}
