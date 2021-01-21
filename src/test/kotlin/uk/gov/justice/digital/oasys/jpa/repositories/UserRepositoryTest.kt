package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest

@SqlGroup(
  Sql(scripts = ["classpath:authentication/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:authentication/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@DisplayName("User Repository Tests")
class UserRepositoryTest(@Autowired private val repository: UserRepository) : IntegrationTest() {

  @Test
  fun `return oasys user by email`() {
    val oasysUser = repository.findOasysUserByEmailAddressIgnoreCase("testemail@test.com")
    assertThat(oasysUser?.oasysUserCode).isEqualTo("USER2")
    assertThat(oasysUser?.emailAddress).isEqualTo("testemail@test.com")
    assertThat(oasysUser?.userStatus?.refElementCode).isEqualTo("ACTIVE")
  }

  @Test
  fun `return oasys user by email ignoring case`() {
    val oasysUser = repository.findOasysUserByEmailAddressIgnoreCase("TeStEmAiL@tEsT.cOm")
    assertThat(oasysUser?.oasysUserCode).isEqualTo("USER2")
    assertThat(oasysUser?.emailAddress).isEqualTo("testemail@test.com")
    assertThat(oasysUser?.userStatus?.refElementCode).isEqualTo("ACTIVE")
  }

  @Test
  fun `throws exception when duplicate email in table`() {
    assertThrows<IncorrectResultSizeDataAccessException> { repository.findOasysUserByEmailAddressIgnoreCase("test@test.com") }
  }

  @Test
  fun `returns null when email does not exist`() {
    val oasysUser = repository.findOasysUserByEmailAddressIgnoreCase("notfound@test.com")
    assertThat(oasysUser).isNull()
  }
}
