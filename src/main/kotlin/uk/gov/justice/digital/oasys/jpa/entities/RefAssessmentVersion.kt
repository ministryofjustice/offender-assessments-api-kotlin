package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "REF_ASS_VERSION")
@IdClass(RefAssessmentVersionPK::class)
data class RefAssessmentVersion(

        @Id @Column(name = "REF_ASS_VERSION_CODE")
        var refAssVersionCode: String? = null,

        @Id
        @Column(name = "VERSION_NUMBER")
        val versionNumber: String? = null,

        @Id
        @Column(name = "REF_ASS_VERSION_UK")
        val refAssVersionUk: Long? = null,

        @Column(name = "START_DATE")
        val startDate: Time? = null,

        @Column(name = "END_DATE")
        val endDate: Time? = null,

        @Column(name = "OASYS_FORM_VERSION")
        val oasysFormVersion: Long? = null,

        @Column(name = "OASYS_SCORING_ALG_VERSION")
        val oasysScoringAlgVersion: Long? = null,

        @Column(name = "REF_MODULE_CODE")
        val refModuleCode: String? = null,

        @Column(name = "CREATE_DATE")
        val createDate: Time? = null,

        @Column(name = "CREATE_USER")
        val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        val lastupdDate: Time? = null,

        @Column(name = "LASTUPD_USER")
        val lastupdUser: String? = null,

        @OneToMany
        @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"))
        private val refSections: List<RefSection>? = null
) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefAssessmentVersion) return false
        return refAssVersionCode == o.refAssVersionCode &&
                versionNumber == o.versionNumber
    }

    override fun hashCode(): Int {
        return 31
    }

}
