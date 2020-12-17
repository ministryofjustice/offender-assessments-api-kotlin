package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "REF_QUESTION")
@IdClass(RefQuestionPK::class)
data class RefQuestion(

  @Id
  @Column(name = "REF_ASS_VERSION_CODE")
  val refAssVersionCode: String? = null,

  @Id
  @Column(name = "VERSION_NUMBER")
  val versionNumber: String? = null,

  @Id
  @Column(name = "REF_SECTION_CODE")
  val refSectionCode: String? = null,

  @Id
  @Column(name = "REF_QUESTION_CODE")
  val refQuestionCode: String? = null,

  @Column(name = "REF_QUESTION_UK")
  val refQuestionUk: Long? = null,

  @Column(name = "DISPLAY_SORT")
  val displaySort: Long? = null,

  @Column(name = "REF_SECTION_QUESTION")
  val refSectionQuestion: String? = null,

  @Column(name = "MANDATORY_IND")
  val mandatoryInd: String? = null,

  @Column(name = "VERSION_NUMBER_PARENT")
  val versionNumberParent: String? = null,

  @Column(name = "REF_SECTION_CODE_PARENT")
  val refSectionCodeParent: String? = null,

  @Column(name = "REF_QUESTION_CODE_PARENT")
  private val refQuestionCodeParent: String? = null,

  @Column(name = "QA_WEIGHTING")
  val qaWeighting: Long? = null,

  @Column(name = "CT_AREA_EST_CODE")
  private val ctAreaEstCode: String? = null,

  @Column(name = "REF_ASS_VERSION_CODE_PARENT")
  private val refAssVersionCodeParent: String? = null,

  @OneToMany
  @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"), JoinColumn(name = "REF_QUESTION_CODE", referencedColumnName = "REF_QUESTION_CODE"))
  val refAnswers: List<RefAnswer>? = null

) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RefQuestion) return false
    return refAssVersionCode == other.refAssVersionCode &&
      versionNumber == other.versionNumber &&
      refSectionCode == other.refSectionCode &&
      refQuestionCode == other.refQuestionCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
