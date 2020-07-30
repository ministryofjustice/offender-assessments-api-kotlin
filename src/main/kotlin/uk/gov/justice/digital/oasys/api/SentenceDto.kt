package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.OffenceBlock
import java.time.LocalDate

data class SentenceDto (
        
        val sentenceCode: String? = null,
        val sentenceDescription: String? = null,
        val custodial: Boolean? = null,
        val cja: Boolean? = null,
        val orderType: RefElementDto? = null,
        val offenceBlockType: RefElementDto? = null,
        val cjaUnpaidHours: Long? = null,
        val cjaSupervisionMonths: Long? = null,
        val activity: String? = null,
        val offenceDate: LocalDate? = null,
        val sentenceDate: LocalDate? = null,
        val sentenceLengthCustodyDays: Long? = null
        
) {
    companion object {
        fun from(offenceBlocks: Set<OffenceBlock?>?): Set<SentenceDto?>? {
            return offenceBlocks?.map { from(it) }?.filterNotNull()?.toSet()
        }

        private fun from(offenceBlock: OffenceBlock?): SentenceDto? {
            val sentenceDetail = offenceBlock?.offenceSentenceDetail
            val sentence = offenceBlock?.sentence

            return SentenceDto(
                    sentence?.sentenceCode,
                    sentence?.sentenceDesc,
                    sentence?.custodialInd?.ynToBoolean(),
                    sentence?.cjaInd?.ynToBoolean(),
                    RefElementDto.from(sentence?.orderType),
                    RefElementDto.from(offenceBlock?.offenceBlockType),
                    sentenceDetail?.cjaUnpaidHours,
                    sentenceDetail?.cjaSupervisionMonths,
                    sentenceDetail?.activityDesc,
                    offenceBlock?.offenceDate,
                    offenceBlock?.sentenceDate,
                    offenceBlock?.sentLengthCustDays)
        }
    }
}