package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "OASYS_ANSWER")
data class OasysAnswer(

        @Id
        @Column(name = "OASYS_ANSWER_PK")
        val oasysAnswerPk: Long? = null,

        @ManyToOne
        @JoinColumn(name = "OASYS_QUESTION_PK")
        var oasysQuestion: OasysQuestion? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"), JoinColumn(name = "REF_QUESTION_CODE", referencedColumnName = "REF_QUESTION_CODE"), JoinColumn(name = "REF_ANSWER_CODE", referencedColumnName = "REF_ANSWER_CODE"))
        val refAnswer: RefAnswer? = null,

        @Column(name = "CREATE_DATE")
        var createDate: Time? = null,

        @Column(name = "CREATE_USER")
        val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        val lastupdDate: Time? = null,

        @Column(name = "LASTUPD_USER")
        val lastupdUser: String? = null
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is OasysAnswer) return false
                return oasysAnswerPk == other.oasysAnswerPk
        }

        override fun hashCode(): Int {
                return 31
        }
}
