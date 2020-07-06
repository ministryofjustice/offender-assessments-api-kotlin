package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.services.domain.CrimiogenicNeed
import java.time.LocalDateTime

data class AssessmentDto(
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
        val voidedDateTime: LocalDateTime? = null,
        val sections: Collection<SectionDto?>? = null,
        val sentence: Collection<SentenceDto?>? = null,
        val childSafeguardingIndicated: Boolean? = null,
        val layer3SentencePlanNeeds: Collection<AssessmentNeedDto?>? = null

) {

    companion object {
        fun from(assessment: Assessment?, childSafeguardingIndicated: Boolean?, needs: Collection<CrimiogenicNeed?>?): AssessmentDto {

            val assessmentVersion = assessment?.assessmentVersion;
            return AssessmentDto(
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
                    assessment?.assessmentVoidedDate,
                    SectionDto.from(assessment?.oasysSections),
                    SentenceDto.from(assessment?.offenceBlocks),
                    childSafeguardingIndicated,
                    AssessmentNeedDto.from(needs))
        }
    }
}
