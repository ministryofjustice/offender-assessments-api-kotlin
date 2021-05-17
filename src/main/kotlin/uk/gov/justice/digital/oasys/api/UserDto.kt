package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.OasysUser

data class UserDto(

  val oasysUserCode: String? = null,
  val userForename1: String? = null,
  val userForename2: String? = null,
  val userForename3: String? = null,
  val userFamilyName: String? = null,
  val email: String? = null,
  val accountStatus: AccountStatus? = null
) {

  companion object {

    fun from(user: OasysUser?): UserDto {
      return UserDto(
        oasysUserCode = user?.oasysUserCode,
        userForename1 = user?.userForename1,
        userForename2 = user?.userForename2,
        userForename3 = user?.userForename3,
        userFamilyName = user?.userFamilyName,
        email = user?.emailAddress,
        accountStatus = user?.userStatus?.refElementCode?.let { AccountStatus.valueOf(it) }
      )
    }
  }
}
