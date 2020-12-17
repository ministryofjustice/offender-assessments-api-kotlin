package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.RefAnswerDto
import uk.gov.justice.digital.oasys.api.RefAssessmentDto
import uk.gov.justice.digital.oasys.api.RefQuestionDto
import uk.gov.justice.digital.oasys.api.RefSectionDto

@SqlGroup(
  Sql(scripts = ["classpath:reference/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:reference/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)

@AutoConfigureWebTestClient
class ReferenceAssessmentControllerTest : IntegrationTest() {
  val versionCode = "TEST1"
  val versionNumber = "1"
  val invalid = "invalid"

  @Test
  fun `can get Reference Assessment for assessment type and version`() {
    webTestClient.get().uri("/referenceassessments/type/$versionCode/revision/$versionNumber")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<RefAssessmentDto>()
      .consumeWith { assertThat(it.responseBody).isEqualTo(validateReferenceAssessment()) }
  }

  @Test
  fun `get Reference Assessment for invalid assessment type code gives Not Found`() {

    webTestClient.get().uri("/referenceassessments/type/$invalid/revision/$versionNumber")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `get Reference Assessment for invalid assessment version number gives Not Found`() {

    webTestClient.get().uri("/referenceassessments/type/$versionCode/revision/$invalid")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  private fun validateReferenceAssessment(): RefAssessmentDto {
    return RefAssessmentDto(
      refAssessmentVersionId = 100L,
      refAssVersionCode = versionCode,
      versionNumber = versionNumber,
      oasysScoringAlgorithmVersion = 3,
      refModuleCode = "ASS010",
      refSections = listOf(
        RefSectionDto(
          refSectionId = null,
          refSectionCode = "TEST_SECTION",
          shortDescription = "Self Assessment Form",
          description = "Self Assessment Form",
          refCrimNeedScoreThreshold = null,
          refScoredForOgp = false,
          refScoredForOvp = false,
          refQuestions = listOf(
            RefQuestionDto(
              refDisplaySort = 15L,
              refMandatoryIndicator = false,
              refQAWeighting = null,
              refQuestionCode = "TEST_Q",
              refQuestionId = 9000L,
              refQuestionText = "Question Text",
              refAnswers = listOf(
                RefAnswerDto(
                  refAnswerCode = "ANSWER_CODE",
                  refAnswerId = 9000L,
                  refDisplaySort = 20L
                )
              )
            )
          )
        )
      )
    )
  }
}
