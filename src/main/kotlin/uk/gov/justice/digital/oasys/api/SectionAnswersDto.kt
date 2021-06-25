package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.api.QuestionAnswerDto.Companion.toAnswersDto
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion

data class SectionAnswersDto(

  @ApiModelProperty(value = "Assessment primary key (OASysSetPK)", example = "1234")
  val assessmentId: Long,

  @ApiModelProperty(value = "Answers for section")
  val sections: Map<String?, Collection<QuestionAnswerDto>>

) {
  companion object {
    fun from(oasysSetPk: Long, questions: Collection<OasysQuestion>): SectionAnswersDto {
      val answersBySection = questions.groupBy { it.refQuestion?.refSectionCode }
        .mapValues { q -> q.value.map { q1 -> q1.toAnswersDto() }.flatten().toSet() }

      return SectionAnswersDto(oasysSetPk, answersBySection)
    }
  }
}
