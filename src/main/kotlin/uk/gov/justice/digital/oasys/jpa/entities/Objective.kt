package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OBJECTIVE")
data class Objective(
  @Id
  @Column(name = "OBJECTIVE_CODE")
  val objectiveCode: String? = null,

  @Column(name = "OBJECTIVE_UK")
  private var objectiveUk: Long? = null,

  @Column(name = "OBJECTIVE_DESC")
  val objectiveDesc: String? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "OBJECTIVE_HEADING_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OBJECTIVE_HEADING_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val objectiveHeading: RefElement? = null,

  @Column(name = "START_DATE")
  private val startDate: LocalDateTime? = null,

  @Column(name = "END_DATE")
  private val endDate: LocalDateTime? = null

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Objective) return false
    return objectiveCode == other.objectiveCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
