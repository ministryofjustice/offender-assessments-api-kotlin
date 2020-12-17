package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.BasicSentencePlanObj
import java.time.LocalDate

data class BasicSentencePlanItemDto(
  val basicSentPlanObjId: Long? = null,
  val includeInPlan: Boolean? = null,
  val offenceBehaviourLink: RefElementDto? = null,
  val objectiveText: String? = null,
  val measureText: String? = null,
  val whatWorkText: String? = null,
  val whoWillDoWorkText: String? = null,
  val timescalesText: String? = null,
  val dateOpened: LocalDate? = null,
  val dateCompleted: LocalDate? = null,
  val problemAreaCompInd: Boolean? = null
) {

  companion object {

    fun from(basicSentencePlanObj: BasicSentencePlanObj?): BasicSentencePlanItemDto? {
      if (basicSentencePlanObj == null) return null

      return BasicSentencePlanItemDto(
        basicSentencePlanObj.basicSentPlanObjPk,
        basicSentencePlanObj.includeInPlanInd.ynToBoolean(),
        RefElementDto.from(basicSentencePlanObj.offenceBehaviourLink),
        basicSentencePlanObj.objectiveText,
        basicSentencePlanObj.measureText,
        basicSentencePlanObj.whatWorkText,
        basicSentencePlanObj.whoWillDoWorkText,
        basicSentencePlanObj.timescalesText,
        basicSentencePlanObj.dateOpened?.toLocalDate(),
        basicSentencePlanObj.dateCompleted?.toLocalDate(),
        basicSentencePlanObj.problemAreaCompInd.ynToBoolean()
      )
    }
  }
}
