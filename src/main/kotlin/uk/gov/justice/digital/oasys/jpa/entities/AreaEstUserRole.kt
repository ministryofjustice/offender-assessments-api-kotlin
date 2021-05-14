package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "AREA_EST_USER_ROLE")
@IdClass(AreaEstUserRolePK::class)
data class AreaEstUserRole(

  @Id
  @Column(name = "CT_AREA_EST_CODE")
  val ctAreaEstCode: String? = null,

  @Id
  @Column(name = "OASYS_USER_CODE")
  val oasysUserCode: String? = null,

  @Id
  @Column(name = "REF_ROLE_CODE")
  val refRoleCode: String? = null,

  @Column(name = "START_DATE")
  val startDate: LocalDateTime? = null,

  @Column(name = "END_DATE")
  val endDate: LocalDateTime? = null,

  @Column(name = "CHECKSUM")
  val checksum: String? = null,

  @Column(name = "CREATE_DATE")
  val createDate: LocalDateTime? = null,

  @Column(name = "CREATE_USER")
  val createUser: String? = null,

  @Column(name = "LASTUPD_DATE")
  val lastupdDate: LocalDateTime? = null,

  @Column(name = "LASTUPD_USER")
  private val lastupdUser: String? = null,

  @ManyToOne
  @JoinColumn(name = "REF_ROLE_CODE", insertable = false, updatable = false)
  private val refRole: RefRole? = null

) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AreaEstUserRole) return false
    return oasysUserCode == other.oasysUserCode &&
      refRoleCode == other.refRoleCode &&
      ctAreaEstCode == other.ctAreaEstCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
