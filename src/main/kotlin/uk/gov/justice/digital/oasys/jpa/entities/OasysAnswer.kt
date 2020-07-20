package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "OASYS_ANSWER")
data class OasysAnswer(

        @Id
        @Column(name = "OASYS_ANSWER_PK")
        var oasysAnswerPk: Long? = null,

        @OneToOne
        @JoinColumn(name = "OASYS_QUESTION_PK")
        var oasysQuestion: OasysQuestion? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"), JoinColumn(name = "REF_QUESTION_CODE", referencedColumnName = "REF_QUESTION_CODE"), JoinColumn(name = "REF_ANSWER_CODE", referencedColumnName = "REF_ANSWER_CODE"))
        var refAnswer: RefAnswer? = null,

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
                if (o !is OasysAnswer) return false
                return oasysAnswerPk == o.oasysAnswerPk
        }

        override fun hashCode(): Int {
                return 31
        }

}
