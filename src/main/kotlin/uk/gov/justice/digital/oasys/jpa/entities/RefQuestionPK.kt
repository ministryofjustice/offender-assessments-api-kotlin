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
        return refAssVersionCode == o.refAssVersionCode &&
                versionNumber == o.versionNumber &&
                refSectionCode == o.refSectionCode &&
                refQuestionCode == o.refQuestionCode
    }

    override fun hashCode(): Int {
        return 31
    }

}
