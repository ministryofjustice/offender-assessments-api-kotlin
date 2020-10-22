package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion

data class AnswerDto(

        @ApiModelProperty(value = "Reference Answer Code", example = "NO")
        val refAnswerCode: String? = null,

        @ApiModelProperty(value = "Answer ID", example = "123456")
        val oasysAnswerId: Long? = null,

        @ApiModelProperty(value = "Reference Answer ID", example = "123456")
        val refAnswerId: Long? = null,

        @ApiModelProperty(value = "Answer Display Order", example = "1")
        val displayOrder: Long? = null,

        @ApiModelProperty(value = "Static text", example = "123456")
        val staticText: String? = null,

        @ApiModelProperty(value = "Free form answer text", example = "Some answer")
        val freeFormText: String? = null,

        @ApiModelProperty(value = "OGP score ", example = "1")
        val ogpScore: Long? = null,

        @ApiModelProperty(value = "OVP score", example = "1")
        val ovpScore: Long? = null,

        @ApiModelProperty(value = "QA raw score", example = "2")
        val qaRawScore: Long? = null
) {

    companion object {
        fun from(question: OasysQuestion?): Collection<AnswerDto> {

            val oasysAnswers: Set<OasysAnswer?> = question?.oasysAnswers
                    ?: return setOf(AnswerDto(freeFormText = question?.freeFormatAnswer))

            return oasysAnswers.mapNotNull { from(it) }
        }

        fun from(oasysAnswer: OasysAnswer?): AnswerDto? {
            val refAnswer = oasysAnswer?.refAnswer

            return AnswerDto(
                    refAnswer?.refAnswerCode,
                    oasysAnswer?.oasysAnswerPk,
                    refAnswer?.refAnswerUk,
                    refAnswer?.displaySort,
                    refAnswer?.refSectionAnswer,
                    oasysAnswer?.oasysQuestion?.freeFormatAnswer,
                    refAnswer?.ogpScore,
                    refAnswer?.ovpScore,
                    refAnswer?.qaRawScore)
        }
    }
}

