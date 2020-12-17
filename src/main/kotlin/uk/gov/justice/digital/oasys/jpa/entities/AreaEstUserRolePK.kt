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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AreaEstUserRolePK) return false
    return oasysUserCode == other.oasysUserCode &&
      refRoleCode == other.refRoleCode &&
      ctAreaEstCode == other.ctAreaEstCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
