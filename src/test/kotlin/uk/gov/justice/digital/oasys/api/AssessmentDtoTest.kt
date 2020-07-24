package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.AssessmentGroup
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import java.time.LocalDateTime

@DisplayName("Assessment DTO Tests")
class AssessmentDtoTest {
    var assessment: Assessment = setupAssessment()

    @Test
    fun `Builds valid Assessment DTO from Entity with no child safeguarding and needs`() {

        val needs= setOf(CriminogenicNeed(SectionHeader.ACCOMMODATION,
                "Accomodation",
                true,
                true,
                true,
                true))

        val assessmentDto = AssessmentDto.from(assessment, null, needs)

        assertThat(assessmentDto.assessmentId).isEqualTo(assessment.oasysSetPk)
        assertThat(assessmentDto.assessorName).isEqualTo(assessment.assessorName)
        assertThat(assessmentDto.assessmentType).isEqualTo(assessment.assessmentType)
        assertThat(assessmentDto.historicStatus).isEqualTo(assessment.group?.historicStatus)
        assertThat(assessmentDto.assessmentStatus).isEqualTo(assessment.assessmentStatus)
        assertThat(assessmentDto.createdDateTime).isEqualTo(assessment.createDate)
        assertThat(assessmentDto.refAssessmentId).isEqualTo(assessment.assessmentVersion?.refAssVersionUk)
        assertThat(assessmentDto.refAssessmentVersionCode).isEqualTo(assessment.assessmentVersion?.refAssVersionCode)
        assertThat(assessmentDto.refAssessmentVersionNumber).isEqualTo(assessment.assessmentVersion?.versionNumber)
        assertThat(assessmentDto.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(assessment.assessmentVersion?.oasysScoringAlgVersion)
        assertThat(assessmentDto.completedDateTime).isEqualTo(assessment.dateCompleted)
        assertThat(assessmentDto.voidedDateTime).isEqualTo(assessment.assessmentVoidedDate)
        assertThat(assessmentDto.sections).hasSize(assessment.oasysSections!!.size)
        assertThat(assessmentDto.childSafeguardingIndicated).isNull()
        assertThat(assessmentDto.layer3SentencePlanNeeds).hasSize(needs.size)
    }
    
    @Test
    fun `Builds valid Assessment DTO from Entity with no child safeguarding and empty needs`() {

        val assessmentDto = AssessmentDto.from(assessment, null, emptySet())

        assertThat(assessmentDto.assessmentId).isEqualTo(assessment.oasysSetPk)
        assertThat(assessmentDto.assessorName).isEqualTo(assessment.assessorName)
        assertThat(assessmentDto.assessmentType).isEqualTo(assessment.assessmentType)
        assertThat(assessmentDto.historicStatus).isEqualTo(assessment.group?.historicStatus)
        assertThat(assessmentDto.assessmentStatus).isEqualTo(assessment.assessmentStatus)
        assertThat(assessmentDto.createdDateTime).isEqualTo(assessment.createDate)
        assertThat(assessmentDto.refAssessmentId).isEqualTo(assessment.assessmentVersion?.refAssVersionUk)
        assertThat(assessmentDto.refAssessmentVersionCode).isEqualTo(assessment.assessmentVersion?.refAssVersionCode)
        assertThat(assessmentDto.refAssessmentVersionNumber).isEqualTo(assessment.assessmentVersion?.versionNumber)
        assertThat(assessmentDto.refAssessmentOasysScoringAlgorithmVersion).isEqualTo(assessment.assessmentVersion?.oasysScoringAlgVersion)
        assertThat(assessmentDto.completedDateTime).isEqualTo(assessment.dateCompleted)
        assertThat(assessmentDto.voidedDateTime).isEqualTo(assessment.assessmentVoidedDate)
        assertThat(assessmentDto.sections).hasSize(assessment.oasysSections!!.size)
        assertThat(assessmentDto.childSafeguardingIndicated).isNull()
        assertThat(assessmentDto.layer3SentencePlanNeeds).isEmpty()
    }


    private fun setupAssessmentGroup() : AssessmentGroup {
        return AssessmentGroup(historicStatus = "Current")
    }

    private fun setupVersion(): RefAssessmentVersion {
        return RefAssessmentVersion(refAssVersionUk = 1L, versionNumber = "Any Version", refAssVersionCode = "Any Ref Version Code", oasysScoringAlgVersion = 2L)
    }

    private fun setupAssessment(): Assessment {
        val created = LocalDateTime.now()
        val completed = created.plusMonths(3)
        val voided = created.plusMonths(4)

        return Assessment(oasysSetPk = 1234L,
                assessorName = "Any Name",
                assessmentStatus = "STATUS",
                assessmentType = "LAYER_3",
                createDate = created,
                dateCompleted = completed,
                assessmentVoidedDate = voided,
                assessmentVersion = setupVersion(),
                oasysSections = setOf(Section()),
                oasysBcsParts = emptySet(),
                group = setupAssessmentGroup()
        )
    }
}