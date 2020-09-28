package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import java.time.LocalDateTime

data class AssessmentDto(

        @ApiModelProperty(value = "Assessment primary key (OASysSetPK)", example = "1234")
        val assessmentId: Long? = null,

        @ApiModelProperty(value = "Assessment Reference Version Code", example = "LAYER3")
        val refAssessmentVersionCode: String? = null,

        @ApiModelProperty(value = "Assessment Reference Version", example = "1")
        val refAssessmentVersionNumber: String? = null,

        @ApiModelProperty(value = "Assessment Reference Version", example = "1")
        val refAssessmentId: Long? = null,

        @ApiModelProperty(value = "Assessment Type", example = "LAYER_£")
        val assessmentType: String? = null,

        @ApiModelProperty(value = "Assessment Status", example = "OPEN")
        val assessmentStatus: String? = null,

        @ApiModelProperty(value = "Assessment Group Historic Status", example = "CURRENT")
        val historicStatus: String? = null,

        @ApiModelProperty(value = "Assessment Scoring Algorithm Version", example = "1")
        val refAssessmentOasysScoringAlgorithmVersion: Long? = null,

        @ApiModelProperty(value = "AssessorName", example = "Layer 3")
        val assessorName: String? = null,

        @ApiModelProperty(value = "Created Date", example = "2020-01-02T16:00:00")
        val created: LocalDateTime? = null,

        @ApiModelProperty(value = "Completed Date", example = "2020-01-02T16:00:00")
        val completed: LocalDateTime? = null,

        @ApiModelProperty(value = "Voided Date", example = "2020-01-02T16:00:00")
        val voided: LocalDateTime? = null,

        @ApiModelProperty(value = "Assessment Sections")
        val sections: Collection<SectionDto?>? = null,

        @ApiModelProperty(value = "Sentences")
        val sentence: Collection<SentenceDto?>? = null,

        @ApiModelProperty(value = "Child Safeguarding flat", example = "true")
        val childSafeguardingIndicated: Boolean? = null,

        @ApiModelProperty(value = "Criminogenic Needs")
        val layer3SentencePlanNeeds: Collection<AssessmentNeedDto?>? = null

) {

    companion object {
        fun from(assessment: Assessment?, childSafeguardingIndicated: Boolean?, needs: Collection<CriminogenicNeed?>): AssessmentDto {

            val assessmentVersion = assessment?.assessmentVersion
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
