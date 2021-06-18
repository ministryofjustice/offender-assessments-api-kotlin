package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime

data class AssessmentSummaryDto(

  @ApiModelProperty(value = "Assessment primary key (OASysSetPK)", example = "1234")
  val assessmentId: Long,

  @ApiModelProperty(value = "Assessment Reference Version Code", example = "LAYER3")
  val refAssessmentVersionCode: String? = null,

  @ApiModelProperty(value = "Assessment Reference Version", example = "1")
  val refAssessmentVersionNumber: String? = null,

  @ApiModelProperty(value = "Assessment Reference Version", example = "1")
  val refAssessmentId: Long? = null,

  @ApiModelProperty(value = "Assessment Type", example = "LAYER_3")
  val assessmentType: String? = null,

  @ApiModelProperty(value = "Assessment Status", example = "OPEN")
  val assessmentStatus: String? = null,

  @ApiModelProperty(value = "Assessment Group Historic Status", example = "CURRENT")
  val historicStatus: String? = null,

  @ApiModelProperty(value = "Assessment Scoring Algorithm Version", example = "1")
  val refAssessmentOasysScoringAlgorithmVersion: Long? = null,

  @ApiModelProperty(value = "AssessorName", example = "Layer 3")
  val assessorName: String? = null,

  @ApiModelProperty(value = "Created Date", example = "2020-01-02T16:00:00")
  val created: LocalDateTime? = null,

  @ApiModelProperty(value = "Completed Date", example = "2020-01-02T16:00:00")
  val completed: LocalDateTime? = null,

  @ApiModelProperty(value = "Voided Date", example = "2020-01-02T16:00:00")
  val voided: LocalDateTime? = null

) {

  companion object {

    fun Collection<Assessment>.toAssessmentsSummaryDto(): Collection<AssessmentSummaryDto> {
      return this.map { it.toAssessmentSummaryDto() }?.toSet()
    }

    fun Assessment.toAssessmentSummaryDto(): AssessmentSummaryDto {
      val assessmentVersion = this.assessmentVersion
      return AssessmentSummaryDto(
        this.oasysSetPk,
        assessmentVersion?.refAssVersionCode,
        assessmentVersion?.versionNumber,
        assessmentVersion?.refAssVersionUk,
        this.assessmentType,
        this.assessmentStatus,
        this.group?.historicStatus,
        assessmentVersion?.oasysScoringAlgVersion,
        this.assessorName,
        this.createDate,
        this.dateCompleted,
        this.assessmentVoidedDate
      )
    }
  }
}
