package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.SectionAnswersDto
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

@SqlGroup(
  Sql(
    scripts = ["classpath:assessments/before-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:assessments/after-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
@AutoConfigureWebTestClient(timeout = "36000")
class AnswersControllerTest : IntegrationTest() {

  private val assessmentId = 5433L

  @Test
  fun `valid oasys assessment Id returns list of answers`() {

    webTestClient.post().uri("/assessments/oasysSetId/$assessmentId/answers")
      .bodyValue(mapOf("10" to setOf("10.98"), "9" to setOf("9.99")))
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<AssessmentAnswersDto>()
      .consumeWith {
        val answers = it.responseBody
        assertThat(answers.assessmentId).isEqualTo(assessmentId)

        assertThat(answers.questionAnswers.map { a -> a.refQuestionCode })
          .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))
        val answer = answers.questionAnswers.first { a -> a.refQuestionCode == "10.98" }
        assertThat(answer.displayOrder).isEqualTo(998)
        assertThat(answer.displayScore).isNull()
        assertThat(answer.oasysQuestionId).isEqualTo(1347931)
        assertThat(answer.questionText).isEqualTo("Issues of emotional wellbeing linked to risk of serious harm, risks to the individual and other risks")
        assertThat(answer.refQuestionId).isEqualTo(1762)

        val questionAnswers = answer.answers.toList()[0]
        assertThat(questionAnswers.displayOrder).isEqualTo(5)
        assertThat(questionAnswers.freeFormText).isNull()
        assertThat(questionAnswers.oasysAnswerId).isEqualTo(5343777L)
        assertThat(questionAnswers.ogpScore).isNull()
        assertThat(questionAnswers.ovpScore).isNull()
        assertThat(questionAnswers.qaRawScore).isNull()
        assertThat(questionAnswers.staticText).isEqualTo("Yes")
        assertThat(questionAnswers.refAnswerCode).isEqualTo("YES")
        assertThat(questionAnswers.refAnswerId).isEqualTo(1995)
      }
  }

  @Test
  fun `OasysSetPK with no questions returns emptyset`() {

    webTestClient.post().uri("/assessments/oasysSetId/12345/answers")
      .bodyValue(mapOf("10" to setOf("10.98"), "9" to setOf("9.99")))
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<AssessmentAnswersDto>()
      .consumeWith {
        val answers = it.responseBody
        assertThat(answers.assessmentId).isEqualTo(12345)
        assertThat(answers.questionAnswers).isEmpty()
      }
  }

  @Test
  fun `can get assessment rosh risk sections by Assessment ID for assessment with ROSH`() {
    val assessmentId = 5433L

    webTestClient.post().uri("/assessments/oasysSetId/$assessmentId/sections/answers")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .bodyValue(
        setOf(
          SectionHeader.ROSH_SCREENING.name,
          SectionHeader.ROSH_FULL_ANALYSIS.name,
          SectionHeader.ROSH_SUMMARY.name
        )
      )
      .exchange()
      .expectStatus().isOk
      .expectBody<SectionAnswersDto>()
      .consumeWith {
        val sectionAnswers = it.responseBody
        assertThat(sectionAnswers?.assessmentId).isEqualTo(5433L)
        assertThat(sectionAnswers?.sections).hasSize(3)
        assertThat(sectionAnswers?.sections?.get(SectionHeader.ROSH_SCREENING.value) ?: emptyList()).hasSize(10)
        assertThat(sectionAnswers?.sections?.get(SectionHeader.ROSH_FULL_ANALYSIS.value) ?: emptyList()).hasSize(4)
        assertThat(sectionAnswers?.sections?.get(SectionHeader.ROSH_SUMMARY.value) ?: emptyList()).hasSize(9)
      }
  }

  @Test
  fun `get latest complete assessment find a completed assessment and rosh sections`() {
    val crn = "X320741"

    webTestClient.post()
      .uri(
        "/assessments/crn/$crn/sections/answers?assessmentStatus=COMPLETE" +
          "&assessmentTypes=LAYER_1,LAYER_3&period=YEAR&periodUnits=100"
      )
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .bodyValue(
        setOf(
          SectionHeader.ROSH_SCREENING.name,
          SectionHeader.ROSH_FULL_ANALYSIS.name,
          SectionHeader.ROSH_SUMMARY.name
        )
      )
      .exchange()
      .expectStatus().isOk
      .expectBody<SectionAnswersDto>()
      .consumeWith {
        val sectionAnswers = it.responseBody
        assertThat(sectionAnswers?.assessmentId).isEqualTo(1L)
        assertThat(sectionAnswers?.sections).hasSize(1)
        assertThat(sectionAnswers?.sections?.get(SectionHeader.ROSH_SCREENING.value) ?: emptyList()).hasSize(10)
      }
  }
}
