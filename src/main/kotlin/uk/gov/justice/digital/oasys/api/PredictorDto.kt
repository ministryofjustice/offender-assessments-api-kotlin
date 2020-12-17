package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime

data class PredictorDto(
  val oasysSetId: Long? = null,
  val refAssessmentVersionCode: String? = null,
  val refAssessmentVersionNumber: String? = null,
  val refAssessmentId: Long ? = null,
  val completedDate: LocalDateTime? = null,
  val voidedDateTime: LocalDateTime? = null,
  val assessmentCompleted: Boolean? = null,
  val otherRisk: RefElementDto? = null,
  val ogr3: Ogrs3Dto? = null,
  val ovp: OvpDto? = null,
  val ogp: OgpDto? = null
) {
  companion object {

    fun from(assessments: Collection<Assessment?>?): Collection<PredictorDto>? {
      return assessments?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
    }

    fun from(assessment: Assessment?): PredictorDto {
      val assessmentVersion = assessment?.assessmentVersion
      return PredictorDto(
        assessment?.oasysSetPk,
        assessmentVersion?.refAssVersionCode,
        assessmentVersion?.versionNumber,
        assessmentVersion?.refAssVersionUk,
        assessment?.dateCompleted,
        assessment?.assessmentVoidedDate,
        assessment?.dateCompleted != null,
        RefElementDto.from(assessment?.otherRiskRecon),
        Ogrs3Dto.from(assessment),
        OvpDto.from(assessment),
        OgpDto.from(assessment)
      )
    }
  }
}
