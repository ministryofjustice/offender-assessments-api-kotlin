package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.services.domain.CrimiogenicNeed
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

class AssessmentNeedDto (
    val section: SectionHeader? = null,
    val name: String? = null,
    val overThreshold: Boolean? = null,
    val riskOfHarm: Boolean? = null,
    val riskOfReoffending: Boolean? = null,
    val flaggedAsNeed: Boolean? = null

) {
    companion object {
        fun from(needs: Collection<CrimiogenicNeed?>?): Collection<AssessmentNeedDto?> {
            return needs?.filter { it != null }?.map { from(it) }?.toSet().orEmpty()
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
