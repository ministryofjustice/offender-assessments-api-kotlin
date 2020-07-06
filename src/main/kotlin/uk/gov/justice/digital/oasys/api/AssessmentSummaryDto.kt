package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

data class AssessmentSummaryDto(

        val assessmentId: Long? = null,
        val refAssessmentVersionCode: String? = null,
        val refAssessmentVersionNumber: String? = null,
        val refAssessmentId: Long? = null,
        val assessmentType: String? = null,
        val assessmentStatus: String? = null,
        val historicStatus: String? = null,
        val refAssessmentOasysScoringAlgorithmVersion: Long? = null,
        val assessorName: String? = null,
        val createdDateTime: LocalDateTime? = null,
        val completedDateTime: LocalDateTime? = null,
        val voidedDateTime: LocalDateTime? = null

) {

    companion object {

        fun from(assessments: Collection<Assessment?>?): Collection<AssessmentSummaryDto>? {
            return assessments?.filterNotNull()?.map { from(it) }?.toSet().orEmpty()
        }

        private fun from(assessment: Assessment?): AssessmentSummaryDto {

            val assessmentVersion = assessment?.assessmentVersion;
            return AssessmentSummaryDto(
                    assessment?.oasysSetPk,
                    assessmentVersion?.refAssVersionCode,
                    assessmentVersion?.versionNumber,
                    assessmentVersion?.refAssVersionUk,
                    assessment?.assessmentType,
                    assessment?.assessmentStatus,
                    assessment?.group?.historicStatus,
                    assessmentVersion?.oasysScoringAlgVersion,
                    assessment?.assessorName,
                    assessment?.createDate,
                    assessment?.dateCompleted,
                    assessment?.assessmentVoidedDate)
        }
    }
}
