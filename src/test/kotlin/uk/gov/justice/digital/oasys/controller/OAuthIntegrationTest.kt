package uk.gov.justice.digital.oasys.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import uk.gov.justice.digital.oasys.jpa.entities.OasysUser
import uk.gov.justice.digital.oasys.jpa.repositories.UserRepository

@AutoConfigureWebTestClient
class OAuthIntegrationTest : IntegrationTest() {

    private val userCode = "USER1"

    @MockkBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `access forbidden when no token provided`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/1")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden to authentication endpoint when no token provided`() {

        webTestClient.get().uri("/authentication/user/$userCode")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden when role is incorrect`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/1")
                .headers(setAuthorisation(roles=listOf("ROLE_INVALID_ROLE")))
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `access granted for authorisation endpoints with OASYS_READ_ONLY role`() {
        every { userRepository.findOasysUserByOasysUserCodeIgnoreCase("USER_CODE") } returns OasysUser()

        webTestClient.get().uri("/authentication/user/USER_CODE")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun `access granted for authorisation endpoints with OASYS_AUTHENTICATION role`() {
        every { userRepository.findOasysUserByOasysUserCodeIgnoreCase("USER_CODE") } returns OasysUser()

        webTestClient.get().uri("/authentication/user/USER_CODE")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_AUTHENTICATION")))
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun `access granted for offender endpoints with OASYS_READ_ONLY role`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/1")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun `access forbidden for offender endpoints with OASYS_AUTHENTICATION role`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/1")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_AUTHENTICATION")))
                .exchange()
                .expectStatus().isForbidden
    }

}

