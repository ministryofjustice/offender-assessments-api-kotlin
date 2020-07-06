package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.*

@Entity
@Table(name = "OASYS_SECTION")
class Section(

        @Id
        @Column(name = "OASYS_SECTION_PK")
        val oasysSectionPk: Long? = null,

        @Column(name = "OASYS_SET_PK")
        val oasysSetPk: Long? = null,

        @Column(name = "SECTION_STATUS_ELM")
        val sectionStatusElm: String? = null,

        @Column(name = "SECT_OGP_WEIGHTED_SCORE")
        val sectOgpWeightedScore: Long? = null,

        @Column(name = "SECT_OVP_WEIGHTED_SCORE")
        val sectOvpWeightedScore: Long? = null,

        @Column(name = "SECT_OTHER_WEIGHTED_SCORE")
        val sectOtherWeightedScore: Long? = null,

        @Column(name = "SECT_OVP_RAW_SCORE")
        val sectOvpRawScore: Long? = null,

        @Column(name = "SECT_OGP_RAW_SCORE")
        val sectOgpRawScore: Long? = null,

        @Column(name = "SECT_OTHER_RAW_SCORE")
        val sectOtherRawScore: Long? = null,

        @Column(name = "LOW_SCORE_NEED_ATTN_IND")
        val lowScoreNeedAttnInd: String? = null,

        @OneToMany
        @JoinColumn(name = "OASYS_SECTION_PK")
        val oasysQuestions: Set<OasysQuestion>? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"))
        val refSection: RefSection? = null



) {

    fun getRefAnswers(questionKeys: Set<String?>): Map<String?, String?>? {
        return oasysQuestions?.filter { q -> questionKeys.contains(q.refQuestion?.refQuestionCode) }
                ?.map { it.oasysAnswer?.refAnswer?.refQuestionCode to it.oasysAnswer?.refAnswer?.refAnswerCode }?.toMap()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Section) return false
        val other: Section = o as Section
        return oasysSectionPk == other.oasysSectionPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
