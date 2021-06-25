package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.time.LocalDateTime

@DisplayName("Predictor DTO Tests")
class PredictorDtoTest {

  private val created = LocalDateTime.now()
  private val completed = created.plusMonths(3)
  private val voided = created.plusMonths(4)

  @Test
  fun `should build valid Predictor DTO`() {
    val assessment = setupAssessment()
    val predictor = PredictorDto.from(assessment)
    assertThat(predictor).isEqualTo(setupValidPredictor())
  }

  @Test
  fun `should not be completed when completed date is null`() {
    val predictor = PredictorDto.from(Assessment())
    assertThat(predictor.completedDate).isNull()
    assertThat(predictor.assessmentCompleted).isFalse
  }

  @Test
  fun `should be completed when completed date is not null`() {
    val predictor = PredictorDto.from(setupAssessment())
    assertThat(predictor.completedDate).isNotNull
    assertThat(predictor.assessmentCompleted).isTrue
  }

  private fun setupVersion(): RefAssessmentVersion {
    return RefAssessmentVersion(
      refAssVersionUk = 1L,
      versionNumber = "Any Version",
      refAssVersionCode = "Any Ref Version Code",
      oasysScoringAlgVersion = 2L
    )
  }

  private fun setupAssessment(): Assessment {
    val version = setupVersion()
    return Assessment(
      oasysSetPk = 1234L,
      assessorName = "Any Name",
      assessmentStatus = "STATUS",
      assessmentType = "LAYER_3",
      createDate = created,
      dateCompleted = completed,
      assessmentVoidedDate = voided,
      assessmentVersion = version,
      otherRiskRecon = RefElement(refElementDesc = "Low", refElementCode = "L")
    )
  }

  private fun setupValidPredictor(): PredictorDto {
    return PredictorDto(
      oasysSetId = 1234L,
      refAssessmentVersionCode = "Any Ref Version Code",
      refAssessmentVersionNumber = "Any Version",
      refAssessmentId = 1L,
      completedDate = completed,
      assessmentCompleted = true,
      assessmentStatus = "STATUS",
      voidedDateTime = voided,
      otherRisk = RefElementDto(description = "Low", code = "L"),
      ogp = OgpDto(),
      ovp = OvpDto(),
      ogr3 = Ogrs3Dto(),
      osp = OspDto(),
      rsr = RsrDto()
    )
  }
}
