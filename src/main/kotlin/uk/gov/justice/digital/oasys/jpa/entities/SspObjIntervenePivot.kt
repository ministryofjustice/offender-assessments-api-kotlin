package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.*

@Entity
@Table(name = "SSP_OBJ_INTERVENE_PIVOT")
data class SspObjIntervenePivot (

        @Id
        @Column(name = "SSP_OBJ_INTERVENE_PIVOT_PK")
        private var sspObjIntervenePivotPk: Long? = null,

        @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
        private val sspObjectivesInSetPk: Long? = null,

        @Column(name = "DELETED_IND")
        private val deletedInd: String? = null,

        @OneToOne
        @JoinColumn(name = "SSP_INTERVENTION_IN_SET_PK", referencedColumnName = "SSP_INTERVENTION_IN_SET_PK")
        private val sspInterventionInSet: SspInterventionInSet? = null

) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SspObjIntervenePivot) return false
        return sspObjIntervenePivotPk == o.sspObjIntervenePivotPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
