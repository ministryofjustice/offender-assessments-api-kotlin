package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "SSP_OBJECTIVE")
data class SspObjective(

  @Id
  @Column(name = "SSP_OBJECTIVE_PK")
  private var sspObjectivePk: Long? = null,

  @Column(name = "OBJECTIVE_DESC") val objectiveDesc: String? = null,

  @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
  private val sspObjectivesInSetPk: Long? = null,

  @ManyToOne
  @JoinColumn(name = "OBJECTIVE_CODE")
  val objective: Objective? = null

) : Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SspObjective) return false
    return sspObjectivePk == other.sspObjectivePk
  }

  override fun hashCode(): Int {
    return 31
  }
}
