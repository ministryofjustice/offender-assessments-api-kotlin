package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BASIC_SENTENCE_PLAN_OBJ")
data class BasicSentencePlanObj(

        @Id
        @Column(name = "BASIC_SENT_PLAN_OBJ_PK")
        @SequenceGenerator(name = "BASIC_SENTENCE_PLAN_OBJ_SEQ", sequenceName = "BASIC_SENTENCE_PLAN_OBJ_SEQ")
        @GeneratedValue(generator = "BASIC_SENTENCE_PLAN_OBJ_SEQ", strategy = GenerationType.SEQUENCE)
        val basicSentPlanObjPk: Long? = null,

        @Column(name = "DISPLAY_SORT")
        val displaySort: Long? = null,

        @Column(name = "INCLUDE_IN_PLAN_IND")
        val includeInPlanInd: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "OFFENCE_BEHAV_LINK_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OFFENCE_BEHAV_LINK_ELM", referencedColumnName = "REF_ELEMENT_CODE")) val offenceBehaviourLink: RefElement? = null,


        @Column(name = "OBJECTIVE_TEXT")
        val objectiveText: String? = null,

        @Column(name = "MEASURE_TEXT")
        val measureText: String? = null,

        @Column(name = "WHAT_WORK_TEXT")
        val whatWorkText: String? = null,

        @Column(name = "WHO_WILL_DO_WORK_TEXT")
        val whoWillDoWorkText: String? = null,

        @Column(name = "TIMESCALES_TEXT")
        val timescalesText: String? = null,

        @Column(name = "OASYS_SET_PK")
        val oasysSetPk: Long? = null,

        @Column(name = "DATE_OPENED")
        val dateOpened: LocalDateTime? = null,

        @Column(name = "DATE_COMPLETED")
        val dateCompleted: LocalDateTime? = null,

        @Column(name = "PROBLEM_AREA_COMP_IND")
        val problemAreaCompInd: String? = null,

        @Column(name = "CREATE_DATE")
        val createDate: LocalDateTime? = null,

        @Column(name = "CREATE_USER")
        val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        val lastupdDate: LocalDateTime? = null,

        @Column(name = "LASTUPD_USER")
        val lastupdUser: String? = null,

        @Column(name = "CF_LAST_BCS_INT")
        val cfLastBcsInt: Long? = null,

        @Column(name = "CF_ORIG_BCS_INT")
        val cfOrigBcsInt: Long? = null
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is BasicSentencePlanObj) return false
        return basicSentPlanObjPk == o.basicSentPlanObjPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
