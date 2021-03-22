package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.PredictorDto
import uk.gov.justice.digital.oasys.api.RiskDto
import java.math.BigDecimal
import java.time.LocalDateTime

@SqlGroup(
  Sql(
    scripts = ["classpath:risks/before-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:risks/after-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)

@AutoConfigureWebTestClient
class RisksControllerTest : IntegrationTest() {

  @Test
  fun `can get risks for Offender with Assessment ID`() {
    val identity = "1234"
    val identityType = "OASYS"
    val assessmentId = 5433L

    webTestClient.get().uri("/offenders/$identityType/$identity/risks/assessment/$assessmentId")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<RiskDto>()
      .consumeWith {
        val risks = it.responseBody
        assertThat(risks).isEqualTo(null)
      }
  }
}
