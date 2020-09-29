package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.*

@Entity
@Table(name = "SSP_WHO_DO_WORK_PIVOT")
class SspWhoDoWorkPivot (
        @Id
        @Column(name = "SSP_WHO_DO_WORK_PIVOT_PK")
        private var sspWhoDoWorkPivotPk: Long? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "WHO_WORK_OBJ_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "WHO_WORK_OBJ_ELM", referencedColumnName = "REF_ELEMENT_CODE")) val whoDoWork: RefElement? = null,

        @Column(name = "COMMENTS") val comments: String? = null,

        @Column(name = "SSP_INTERVENTION_IN_SET_PK")
        private val sspInterventionInSetPk: Long? = null
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SspWhoDoWorkPivot) return false
        return sspWhoDoWorkPivotPk == other.sspWhoDoWorkPivotPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
