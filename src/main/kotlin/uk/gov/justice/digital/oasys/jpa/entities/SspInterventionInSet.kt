package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.*

@Entity
@Table(name = "SSP_INTERVENTION_IN_SET")
data class SspInterventionInSet(

        @Id
        @Column(name = "SSP_INTERVENTION_IN_SET_PK")
        private var sspInterventionInSetPk: Long? = null,

        @Column(name = "COPIED_FORWARD_INDICATOR")
        private val copiedForwardIndicator: String? = null,

        @Column(name = "INTERVENTION_COMMENT")
        private val interventionComment: String? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "TIMESCALE_FOR_INT_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "TIMESCALE_FOR_INT_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val timescaleForIntervention: RefElement? = null,

        @Column(name = "COPIED_FROM_SSP_INT_IN_SET")
        private val copiedFromSspIntInSet: Long? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "INTERVENTION_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "INTERVENTION_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val intervention: RefElement? = null,

        @Column(name = "CF_LAST_ASSMT_INT")
        private val cfLastAssmtInt: Long? = null,

        @Column(name = "CF_ORIG_ASSMT_INT")
        private val cfOrigAssmtInt: Long? = null,

        @OneToMany
        @JoinColumn(name = "SSP_INTERVENTION_IN_SET_PK", referencedColumnName = "SSP_INTERVENTION_IN_SET_PK", insertable = false, updatable = false)
        private val sspWhoDoWorkPivot: Set<SspWhoDoWorkPivot>? = null,

        @ManyToOne
        @JoinColumn(name = "SSP_INTERVENTION_IN_SET_PK", referencedColumnName = "SSP_INTERVENTION_IN_SET_PK", insertable = false, updatable = false)
        private val sspInterventionMeasure: SspInterventionMeasure? = null
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SspInterventionInSet) return false
        return sspInterventionInSetPk == o.sspInterventionInSetPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
