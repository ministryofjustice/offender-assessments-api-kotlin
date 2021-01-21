package uk.gov.justice.digital.oasys.services

import com.microsoft.applicationinsights.TelemetryClient
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.AccountStatus
import uk.gov.justice.digital.oasys.api.OffenderPermissionLevel
import uk.gov.justice.digital.oasys.api.OffenderPermissionResource
import uk.gov.justice.digital.oasys.jpa.entities.AreaEstUserRole
import uk.gov.justice.digital.oasys.jpa.entities.CtAreaEst
import uk.gov.justice.digital.oasys.jpa.entities.OasysUser
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.repositories.AuthenticationRepository
import uk.gov.justice.digital.oasys.jpa.repositories.UserRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import uk.gov.justice.digital.oasys.services.exceptions.UserNotAuthorisedException

@ExtendWith(MockKExtension::class)
@DisplayName("Offender Service Tests")
class AuthenticationServiceTest {

  private val authenticationRepository: AuthenticationRepository = mockk()
  private val userRepository: UserRepository = mockk()
  private val telemetryClient: TelemetryClient = mockk(relaxed = true)
  private val service = AuthenticationService(userRepository, authenticationRepository, telemetryClient)

  private val username = "TEST_USER"
  private val password = "PASSWORD"
  private val sessionId = 123456L
  private val offenderId = 1L

  @Test
  fun `return User for OASys user code`() {
    val user = setupUser()
    val userCode = "TEST_USER"

    every { userRepository.findOasysUserByOasysUserCodeIgnoreCase(userCode) } returns user

    val oasysUser = service.getUserByUserId(userCode)
    assertThat(oasysUser.firstName).isEqualTo("Name 1")
    assertThat(oasysUser.lastName).isEqualTo("Last Name")
    assertThat(oasysUser.email).isEqualTo("test@test.com")
    assertThat(oasysUser.userName).isEqualTo("TEST_USER")
    assertThat(oasysUser.userId).isEqualTo("TEST_USER")
    assertThat<String>(oasysUser.regions).contains("1234")

    verify(exactly = 1) { userRepository.findOasysUserByOasysUserCodeIgnoreCase(userCode) }
  }

  @Test
  fun `throws not found exception when no user returns`() {
    val userCode = username
    every { userRepository.findOasysUserByOasysUserCodeIgnoreCase(userCode) } returns null
    assertThrows<EntityNotFoundException> { service.getUserByUserId(userCode) }
  }

  @Test
  fun `returns true when user is authenticated`() {
    every { authenticationRepository.validateCredentials(username, password) } returns """{STATE: "SUCCESS"}"""
    val result = service.validateUserCredentials(username, password)
    assertThat(result.authenticated).isTrue()
    verify(exactly = 1) { authenticationRepository.validateCredentials(username, password) }
  }

  @Test
  fun `throws exception when user is not authenticated`() {
    every { authenticationRepository.validateCredentials(username, "BADPASSWORD") } returns """{STATE: "FAILED"}"""
    val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials(username, "BADPASSWORD") }
    assertThat(exception.message).isEqualTo("Invalid username or password")
    verify(exactly = 1) { authenticationRepository.validateCredentials(username, "BADPASSWORD") }
  }

  @Test
  fun `throws exception when function returns invalid JSON`() {
    every { authenticationRepository.validateCredentials(username, "BADPASSWORD") } returns ""
    val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials(username, "BADPASSWORD") }
    assertThat(exception.message).isEqualTo("Failed to parse authentication check result from OASys")
    verify(exactly = 1) { authenticationRepository.validateCredentials(username, "BADPASSWORD") }
  }

  @Test
  fun `throws exception when password is null`() {
    val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials(username, null) }
    assertThat(exception.message).isEqualTo("User credentials cannot be empty")
    verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
  }

  @Test
  fun `throws exception when username is null`() {
    val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials(null, password) }
    assertThat(exception.message).isEqualTo("User credentials cannot be empty")
    verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
  }

  @Test
  fun `throws exception when username and password are null`() {
    val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials(null, null) }
    assertThat(exception.message).isEqualTo("User credentials cannot be empty")
    verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
  }

  @Test
  fun `returns WRITE when user is can write sentence plan`() {
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "EDIT"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, sessionId, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.WRITE)
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `returns READ when user is can only read sentence plan`() {
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "READ"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, sessionId, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `returns UNAUTHORISED when user has no access to sentence plan`() {
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "NO_ACCESS"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, sessionId, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.UNAUTHORISED)
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `returns UNAUTHORISED when function returns invalid JSON`() {
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "INVALID_RESULT"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, sessionId, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.UNAUTHORISED)
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `retrieves session from database when no session ID provided`() {
    every { userRepository.findCurrentUserSessionForOffender(offenderId, username) } returns sessionId
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "READ"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, null, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
    verify(exactly = 1) { userRepository.findCurrentUserSessionForOffender(offenderId, username) }
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `does not retrieve session from database when session ID provided`() {
    every { userRepository.findCurrentUserSessionForOffender(offenderId, username) } returns sessionId
    every { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) } returns """{STATE: "READ"}"""
    val result = service.userCanAccessOffenderRecord(username, offenderId, sessionId, OffenderPermissionResource.SENTENCE_PLAN)
    assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
    verify(exactly = 0) { userRepository.findCurrentUserSessionForOffender(offenderId, username) }
    verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession(username, offenderId, sessionId) }
  }

  @Test
  fun `return User for email`() {
    val user = setupUser()
    val email = "test@test.com"

    every { userRepository.findOasysUserByEmailAddressIgnoreCase(email) } returns user

    val userDto = service.getUserCodeByEmail(email)
    assertThat(userDto.userForename1).isEqualTo("Name 1")
    assertThat(userDto.userForename2).isEqualTo("Name 2")
    assertThat(userDto.userForename3).isEqualTo("Name 3")
    assertThat(userDto.userFamilyName).isEqualTo("Last Name")
    assertThat(userDto.accountStatus).isEqualTo(AccountStatus.ACTIVE)
    assertThat(userDto.email).isEqualTo("test@test.com")
    assertThat(userDto.oasysUserCode).isEqualTo("TEST_USER")

    verify(exactly = 1) { userRepository.findOasysUserByEmailAddressIgnoreCase(email) }
  }

  @Test
  fun `throws exception when email is null`() {
    val exception = assertThrows<IllegalArgumentException> { service.getUserCodeByEmail(null) }
    assertThat(exception.message).isEqualTo("Email cannot be blank")
    verify(exactly = 0) { userRepository.findOasysUserByEmailAddressIgnoreCase(any()) }
  }

  @Test
  fun `throws exception when email does not exist`() {
    val email = "notfound@test.com"
    every { userRepository.findOasysUserByEmailAddressIgnoreCase(email) } returns null

    val exception = assertThrows<EntityNotFoundException> { service.getUserCodeByEmail(email) }
    assertThat(exception.message).isEqualTo("User for email $email, not found")
  }

  private fun setupUser(): OasysUser {
    return OasysUser(
      "TEST_USER",
      1L,
      "Name 1",
      "Name 2",
      "Name 3",
      "Last Name",
      "test@test.com",
      RefElement(refCategoryCode = "USER_STATUS", refElementCode = "ACTIVE", refElementDesc = "Active"),
      CtAreaEst(1L),
      listOf(AreaEstUserRole(ctAreaEstCode = "1234"))
    )
  }
}
