package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "OASYS_QUESTION")
data class OasysQuestion(
        @Id
        @Column(name = "OASYS_QUESTION_PK")
        val oasysQuestionPk: Long? = null,

        @Column(name = "ADDITIONAL_NOTE")
        val additionalNote: String? = null,

        @Column(name = "OASYS_SECTION_PK")
        val oasysSectionPk: Long? = null,

        @Column(name = "FREE_FORMAT_ANSWER")
        val freeFormatAnswer: String? = null,

        @Column(name = "DISPLAY_SCORE")
        val displayScore: Long? = null,

        @Column(name = "DISCLOSED_IND")
        val disclosedInd: String? = null,

        @Column(name = "CURRENTLY_HIDDEN_IND")
        val currentlyHiddenInd: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "REF_QUESTION_CODE", referencedColumnName = "REF_QUESTION_CODE"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"))
        val refQuestion: RefQuestion? = null,

        @OneToOne(mappedBy = "oasysQuestion")
        var oasysAnswer: OasysAnswer? = null,

        @Column(name = "CREATE_DATE")
        private var createDate: Time? = null,

        @Column(name = "CREATE_USER")
        private val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        private val lastupdDate: Time? = null,

        @Column(name = "LASTUPD_USER")
        private val lastupdUser: String? = null

) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is OasysQuestion) return false
        return oasysQuestionPk == o.oasysQuestionPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
