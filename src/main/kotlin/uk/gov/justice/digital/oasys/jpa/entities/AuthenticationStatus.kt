package uk.gov.justice.digital.oasys.jpa.entities

import com.fasterxml.jackson.annotation.JsonProperty

class AuthorisationStatus(
        @JsonProperty("STATE")
        val state: AuthorisationState? = null
) {
    enum class AuthorisationState {
        EDIT, READ, NO_ACCESS, INVALID_SESSION, FAILED_TO_LIST
    }
}

class AuthenticationStatus(
        @JsonProperty("STATE")
        val state: String? = null
) {
    fun isAuthenticated(): Boolean {
        return state == "SUCCESS"
    }

}
