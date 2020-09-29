package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "SSP_OBJECTIVE_MEASURE")
data class SspObjectiveMeasure (

        @Id
        @Column(name = "SSP_OBJECTIVE_MEASURE_PK")
        private var sspObjectiveMeasurePk: Long? = null,

        @Column(name = "OBJECTIVE_STATUS_COMMENTS") val objectiveStatusComments: String? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "OBJECTIVE_STATUS_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OBJECTIVE_STATUS_ELM", referencedColumnName = "REF_ELEMENT_CODE")) val objectiveStatus: RefElement? = null,

        @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
        private val sspObjectivesInSetPk: Long? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SspObjectiveMeasure) return false
        return sspObjectiveMeasurePk == other.sspObjectiveMeasurePk
    }

    override fun hashCode(): Int {
        return 31
    }

}
