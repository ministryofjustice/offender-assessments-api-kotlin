package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.RiskAnswerDto
import uk.gov.justice.digital.oasys.api.RiskDto
import uk.gov.justice.digital.oasys.api.RiskQuestionDto

@SqlGroup(
  Sql(scripts = ["classpath:risks/before-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:risks/after-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient
class RisksControllerTest : IntegrationTest() {

  @Test
  fun `can get risks by Assessment ID for assessment with ROSH and SARA`() {
    val assessmentId = 9487347

    webTestClient.get().uri("/offenders/risks/assessment/$assessmentId")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<RiskDto>()
      .consumeWith {
        val risks = it.responseBody
        assertThat(risks?.oasysSetId).isEqualTo(9487347)
        assertThat(risks?.rosha?.riskQuestions).hasSize(10)
        assertThat(risks?.rosha?.riskQuestions?.get(0)).isEqualTo(validRoshaQuestion())

        assertThat(risks?.sara?.oasysSetId).isEqualTo(9487348)
        assertThat(risks?.sara?.riskQuestions).hasSize(2)
        assertThat(risks?.sara?.riskQuestions?.get(0)).isEqualTo(validSaraQuestion())

        assertThat(risks?.rosh?.oasysSetId).isEqualTo(9487347)
        assertThat(risks?.rosh?.riskQuestions).hasSize(2)
        assertThat(risks?.rosh?.riskQuestions).containsExactly(validRoshQuestion1(), validRoshQuestion2())
        assertThat(risks?.childSafeguardingIndicated).isTrue
      }
  }

  @Test
  fun `can get risks for SARA Assessment by Assessment ID`() {
    val assessmentId = 9487348

    webTestClient.get().uri("/offenders/risks/assessment/$assessmentId")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<RiskDto>()
      .consumeWith {
        val risks = it.responseBody
        assertThat(risks?.oasysSetId).isEqualTo(9487348)
        assertThat(risks?.rosha).isNull()

        assertThat(risks?.sara?.oasysSetId).isEqualTo(9487348)
        assertThat(risks?.sara?.riskQuestions).hasSize(2)
        assertThat(risks?.sara?.riskQuestions?.get(0)).isEqualTo(validSaraQuestion())
      }
  }

  @Test
  fun `assessment ID returns not found`() {
    val assessmentId = 0L

    webTestClient.get().uri("/offenders/risks/assessment/$assessmentId")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `can get risks for Offender with Assessment with ROSHA and SARA`() {
    val identity = 1234L
    val identityType = "oasysOffenderId"

    webTestClient.get().uri("/offenders/$identityType/$identity/risks")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(RiskDto::class.java)
      .consumeWith<ListBodySpec<RiskDto>> {
        val risks = it.responseBody
        assertThat(risks).hasSize(2)
        assertThat(risks?.get(0)?.oasysSetId).isEqualTo(9487347)
        assertThat(risks?.get(0)?.rosha?.riskQuestions).hasSize(10)
        assertThat(risks?.get(0)?.rosha?.riskQuestions?.get(0)).isEqualTo(validRoshaQuestion())

        assertThat(risks?.get(0)?.sara?.oasysSetId).isEqualTo(9487348)
        assertThat(risks?.get(0)?.sara?.riskQuestions).hasSize(2)
        assertThat(risks?.get(0)?.sara?.riskQuestions?.get(0)).isEqualTo(validSaraQuestion())

        assertThat(risks?.get(0)?.rosh?.oasysSetId).isEqualTo(9487347)
        assertThat(risks?.get(0)?.rosh?.riskQuestions).hasSize(2)
        assertThat(risks?.get(0)?.rosh?.riskQuestions).containsExactly(validRoshQuestion1(), validRoshQuestion2())
        assertThat(risks?.get(0)?.childSafeguardingIndicated).isTrue

        assertThat(risks?.get(1)?.rosh).isNull()
        assertThat(risks?.get(1)?.childSafeguardingIndicated).isNull()
      }
  }

  @Test
  fun `offender id returns not found`() {
    val identity = 0L
    val identityType = "oasysOffenderId"

    webTestClient.get().uri("/offenders/$identityType/$identity/risks")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `access forbidden when no authority`() {

    webTestClient.get().uri("/offenders/risks/assessment/1234")
      .header("Content-Type", "application/json")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `access forbidden when no role`() {

    webTestClient.get().uri("/offenders/risks/assessment/1234")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  fun validRoshaQuestion(): RiskQuestionDto {
    return RiskQuestionDto(
      refQuestionCode = "SUM6.1.2",
      questionText = "Risk in Custody",
      currentlyHidden = false,
      disclosed = false,
      answers = listOf(RiskAnswerDto(refAnswerCode = "H", staticText = "High"))
    )
  }

  fun validSaraQuestion(): RiskQuestionDto {
    return RiskQuestionDto(
      refQuestionCode = "SR76.1.1",
      questionText = "Risk Rating",
      currentlyHidden = false,
      disclosed = false,
      answers = listOf(RiskAnswerDto(refAnswerCode = "3", staticText = "High"))
    )
  }

  fun validRoshQuestion1(): RiskQuestionDto {
    return RiskQuestionDto(
      refQuestionCode = "R2.1",
      questionText = "Is the offender, now or on release, likely to live with, or have frequent contact with, any child who is on the child protection register or is being looked after by the local authority.",
      currentlyHidden = false,
      disclosed = false,
      answers = listOf(RiskAnswerDto(refAnswerCode = "YES", staticText = "Yes"))
    )
  }

  fun validRoshQuestion2(): RiskQuestionDto {
    return RiskQuestionDto(
      refQuestionCode = "R2.2",
      questionText = "Are there any concerns in relation to children",
      currentlyHidden = false,
      disclosed = false,
      answers = listOf(RiskAnswerDto(refAnswerCode = "YES", staticText = "Yes"))
    )
  }
}
