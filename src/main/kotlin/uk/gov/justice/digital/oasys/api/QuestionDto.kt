package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

data class QuestionDto(

  @ApiModelProperty(value = "Reference Question ID", example = "123456")
  var refQuestionId: Long? = null,

  @ApiModelProperty(value = "Question Code", example = "10.98")
  val refQuestionCode: String? = null,

  @ApiModelProperty(value = "Unique Question ID", example = "123456")
  val oasysQuestionId: Long? = null,

  @ApiModelProperty(value = "Display Order", example = "123456")
  val displayOrder: Long? = null,

  @ApiModelProperty(value = "Display Score, example", example = "123456")
  val displayScore: Long? = null,

  @ApiModelProperty(value = "Question Text", example = "123456")
  val questionText: String? = null,

  @ApiModelProperty(value = "Currently Hidden")
  val currentlyHidden: Boolean? = null,

  @ApiModelProperty(value = "Disclosed")
  val disclosed: Boolean? = null,

  @ApiModelProperty(value = "Question Answer")
  val answers: Collection<AnswerDto> = emptySet()
) {

  companion object {
    val roshQuestionCodes = setOf("R2.1", "R2.2")
    val rsrQuestionCodes = setOf("RA")
    val saraQuestionCodes = setOf("SR76.1.1", "SR77.1.1")
    val roshSumQuestionCodes = setOf(
      "SUM6.1.2", "SUM6.2.1", "SUM6.2.2", "SUM6.3.1", "SUM6.3.2", "SUM6.4.1", "SUM6.4.2", "SUM6.5.2"
    )
    val roshFullQuestionCodes = setOf("FA31", "FA32")
    val roshaQuestionCodes = setOf(roshSumQuestionCodes, roshFullQuestionCodes).flatten().toSet()

    fun from(oasysQuestions: Collection<OasysQuestion?>?): Set<QuestionDto> {
      return oasysQuestions?.mapNotNull { from(it) }?.toSet().orEmpty()
    }

    fun from(question: OasysQuestion?): QuestionDto? {
      if (question == null) return null
      val refQuestion = question.refQuestion
      return QuestionDto(
        refQuestion?.refQuestionUk,
        refQuestion?.refQuestionCode,
        question.oasysQuestionPk,
        refQuestion?.displaySort,
        question.displayScore,
        refQuestion?.refSectionQuestion,
        question.currentlyHiddenInd.ynToBoolean(),
        question.disclosedInd.ynToBoolean(),
        AnswerDto.from(question)
      )
    }

    fun from(refQuestion: RefQuestion?): QuestionDto? {
      return if (refQuestion == null) {
        null
      } else QuestionDto(
        refQuestionId = refQuestion.refQuestionUk,
        refQuestionCode = refQuestion.refQuestionCode,
        displayOrder = refQuestion.displaySort,
        questionText = refQuestion.refSectionQuestion
      )
    }
  }
}
