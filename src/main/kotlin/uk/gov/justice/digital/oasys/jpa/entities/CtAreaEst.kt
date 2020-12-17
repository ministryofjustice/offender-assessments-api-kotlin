package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "CT_AREA_EST")
data class CtAreaEst(
  @Column(name = "CT_AREA_EST_UK")
  var ctAreaEstUk: Long? = null,
  @javax.persistence.Id
  @Column(name = "CT_AREA_EST_CODE")
  val ctAreaEstCode: String? = null,

  @Column(name = "AREA_EST_CTID")
  val areaEstCtid: String? = null,

  @Column(name = "AREA_EST_NAME")
  val areaEstName: String? = null,

  @Column(name = "START_DATE")
  val startDate: Time? = null,

  @Column(name = "END_DATE")
  val endDate: Time? = null,

  @Column(name = "LAO_CONTACT_NUMBER")
  val laoContactNumber: String? = null,

  @Column(name = "SHOW_SDR_OBJ_IND")
  val showSdrObjInd: String? = null,

  @Column(name = "SHOW_SDR_HINTS_IND")
  val showSdrHintsInd: String? = null,

  @Column(name = "SHOW_CRIM_GRAPH_IND")
  val showCrimGraphInd: String? = null,

  @Column(name = "SDR_CONCERN_NONE_IND")
  private val sdrConcernNoneInd: String? = null,

  @Column(name = "SDR_CONCERN_SUICIDE_IND")
  private val sdrConcernSuicideInd: String? = null,

  @Column(name = "SDR_CONCERN_SELFHARM_IND")
  private val sdrConcernSelfharmInd: String? = null,

  @Column(name = "SDR_CONCERN_CUSTODY_IND")
  private val sdrConcernCustodyInd: String? = null,

  @Column(name = "SDR_CONCERN_HOSTEL_IND")
  private val sdrConcernHostelInd: String? = null,

  @Column(name = "SDR_CONCERN_VULNERABLE_IND")
  private val sdrConcernVulnerableInd: String? = null,

  @Column(name = "DATA_EXPORT_PATH")
  private val dataExportPath: String? = null,

  @Column(name = "NOMIS_CODE")
  private val nomisCode: String? = null,

  @Column(name = "CHECKSUM")
  private val checksum: String? = null,

  @Column(name = "CREATE_DATE")
  private val createDate: Time? = null,

  @Column(name = "CREATE_USER")
  private val createUser: String? = null,

  @Column(name = "LASTUPD_DATE")
  private val lastupdDate: Time? = null,

  @Column(name = "LASTUPD_USER")
  private val lastupdUser: String? = null,

  @Column(name = "PSR_LOGO_FILENAME")
  private val psrLogoFilename: String? = null,

  @Column(name = "RUN_QA_SAMPLE_IND")
  private val runQaSampleInd: String? = null,

  @Column(name = "BCS_ON_RECEPTION_IND")
  private val bcsOnReceptionInd: String? = null
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CtAreaEst) return false
    return ctAreaEstCode == other.ctAreaEstCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
