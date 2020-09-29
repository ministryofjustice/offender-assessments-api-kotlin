package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "SSP_OBJECTIVES_IN_SET")
data class SspObjectivesInSet(

        @Id
        @Column(name = "SSP_OBJECTIVES_IN_SET_PK")
        val sspObjectivesInSetPk: Long? = null,

        @Column(name = "COPIED_FORWARD_INDICATOR")
        val copiedForwardIndicator: String? = null,

        @Column(name = "HOW_PROGRESS_MEASURED")
        val howProgressMeasured: String? = null,

        @Column(name = "DISPLAY_SORT")
        val displaySort: Long? = null,

        @ManyToOne
        @JoinColumns(JoinColumn(name = "OBJECTIVE_TYPE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OBJECTIVE_TYPE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        val objectiveType: RefElement? = null,

        @Column(name = "LAST_UPDATE_DATE")
        val lastUpdateDate: LocalDateTime? = null,

        @Column(name = "OASYS_SET_PK")
        val oasysSetPk: Long? = null,

        @Column(name = "MIG_GUID")
        val migGuid: String? = null,

        @Column(name = "MIG_ID")
        val migId: String? = null,

        @Column(name = "CF_LAST_ASSMT_OBJ")
        val cfLastAssmtObj: Long? = null,

        @Column(name = "CF_ORIG_ASSMT_OBJ")
        val cfOrigAssmtObj: Long? = null,

        @OneToMany
        @JoinColumn(name = "SSP_OBJECTIVES_IN_SET_PK", referencedColumnName = "SSP_OBJECTIVES_IN_SET_PK")
        val sspObjIntervenePivots: Set<SspObjIntervenePivot>? = null,

        @ManyToOne
        @JoinColumn(name = "SSP_OBJECTIVES_IN_SET_PK", referencedColumnName = "SSP_OBJECTIVES_IN_SET_PK", insertable = false, updatable = false)
        val sspObjective: SspObjective? = null,

        @ManyToOne
        @JoinColumn(name = "SSP_OBJECTIVES_IN_SET_PK", referencedColumnName = "SSP_OBJECTIVES_IN_SET_PK", insertable = false, updatable = false)
        val sspObjectiveMeasure: SspObjectiveMeasure? = null,

        @OneToMany
        @JoinColumn(name = "SSP_OBJECTIVES_IN_SET_PK", referencedColumnName = "SSP_OBJECTIVES_IN_SET_PK")
        val sspCrimNeedObjPivots: Set<SspCrimNeedObjPivot>? = null

) {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is SspObjectivesInSet) return false
                return sspObjectivesInSetPk == other.sspObjectivesInSetPk
        }

        override fun hashCode(): Int {
                return 31
        }

}
