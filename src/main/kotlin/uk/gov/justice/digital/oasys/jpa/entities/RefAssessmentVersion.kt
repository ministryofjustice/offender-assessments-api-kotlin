package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "REF_ASS_VERSION")
@IdClass(RefAssessmentVersionPK::class)
data class RefAssessmentVersion(

  @Id @Column(name = "REF_ASS_VERSION_CODE")
  var refAssVersionCode: String? = null,

  @Id
  @Column(name = "VERSION_NUMBER")
  val versionNumber: String? = null,

  @Id
  @Column(name = "REF_ASS_VERSION_UK")
  val refAssVersionUk: Long? = null,

  @Column(name = "START_DATE")
  val startDate: Time? = null,

  @Column(name = "END_DATE")
  val endDate: Time? = null,

  @Column(name = "OASYS_FORM_VERSION")
  val oasysFormVersion: Long? = null,

  @Column(name = "OASYS_SCORING_ALG_VERSION")
  val oasysScoringAlgVersion: Long? = null,

  @Column(name = "REF_MODULE_CODE")
  val refModuleCode: String? = null,

  @Column(name = "CREATE_DATE")
  val createDate: Time? = null,

  @Column(name = "CREATE_USER")
  val createUser: String? = null,

  @Column(name = "LASTUPD_DATE")
  val lastupdDate: Time? = null,

  @Column(name = "LASTUPD_USER")
  val lastupdUser: String? = null,

  @OneToMany
  @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"))
  val refSections: List<RefSection>? = null
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RefAssessmentVersion) return false
    return refAssVersionCode == other.refAssVersionCode &&
      versionNumber == other.versionNumber
  }

  override fun hashCode(): Int {
    return 31
  }
}
