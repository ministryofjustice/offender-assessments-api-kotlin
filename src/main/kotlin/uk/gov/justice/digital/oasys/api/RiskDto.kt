package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime

data class RiskDto(
  val oasysSetId: Long? = null,
  val refAssessmentVersionCode: String? = null,
  val refAssessmentVersionNumber: String? = null,
  val refAssessmentId: Long ? = null,
  val completedDate: LocalDateTime? = null,
  val voidedDateTime: LocalDateTime? = null,
  val assessmentCompleted: Boolean? = null,
  val assessmentStatus: String? = null,

) {
  companion object {

    val riskQuestions = mapOf<String, Collection<String>>(
      "SARA" to listOf("")


    )


    fun from(assessments: Collection<Assessment?>?): Collection<RiskDto>? {
      return assessments?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
    }

    fun from(assessment: Assessment?): RiskDto {
      val assessmentVersion = assessment?.assessmentVersion
      return RiskDto(
        assessment?.oasysSetPk,
        assessmentVersion?.refAssVersionCode,
        assessmentVersion?.versionNumber,
        assessmentVersion?.refAssVersionUk,
        assessment?.dateCompleted,
        assessment?.assessmentVoidedDate,
        assessment?.dateCompleted != null,
        assessment?.assessmentStatus,

      )
    }
  }
}
