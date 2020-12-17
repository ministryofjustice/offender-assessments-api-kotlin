package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDate

data class BasicSentencePlanDto(
  val sentencePlanId: Long? = null,
  val createdDate: LocalDate? = null,
  val basicSentencePlanItems: List<BasicSentencePlanItemDto?>? = null
) {

  companion object {

    fun from(assessments: Collection<Assessment?>?): Set<BasicSentencePlanDto> {
      return assessments?.filterNotNull()?.mapNotNull { from(it) }?.toSet().orEmpty()
    }

    fun from(assessment: Assessment?): BasicSentencePlanDto? {
      if (assessment == null) return null

      val basicSentencePlanItems =
        assessment.basicSentencePlanList?.mapNotNull {
          BasicSentencePlanItemDto.from(it)
        }?.toList()

      if (basicSentencePlanItems.isNullOrEmpty()) return null

      return BasicSentencePlanDto(
        assessment.oasysSetPk,
        assessment.createDate?.toLocalDate(),
        basicSentencePlanItems
      )
    }
  }
}
