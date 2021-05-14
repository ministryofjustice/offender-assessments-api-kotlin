package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AccountStatus
import uk.gov.justice.digital.oasys.api.UserDto
import uk.gov.justice.digital.oasys.api.UserRequestByEmail

@SqlGroup(
  Sql(
    scripts = ["classpath:authentication/before-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:authentication/after-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
@AutoConfigureWebTestClient
class AuthenticationControllerUserTest : IntegrationTest() {

  private val email = UserRequestByEmail("testemail@test.com")

  @Test
  fun `Returns oasys user by email`() {

    webTestClient.post().uri("/authentication/user/email")
      .bodyValue(email)
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<UserDto>()
      .consumeWith { assertThat(it.responseBody).isEqualTo(validUserDto()) }
  }

  @Test
  fun `Returns oasys user by email ignoring case`() {

    webTestClient.post().uri("/authentication/user/email")
      .bodyValue(UserRequestByEmail("TeStEmAiL@tEsT.CoM"))
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<UserDto>()
      .consumeWith { assertThat(it.responseBody).isEqualTo(validUserDto()) }
  }

  @Test
  fun `Invalid email returns Not Found`() {

    webTestClient.post().uri("/authentication/user/email")
      .bodyValue(UserRequestByEmail("notfound@test.com"))
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Duplicate email address in table returns Internal server error`() {

    webTestClient.post().uri("/authentication/user/email")
      .bodyValue(UserRequestByEmail("test@test.com"))
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().is5xxServerError
  }

  fun validUserDto(): UserDto {
    return UserDto(
      oasysUserCode = "USER2",
      userForename1 = "JOHN",
      userForename2 = "Middle",
      userForename3 = "Middle 2",
      userFamilyName = "SMITH",
      email = "testemail@test.com",
      regions = setOf("West Yorkshire", "Wakefield (HMP)"),
      accountStatus = AccountStatus.ACTIVE
    )
  }
}
