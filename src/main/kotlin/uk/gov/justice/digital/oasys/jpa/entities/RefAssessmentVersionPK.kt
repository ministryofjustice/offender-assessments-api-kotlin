package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class RefAssessmentVersionPK (
        @Id
        @Column(name = "REF_ASS_VERSION_CODE")
        private val refAssVersionCode: String? = null,

        @Id
        @Column(name = "VERSION_NUMBER")
        private val versionNumber: String? = null
) : Serializable
{
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefAssessmentVersionPK) return false
        return refAssVersionCode == o.refAssVersionCode &&
                versionNumber == o.versionNumber
    }

    override fun hashCode(): Int {
        return 31
    }
}
