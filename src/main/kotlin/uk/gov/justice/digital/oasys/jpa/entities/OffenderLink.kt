package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "OFFENDER_LINK")
data class OffenderLink (
    @Id
    @Column(name = "OFFENDER_LINK_PK")
    val offenderLinkPk: Long? = null,

    @Column(name = "INITIATING_OFFENDER")
    val initiatingOffenderPK: Long? = null,

    @Column(name = "DECIDING_OFFENDER")
    val decidingOffenderPK: Long? = null,

    @Column(name = "MERGED_OFFENDER")
    val mergedOffenderPK: Long? = null,

    @Column(name = "REVERSED_IND")
    val reversed: Boolean? = null,

    @Column(name = "REVERSAL_ALLOWED_IND")
    val reversalAllowed: Boolean? = null,

    @Column(name = "ORIG_INITATING_PNC")
    val originalInitiatingPnc: String? = null,

    @Column(name = "DECIDER_CANCEL_IND")
    val deciderCancel: Boolean? = null,

    @Column(name = "LASTUPD_DATE")
    val lastUpdateDate: LocalDateTime? = null,

    @Column(name = "CREATE_DATE")
    val createDate: LocalDateTime? = null,

    @OneToOne
    @JoinColumns(JoinColumn(name = "LINK_TYPE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "LINK_TYPE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
    val linkType: RefElement? = null
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is OffenderLink) return false
        return offenderLinkPk == o.offenderLinkPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
