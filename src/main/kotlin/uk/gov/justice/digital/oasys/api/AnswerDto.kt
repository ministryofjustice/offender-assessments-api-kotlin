package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.OasysAnswer
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion

data class AnswerDto(

        val refAnswerId: Long? = null,
        val refAnswerCode: String? = null,
        val oasysAnswerId: Long? = null,
        val displayOrder: Long? = null,
        val staticText: String? = null,
        val freeFormText: String? = null,
        val ogpScore: Long? = null,
        val ovpScore: Long? = null,
        val qaRawScore: Long? = null
) {

    companion object {
        fun from(question: OasysQuestion?): AnswerDto? {

            val oasysAnswer: OasysAnswer? = question?.oasysAnswer
                    ?: return AnswerDto(freeFormText = question?.freeFormatAnswer)

            val refAnswer = oasysAnswer?.refAnswer

            return AnswerDto(
                    refAnswer?.refAnswerUk,
                    refAnswer?.refAnswerCode,
                    oasysAnswer?.oasysAnswerPk,
                    refAnswer?.displaySort,
                    refAnswer?.refSectionAnswer,
                    oasysAnswer?.oasysQuestion?.freeFormatAnswer,
                    refAnswer?.ogpScore,
                    refAnswer?.ovpScore,
                    refAnswer?.qaRawScore)
        }
    }
}

