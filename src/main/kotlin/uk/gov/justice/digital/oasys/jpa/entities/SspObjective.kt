package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "SSP_OBJECTIVE")
data class SspObjective (

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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SspObjective) return false
        return sspObjectivePk == o.sspObjectivePk
    }

    override fun hashCode(): Int {
        return 31
    }

}
