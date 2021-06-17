package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentNeedDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import java.time.LocalDateTime

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
@AutoConfigureWebTestClient
class AssessmentControllerTest : IntegrationTest() {

  private val validOasysSetId = 5433L
  private val oasysSetIdForCompletedAssessment = 5432L

  @Test
  fun `access forbidden when no authority`() {

    webTestClient.get().uri("/assessments/oasysSetId/$validOasysSetId")
      .header("Content-Type", "application/json")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `access forbidden when no role`() {

    webTestClient.get().uri("/assessments/oasysSetId/$validOasysSetId")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `oasys assessment PK returns assessment`() {

    webTestClient.get().uri("/assessments/oasysSetId/$validOasysSetId")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<AssessmentDto>()
      .consumeWith { validateAssessment(it.responseBody) }
  }

  @Test
  fun `oasys assessment PK returns not found`() {

    webTestClient.get().uri("/assessments/oasysSetId/99999")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `oasys assessment PK returns all assessment needs`() {

    webTestClient.get().uri("/assessments/oasysSetId/$validOasysSetId/needs")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentNeedDto>>()
      .consumeWith {
        val needs = it.responseBody
        assertThat(needs).hasSize(10)
      }
  }

  @Test
  fun `get latest complete assessment doesn't find an assessment`() {
    val crn = "CRN"

    webTestClient.get()
      .uri(
        "/offenders/crn/$crn/assessments?assessmentStatus=COMPLETE" +
          "&assessmentTypes=LAYER_1,LAYER_3&period=YEAR&periodUnits=100"
      )
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<AssessmentSummaryDto>()
      .consumeWith {
        val assessment = it.responseBody
        validateCompletedAssessmentSummary(assessment)
      }
  }

  @Test
  fun `can't assessments for offender`() {
    val crn = 999

    webTestClient.get().uri("/offenders/crn/$crn/assessments?assessmentStatus=COMPLETE")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  fun validateAssessment(assessment: AssessmentDto) {
    assertThat(assessment.assessmentId).isEqualTo(validOasysSetId)
    assertThat(assessment.refAssessmentVersionCode).isEqualTo("LAYER3")
    assertThat(assessment.refAssessmentVersionNumber).isEqualTo("1")
    assertThat(assessment.refAssessmentId).isEqualTo(4L)
    assertThat(assessment.assessmentType).isEqualTo("LAYER_3")
    assertThat(assessment.assessmentStatus).isEqualTo("OPEN")
    assertThat(assessment.historicStatus).isEqualTo("CURRENT")
    assertThat(assessment.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(3L)
    assertThat(assessment.assessorName).isEqualTo("Probation Test")
    assertThat(assessment.created).isEqualTo(LocalDateTime.of(2018, 5, 21, 23, 0, 9))
    assertThat(assessment.completed).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
    assertThat(assessment.voided).isNull()

    // Sections check
    assertThat(assessment.sections).hasSize(19)
  }

  private fun validateCompletedAssessmentSummary(assessmentSummary: AssessmentSummaryDto) {
    assertThat(assessmentSummary.assessmentId).isEqualTo(oasysSetIdForCompletedAssessment)
    assertThat(assessmentSummary.refAssessmentVersionCode).isEqualTo("LAYER3")
    assertThat(assessmentSummary.refAssessmentVersionNumber).isEqualTo("1")
    assertThat(assessmentSummary.refAssessmentId).isEqualTo(4L)
    assertThat(assessmentSummary.assessmentType).isEqualTo("LAYER_3")
    assertThat(assessmentSummary.assessmentStatus).isEqualTo("COMPLETE")
    assertThat(assessmentSummary.historicStatus).isEqualTo("CURRENT")
    assertThat(assessmentSummary.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(3L)
    assertThat(assessmentSummary.assessorName).isEqualTo("Probation Test")
    assertThat(assessmentSummary.created).isEqualTo(LocalDateTime.of(2018, 5, 20, 23, 0, 9))
    assertThat(assessmentSummary.completed).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
    assertThat(assessmentSummary.voided).isNull()
  }
}
