package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import uk.gov.justice.digital.oasys.services.domain.NeedSeverity
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

data class AssessmentNeedDto(

  @ApiModelProperty(value = "Section Header", example = "ACCOMMODATION")
  val section: SectionHeader? = null,

  @ApiModelProperty(value = "Need name", example = "Accommodation")
  val name: String? = null,

  @ApiModelProperty(value = "Over Criminogenic Need score threshold", example = "Accommodation")
  val overThreshold: Boolean? = null,

  @ApiModelProperty(value = "Related to risk of harm", example = "true")
  val riskOfHarm: Boolean? = null,

  @ApiModelProperty(value = "Related to risk of reoffending", example = "true")
  val riskOfReoffending: Boolean? = null,

  @ApiModelProperty(value = "Low scoring area manually flagged by Offender Manager", example = "true")
  val flaggedAsNeed: Boolean? = null,

  @ApiModelProperty(value = "Need severity", example = "true")
  val severity: NeedSeverity? = null,

  @ApiModelProperty(value = "Identified as Criminogenic Need", example = "true")
  val identifiedAsNeed: Boolean? = null

) {
  companion object {
    fun from(needs: Collection<CriminogenicNeed?>?): Collection<AssessmentNeedDto> {
      return needs?.filterNotNull()?.mapNotNull { from(it) }?.toSet().orEmpty()
    }

    private fun from(need: CriminogenicNeed?): AssessmentNeedDto? {
      return AssessmentNeedDto(
        need?.section,
        need?.name,
        need?.overThreshold,
        need?.riskOfHarm,
        need?.riskOfReoffending,
        need?.flaggedAsNeed,
        need?.severity,
        need?.anyRiskFlagged()
      )
    }
  }
}
