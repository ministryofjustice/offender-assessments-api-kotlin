package uk.gov.justice.digital.oasys.jpa.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OASYS_USER")
data class OasysUser(

  @Id
  @Column(name = "OASYS_USER_CODE")
  val oasysUserCode: String? = null,

  @Column(name = "OASYS_USER_UK")
  val oasysUserUk: Long? = null,

  @Column(name = "USER_FORENAME_1")
  val userForename1: String? = null,

  @Column(name = "USER_FORENAME_2")
  val userForename2: String? = null,

  @Column(name = "USER_FORENAME_3")
  val userForename3: String? = null,

  @Column(name = "USER_FAMILY_NAME")
  val userFamilyName: String? = null,

  @Column(name = "EMAIL_ADDRESS")
  val emailAddress: String? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "USER_STATUS_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "USER_STATUS_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val userStatus: RefElement? = null,

  @OneToOne
  @JoinColumn(name = "CT_AREA_EST_CODE")
  val ctAreaEst: CtAreaEst? = null,

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "OASYS_USER_CODE", referencedColumnName = "OASYS_USER_CODE")
  val roles: List<AreaEstUserRole>? = null
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OasysUser) return false
    return oasysUserCode == other.oasysUserCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
