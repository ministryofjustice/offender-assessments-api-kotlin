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
        val user = setupUser()
        val userCode = "TEST_USER"

        every { userRepository.findOasysUserByOasysUserCodeIgnoreCase(userCode) } returns null
        assertThrows<EntityNotFoundException> { service.getUserByUserId(userCode) }
    }


    @Test
    fun `returns true when user is authenticated`() {
        every { authenticationRepository.validateCredentials("TEST_USER", "PASSWORD") } returns """{STATE: "SUCCESS"}"""
        val result = service.validateUserCredentials("TEST_USER", "PASSWORD")
        assertThat(result.authenticated).isTrue()
        verify(exactly = 1) { authenticationRepository.validateCredentials("TEST_USER", "PASSWORD") }
    }

    @Test
    fun `throws exception when user is not authenticated`() {
        every { authenticationRepository.validateCredentials("TEST_USER", "BADBASSWORD") } returns """{STATE: "FAILED"}"""
        val exception = assertThrows<UserNotAuthorisedException> {service.validateUserCredentials("TEST_USER", "BADBASSWORD") }
        assertThat(exception.message).isEqualTo("Invalid username or password")
        verify(exactly = 1) { authenticationRepository.validateCredentials("TEST_USER", "BADBASSWORD") }
    }

    @Test
    fun `throws exception when function returns invalid JSON`() {
        every { authenticationRepository.validateCredentials("TEST_USER", "BADBASSWORD") } returns ""
        val exception = assertThrows<UserNotAuthorisedException> { service.validateUserCredentials("TEST_USER", "BADBASSWORD") }
        assertThat(exception.message).isEqualTo("Failed to parse authentication check result from OASys")
        verify(exactly = 1) { authenticationRepository.validateCredentials("TEST_USER", "BADBASSWORD") }
    }

    @Test
    fun `throws exception when password is null`() {
        val exception = assertThrows<UserNotAuthorisedException> {service.validateUserCredentials("TEST_USER", null) }
        assertThat(exception.message).isEqualTo("User credentials cannot be empty")
        verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
    }

    @Test
    fun `throws exception when username is null`() {
        val exception = assertThrows<UserNotAuthorisedException> {service.validateUserCredentials(null, "PASSWORD") }
        assertThat(exception.message).isEqualTo("User credentials cannot be empty")
        verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
    }

    @Test
    fun `throws exception when username and apssword are null`() {
        val exception = assertThrows<UserNotAuthorisedException> {service.validateUserCredentials(null, null) }
        assertThat(exception.message).isEqualTo("User credentials cannot be empty")
        verify(exactly = 0) { authenticationRepository.validateCredentials(any(), any()) }
    }

    @Test
    fun `returns WRITE when user is can write sentence plan`() {
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "EDIT"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, 123456L, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.WRITE)
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    @Test
    fun `returns READ when user is can only read sentence plan`() {
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "READ"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, 123456L, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    @Test
    fun `returns UNAUTHORISED when user has no access to sentence plan`() {
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "NO_ACCESS"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, 123456L, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.UNAUTHORISED)
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    @Test
    fun `returns UNAUTHORISED when function returns invalid JSON`() {
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "INVALID_RESULT"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, 123456L, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.UNAUTHORISED)
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    @Test
    fun `retrieves session from database when no session ID provided`() {
        every { userRepository.findCurrentUserSessionForOffender(1, "TEST_USER") } returns 123456L
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "READ"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, null, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
        verify(exactly = 1) { userRepository.findCurrentUserSessionForOffender(1, "TEST_USER") }
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    @Test
    fun `does not retrieve session from database when session ID provided`() {
        every { userRepository.findCurrentUserSessionForOffender(1, "TEST_USER") } returns 123456L
        every { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) } returns """{STATE: "READ"}"""
        val result = service.userCanAccessOffenderRecord("TEST_USER", 1L, 123456L, OffenderPermissionResource.SENTENCE_PLAN)
        assertThat(result.offenderPermissionLevel).isEqualTo(OffenderPermissionLevel.READ_ONLY)
        verify(exactly = 0) { userRepository.findCurrentUserSessionForOffender(1, "TEST_USER") }
        verify(exactly = 1) { authenticationRepository.validateUserSentencePlanAccessWithSession("TEST_USER", 1, 123456) }
    }

    fun setupUser(): OasysUser {
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
                listOf(AreaEstUserRole(ctAreaEstCode = "1234")))
    }




}