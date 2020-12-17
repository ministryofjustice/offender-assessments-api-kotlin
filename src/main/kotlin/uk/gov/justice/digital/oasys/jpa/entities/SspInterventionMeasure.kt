package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "SSP_INTERVENTION_MEASURE")
data class SspInterventionMeasure(
  @Id @Column(name = "SSP_INTERVENTION_MEASURE_PK")
  private var sspInterventionMeasurePk: Long? = null,

  @Column(name = "INTERVENE_MEAS_COMMENT")
  val interventionStatusComments: String? = null,

  @ManyToOne
  @JoinColumns(JoinColumn(name = "INTERVENE_MEAS_CODE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "INTERVENE_MEAS_CODE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val interventionStatus: RefElement? = null,

  @Column(name = "SSP_INTERVENTION_IN_SET_PK")
  private val sspInterventionsInSetPk: Long? = null

) : Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SspInterventionMeasure) return false
    return sspInterventionMeasurePk == other.sspInterventionMeasurePk
  }

  override fun hashCode(): Int {
    return 31
  }
}
