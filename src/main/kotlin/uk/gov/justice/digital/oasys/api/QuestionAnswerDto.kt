package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion

data class QuestionAnswerDto(
  @ApiModelProperty(value = "Question Code", example = "10.98")
  val refQuestionCode: String? = null,

  @ApiModelProperty(value = "Question Text", example = "123456")
  val questionText: String? = null,

  @ApiModelProperty(value = "Reference Answer Code", example = "NO")
  val refAnswerCode: String? = null,

  @ApiModelProperty(value = "Static text", example = "123456")
  val staticText: String? = null,

  @ApiModelProperty(value = "Free form answer text", example = "Some answer")
  val freeFormText: String? = null,

) {

  companion object {
    fun OasysQuestion.toAnswersDto(): Collection<QuestionAnswerDto> {
      var oasysAnswers = if (this?.oasysAnswers.isNullOrEmpty()) {
        return setOf(
          QuestionAnswerDto(
            refQuestionCode = this?.refQuestion?.refQuestionCode,
            questionText = this?.refQuestion?.refSectionQuestion,
            freeFormText = this?.additionalNote ?: this?.freeFormatAnswer
          )
        )
      } else {
        this?.oasysAnswers
      }

      return oasysAnswers?.mapNotNull { it?.toAnswerDto() }?.toSet() ?: emptySet()
    }

    private fun OasysAnswer.toAnswerDto(): QuestionAnswerDto {
      val refAnswer = this?.refAnswer
      return QuestionAnswerDto(
        refAnswer?.refQuestionCode,
        this?.oasysQuestion?.refQuestion?.refSectionQuestion,
        refAnswer?.refAnswerCode,
        refAnswer?.refSectionAnswer,
        this?.oasysQuestion?.freeFormatAnswer
      )
    }
  }
}
