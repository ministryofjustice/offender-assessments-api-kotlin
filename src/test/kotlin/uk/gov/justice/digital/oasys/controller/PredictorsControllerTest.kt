package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec
import uk.gov.justice.digital.oasys.api.PredictorDto
import java.math.BigDecimal
import java.time.LocalDateTime

@SqlGroup(
  Sql(scripts = ["classpath:ogr-ovp-ogp/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:ogr-ovp-ogp/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)

@AutoConfigureWebTestClient
class PredictorsControllerTest : IntegrationTest() {

  private val oasysSetID1 = 5430L
  private val oasysSetID2 = 5431L

  private val pnc = "PNC"
  private val invalidPnc = "pnc2"
  private val crn = "CRN"
  private val invalidCrn = "crn2"
  private val nomis = "NOMIS"
  private val invalidNomis = "nomisId2"
  private val bookingId = "BOOKIN"
  private val invalidBookingId = "bookingId2"

  @Test
  fun `can get OGP for Offender CRNs`() {
    webTestClient.get().uri("/offenders/crn/$crn/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(PredictorDto::class.java)
      .consumeWith<ListBodySpec<PredictorDto>> {
        val predictors = it.responseBody
        assertThat(predictors?.map { a -> a.oasysSetId })
          .containsExactlyInAnyOrderElementsOf(setOf(oasysSetID1, oasysSetID2))
        validatePredictors(predictors)
      }
  }

  @Test
  fun `get predictors for unknown Offender gives Not Found`() {

    webTestClient.get().uri("/offenders/crn/$invalidCrn/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `can get predictors for Offender PNC`() {
    webTestClient.get().uri("/offenders/pnc/$pnc/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(PredictorDto::class.java)
      .consumeWith<ListBodySpec<PredictorDto>> {
        val predictors = it.responseBody
        assertThat(predictors?.map { a -> a.oasysSetId })
          .containsExactlyInAnyOrderElementsOf(setOf(oasysSetID1, oasysSetID2))
        validatePredictors(predictors)
      }
  }

  @Test
  fun `get predictors for unknown offender PNC gives Not Found`() {
    webTestClient.get().uri("/offenders/pnc/$invalidPnc/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `can get predictors for offender nomis ID`() {
    webTestClient.get().uri("/offenders/nomisId/$nomis/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(PredictorDto::class.java)
      .consumeWith<ListBodySpec<PredictorDto>> {
        val predictors = it.responseBody
        assertThat(predictors?.map { a -> a.oasysSetId })
          .containsExactlyInAnyOrderElementsOf(setOf(oasysSetID1, oasysSetID2))
        validatePredictors(predictors)
      }
  }

  @Test
  fun `get predictors for unknown offender nomis ID gives Not Found`() {
    webTestClient.get().uri("/offenders/nomisId/$invalidNomis/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `can get predictors for offender booking ID`() {
    webTestClient.get().uri("/offenders/bookingId/$bookingId/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(PredictorDto::class.java)
      .consumeWith<ListBodySpec<PredictorDto>> {
        val predictors = it.responseBody
        assertThat(predictors?.map { a -> a.oasysSetId })
          .containsExactlyInAnyOrderElementsOf(setOf(oasysSetID1, oasysSetID2))
        validatePredictors(predictors)
      }
  }

  @Test
  fun `get predictors for unknown offender booking ID gives Not Found`() {
    webTestClient.get().uri("/offenders/bookingId/$invalidBookingId/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `can get predictors for Oasys offender PK`() {
    webTestClient.get().uri("/offenders/oasysOffenderId/1234/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBodyList(PredictorDto::class.java)
      .consumeWith<ListBodySpec<PredictorDto>> {
        val predictors = it.responseBody
        assertThat(predictors?.map { a -> a.oasysSetId })
          .containsExactlyInAnyOrderElementsOf(setOf(oasysSetID1, oasysSetID2))
        validatePredictors(predictors)
      }
  }

  @Test
  fun `get predictors for unknown Oasys offender PK gives Not Found`() {
    webTestClient.get().uri("/offenders/oasysOffenderId/2/predictors")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  private fun validatePredictors(predictors: List<PredictorDto>?) {

    val predictor1: PredictorDto? = predictors?.firstOrNull { it.oasysSetId == oasysSetID1 }
    val predictor2: PredictorDto? = predictors?.firstOrNull { it.oasysSetId == oasysSetID2 }

    // Oasys Set 5430L
    assertThat(predictor1?.oasysSetId).isEqualTo(oasysSetID1)
    assertThat(predictor1?.refAssessmentVersionCode).isEqualTo("LAYER3")
    assertThat(predictor1?.refAssessmentVersionNumber).isEqualTo("1")
    assertThat(predictor1?.refAssessmentId).isEqualTo(4L)
    assertThat(predictor1?.completedDate).isNull()
    assertThat(predictor1?.voidedDateTime).isNull()
    assertThat(predictor1?.assessmentCompleted).isEqualTo(false)

    // OGP
    assertThat(predictor1?.ogp?.ogpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor1?.ogp?.ogpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(7))
    assertThat(predictor1?.ogp?.ogpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(10))
    assertThat(predictor1?.ogp?.ogp1Year).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor1?.ogp?.ogp2Year).isEqualTo(BigDecimal.valueOf(8))
    assertThat(predictor1?.ogp?.ogpRisk?.description).isEqualTo("Low")
    assertThat(predictor1?.ogp?.ogpRisk?.code).isEqualTo("L")

    // OVP
    assertThat(predictor1?.ovp?.ovpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(14))
    assertThat(predictor1?.ovp?.ovpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor1?.ovp?.ovpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(17))
    assertThat(predictor1?.ovp?.ovp1Year).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor1?.ovp?.ovp2Year).isEqualTo(BigDecimal.valueOf(7))
    assertThat(predictor1?.ovp?.ovpRisk?.description).isEqualTo("Low")
    assertThat(predictor1?.ovp?.ovpRisk?.code).isEqualTo("L")
    assertThat(predictor1?.ovp?.ovpPreviousWeightedScore).isEqualTo(BigDecimal.valueOf(5))
    assertThat(predictor1?.ovp?.ovpViolentWeightedScore).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor1?.ovp?.ovpNonViolentWeightedScore).isEqualTo(BigDecimal.valueOf(0))
    assertThat(predictor1?.ovp?.ovpAgeWeightedScore).isEqualTo(BigDecimal.valueOf(0))
    assertThat(predictor1?.ovp?.ovpSexWeightedScore).isEqualTo(BigDecimal.valueOf(5))

    // OGRs
    assertThat(predictor1?.ogr3?.ogrs3_1Year).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor1?.ogr3?.ogrs3_2Year).isEqualTo(BigDecimal.valueOf(5))
    assertThat(predictor1?.ogr3?.reconvictionRisk?.description).isEqualTo("Low")
    assertThat(predictor1?.ogr3?.reconvictionRisk?.code).isEqualTo("L")

    // Oasys Set 5431L
    assertThat(predictor2?.oasysSetId).isEqualTo(oasysSetID2)
    assertThat(predictor2?.refAssessmentVersionCode).isEqualTo("LAYER3")
    assertThat(predictor2?.refAssessmentVersionNumber).isEqualTo("1")
    assertThat(predictor2?.refAssessmentId).isEqualTo(4L)
    assertThat(predictor2?.completedDate).isEqualTo(LocalDateTime.of(2018, 7, 21, 23, 0, 9))
    assertThat(predictor2?.voidedDateTime).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
    assertThat(predictor2?.assessmentCompleted).isEqualTo(true)

    // OGP
    assertThat(predictor2?.ogp?.ogpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor2?.ogp?.ogpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(7))
    assertThat(predictor2?.ogp?.ogpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(10))
    assertThat(predictor2?.ogp?.ogp1Year).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor2?.ogp?.ogp2Year).isEqualTo(BigDecimal.valueOf(8))
    assertThat(predictor2?.ogp?.ogpRisk?.description).isEqualTo("Low")
    assertThat(predictor2?.ogp?.ogpRisk?.code).isEqualTo("L")

    // OVP
    assertThat(predictor2?.ovp?.ovpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(14))
    assertThat(predictor2?.ovp?.ovpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor2?.ovp?.ovpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(17))
    assertThat(predictor2?.ovp?.ovp1Year).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor2?.ovp?.ovp2Year).isEqualTo(BigDecimal.valueOf(7))
    assertThat(predictor2?.ovp?.ovpRisk?.description).isEqualTo("Low")
    assertThat(predictor2?.ovp?.ovpRisk?.code).isEqualTo("L")
    assertThat(predictor2?.ovp?.ovpPreviousWeightedScore).isEqualTo(BigDecimal.valueOf(5))
    assertThat(predictor2?.ovp?.ovpViolentWeightedScore).isEqualTo(BigDecimal.valueOf(4))
    assertThat(predictor2?.ovp?.ovpNonViolentWeightedScore).isEqualTo(BigDecimal.valueOf(0))
    assertThat(predictor2?.ovp?.ovpAgeWeightedScore).isEqualTo(BigDecimal.valueOf(0))
    assertThat(predictor2?.ovp?.ovpSexWeightedScore).isEqualTo(BigDecimal.valueOf(5))

    // OGRs
    assertThat(predictor2?.ogr3?.ogrs3_1Year).isEqualTo(BigDecimal.valueOf(3))
    assertThat(predictor2?.ogr3?.ogrs3_2Year).isEqualTo(BigDecimal.valueOf(5))
    assertThat(predictor2?.ogr3?.reconvictionRisk?.description).isEqualTo("Low")
    assertThat(predictor2?.ogr3?.reconvictionRisk?.code).isEqualTo("L")
  }
}
