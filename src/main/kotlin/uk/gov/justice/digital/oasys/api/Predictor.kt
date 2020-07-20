package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime

 data class Predictor (
        var oasysSetId: Long? = null,
        var refAssessmentVersionCode: String? = null,
        var refAssessmentVersionNumber: String? = null,
        var refAssessmentId: Long ? = null,
        var completedDate: LocalDateTime? = null,
        var voidedDateTime: LocalDateTime? = null,
        var assessmentCompleted: Boolean? = null,
        var otherRisk: RefElementDto? = null,
        var ogr3: Ogrs3? = null,
        var ovp: Ovp? = null,
        var ogp: Ogp? = null
) {
  companion object {
      fun from(assessment: Assessment?): Predictor? {
          return if (assessment == null){
              null
          } else {
              val assessmentVersion = assessment.assessmentVersion
              return Predictor(
                      assessment.oasysSetPk,
                      assessmentVersion?.refAssVersionCode,
                      assessmentVersion?.versionNumber,
                      assessmentVersion?.refAssVersionUk,
                      assessment.dateCompleted,
                      assessment.assessmentVoidedDate,
                      assessment.dateCompleted != null,
                      RefElementDto.from(assessment.otherRiskRecon),
                      Ogrs3.from(assessment),
                      Ovp.from(assessment),
                      Ogp.from(assessment)
              )
          }
      }
  }
}

