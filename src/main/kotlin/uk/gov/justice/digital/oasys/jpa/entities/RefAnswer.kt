package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "REF_ANSWER")
@IdClass(RefAnswerPK::class)
data class RefAnswer(
        @Id
        @Column(name = "REF_ASS_VERSION_CODE")
        var refAssVersionCode: String? = null,

        @Id
        @Column(name = "VERSION_NUMBER")
        val versionNumber: String? = null,

        @Id
        @Column(name = "REF_SECTION_CODE")
        val refSectionCode: String? = null,

        @Id
        @Column(name = "REF_QUESTION_CODE")
        val refQuestionCode: String? = null,

        @Id
        @Column(name = "REF_ANSWER_CODE")
        val refAnswerCode: String? = null,

        @Column(name = "REF_ANSWER_UK")
        val refAnswerUk: Long? = null,

        @Column(name = "DISPLAY_SORT")
        val displaySort: Long? = null,

        @Column(name = "REF_SECTION_ANSWER")
        val refSectionAnswer: String? = null,

        @Column(name = "DEFAULT_DISPLAY_SCORE")
        val defaultDisplayScore: Long? = null,

        @Column(name = "OGP_SCORE")
        val ogpScore: Long? = null,

        @Column(name = "OVP_SCORE")
        val ovpScore: Long? = null,

        @Column(name = "CHECKSUM")
        val checksum: String? = null,

        @Column(name = "QA_RAW_SCORE")
        val qaRawScore: Long? = null,

        @Column(name = "CREATE_DATE")
        val createDate: LocalDateTime? = null,

        @Column(name = "CREATE_USER")
        val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        val lastupdDate: LocalDateTime? = null,

        @Column(name = "LASTUPD_USER")
        val lastupdUser: String? = null
) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefAnswer) return false
        return refAssVersionCode == o.refAssVersionCode &&
                versionNumber == o.versionNumber &&
                refSectionCode == o.refSectionCode &&
                refQuestionCode == o.refQuestionCode &&
                refAnswerCode == o.refAnswerCode
    }

    override fun hashCode(): Int {
        return 31
    }


}
