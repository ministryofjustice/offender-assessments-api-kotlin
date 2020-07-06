package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "AREA_EST_USER_ROLE")
@IdClass(AreaEstUserRolePK::class)
data class AreaEstUserRole(

        @Id
        @Column(name = "CT_AREA_EST_CODE")
        val ctAreaEstCode: String? = null,

        @Column(name = "AREA_EST_USER_ROLE_UK")
        var areaEstUserRoleUk: Long? = null,
        @javax.persistence.Id
        @Column(name = "OASYS_USER_CODE")
        val oasysUserCode: String? = null,

        @Id
        @Column(name = "REF_ROLE_CODE")
        val refRoleCode: String? = null,

        @Column(name = "START_DATE")
        val startDate: Time? = null,

        @Column(name = "END_DATE")
        val endDate: Time? = null,

        @Column(name = "CHECKSUM")
        val checksum: String? = null,

        @Column(name = "CREATE_DATE")
        val createDate: Time? = null,

        @Column(name = "CREATE_USER")
        val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        val lastupdDate: Time? = null,

        @Column(name = "LASTUPD_USER")
        private val lastupdUser: String? = null,

        @ManyToOne
        @JoinColumn(name = "REF_ROLE_CODE", insertable = false, updatable = false)
        private val refRole: RefRole? = null

) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is AreaEstUserRole) return false
        val that: AreaEstUserRole = o
        return oasysUserCode == that.oasysUserCode &&
                refRoleCode == that.refRoleCode &&
                ctAreaEstCode == that.ctAreaEstCode
    }

    override fun hashCode(): Int {
        return 31
    }


}
