package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime
import java.util.*

 class Predictor (
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

   val assessmentVersion = assessment?.assessmentVersion

   return Predictor(
           assessment?.oasysSetPk,
           assessmentVersion?.refAssVersionCode,
           assessmentVersion?.versionNumber,
           assessmentVersion?.refAssVersionUk,
           assessment?.dateCompleted,
           assessment?.assessmentVoidedDate,
           Objects.nonNull(assessment?.dateCompleted),
           RefElementDto.from(assessment?.otherRiskRecon),
           Ogrs3.from(assessment),
           Ovp.from(assessment),
           Ogp.from(assessment)
   )
  }
 }
}

