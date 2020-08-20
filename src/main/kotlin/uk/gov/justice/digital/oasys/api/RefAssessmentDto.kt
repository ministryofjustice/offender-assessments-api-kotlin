package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion

data class RefAssessmentDto(
        val refAssessmentVersionId: Long? = null,
        val refAssVersionCode: String? = null,
        val versionNumber: String? = null,
        val oasysScoringAlgorithmVersion: Long? = null,
        val refModuleCode: String? = null,
        val refSections: Collection<RefSectionDto>? = null
) {

    companion object {

        fun from(version: RefAssessmentVersion?): RefAssessmentDto? {
            if (version == null){
                return null
            }
            return RefAssessmentDto(
                    version.refAssVersionUk,
                    version.refAssVersionCode,
                    version.versionNumber,
                    version.oasysScoringAlgVersion,
                    version.refModuleCode,
                    RefSectionDto.from(version.refSections))
        }
    }
}