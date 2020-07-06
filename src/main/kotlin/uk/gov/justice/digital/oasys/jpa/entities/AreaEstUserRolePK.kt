package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class AreaEstUserRolePK(

        @Column(name = "OASYS_USER_CODE")
        @Id
        private val oasysUserCode: String? = null,

        @Column(name = "REF_ROLE_CODE")
        @Id
        private val refRoleCode: String? = null,

        @Column(name = "CT_AREA_EST_CODE")
        @Id
        private val ctAreaEstCode: String? = null

) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is AreaEstUserRolePK) return false
        val that: AreaEstUserRolePK = o
        return oasysUserCode == that.oasysUserCode &&
                refRoleCode == that.refRoleCode &&
                ctAreaEstCode == that.ctAreaEstCode
    }

    override fun hashCode(): Int {
        return 31
    }

}
