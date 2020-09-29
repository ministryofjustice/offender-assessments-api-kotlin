package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "OASYS_BCS_PART")
data class OasysBcsPart (

    @Id
    @Column(name = "OASYS_BCS_PART_PK")
    val oasysBcsPartPk: Long? = null,

    @Column(name = "BCS_PART_CAT")
    val bcsPartCat: String? = null,

    @Column(name = "BCS_PART_ELM")
    val bcsPart: String? = null,

    @Column(name = "OFFENDER_PK")
    val offenderPk: Long? = null,

    @Column(name = "BCS_PART_STATUS_CAT")
    val bcsPartStatusCat: String? = null,

    @Column(name = "BCS_PART_STATUS_ELM")
    val bcsPartStatus: String? = null,

    @Column(name = "PART1_CHECKED_IND")
    val part1CheckedInd: String? = null,

    @Column(name = "PART1_CHECKED_DATE")
    val part1CheckedDate: LocalDateTime? = null,

    @Column(name = "BCS_PART_USER_AREA")
    private val bcsPartUserArea: String? = null,

    @Column(name = "BCS_PART_USER_POSITION")
    private val bcsPartUserPosition: String? = null,

    @Column(name = "BCS_PART_COMP_DATE")
    private val bcsPartCompDate: LocalDateTime? = null,

    @Column(name = "OASYS_SET_PK")
    private val oasysSetPk: Long? = null,

    @Column(name = "CHECKSUM")
    private val checksum: String? = null,

    @Column(name = "CREATE_DATE")
    private val createDate: LocalDateTime? = null,

    @Column(name = "LASTUPD_DATE")
    private val lastupdDate: LocalDateTime? = null,

    @Column(name = "CREATE_USER")
    private val createUser: String? = null,

    @Column(name = "LASTUPD_USER")
    private val lastupdUser: String? = null,

    @Column(name = "PRA_COMPLETE")
    private val praComplete: String? = null,

    @Column(name = "PRA_COMP_USER")
    private val praCompUser: String? = null,

    @Column(name = "PRA_COMP_DATE")
    private val praCompDate: LocalDateTime? = null,

    @Column(name = "LOCK_INCOMPLETE_REASON_CAT")
    private val lockIncompleteReasonCat: String? = null,

    @Column(name = "LOCK_INCOMPLETE_REASON_ELM")
    private val lockIncompleteReason: String? = null,

    @Column(name = "LOCK_INCOMPLETE_OTHER_TEXT")
    private val lockIncompleteOtherText: String? = null,

    @OneToOne
    @JoinColumn(name = "PART1_CHECKED_USER", referencedColumnName = "OASYS_USER_CODE")
    private val part1CheckedUser: OasysUser? = null,

    @OneToOne
    @JoinColumn(name = "BCS_PART_USER", referencedColumnName = "OASYS_USER_CODE")
    private val bcsPartUser: OasysUser? = null

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OasysBcsPart) return false
        return oasysBcsPartPk == other.oasysBcsPartPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
