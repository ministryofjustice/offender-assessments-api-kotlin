package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspWhoDoWorkPivot

data class WhoDoingWorkDto (
        val code: String? = null,
        val description: String? = null,
        val comments: String? = null
) {

    companion object{
        fun from(sspWhoDoWorkPivot: Set<SspWhoDoWorkPivot?>?): Set<WhoDoingWorkDto> {
            return sspWhoDoWorkPivot?.filterNotNull()?.map {
                        WhoDoingWorkDto(
                                code = it.whoDoWork?.refElementCode,
                                comments = it.comments,
                                description = it.whoDoWork?.refElementDesc)
                    }?.toSet().orEmpty()
        }
    }
}
