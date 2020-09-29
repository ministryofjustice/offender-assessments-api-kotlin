package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class RefAnswerPK (

        @Id
        @Column(name = "REF_ASS_VERSION_CODE")
        private var refAssVersionCode: String? = null,

        @Id
        @Column(name = "VERSION_NUMBER")
        private val versionNumber: String? = null,

        @Id
        @Column(name = "REF_SECTION_CODE")
        private val refSectionCode: String? = null,

        @Id
        @Column(name = "REF_QUESTION_CODE")
        private val refQuestionCode: String? = null,

        @Id
        @Column(name = "REF_ANSWER_CODE")
        private val refAnswerCode: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefAnswerPK) return false
        return  refAssVersionCode == other.refAssVersionCode &&
                versionNumber == other.versionNumber &&
                refSectionCode == other.refSectionCode &&
                refQuestionCode == other.refQuestionCode &&
                refAnswerCode == other.refAnswerCode
    }

    override fun hashCode(): Int {
        return 31
    }
}
