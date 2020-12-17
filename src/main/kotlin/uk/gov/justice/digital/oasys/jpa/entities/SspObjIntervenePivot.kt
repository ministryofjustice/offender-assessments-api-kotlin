package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "SSP_OBJ_INTERVENE_PIVOT")
data class SspObjIntervenePivot(

  @Id
  @Column(name = "SSP_OBJ_INTERVENE_PIVOT_PK")
  private var sspObjIntervenePivotPk: Long? = null,

  @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
  private val sspObjectivesInSetPk: Long? = null,

  @Column(name = "DELETED_IND")
  private val deletedInd: String? = null,

  @OneToOne
  @JoinColumn(name = "SSP_INTERVENTION_IN_SET_PK", referencedColumnName = "SSP_INTERVENTION_IN_SET_PK") val sspInterventionInSet: SspInterventionInSet? = null

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SspObjIntervenePivot) return false
    return sspObjIntervenePivotPk == other.sspObjIntervenePivotPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
