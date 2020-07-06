package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "BASIC_SENTENCE_PLAN_OBJ")
data class BasicSentencePlanObj (

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
    @JoinColumns(JoinColumn(name = "OFFENCE_BEHAV_LINK_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OFFENCE_BEHAV_LINK_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
    private val offenceBehaviourLink: RefElement? = null,


    @Column(name = "OBJECTIVE_TEXT")
    private val objectiveText: String? = null,

    @Column(name = "MEASURE_TEXT")
    private val measureText: String? = null,

    @Column(name = "WHAT_WORK_TEXT")
    private val whatWorkText: String? = null,

    @Column(name = "WHO_WILL_DO_WORK_TEXT")
    private val whoWillDoWorkText: String? = null,

    @Column(name = "TIMESCALES_TEXT")
    private val timescalesText: String? = null,

    @Column(name = "OASYS_SET_PK")
    private val oasysSetPk: Long? = null,

    @Column(name = "DATE_OPENED")
    private val dateOpened: LocalDateTime? = null,

    @Column(name = "DATE_COMPLETED")
    private val dateCompleted: LocalDateTime? = null,

    @Column(name = "PROBLEM_AREA_COMP_IND")
    private val problemAreaCompInd: String? = null,

    @Column(name = "MIG_GUID")
    private val migGuid: String? = null,

    @Column(name = "MIG_ID")
    private val migId: String? = null,

    @Column(name = "CHECKSUM")
    private val checksum: String? = null,

    @Column(name = "CREATE_DATE")
    private val createDate: LocalDateTime? = null,

    @Column(name = "CREATE_USER")
    private val createUser: String? = null,

    @Column(name = "LASTUPD_DATE")
    private val lastupdDate: LocalDateTime? = null,

    @Column(name = "LASTUPD_USER")
    private val lastupdUser: String? = null,

    @Column(name = "CF_LAST_BCS_INT")
    private val cfLastBcsInt: Long? = null,

    @Column(name = "CF_ORIG_BCS_INT")
    private val cfOrigBcsInt: Long? = null
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
