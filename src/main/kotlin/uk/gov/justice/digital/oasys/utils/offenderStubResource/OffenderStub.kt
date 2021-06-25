package uk.gov.justice.digital.oasys.utils.offenderStubResource

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "CMS_STUB_OFFENDER")
data class OffenderStub(
  @Id
  @Column(name = "CMS_STUB_OFFENDER_PK")
  val offenderPk: Long,

  @Column(name = "CMS_PROB_NUMBER")
  val crn: String,

  @Column(name = "PNC")
  val pnc: String,

  @Column(name = "FAMILY_NAME")
  val familyName: String,

  @Column(name = "FORENAME_1")
  val forename1: String? = null,

  @Column(name = "LAO_INDICATOR")
  val laoIndicator: String? = null,

  @Column(name = "CT_AREA_EST_CODE")
  val areaCode: String = "WWS",

  @Column(name = "CREATE_DATE")
  val createDate: LocalDateTime = LocalDateTime.now(),

  @Column(name = "CREATE_USER")
  val createUser: String = "STUARTWHITLAM",

  @Column(name = "LASTUPD_DATE")
  val updatedDate: LocalDateTime = LocalDateTime.now(),

  @Column(name = "LASTUPD_USER")
  val updatedUser: String = "STUARTWHITLAM",
)
