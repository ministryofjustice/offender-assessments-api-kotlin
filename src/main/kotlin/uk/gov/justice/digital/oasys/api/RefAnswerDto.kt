package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.RefAnswer

data class RefAnswerDto (
        val refAnswerId: Long? = null,
        val refAnswerCode: String? = null,
        val refDisplaySort: Long? = null
) {
    companion object {
        fun from(refAnswers: Collection<RefAnswer?>?): Collection<RefAnswerDto> {
            return refAnswers?.filterNotNull()?.map { from(it)}.orEmpty()
        }

        private fun from(refAnswer: RefAnswer): RefAnswerDto {
            return RefAnswerDto(refAnswerCode = refAnswer.refAnswerCode,
                                refAnswerId = refAnswer.refAnswerUk,
                                refDisplaySort = refAnswer.displaySort)
        }
    }
}