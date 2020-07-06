package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Section

data class SectionDto(
        val sectionId: Long? = null,
        val assessmentId: Long? = null,
        val refAssessmentVersionCode: String? = null,
        val refSectionVersionNumber: String? = null,
        val refSectionCode: String? = null,
        val refSectionCrimNeedScoreThreshold: Long? = null,
        val status: String? = null,
        val sectionOgpWeightedScore: Long? = null,
        val sectionOgpRawScore: Long? = null,
        val sectionOvpWeightedScore: Long? = null,
        val sectionOvpRawScore: Long? = null,
        val sectionOtherWeightedScore: Long? = null,
        val sectionOtherRawScore: Long? = null,
        val lowScoreAttentionNeeded: String? = null,
        val questions: Collection<QuestionDto?>? = null
) {

    companion object {

        fun from(sections: Collection<Section?>?): Collection<SectionDto?> {
            return sections?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
        }

        private fun from(section: Section?): SectionDto? {
            val refSection = section?.refSection
            return SectionDto(
                    section?.oasysSectionPk,
                    section?.oasysSetPk,
                    refSection?.refAssVersionCode,
                    refSection?.versionNumber,
                    refSection?.refSectionCode,
                    refSection?.crimNeedScoreThreshold,
                    section?.sectionStatusElm,
                    section?.sectOgpWeightedScore,
                    section?.sectOgpRawScore,
                    section?.sectOvpWeightedScore,
                    section?.sectOvpRawScore,
                    section?.sectOtherWeightedScore,
                    section?.sectOtherRawScore,
                    section?.lowScoreNeedAttnInd,
                    QuestionDto.from(section?.oasysQuestions)
            )
        }
    }

}
