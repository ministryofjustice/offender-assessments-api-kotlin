package uk.gov.justice.digital.oasys.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AuthenticationDto
import uk.gov.justice.digital.oasys.api.ErrorResponse
import uk.gov.justice.digital.oasys.api.OasysUserAuthenticationDto
import uk.gov.justice.digital.oasys.api.ValidateUserRequest
import uk.gov.justice.digital.oasys.jpa.repositories.AuthenticationRepository

@SqlGroup(
        Sql(scripts = ["classpath:authentication/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:authentication/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient
class AuthenticationControllerTest : IntegrationTest() {

    private val userCode = "USER1"
    private val validUserCode = "USER_CODE"

    @MockkBean
    private lateinit var authenticationRepository: AuthenticationRepository

    @Test
    fun `access forbidden when no authority`() {

       webTestClient.get().uri("/authentication/user/$userCode")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden when no role`() {

        webTestClient.get().uri("/authentication/user/$userCode")
                .headers(setAuthorisation())
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `oasys user code returns user`() {

        webTestClient.get().uri("/authentication/user/$userCode")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OasysUserAuthenticationDto>()
                .consumeWith {validateUser(it.responseBody)}
    }

    @Test
    fun `oasys user code returns not found`() {

        webTestClient.get().uri("/authentication/user/UNKNOWN_USER")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `valid credentials returns OK 200`() {

        every { authenticationRepository.validateCredentials(validUserCode, "PASSWORD") } returns """{STATE: "SUCCESS"}"""
        val user = ValidateUserRequest("USER_CODE", "PASSWORD")

        webTestClient.post().uri("/authentication/user/validate")
                .bodyValue(user)
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<AuthenticationDto>()
                .consumeWith {
                    assertThat(it.responseBody?.authenticated).isTrue()
                }
    }

    @Test
    fun `invalid credentials returns 401`() {

        every { authenticationRepository.validateCredentials(validUserCode, "INVALIDPASSWORD") } returns """{STATE: "FAILURE"}"""
        val user = ValidateUserRequest("USER_CODE", "INVALIDPASSWORD")

        webTestClient.post().uri("/authentication/user/validate")
                .bodyValue(user)
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isUnauthorized
                .expectBody<ErrorResponse>()
                .consumeWith {
                    assertThat(it.responseBody?.status).isEqualTo(401)
                    assertThat(it.responseBody?.developerMessage).isEqualTo("Invalid username or password")
                }
    }

    @Test
    fun `invalid OASys response returns 401`() {

        every { authenticationRepository.validateCredentials(validUserCode, "INVALIDPASSWORD") } returns null
        val user = ValidateUserRequest("USER_CODE", "INVALIDPASSWORD")

        webTestClient.post().uri("/authentication/user/validate")
                .bodyValue(user)
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isUnauthorized
                .expectBody<ErrorResponse>()
                .consumeWith {
                    assertThat(it.responseBody?.status).isEqualTo(401)
                    assertThat(it.responseBody?.developerMessage).isEqualTo("No response from OASys authentication function for user, USER_CODE")
                }
    }

    private fun validateUser(user: OasysUserAuthenticationDto?) {
        assertThat(user?.userName).isEqualTo("USER1")
        assertThat(user?.userId).isEqualTo("USER1")
        assertThat(user?.firstName).isEqualTo("JOHN")
        assertThat(user?.lastName).isEqualTo("SMITH")
        assertThat(user?.enabled).isTrue()
        assertThat(user?.email).isEqualTo("test@test.com")
        assertThat(user?.regions).hasSize(1)

    }
}