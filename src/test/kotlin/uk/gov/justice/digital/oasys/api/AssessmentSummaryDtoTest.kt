package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.AssessmentGroup
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.Section
import java.time.LocalDateTime

@DisplayName("Assessment Summary Tests")
class AssessmentSummaryDtoTest {

  val assessment = setupAssessment()

  @Test
  fun `Builds valid Assessment Summary DTO from Entity `() {
    val assessmentDto = AssessmentSummaryDto.from(setOf(assessment)).first()

    assertThat(assessmentDto.assessmentId).isEqualTo(assessment.oasysSetPk)
    assertThat(assessmentDto.assessorName).isEqualTo(assessment.assessorName)
    assertThat(assessmentDto.assessmentType).isEqualTo(assessment.assessmentType)
    assertThat(assessmentDto.historicStatus).isEqualTo(assessment.group?.historicStatus)
    assertThat(assessmentDto.assessmentStatus).isEqualTo(assessment.assessmentStatus)
    assertThat(assessmentDto.created).isEqualTo(assessment.createDate)
    assertThat(assessmentDto.refAssessmentId).isEqualTo(assessment.assessmentVersion?.refAssVersionUk)
    assertThat(assessmentDto.refAssessmentVersionCode).isEqualTo(assessment.assessmentVersion?.refAssVersionCode)
    assertThat(assessmentDto.refAssessmentVersionNumber).isEqualTo(assessment.assessmentVersion?.versionNumber)
    assertThat(assessmentDto.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(assessment.assessmentVersion?.oasysScoringAlgVersion)
    assertThat(assessmentDto.completed).isEqualTo(assessment.dateCompleted)
    assertThat(assessmentDto.voided).isEqualTo(assessment.assessmentVoidedDate)
  }

  @Test
  fun `Builds valid Assessment Summary DTO Null`() {
    val assessmentDto = AssessmentSummaryDto.from(null)
    assertThat(assessmentDto).isEmpty()
  }

  @Test
  fun `Builds valid Assessment Summary DTO empty set`() {
    val assessmentDto = AssessmentSummaryDto.from(emptySet())
    assertThat(assessmentDto).isEmpty()
  }

  @Test
  fun `Builds valid Assessment Summary DTO null values in set set`() {
    val assessmentDto = AssessmentSummaryDto.from(setOf(null))
    assertThat(assessmentDto).isEmpty()
  }

  private fun setupAssessmentGroup(): AssessmentGroup {
    return AssessmentGroup(historicStatus = "Current")
  }

  private fun setupVersion(): RefAssessmentVersion {
    return RefAssessmentVersion(refAssVersionUk = 1L, versionNumber = "Any Version", refAssVersionCode = "Any Ref Version Code", oasysScoringAlgVersion = 2L)
  }

  private fun setupAssessment(): Assessment {
    val created = LocalDateTime.now()
    val completed = created.plusMonths(3)
    val voided = created.plusMonths(4)

    return Assessment(
      oasysSetPk = 1234L,
      assessorName = "Any Name",
      assessmentStatus = "STATUS",
      assessmentType = "LAYER_3",
      createDate = created,
      dateCompleted = completed,
      assessmentVoidedDate = voided,
      assessmentVersion = setupVersion(),
      oasysSections = setOf(Section()),
      oasysBcsParts = emptySet(),
      group = setupAssessmentGroup()
    )
  }
}
