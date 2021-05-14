package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest

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
@DisplayName("User Repository Tests")
class AreaRepositoryTest(@Autowired private val repository: AreaRepository) : IntegrationTest() {

  @Test
  fun `return area names by area codes`() {
    val areas = repository.findCtAreaEstByCtAreaEstCodes(setOf("1651", "1653"))

    assertThat(areas).containsExactly("Lancashire", "Thames Valley CRC")
  }
}
