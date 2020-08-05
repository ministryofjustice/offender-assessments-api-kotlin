package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspCrimNeedObjPivot

data class CriminogenicNeedDto (
        var code: String? = null,
        var description: String? = null,
        var priority: Long? = null
) {
    companion object {
        fun from(sspCrimNeedObjPivots: Set<SspCrimNeedObjPivot>?): Set<CriminogenicNeedDto?> {
            return sspCrimNeedObjPivots?.map {
                CriminogenicNeedDto(
                        code = it.criminogenicNeed?.refElementCode,
                        description = it.criminogenicNeed?.refElementDesc,
                        priority = it.displaySort)
            }
                    ?.toSet().orEmpty()
        }
    }
}
