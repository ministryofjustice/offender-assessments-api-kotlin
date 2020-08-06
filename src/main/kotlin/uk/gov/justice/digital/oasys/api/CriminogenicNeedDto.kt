package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspCrimNeedObjPivot

data class CriminogenicNeedDto (
        var code: String? = null,
        var description: String? = null,
        var priority: Long? = null
) {
    companion object {
        fun from(sspCrimNeedObjPivots: Set<SspCrimNeedObjPivot>?): Set<CriminogenicNeedDto?> {
            return sspCrimNeedObjPivots?.map { from(it) }?.toSet().orEmpty()
        }

      private fun from( sspCrimNeedObjPivot: SspCrimNeedObjPivot?): CriminogenicNeedDto?{
          return CriminogenicNeedDto(
                  code = sspCrimNeedObjPivot?.criminogenicNeed?.refElementCode,
                  description = sspCrimNeedObjPivot?.criminogenicNeed?.refElementDesc,
                  priority = sspCrimNeedObjPivot?.displaySort)
      }

    }
}
