package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.OasysUser
data class UserProfileDto(
  val userCode: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val email: String? = null,
  val regions: Set<RegionDto> = emptySet(),
  val enabled: Boolean? = null
) {
  companion object {
    fun from(oasysUser: OasysUser, regions: Set<RegionDto>): UserProfileDto {
      return UserProfileDto(
        oasysUser.oasysUserCode,
        oasysUser.userForename1,
        oasysUser.userFamilyName,
        oasysUser.emailAddress,
        regions,
        enabled = oasysUser.userStatus?.refElementCode.equals("ACTIVE")
      )
    }
  }
}
