package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "SSP_INTERVENTION_MEASURE")
data class SspInterventionMeasure (
        @Id @Column(name = "SSP_INTERVENTION_MEASURE_PK")
        private var sspInterventionMeasurePk: Long? = null,

        @Column(name = "INTERVENE_MEAS_COMMENT")
        private val InterventionStatusComments: String? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "INTERVENE_MEAS_CODE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "INTERVENE_MEAS_CODE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val interventionStatus: RefElement? = null,

        @Column(name = "SSP_INTERVENTION_IN_SET_PK")
        private val sspInterventionsInSetPk: Long? = null

) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SspInterventionMeasure) return false
        return sspInterventionMeasurePk == o.sspInterventionMeasurePk
    }

    override fun hashCode(): Int {
        return 31
    }

}
