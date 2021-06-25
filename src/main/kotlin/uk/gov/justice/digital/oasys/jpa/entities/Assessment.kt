package uk.gov.justice.digital.oasys.jpa.entities

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OASYS_SET")
data class Assessment(

  @Id
  @Column(name = "OASYS_SET_PK")
  val oasysSetPk: Long? = null,

  @Column(name = "ASSESSOR_NAME")
  val assessorName: String? = null,

  @Column(name = "ASSESSMENT_STATUS_ELM")
  val assessmentStatus: String? = null,

  @Column(name = "ASSESSMENT_TYPE_ELM")
  val assessmentType: String? = null,

  @Column(name = "PURPOSE_ASSESSMENT_ELM")
  val assessmentPurposeType: String? = null,

  @Column(name = "CREATE_DATE")
  val createDate: LocalDateTime? = null,

  @Column(name = "DELETED_DATE")
  val deletedDate: LocalDateTime? = null,

  @Column(name = "DATE_COMPLETED")
  val dateCompleted: LocalDateTime? = null,

  @Column(name = "ASSESSMENT_VOIDED_DATE")
  val assessmentVoidedDate: LocalDateTime? = null,

  @Column(name = "OGP_ST_WESC")
  val ogpStWesc: BigDecimal? = null,

  @Column(name = "OGP_DY_WESC")
  val ogpDyWesc: BigDecimal? = null,

  @Column(name = "OGP_TOT_WESC")
  val ogpTotWesc: BigDecimal? = null,

  @Column(name = "OVP_ST_WESC")
  val ovpStWesc: BigDecimal? = null,

  @Column(name = "OVP_DY_WESC")
  val ovpDyWesc: BigDecimal? = null,

  @Column(name = "OVP_TOT_WESC")
  val ovpTotWesc: BigDecimal? = null,

  @Column(name = "OGRS3_1YEAR")
  val ogrs31Year: BigDecimal? = null,

  @Column(name = "OGRS3_2YEAR")
  val ogrs32Year: BigDecimal? = null,

  @Column(name = "OGP_1YEAR")
  val ogp1Year: BigDecimal? = null,

  @Column(name = "OGP_2YEAR")
  val ogp2Year: BigDecimal? = null,

  @Column(name = "OVP_1YEAR")
  val ovp1Year: BigDecimal? = null,

  @Column(name = "OVP_2YEAR")
  val ovp2Year: BigDecimal? = null,

  @Column(name = "OVP_PREV_WESC")
  val ovpPrevWesc: BigDecimal? = null,

  @Column(name = "OVP_VIO_WESC")
  val ovpVioWesc: BigDecimal? = null,

  @Column(name = "OVP_NON_VIO_WESC")
  val ovpNonVioWesc: BigDecimal? = null,

  @Column(name = "OVP_AGE_WESC")
  val ovpAgeWesc: BigDecimal? = null,

  @Column(name = "OVP_SEX_WESC")
  val ovpSexWesc: BigDecimal? = null,

  @Column(name = "OSP_I_PERCENTAGE_SCORE")
  val ospIndecentPercentageScore: BigDecimal? = null,

  @Column(name = "OSP_C_PERCENTAGE_SCORE")
  val ospContactPercentageScore: BigDecimal? = null,

  @Column(name = "RSR_PERCENTAGE_SCORE")
  val rsrPercentageScore: BigDecimal? = null,

  @Column(name = "RSR_STATIC_OR_DYNAMIC")
  val rsrStaticOrDynamic: String? = null,

  @Column(name = "RSR_ALGORITHM_VERSION")
  val rsrAlgorithmVersion: Long? = null,

  // removed until OASys 6.26 is released to prod
  //  @Column(name = "CREATED_EXTERNALLY_IND")
  //  val createdExternally: String? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"))
  val assessmentVersion: RefAssessmentVersion? = null,

  @OneToMany
  @JoinColumn(name = "OASYS_SET_PK", referencedColumnName = "OASYS_SET_PK")
  val oasysSections: Collection<Section>? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OGP_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OGP_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val ogpRiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OGRS3_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OGRS3_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val ogrs3RiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OVP_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OVP_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val ovpRiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OTHER_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OTHER_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val otherRiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OSP_I_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OSP_I_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val ospIndecentRiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OSP_C_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OSP_C_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val ospContactRiskRecon: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "RSR_RISK_RECON_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "RSR_RISK_RECON_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val rsrRiskRecon: RefElement? = null,

  @ManyToOne
  @JoinColumn(name = "OASYS_ASSESSMENT_GROUP_PK")
  val group: AssessmentGroup? = null,

  @OneToMany
  @JoinColumn(name = "OASYS_SET_PK", referencedColumnName = "OASYS_SET_PK")
  val oasysBcsParts: Set<OasysBcsPart?>? = mutableSetOf<OasysBcsPart>(),

  @OneToMany
  @JoinColumn(name = "OASYS_SET_PK", referencedColumnName = "OASYS_SET_PK")
  val basicSentencePlanList: Set<BasicSentencePlanObj?>? = mutableSetOf(),

  @OneToMany
  @JoinColumn(name = "OASYS_SET_PK", referencedColumnName = "OASYS_SET_PK")
  val sspObjectivesInSets: Set<SspObjectivesInSet?>? = mutableSetOf(),

  @ManyToOne
  @JoinColumn(name = "PARENT_OASYS_SET_PK")
  val parentPk: Assessment? = null,

  @OneToMany(mappedBy = "parentPk")
  val childAssessments: Set<Assessment?> = mutableSetOf()

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Assessment) return false
    return oasysSetPk == other.oasysSetPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
