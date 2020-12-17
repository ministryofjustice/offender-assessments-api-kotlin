package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.OasysUser

data class OasysUserAuthenticationDto(
  val userId: String? = null,
  val userName: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val email: String? = null,
  val regions: Set<String> = emptySet(),
  val enabled: Boolean? = null
) {
  companion object {
    fun from(oasysUser: OasysUser): OasysUserAuthenticationDto {
      val enabled = oasysUser.userStatus?.refElementCode.equals("ACTIVE")
      val regions = oasysUser.roles?.mapNotNull { it.ctAreaEstCode }?.take(10)?.toSet().orEmpty()
      return OasysUserAuthenticationDto(
        oasysUser.oasysUserCode,
        oasysUser.oasysUserCode,
        oasysUser.userForename1,
        oasysUser.userFamilyName,
        oasysUser.emailAddress,
        regions,
        enabled
      )
    }
  }
}
