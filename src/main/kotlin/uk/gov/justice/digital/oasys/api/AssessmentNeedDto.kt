package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.services.domain.CrimiogenicNeed
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

class AssessmentNeedDto (

        @ApiModelProperty(value = "Section Header", example = "ACCOMMODATION")
        val section: SectionHeader? = null,

        @ApiModelProperty(value = "Need name", example = "Accommodation")
        val name: String? = null,

        @ApiModelProperty(value = "Over Crimiogenic Need score threshold", example = "Accommodation")
        val overThreshold: Boolean? = null,

        @ApiModelProperty(value = "Related to risk of harm", example = "true")
        val riskOfHarm: Boolean? = null,

        @ApiModelProperty(value = "Related to risk of reoffending", example = "true")
        val riskOfReoffending: Boolean? = null,

        @ApiModelProperty(value = "Low scoring are flagged", example = "true")
        val flaggedAsNeed: Boolean? = null

) {
    companion object {
        fun from(needs: Collection<CrimiogenicNeed?>?): Collection<AssessmentNeedDto?> {
            return needs?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
        }

        private fun from(need: CrimiogenicNeed?): AssessmentNeedDto? {
            return AssessmentNeedDto(
                    need?.section,
                    need?.name,
                    need?.overThreshold,
                    need?.riskOfHarm,
                    need?.riskOfReoffending,
                    need?.flaggedAsNeed)
        }
    }
}
