package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.api.OffenderIdentifier
import java.time.LocalDateTime

@SqlGroup(
  Sql(
    scripts = ["classpath:assessments/before-test-summary.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:assessments/after-test-summary.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
@AutoConfigureWebTestClient(timeout = "36000")
class AssessmentControllerTestSummaries : IntegrationTest() {

  private val oasysOffenderId = 1234L
  private val pnc = "PNC"
  private val crn = "CRN"
  private val nomis = "NOMIS"
  private val booking = "BOOKIN"

  private val layerOneAssessmentId = 5434L
  private val openAssessmentId = 5433L
  private val completeAssessmentId = 5432L
  private val completeAssessmentId2 = 5436L
  private val voidedAssessmentId = 5431L
  private val historicAssessmentId = 5430L

  @Test
  fun `oasys offender pk returns list of assessment summaries`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(6)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )

        validateOpenAssessmentSummary(summaries.first { s -> s.assessmentId == openAssessmentId })
      }
  }

  @Test
  fun `oasys offender pk returns not found`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/999/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Nomis Booking ID returns list of assessment summaries`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.BOOKING.value}/$booking/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(6)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )

        validateOpenAssessmentSummary(summaries.first { s -> s.assessmentId == openAssessmentId })
      }
  }

  @Test
  fun `Nomis Booking ID returns not found`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.BOOKING.value}/999/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `CRN returns list of assessment summaries`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/$crn/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(6)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )

        validateOpenAssessmentSummary(summaries.first { s -> s.assessmentId == openAssessmentId })
      }
  }

  @Test
  fun `CRN returns not found`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/999/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Nomis ID returns list of assessment summaries`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/$nomis/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(6)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )

        validateOpenAssessmentSummary(summaries.first { s -> s.assessmentId == openAssessmentId })
      }
  }

  @Test
  fun `Nomis ID returns not found`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/999/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `PNC returns list of assessment summaries`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/$pnc/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(6)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )

        validateOpenAssessmentSummary(summaries.first { s -> s.assessmentId == openAssessmentId })
      }
  }

  @Test
  fun `PNC returns not found`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/999/assessments/summary")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `assessment status returns only COMPLETE summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?assessmentStatus=COMPLETE")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(4)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              voidedAssessmentId,
              completeAssessmentId,
              completeAssessmentId2
            )
          )
        assertThat(summaries.map { a -> a.assessmentStatus }).containsOnly("COMPLETE")
      }
  }

  @Test
  fun `voided filter returns only voided assessment summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?voided=true")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(1)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(setOf(voidedAssessmentId))
        assertThat(summaries.map { a -> a.voided }).doesNotContainNull()
      }
  }

  @Test
  fun `not voided filter returns only non voided assessment summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?voided=false")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(5)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              layerOneAssessmentId
            )
          )
        assertThat(summaries.map { a -> a.voided }).containsOnlyNulls()
      }
  }

  @Test
  fun `assessment type filter returns only LAYER_3 type summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?assessmentType=laYER_3")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(4)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              historicAssessmentId,
              openAssessmentId,
              completeAssessmentId,
              voidedAssessmentId
            )
          )
        assertThat(summaries.map { a -> a.assessmentType }).containsOnly("LAYER_3")
      }
  }

  @Test
  fun `assessment type filter returns only LAYER_1 type summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?assessmentType=LAYER_1")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(2)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(setOf(layerOneAssessmentId, completeAssessmentId2))
        assertThat(summaries.map { a -> a.assessmentType }).containsOnly("LAYER_1")
      }
  }

  @Test
  fun `assessment historic status returns only CURRENT summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?historicStatus=CurreNT")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(5)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(
            setOf(
              openAssessmentId,
              completeAssessmentId,
              completeAssessmentId2,
              voidedAssessmentId,
              layerOneAssessmentId
            )
          )
        assertThat(summaries.map { a -> a.historicStatus }).containsOnly("CURRENT")
      }
  }

  @Test
  fun `assessment historic status returns only OTHER summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?historicStatus=OTheR")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(1)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId))
        assertThat(summaries.map { a -> a.historicStatus }).containsOnly("OTHER")
      }
  }

  @Test
  fun `assessment historic status and assessment status returns only combined summaries`() {

    webTestClient.get()
      .uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/summary?historicStatus=current&assessmentType=layer_3")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<AssessmentSummaryDto>>()
      .consumeWith {
        val summaries = it.responseBody

        assertThat(summaries).hasSize(3)
        assertThat(summaries.map { a -> a.assessmentId })
          .containsExactlyInAnyOrderElementsOf(setOf(openAssessmentId, completeAssessmentId, voidedAssessmentId))
        assertThat(summaries.map { a -> a.historicStatus }).containsOnly("CURRENT")
        assertThat(summaries.map { a -> a.assessmentType }).containsOnly("LAYER_3")
      }
  }

  private fun validateOpenAssessmentSummary(assessmentSummary: AssessmentSummaryDto) {
    assertThat(assessmentSummary.assessmentId).isEqualTo(openAssessmentId)
    assertThat(assessmentSummary.refAssessmentVersionCode).isEqualTo("LAYER3")
    assertThat(assessmentSummary.refAssessmentVersionNumber).isEqualTo("1")
    assertThat(assessmentSummary.refAssessmentId).isEqualTo(4L)
    assertThat(assessmentSummary.assessmentType).isEqualTo("LAYER_3")
    assertThat(assessmentSummary.assessmentStatus).isEqualTo("OPEN")
    assertThat(assessmentSummary.historicStatus).isEqualTo("CURRENT")
    assertThat(assessmentSummary.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(3L)
    assertThat(assessmentSummary.assessorName).isEqualTo("Probation Test")
    assertThat(assessmentSummary.created).isEqualTo(LocalDateTime.of(2018, 5, 21, 23, 0, 9))
    assertThat(assessmentSummary.completed).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
    assertThat(assessmentSummary.voided).isNull()
  }
}
