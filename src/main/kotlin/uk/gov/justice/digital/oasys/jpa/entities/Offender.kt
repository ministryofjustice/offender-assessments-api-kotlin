package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "OFFENDER")
data class Offender(
  @Id
  @Column(name = "OFFENDER_PK")
  val offenderPk: Long,

  @Column(name = "LIMITED_ACCESS_OFFENDER")
  val limitedAccessOffender: String? = null,

  @Column(name = "PNC")
  val pnc: String? = null,

  @Column(name = "CMS_PROB_NUMBER")
  val cmsProbNumber: String? = null,

  @Column(name = "CMS_PRIS_NUMBER")
  val cmsPrisNumber: String? = null,

  @Column(name = "LEGACY_CMS_PROB_NUMBER")
  val legacyCmsProbNumber: String? = null,

  @Column(name = "CRO_NUMBER")
  val croNumber: String? = null,

  @Column(name = "PRISON_NUMBER")
  val prisonNumber: String? = null,

  @Column(name = "MERGE_PNC_NUMBER")
  val mergePncNumber: String? = null,

  @Column(name = "FAMILY_NAME")
  val familyName: String? = null,

  @Column(name = "FORENAME_1")
  val forename1: String? = null,

  @Column(name = "FORENAME_2")
  val forename2: String? = null,

  @Column(name = "FORENAME_3")
  val forename3: String? = null,

  @Column(name = "MERGED_IND")
  val mergeIndicated: String? = null,

  @Column(name = "DELETED_DATE")
  val deletedDate: String? = null,

  @Column(name = "RISK_TO_OTHERS_ELM")
  val riskToOthers: String? = null,

  @Column(name = "RISK_TO_SELF_ELM")
  val riskToSelf: String? = null,

  @Transient
  var mergedOffenderPK: Long? = null
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Offender) return false
    return offenderPk == other.offenderPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
