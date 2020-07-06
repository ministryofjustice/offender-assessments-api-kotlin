package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class RefSectionPK (

        @Id
        @Column(name = "REF_ASS_VERSION_CODE")
        val refAssVersionCode: String? = null,

        @Id
        @Column(name = "VERSION_NUMBER")
        val versionNumber: String? = null,

        @Id
        @Column(name = "REF_SECTION_CODE")
        val refSectionCode: String? = null
) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefSectionPK) return false
        val that = o
        return  refAssVersionCode == that.refAssVersionCode &&
                versionNumber == that.versionNumber &&
                refSectionCode == that.refSectionCode
    }

    override fun hashCode(): Int {
        return 31
    }
}
