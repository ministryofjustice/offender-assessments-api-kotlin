package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "REF_SECTION")
@IdClass(RefSectionPK::class)
data class RefSection(

        @Id @Column(name = "REF_ASS_VERSION_CODE")
        val refAssVersionCode: String? = null,
        @Id
        @Column(name = "VERSION_NUMBER")
        val versionNumber: String? = null,

        @Id
        @Column(name = "REF_SECTION_CODE")
        val refSectionCode: String? = null,

        @Column(name = "REF_SECTION_UK")
        val refSectionUk: Long? = null,

        @Column(name = "FORM_SEQUENCE")
        val formSequence: Long? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "SECTION_TYPE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "SECTION_TYPE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        val sectionType: RefElement? = null,

        @Column(name = "CRIM_NEED_SCORE_THRESHOLD")
        val crimNeedScoreThreshold: Long? = null,

        @Column(name = "SCORED_FOR_OGP")
        val scoredForOgp: String? = null,

        @Column(name = "SCORED_FOR_OVP")
        val scoredForOvp: String? = null,

        @OneToMany
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"))
        val refQuestions: List<RefQuestion>? = null


) {
    fun hasSectionType(): Boolean {
        return sectionType != null
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefSection) return false
        val that = o
        return refAssVersionCode == that.refAssVersionCode &&
                versionNumber == that.versionNumber &&
                refSectionCode == that.refSectionCode
    }

    override fun hashCode(): Int {
        return 31
    }
}
