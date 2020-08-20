package uk.gov.justice.digital.oasys.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AuthorisationDto
import uk.gov.justice.digital.oasys.api.OffenderPermissionLevel
import uk.gov.justice.digital.oasys.api.OffenderPermissionResource
import uk.gov.justice.digital.oasys.jpa.repositories.AuthenticationRepository
import uk.gov.justice.digital.oasys.jpa.repositories.UserRepository

@AutoConfigureWebTestClient
class AuthenticationControllerSentencePlanTest : IntegrationTest() {

    @MockkBean
    private lateinit var authenticationRepository: AuthenticationRepository

    @MockkBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `user with valid sentence plan role and session ID returns 200`() {

        every { authenticationRepository.validateUserSentencePlanAccessWithSession("USER_CODE", 1L, 123456L) } returns """{STATE: "EDIT"}"""

        webTestClient.get().uri("/authentication/user/USER_CODE/offender/1/SENTENCE_PLAN?sessionId=123456")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<AuthorisationDto>()
                .consumeWith {
                    val response = it.responseBody
                    assertThat(response?.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.WRITE)
                    assertThat(response?.oasysUserCode).isEqualTo("USER_CODE")
                    assertThat(response?.oasysOffenderId).isEqualTo(1L)
                    assertThat(response?.offenderPermissionResource).isEqualTo(OffenderPermissionResource.SENTENCE_PLAN)
                }
    }

    @Test
    fun `user with invalid sentence plan role returns 200`() {

        every { authenticationRepository.validateUserSentencePlanAccessWithSession("USER_CODE", 1L, 123456L) } returns """{STATE: "NO_ACCESS"}"""

        webTestClient.get().uri("/authentication/user/USER_CODE/offender/1/SENTENCE_PLAN?sessionId=123456")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<AuthorisationDto>()
                .consumeWith {
                    val response = it.responseBody
                    assertThat(response?.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.UNAUTHORISED)
                    assertThat(response?.oasysUserCode).isEqualTo("USER_CODE")
                    assertThat(response?.oasysOffenderId).isEqualTo(1L)
                    assertThat(response?.offenderPermissionResource).isEqualTo(OffenderPermissionResource.SENTENCE_PLAN)
                }
    }

    @Test
    fun `user with valid sentence plan role but no session ID returns 200`() {

        every { authenticationRepository.validateUserSentencePlanAccessWithSession("USER_CODE", 1L, 123456L) } returns """{STATE: "EDIT"}"""
        every { userRepository.findCurrentUserSessionForOffender(1L, "USER_CODE") } returns 123456L

        webTestClient.get().uri("/authentication/user/USER_CODE/offender/1/SENTENCE_PLAN")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<AuthorisationDto>()
                .consumeWith {
                    val response = it.responseBody
                    assertThat(response?.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.WRITE)
                    assertThat(response?.oasysUserCode).isEqualTo("USER_CODE")
                    assertThat(response?.oasysOffenderId).isEqualTo(1L)
                    assertThat(response?.offenderPermissionResource).isEqualTo(OffenderPermissionResource.SENTENCE_PLAN)
                }
    }
}