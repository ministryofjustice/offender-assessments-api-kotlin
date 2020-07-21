package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime

 data class Predictor (
        val oasysSetId: Long? = null,
        val refAssessmentVersionCode: String? = null,
        val refAssessmentVersionNumber: String? = null,
        val refAssessmentId: Long ? = null,
        val completedDate: LocalDateTime? = null,
        val voidedDateTime: LocalDateTime? = null,
        val assessmentCompleted: Boolean? = null,
        val otherRisk: RefElementDto? = null,
        val ogr3: Ogrs3? = null,
        val ovp: Ovp? = null,
        val ogp: Ogp? = null
) {
  companion object {

      fun from(assessments: Collection<Assessment?>?): Collection<Predictor>? {
          return assessments?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
      }

     fun from(assessment: Assessment?): Predictor {
          val assessmentVersion = assessment?.assessmentVersion
          return Predictor( assessment?.oasysSetPk,
                  assessmentVersion?.refAssVersionCode,
                  assessmentVersion?.versionNumber,
                  assessmentVersion?.refAssVersionUk,
                  assessment?.dateCompleted,
                  assessment?.assessmentVoidedDate,
                  assessment?.dateCompleted != null,
                  RefElementDto.from(assessment?.otherRiskRecon),
                  Ogrs3.from(assessment),
                  Ovp.from(assessment),
                  Ogp.from(assessment))
      }
  }
}

