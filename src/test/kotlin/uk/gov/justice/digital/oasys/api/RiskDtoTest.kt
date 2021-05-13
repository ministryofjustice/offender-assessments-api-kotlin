package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import java.time.LocalDateTime

@DisplayName("Risk DTO Tests")
class RiskDtoTest {

  private val created = LocalDateTime.now()
  private val completed = created.plusMonths(3)
  private val voided = created.plusMonths(4)

  private val rosha = roshAssessment()
  private val sara = saraAssessment()
  private val roshaSara = assessment()

  @Test
  fun `build Risk DTO from Assessment with ROSHA and SARA`() {

    val riskDto = RiskDto.fromRoshaWithSara(roshaSara, roshaAnswers(), saraAnswers(), null, null)
    assertThat(riskDto.oasysSetId).isEqualTo(1111)
    assertThat(riskDto.refAssessmentId).isEqualTo(1)
    assertThat(riskDto.refAssessmentVersionCode).isEqualTo("version code")
    assertThat(riskDto.refAssessmentVersionNumber).isEqualTo("version number")
    assertThat(riskDto.completedDate).isEqualTo(completed)
    assertThat(riskDto.voidedDateTime).isEqualTo(voided)
    assertThat(riskDto.assessmentCompleted).isEqualTo(true)
    assertThat(riskDto.assessmentStatus).isEqualTo("COMPLETED")
    assertThat(riskDto.sara?.riskQuestions).isEmpty()
    assertThat(riskDto.rosha?.riskQuestions).isEmpty()
    assertThat(riskDto.childSafeguardingIndicated).isNull()
    assertThat(riskDto.rosh).isNull()
    assertThat(riskDto.isRSROnly).isNull()
  }

  @Test
  fun `build Risk DTO from Assessment with ROSHA, no SARA`() {

    val riskDto = RiskDto.fromRosha(rosha, roshaAnswers(), true, true)
    assertThat(riskDto.oasysSetId).isEqualTo(2222)
    assertThat(riskDto.refAssessmentId).isEqualTo(1)
    assertThat(riskDto.refAssessmentVersionCode).isEqualTo("version code")
    assertThat(riskDto.refAssessmentVersionNumber).isEqualTo("version number")
    assertThat(riskDto.completedDate).isEqualTo(completed)
    assertThat(riskDto.voidedDateTime).isEqualTo(voided)
    assertThat(riskDto.assessmentCompleted).isEqualTo(true)
    assertThat(riskDto.assessmentStatus).isEqualTo("COMPLETED")
    assertThat(riskDto.sara?.riskQuestions).isNull()
    assertThat(riskDto.rosha?.riskQuestions).isEmpty()
    assertThat(riskDto.childSafeguardingIndicated).isTrue
    assertThat(riskDto.rosh?.riskQuestions).isEmpty()
    assertThat(riskDto.isRSROnly).isTrue
  }

  @Test
  fun `build Risk DTO from SARA Assessment`() {

    val riskDto = RiskDto.fromSara(sara, saraAnswers(), false, false)
    assertThat(riskDto.oasysSetId).isEqualTo(3333)
    assertThat(riskDto.refAssessmentId).isEqualTo(1)
    assertThat(riskDto.refAssessmentVersionCode).isEqualTo("version code")
    assertThat(riskDto.refAssessmentVersionNumber).isEqualTo("version number")
    assertThat(riskDto.completedDate).isEqualTo(completed)
    assertThat(riskDto.voidedDateTime).isEqualTo(voided)
    assertThat(riskDto.assessmentCompleted).isEqualTo(true)
    assertThat(riskDto.assessmentStatus).isEqualTo("COMPLETED")
    assertThat(riskDto.sara?.riskQuestions).isEmpty()
    assertThat(riskDto.rosha?.riskQuestions).isNull()
    assertThat(riskDto.childSafeguardingIndicated).isFalse
    assertThat(riskDto.rosh?.riskQuestions).isEmpty()
    assertThat(riskDto.isRSROnly).isFalse
  }

  private fun assessment(): Assessment {
    return Assessment(
      oasysSetPk = 1111,
      assessmentStatus = "COMPLETED",
      assessmentVersion = RefAssessmentVersion(
        refAssVersionCode = "version code",
        versionNumber = "version number",
        refAssVersionUk = 1
      ),
      dateCompleted = completed,
      assessmentVoidedDate = voided,
      childAssessments = setOf(saraAssessment())
    )
  }

  private fun roshAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 2222L,
      assessmentStatus = "COMPLETED",
      assessmentVersion = RefAssessmentVersion(
        refAssVersionCode = "version code",
        versionNumber = "version number",
        refAssVersionUk = 1
      ),
      dateCompleted = completed,
      assessmentVoidedDate = voided
    )
  }

  private fun saraAssessment(): Assessment {
    return Assessment(
      oasysSetPk = 3333L,
      assessmentStatus = "COMPLETED",
      assessmentVersion = RefAssessmentVersion(
        refAssVersionCode = "version code",
        versionNumber = "version number",
        refAssVersionUk = 1
      ),
      dateCompleted = completed,
      assessmentVoidedDate = voided
    )
  }

  private fun roshaAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 1111,
      questionAnswers = listOf(QuestionDto())
    )
  }

  private fun saraAnswers(): AssessmentAnswersDto {
    return AssessmentAnswersDto(
      assessmentId = 3333,
      questionAnswers = listOf(QuestionDto())
    )
  }
}
