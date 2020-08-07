package uk.gov.justice.digital.oasys.api

data class AuthorisationDto(
        val oasysUserCode: String? = null,
        val oasysOffenderId: Long? = null,
        val offenderPermissionLevel: OffenderPermissionLevel? = null,
        val offenderPermissionResource: OffenderPermissionResource? = null
)