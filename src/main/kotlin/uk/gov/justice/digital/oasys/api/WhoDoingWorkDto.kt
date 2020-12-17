package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.SspWhoDoWorkPivot

data class WhoDoingWorkDto(
  val code: String? = null,
  val description: String? = null,
  val comments: String? = null
) {

  companion object {

    fun from(sspWhoDoWorkPivot: Set<SspWhoDoWorkPivot?>?): Set<WhoDoingWorkDto> {
      return sspWhoDoWorkPivot?.mapNotNull { from(it) }?.toSet().orEmpty()
    }

    private fun from(sspWhoDoWorkPivot: SspWhoDoWorkPivot?): WhoDoingWorkDto? {
      return WhoDoingWorkDto(
        code = sspWhoDoWorkPivot?.whoDoWork?.refElementCode,
        comments = sspWhoDoWorkPivot?.comments,
        description = sspWhoDoWorkPivot?.whoDoWork?.refElementDesc
      )
    }
  }
}
