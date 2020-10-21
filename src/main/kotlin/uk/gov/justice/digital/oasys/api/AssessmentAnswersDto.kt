package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion

data class AssessmentAnswersDto(

        @ApiModelProperty(value = "Assessment primary key (OASysSetPK)", example = "1234")
        val assessmentId: Long? = null,

        @ApiModelProperty(value = "Question answers")
        val questionAnswers: Collection<QuestionDto>

) {
    companion object {
        fun from(oasysSetPk: Long, questions: Collection<OasysQuestion>): AssessmentAnswersDto {
            return AssessmentAnswersDto(oasysSetPk, QuestionDto.from(questions))
        }
    }
}