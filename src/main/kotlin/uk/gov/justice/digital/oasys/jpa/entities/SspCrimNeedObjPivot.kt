package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.*

@Entity
@Table(name = "SSP_CRIM_NEED_OBJ_PIVOT")
class SspCrimNeedObjPivot (

        @Id
        @Column(name = "SSP_CRIM_NEED_OBJ_PIVOT_PK")
        private var sspCrimNeedObjPivotPk: Long? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "CRIMINOGENIC_NEED_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "CRIMINOGENIC_NEED_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val criminogenicNeed: RefElement? = null,

        @Column(name = "DISPLAY_SORT")
        private val displaySort: Long? = null,

        @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
        private val sspObjectivesInSetPk: Long? = null

        ) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is SspCrimNeedObjPivot) return false
        return sspCrimNeedObjPivotPk == o.sspCrimNeedObjPivotPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
