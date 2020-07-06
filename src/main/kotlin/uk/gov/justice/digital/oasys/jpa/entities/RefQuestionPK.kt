package uk.gov.justice.digital.oasys.jpa.entities


import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class RefQuestionPK(

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
        private val refQuestionCode: String? = null

) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefQuestionPK) return false
        val that = o
        return refAssVersionCode == that.refAssVersionCode &&
                versionNumber == that.versionNumber &&
                refSectionCode == that.refSectionCode &&
                refQuestionCode == that.refQuestionCode
    }

    override fun hashCode(): Int {
        return 31
    }

}
