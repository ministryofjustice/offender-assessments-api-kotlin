package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "REF_ROLE")
data class RefRole(
  @Column(name = "REF_ROLE_UK")
  private var refRoleUk: Long? = null,
  @javax.persistence.Id
  @Column(name = "REF_ROLE_CODE")
  private val refRoleCode: String? = null,

  @Column(name = "REF_ROLE_DESC")
  private val refRoleDesc: String? = null,

  @Column(name = "ROLE_TYPE_ELM")
  private val roleTypeElm: String? = null,

  @Column(name = "ROLE_TYPE_CAT")
  private val roleTypeCat: String? = null,

  @Column(name = "CT_AREA_EST_CODE")
  private val ctAreaEstCode: String? = null,

  @Column(name = "RESTRICTED_IND")
  private val restrictedInd: String? = null,

  @Column(name = "CHECKSUM")
  private val checksum: String? = null,

  @Column(name = "CREATE_DATE")
  private val createDate: Time? = null,

  @Column(name = "CREATE_USER")
  private val createUser: String? = null,

  @Column(name = "LASTUPD_DATE")
  private val lastupdDate: Time? = null,

  @Column(name = "LASTUPD_USER")
  private val lastupdUser: String? = null

) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RefRole) return false
    return refRoleCode == other.refRoleCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
