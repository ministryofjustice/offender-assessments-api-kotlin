package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty

data class RiskAssessmentAnswersDto(
  @ApiModelProperty(value = "Risk Questions and Answers")
  val riskQuestions: List<RiskQuestionDto>? = null
) {
  companion object {

    private var riskQuestionIds: Set<String>? = null

    fun from(assessment: AssessmentAnswersDto, riskType: String):RiskAssessmentAnswersDto {

      riskQuestionIds = when (riskType) {
        "SARA" -> setOf("SR76.1.1", "SR77.1.1")
        "ROSHA" -> setOf("SUM6.1.2", "SUM6.2.1", "SUM6.2.2", "SUM6.3.1", "SUM6.3.2", "SUM6.4.1", "SUM6.4.2", "SUM6.5.2", "FA31", "FA32")
        else -> emptySet()
      }
      val riskQuestions = assessment.questionAnswers.filter{ riskQuestionIds!!.contains(it.refQuestionCode) }
      return RiskAssessmentAnswersDto(
        riskQuestions = riskQuestions.map { RiskQuestionDto.from(it) }
      )
    }
  }
}

class RiskQuestionDto(

  @ApiModelProperty(value = "Question Code", example = "10.98")
  val refQuestionCode: String? = null,

  @ApiModelProperty(value = "Question Text", example = "123456")
  val questionText: String? = null,

  @ApiModelProperty(value = "Currently Hidden")
  val currentlyHidden: Boolean? = null,

  @ApiModelProperty(value = "Disclosed")
  val disclosed: Boolean? = null,

  @ApiModelProperty(value = "Question Answer")
  val answers: Collection<RiskAnswerDto> = emptySet()
) {

  companion object {
    fun from(question: QuestionDto): RiskQuestionDto{
      return RiskQuestionDto(
        refQuestionCode = question.refQuestionCode,
        questionText = question.questionText,
        currentlyHidden = question.currentlyHidden,
        disclosed = question.disclosed,
        answers = question.answers.map { RiskAnswerDto.from(it) }
      )
    }
  }
}

class RiskAnswerDto(
  @ApiModelProperty(value = "Reference Answer Code", example = "NO")
  val refAnswerCode: String? = null,

  @ApiModelProperty(value = "Static text", example = "123456")
  val staticText: String? = null,

) {
  companion object{
    fun from(answer: AnswerDto): RiskAnswerDto{
      return RiskAnswerDto(
        refAnswerCode = answer.refAnswerCode,
        staticText = answer.staticText
      )
    }
  }
}
