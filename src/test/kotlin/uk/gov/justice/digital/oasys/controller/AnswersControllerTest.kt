package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto

@SqlGroup(
        Sql(scripts = ["classpath:assessments/before-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:assessments/after-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient(timeout = "36000")
class AnswersControllerTest : IntegrationTest() {

    private val assessmentId = 5433L

    @Test
    fun `valid oasys assessment Id returns list of answers`() {

        webTestClient.post().uri("/assessments/oasysSetId/${assessmentId}/answers")
                .bodyValue(setOf("10.98", "9.99"))
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<AssessmentAnswersDto>()
                .consumeWith {
                    val answers = it.responseBody
                    assertThat(answers.assessmentId).isEqualTo(assessmentId)

                    assertThat(answers.questionAnswers.map{ a -> a.refQuestionCode})
                            .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))
                    val answer = answers.questionAnswers.first { a -> a.refQuestionCode == "10.98" }
                    assertThat(answer.displayOrder).isEqualTo(998)
                    assertThat(answer.displayScore).isNull()
                    assertThat(answer.oasysQuestionId).isEqualTo(1347931)
                    assertThat(answer.questionText).isEqualTo("Issues of emotional wellbeing linked to risk of serious harm, risks to the individual and other risks")
                    assertThat(answer.refQuestionId).isEqualTo(1762)

                    assertThat(answer.answers.toList()[0].displayOrder).isEqualTo(5)
                    assertThat(answer.answers.toList()[0].freeFormText).isNull()
                    assertThat(answer.answers.toList()[0].oasysAnswerId).isEqualTo(5343777L)
                    assertThat(answer.answers.toList()[0].ogpScore).isNull()
                    assertThat(answer.answers.toList()[0].ovpScore).isNull()
                    assertThat(answer.answers.toList()[0].qaRawScore).isNull()
                    assertThat(answer.answers.toList()[0].staticText).isEqualTo("Yes")
                    assertThat(answer.answers.toList()[0].refAnswerCode).isEqualTo("YES")
                    assertThat(answer.answers.toList()[0].refAnswerId).isEqualTo(1995)
                }
    }

    @Test
    fun `unknown OasysSetPK returns not found`() {

        webTestClient.post().uri("/assessments/oasysSetId/12345/answers")
                .bodyValue(setOf("10.98", "9.99"))
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }
}