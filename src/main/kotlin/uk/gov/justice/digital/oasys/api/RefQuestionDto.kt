package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.RefQuestion

data class RefQuestionDto (
        val refQuestionId: Long? = null,
        val refQuestionCode: String? = null,
        val refDisplaySort: Long? = null,
        val refQuestionText: String? = null,
        val refMandatoryIndicator: Boolean? = null,
        val refQAWeighting: Long? = null,
        val refCtAreaEstCode: String? = null,
        val refAnswers: Collection<RefAnswerDto>? = null
) {

    companion object {

        fun from(refQuestions: Collection<RefQuestion?>?): Collection<RefQuestionDto> {
            return refQuestions?.filterNotNull()?.map { from(it) }.orEmpty()
        }

        private fun from(refQuestion: RefQuestion): RefQuestionDto {
            return RefQuestionDto(
                    refDisplaySort = refQuestion.displaySort,
                    refMandatoryIndicator = refQuestion.mandatoryInd.ynToBoolean(),
                    refQAWeighting = refQuestion.qaWeighting,
                    refQuestionCode = refQuestion.refQuestionCode,
                    refQuestionId = refQuestion.refQuestionUk,
                    refQuestionText = refQuestion.refSectionQuestion,
                    refAnswers = RefAnswerDto.from(refQuestion.refAnswers)
            )
        }
    }
}