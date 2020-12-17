package uk.gov.justice.digital.oasys.api

data class ValidateUserRequest(
  var user: String? = null,
  var password: String? = null
)
