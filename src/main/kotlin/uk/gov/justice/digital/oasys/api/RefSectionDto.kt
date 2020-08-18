package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.RefSection

data class RefSectionDto (
        val refSectionId: Long? = null,
        val refSectionCode: String? = null,
        val shortDescription: String? = null,
        val description: String? = null,
        val refCrimNeedScoreThreshold: Long? = null,
        val refScoredForOgp: Boolean? = false,
        val refScoredForOvp: Boolean? = false,
        val refQuestions: Collection<RefQuestionDto>? = null
) {

    companion object {

        fun from(refSections: Collection<RefSection?>?): Collection<RefSectionDto> {
            return refSections?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
        }

        private fun from(refSection: RefSection): RefSectionDto {
            return RefSectionDto(
                    refSection.crimNeedScoreThreshold,
                    refSection.refSectionCode,
                    refSection.sectionType?.refElementShortDesc,
                    refSection.sectionType?.refElementDesc,
                    refSection.crimNeedScoreThreshold,
                    refSection.scoredForOgp.ynToBoolean(),
                    refSection.scoredForOvp.ynToBoolean(),
                    RefQuestionDto.from(refSection.refQuestions))
        }
    }
}