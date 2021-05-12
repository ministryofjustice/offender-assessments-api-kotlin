package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshQuestionIds
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshaQuestionIds
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.saraQuestionIds

data class RiskAssessmentAnswersDto(
  @ApiModelProperty(value = "Assessment ID/Oasys Set ID for SARA risk answers")
  val oasysSetId: Long? = null,

  @ApiModelProperty(value = "Risk Questions and Answers")
  val riskQuestions: List<RiskQuestionDto>? = null
) {
  companion object {

    fun fromRosha(assessmentAnswers: AssessmentAnswersDto): RiskAssessmentAnswersDto {
      return riskAssessmentAnswersDto(assessmentAnswers, roshaQuestionIds)
    }

    fun fromSara(assessmentAnswers: AssessmentAnswersDto): RiskAssessmentAnswersDto {
      return riskAssessmentAnswersDto(assessmentAnswers, saraQuestionIds)
    }

    fun fromRosh(assessmentAnswers: AssessmentAnswersDto): RiskAssessmentAnswersDto {
      return riskAssessmentAnswersDto(assessmentAnswers, roshQuestionIds)
    }

    fun riskAssessmentAnswersDto(
      assessmentAnswers: AssessmentAnswersDto,
      questionCodes: Set<String>
    ): RiskAssessmentAnswersDto {
      val riskQuestions = assessmentAnswers.questionAnswers.filter { questionCodes.contains(it.refQuestionCode) }
      return RiskAssessmentAnswersDto(
        oasysSetId = assessmentAnswers.assessmentId,
        riskQuestions = riskQuestions.map { RiskQuestionDto.from(it) }
      )
    }
  }
}

data class RiskQuestionDto(

  @ApiModelProperty(value = "Question Code", example = "10.98")
  val refQuestionCode: String? = null,

  @ApiModelProperty(value = "Question Text", example = "123456")
  val questionText: String? = null,

  @ApiModelProperty(value = "Currently Hidden")
  val currentlyHidden: Boolean? = null,

  @ApiModelProperty(value = "Disclosed")
  val disclosed: Boolean? = null,

  @ApiModelProperty(value = "Question Answer")
  val answers: Collection<RiskAnswerDto> = emptyList()
) {

  companion object {
    fun from(question: QuestionDto): RiskQuestionDto {
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

data class RiskAnswerDto(
  @ApiModelProperty(value = "Reference Answer Code", example = "NO")
  val refAnswerCode: String? = null,

  @ApiModelProperty(value = "Static text", example = "123456")
  val staticText: String? = null,

) {
  companion object {
    fun from(answer: AnswerDto): RiskAnswerDto {
      return RiskAnswerDto(
        refAnswerCode = answer.refAnswerCode,
        staticText = answer.staticText
      )
    }
  }
}
