package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.PredictorDto
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Predictor Service Tests")
class PredictorsServiceTest {

  private val offenderService = mockk<OffenderService>()
  private val assessmentRepository: AssessmentRepository = mockk()
  private val predictorsService: PredictorsService = PredictorsService(offenderService, assessmentRepository)
  private val createdDate = LocalDateTime.now()

  @Test
  fun `should get all Predictors For Offender`() {
    val assessment = setupAssessment()

    every { offenderService.getOffenderIdByIdentifier("OASYS", "1") } returns (1L)
    every { assessmentRepository.getAssessmentsForOffender(1L) } returns(setOf(assessment))

    val predictors = predictorsService.getAllPredictorsForOffender("OASYS", "1")

    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(1L) }
    verify(exactly = 1) { offenderService.getOffenderIdByIdentifier("OASYS", "1") }

    validatePredictors(predictors?.first())
  }

  private fun validatePredictors(predictorDto: PredictorDto?) {
    SoftAssertions().apply {
      assertThat(predictorDto?.oasysSetId).isEqualTo(1234L)
      assertThat(predictorDto?.refAssessmentVersionCode).isEqualTo("Any Ref Version Code")
      assertThat(predictorDto?.refAssessmentVersionNumber).isEqualTo("Any Version")
      assertThat(predictorDto?.refAssessmentId).isEqualTo(1L)
      assertThat(predictorDto?.completedDate).isEqualTo(createdDate.plusMonths(3))
      assertThat(predictorDto?.voidedDateTime).isEqualTo(createdDate.plusMonths(4))
      assertThat(predictorDto?.assessmentCompleted).isEqualTo(true)
      assertThat(predictorDto?.otherRisk).isEqualTo(RefElementDto(code = "L", shortDescription = null, description = "Low"))

      assertThat(predictorDto?.ogp?.ogpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(1))
      assertThat(predictorDto?.ogp?.ogpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(2))
      assertThat(predictorDto?.ogp?.ogpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(3))
      assertThat(predictorDto?.ogp?.ogp1Year).isEqualTo(BigDecimal.valueOf(4))
      assertThat(predictorDto?.ogp?.ogp2Year).isEqualTo(BigDecimal.valueOf(5))
      assertThat(predictorDto?.ogp?.ogpRisk).isEqualTo(RefElementDto(code = "L", description = "Low"))

      assertThat(predictorDto?.ovp?.ovpStaticWeightedScore).isEqualTo(BigDecimal.valueOf(8))
      assertThat(predictorDto?.ovp?.ovpDynamicWeightedScore).isEqualTo(BigDecimal.valueOf(4))
      assertThat(predictorDto?.ovp?.ovpTotalWeightedScore).isEqualTo(BigDecimal.valueOf(9))
      assertThat(predictorDto?.ovp?.ovp1Year).isEqualTo(BigDecimal.valueOf(1))
      assertThat(predictorDto?.ovp?.ovp2Year).isEqualTo(BigDecimal.valueOf(2))
      assertThat(predictorDto?.ovp?.ovpRisk).isEqualTo(RefElementDto(code = "L", description = "Low"))
      assertThat(predictorDto?.ovp?.ovpPreviousWeightedScore).isEqualTo(BigDecimal.valueOf(6))
      assertThat(predictorDto?.ovp?.ovpViolentWeightedScore).isEqualTo(BigDecimal.valueOf(10))
      assertThat(predictorDto?.ovp?.ovpNonViolentWeightedScore).isEqualTo(BigDecimal.valueOf(5))
      assertThat(predictorDto?.ovp?.ovpAgeWeightedScore).isEqualTo(BigDecimal.valueOf(3))
      assertThat(predictorDto?.ovp?.ovpSexWeightedScore).isEqualTo(BigDecimal.valueOf(7))

      assertThat(predictorDto?.ogr3?.ogrs3_1Year).isEqualTo(BigDecimal.valueOf(4))
      assertThat(predictorDto?.ogr3?.ogrs3_2Year).isEqualTo(BigDecimal.valueOf(5))
      assertThat(predictorDto?.ogr3?.reconvictionRisk).isEqualTo(RefElementDto(code = "L", description = "Low"))

      assertThat(predictorDto?.osp?.ospIndecentRiskRecon).isEqualTo(RefElementDto(code = "L", shortDescription = "Long", description = "Long"))
      assertThat(predictorDto?.osp?.ospIndecentPercentageScore).isEqualTo(BigDecimal.valueOf(50.1234))
      assertThat(predictorDto?.osp?.ospContactRiskRecon).isEqualTo(RefElementDto(code = "L", shortDescription = "Long", description = "Long"))
      assertThat(predictorDto?.osp?.ospContactPercentageScore).isEqualTo(BigDecimal.valueOf(50.1234))

      assertThat(predictorDto?.rsr?.rsrRiskRecon).isEqualTo(RefElementDto(code = "L", shortDescription = "Long", description = "Long"))
      assertThat(predictorDto?.rsr?.rsrAlgorithmVersion).isEqualTo(11)
      assertThat(predictorDto?.rsr?.rsrPercentageScore).isEqualTo(BigDecimal.valueOf(50.1234))
      assertThat(predictorDto?.rsr?.rsrStaticOrDynamic).isEqualTo("STATIC")
    }.assertAll()
  }

  private fun setupAssessment(): Assessment {

    return Assessment(
      oasysSetPk = 1234L,
      assessmentStatus = "STATUS",
      assessmentType = "LAYER_3",
      createDate = createdDate,
      dateCompleted = createdDate.plusMonths(3),
      assessmentVoidedDate = createdDate.plusMonths(4),
      assessmentVersion = RefAssessmentVersion(refAssVersionUk = 1L, versionNumber = "Any Version", refAssVersionCode = "Any Ref Version Code"),
      otherRiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L"),

      ogpStWesc = BigDecimal.valueOf(1),
      ogpDyWesc = BigDecimal.valueOf(2),
      ogpTotWesc = BigDecimal.valueOf(3),
      ogp1Year = BigDecimal.valueOf(4),
      ogp2Year = BigDecimal.valueOf(5),
      ogpRiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L"),

      ovp1Year = BigDecimal.valueOf(1),
      ovp2Year = BigDecimal.valueOf(2),
      ovpAgeWesc = BigDecimal.valueOf(3),
      ovpDyWesc = BigDecimal.valueOf(4),
      ovpNonVioWesc = BigDecimal.valueOf(5),
      ovpPrevWesc = BigDecimal.valueOf(6),
      ovpSexWesc = BigDecimal.valueOf(7),
      ovpStWesc = BigDecimal.valueOf(8),
      ovpTotWesc = BigDecimal.valueOf(9),
      ovpVioWesc = BigDecimal.valueOf(10),
      ovpRiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L"),

      ogrs31Year = BigDecimal.valueOf(4),
      ogrs32Year = BigDecimal.valueOf(5),
      ogrs3RiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L"),

      ospIndecentRiskRecon = RefElement(refElementCode = "L", refElementDesc = "Long", refElementShortDesc = "Long"),
      ospIndecentPercentageScore = BigDecimal.valueOf(50.1234),
      ospContactRiskRecon = RefElement(refElementCode = "L", refElementDesc = "Long", refElementShortDesc = "Long"),
      ospContactPercentageScore = BigDecimal.valueOf(50.1234),

      rsrRiskRecon = RefElement(refElementCode = "L", refElementDesc = "Long", refElementShortDesc = "Long"),
      rsrPercentageScore = BigDecimal.valueOf(50.1234),
      rsrStaticOrDynamic = "STATIC",
      rsrAlgorithmVersion = 11
    )
  }
}
