package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "OASYS_USER")
data class OasysUser(

        @Column(name = "OASYS_USER_UK")
        private val oasysUserUk: Long? = null,

        @Id
        @Column(name = "OASYS_USER_CODE")
        private val oasysUserCode: String? = null,

        @Column(name = "USER_FORENAME_1")
        private val userForename1: String? = null,

        @Column(name = "USER_FORENAME_2")
        private val userForename2: String? = null,

        @Column(name = "USER_FORENAME_3")
        private val userForename3: String? = null,

        @Column(name = "USER_FAMILY_NAME")
        private val userFamilyName: String? = null,

        @Column(name = "PASSWORD_ENCRYPTED")
        private val passwordEncrypted: String? = null,

        @Column(name = "PASSWORD_CHANGE_DATE")
        private val passwordChangeDate: Time? = null,

        @Column(name = "LAST_LOGIN")
        private val lastLogin: Time? = null,

        @Column(name = "FAILED_LOGIN_ATTEMPTS")
        private val failedLoginAttempts: Long? = null,

        @Column(name = "SYSTEM_IND")
        private val systemInd: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "USER_STATUS_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "USER_STATUS_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val userStatus: RefElement? = null,

        @Column(name = "DATE_OF_BIRTH")
        private val dateOfBirth: Time? = null,

        @Column(name = "PASSWORD")
        private val password: String? = null,

        @Column(name = "EMAIL_ADDRESS")
        private val emailAddress: String? = null,

        @Column(name = "LEGACY_USER_CODE")
        private val legacyUserCode: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "MIGRATION_SOURCE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "MIGRATION_SOURCE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        private val migrationSource: RefElement? = null,

        @Column(name = "CHECKSUM")
        private val checksum: String? = null,

        @Column(name = "CREATE_DATE")
        private val createDate: Time? = null,

        @Column(name = "CREATE_USER")
        private val createUser: String? = null,

        @Column(name = "LASTUPD_DATE")
        private val lastupdDate: Time? = null,

        @Column(name = "LASTUPD_USER")
        private val lastupdUser: String? = null,

        @OneToOne
        @JoinColumn(name = "CT_AREA_EST_CODE")
        private val ctAreaEst: CtAreaEst? = null,

        @Column(name = "EXCL_DEACT_IND")
        private val exclDeactInd: String? = null,

        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "OASYS_USER_CODE", referencedColumnName = "OASYS_USER_CODE")
        private val roles: List<AreaEstUserRole>? = null
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is OasysUser) return false
        return oasysUserCode == o.oasysUserCode
    }

    override fun hashCode(): Int {
        return 31
    }


}
