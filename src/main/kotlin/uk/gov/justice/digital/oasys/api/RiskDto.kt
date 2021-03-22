package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

data class RiskDto(

  @ApiModelProperty(value = "Oasys Set ID/ Assessment ID", example = "1234")
  val oasysSetId: Long? = null,

  @ApiModelProperty(value = "Assessment Reference Version Code", example = "Layer3")
  val refAssessmentVersionCode: String? = null,

  @ApiModelProperty(value = "Assessment Reference Version", example = "1")
  val refAssessmentVersionNumber: String? = null,

  @ApiModelProperty(value = "Assessment Reference Version", example = "1")
  val refAssessmentId: Long ? = null,

  @ApiModelProperty(value = "Completed Date", example = "2020-01-02T16:00:00")
  val completedDate: LocalDateTime? = null,

  @ApiModelProperty(value = "Voided Date", example = "2020-01-02T16:00:00")
  val voidedDateTime: LocalDateTime? = null,

  @ApiModelProperty(value = "Assessment Completed True/False", example = "false")
  val assessmentCompleted: Boolean? = null,

  @ApiModelProperty(value = "Assessment Status", example = "OPEN")
  val assessmentStatus: String? = null,

  @ApiModelProperty(value = "SARA risk answers")
  val sara: RiskAssessmentAnswersDto? = null,

  @ApiModelProperty(value = "ROSHA risk answers")
  val rosha: RiskAssessmentAnswersDto? = null,

  ) {
  companion object {

    fun from(assessments: Collection<AssessmentDto?>?, answers: AssessmentAnswersDto): Collection<RiskDto> {
      return assessments?.filterNotNull()?.map { from(it, answers) }?.toSet().orEmpty()
    }

    fun from(assessment: AssessmentDto?, answers: AssessmentAnswersDto): RiskDto {
      return RiskDto(
        oasysSetId = assessment?.assessmentId,
        refAssessmentVersionCode = assessment?.refAssessmentVersionCode,
        refAssessmentVersionNumber = assessment?.refAssessmentVersionNumber,
        refAssessmentId = assessment?.refAssessmentId,
        completedDate = assessment?.completed,
        voidedDateTime =  assessment?.voided,
        assessmentCompleted = assessment?.completed != null,
        assessmentStatus = assessment?.assessmentStatus,
        sara = RiskAssessmentAnswersDto.from(answers, "SARA"),
        rosha = RiskAssessmentAnswersDto.from(answers, "ROSHA"))
    }

    fun from(assessmentSummaryDto: AssessmentSummaryDto, answers: AssessmentAnswersDto): RiskDto {
      return RiskDto(
        oasysSetId = assessmentSummaryDto.assessmentId,
        refAssessmentVersionCode = assessmentSummaryDto.refAssessmentVersionCode,
        refAssessmentVersionNumber = assessmentSummaryDto.refAssessmentVersionNumber,
        refAssessmentId = assessmentSummaryDto.refAssessmentId,
        completedDate = assessmentSummaryDto.completed,
        voidedDateTime =  assessmentSummaryDto.voided,
        assessmentCompleted = assessmentSummaryDto.completed != null,
        assessmentStatus = assessmentSummaryDto.assessmentStatus,
        sara = RiskAssessmentAnswersDto.from(answers, "SARA"),
        rosha = RiskAssessmentAnswersDto.from(answers, "ROSHA"))
    }
  }
}
