package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "COURT")
data class Court(
  @Id @Column(name = "COURT_PK")
  val courtPk: Long? = null,

  @Column(name = "COURT_CODE")
  val courtCode: String? = null,

  @Column(name = "COURT_NAME")
  val courtName: String? = null,

  @Column(name = "START_DATE")
  val startDate: Time? = null,

  @Column(name = "END_DATE")
  val endDate: Time? = null,

  @Column(name = "GENERAL_CODE")
  val generalCode: String? = null

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Court) return false
    return courtPk == other.courtPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
