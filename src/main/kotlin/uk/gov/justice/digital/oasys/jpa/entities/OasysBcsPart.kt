package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OASYS_BCS_PART")
data class OasysBcsPart(

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
  val bcsPartUserArea: String? = null,

  @Column(name = "BCS_PART_USER_POSITION")
  val bcsPartUserPosition: String? = null,

  @Column(name = "BCS_PART_COMP_DATE")
  val bcsPartCompDate: LocalDateTime? = null,

  @Column(name = "OASYS_SET_PK")
  val oasysSetPk: Long? = null,

  @Column(name = "CHECKSUM")
  val checksum: String? = null,

  @Column(name = "CREATE_DATE")
  val createDate: LocalDateTime? = null,

  @Column(name = "LASTUPD_DATE")
  val lastupdDate: LocalDateTime? = null,

  @Column(name = "CREATE_USER")
  val createUser: String? = null,

  @Column(name = "LASTUPD_USER")
  val lastupdUser: String? = null,

  @Column(name = "PRA_COMPLETE")
  val praComplete: String? = null,

  @Column(name = "PRA_COMP_USER")
  val praCompUser: String? = null,

  @Column(name = "PRA_COMP_DATE")
  val praCompDate: LocalDateTime? = null,

  @Column(name = "LOCK_INCOMPLETE_REASON_CAT")
  val lockIncompleteReasonCat: String? = null,

  @Column(name = "LOCK_INCOMPLETE_REASON_ELM")
  val lockIncompleteReason: String? = null,

  @Column(name = "LOCK_INCOMPLETE_OTHER_TEXT")
  val lockIncompleteOtherText: String? = null,

  @OneToOne
  @JoinColumn(name = "PART1_CHECKED_USER", referencedColumnName = "OASYS_USER_CODE")
  val part1CheckedUser: OasysUser? = null,

  @OneToOne
  @JoinColumn(name = "BCS_PART_USER", referencedColumnName = "OASYS_USER_CODE")
  val bcsPartUser: OasysUser? = null

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
