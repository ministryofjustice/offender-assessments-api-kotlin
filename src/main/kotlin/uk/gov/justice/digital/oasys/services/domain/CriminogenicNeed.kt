package uk.gov.justice.digital.oasys.services.domain

class CriminogenicNeed (
    val section: SectionHeader? = null,
    val name: String? = null,
    val riskOfHarm: Boolean? = null,
    val riskOfReoffending: Boolean? = null,
    val overThreshold: Boolean? = null,
    val flaggedAsNeed: Boolean? = null

) {
    fun anyRiskFlagged(): Boolean {
        return riskOfHarm?:false || riskOfReoffending?:false || overThreshold?:false || flaggedAsNeed?:false
    }
}