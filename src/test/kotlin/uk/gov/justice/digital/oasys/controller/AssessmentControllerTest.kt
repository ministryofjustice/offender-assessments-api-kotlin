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
import uk.gov.justice.digital.oasys.api.OffenderIdentifier
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import java.time.LocalDate
import java.time.LocalDateTime

@SqlGroup(
  Sql(scripts = ["classpath:assessments/before-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:assessments/after-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient
class AssessmentControllerTest : IntegrationTest() {

  private val validOasysSetId = 5433L
  private val oasysOffenderId = 1234L

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
  fun `offender PK returns latest date assessment by created date`() {

    webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/$oasysOffenderId/assessments/latest")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<AssessmentDto>()
      .consumeWith {
        val assessment = it.responseBody
        assertThat(assessment?.assessmentId).isEqualTo(5434L)
        assertThat(assessment?.created).isEqualToIgnoringSeconds(LocalDateTime.of(2018, 5, 22, 23, 0))
      }
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
    assertThat(assessment.sections).hasSize(17)

    // sentence data
    val sentence = assessment.sentence?.first { s -> s?.sentenceCode == "310" }
    assertThat(sentence?.sentenceLengthCustodyDays).isEqualTo(100)
    assertThat(sentence?.sentenceDate).isEqualTo(LocalDate.of(2017, 10, 1))
    assertThat(sentence?.offenceDate).isEqualTo(LocalDate.of(2017, 8, 1))
    assertThat(sentence?.activity).isEqualTo("Activity 1")
    assertThat(sentence?.cja).isFalse()
    assertThat(sentence?.custodial).isTrue()
    assertThat(sentence?.cjaSupervisionMonths).isNull()
    assertThat(sentence?.cjaUnpaidHours).isEqualTo(120)
    assertThat(sentence?.orderType?.code).isEqualTo("S")
    assertThat(sentence?.offenceBlockType?.code).isEqualTo("PRINCIPAL_PROPOSAL")
    assertThat(sentence?.sentenceDescription).isEqualTo("Life")
  }
}
