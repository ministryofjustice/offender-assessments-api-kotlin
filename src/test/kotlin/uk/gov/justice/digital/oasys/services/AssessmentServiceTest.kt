package uk.gov.justice.digital.oasys.services

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.OffenderIdentifier
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.AssessmentGroup
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Assessment Service Tests")
class AssessmentServiceTest {

    private val assessmentRepository: AssessmentRepository = mockk()
    private val offenderService: OffenderService = mockk()
    private val sectionService: SectionService = mockk()
    private val assessmentsService = AssessmentService(assessmentRepository, offenderService, sectionService)

    @Test
    fun `return assessment for OASys Set ID`() {
        val assessment = setupAssessment()
        val oasysSetPk = 1234L
        every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
        every { sectionService.getSectionForAssessment(oasysSetPk, any()) } returns null
        every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

        assessmentsService.getAssessment(oasysSetPk)
        verify(exactly = 1) { assessmentRepository.getAssessment(oasysSetPk)  }
    }

    @Test
    fun `throws not found exception when null assessment returned`() {
        val assessment = setupAssessment()
        val oasysSetPk = 1234L
        every { assessmentRepository.getAssessment(oasysSetPk) } returns null
        every { sectionService.getSectionForAssessment(oasysSetPk, any()) } returns null
        every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

        assertThrows<EntityNotFoundException> { assessmentsService.getAssessment(oasysSetPk) }

        verify(exactly = 1) { assessmentRepository.getAssessment(oasysSetPk)  }
        verify(never()) { sectionService.getSectionForAssessment(oasysSetPk, any())}
        verify(never()) { sectionService.getSectionsForAssessment(oasysSetPk, any())}
    }

    @Test
    fun `return assessments for OASys Offender ID`() {
        val assessment = setupAssessment()
        val oasysOffenderPk = 1L
        every { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString()) } returns oasysOffenderPk
        every { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) } returns setOf(assessment)

        val assessments = assessmentsService.getAssessmentsForOffender(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString(), null, null, null, null)

        assertThat(assessments).hasSize(1)
        verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString())  }
        verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null)  }
    }

    @Test
    fun `return empty collection of assessments for OASys Offender ID`() {
        val oasysOffenderPk = 1L
        every { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString()) } returns oasysOffenderPk
        every { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) } returns emptySet()

        val assessments = assessmentsService.getAssessmentsForOffender(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString(), null, null, null, null)

        assertThat(assessments).hasSize(0)
        verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString())  }
        verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null)  }
    }

    @Test
    fun `return latest assessment for OASys Offender ID`() {
        val assessment = setupAssessment()
        val oasysOffenderPk = 1L
        val oasysSetPk = 1234L

        every { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString()) } returns oasysOffenderPk
        every { assessmentRepository.getLatestAssessmentForOffender(oasysOffenderPk, null, null, null, null) } returns assessment
        every { sectionService.getSectionForAssessment(oasysSetPk, any()) } returns null
        every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

        assessmentsService.getLatestAssessmentForOffender(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString(), null, null, null, null)

        verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString())  }
        verify(exactly = 1) { assessmentRepository.getLatestAssessmentForOffender(oasysOffenderPk, null, null, null, null)  }
    }

    @Test
    fun `throws not found exception when no latest assessment returned`() {
        val oasysOffenderPk = 1L
        every { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString()) } returns oasysOffenderPk
        every { assessmentRepository.getLatestAssessmentForOffender(oasysOffenderPk, null, null, null, null) } returns null

        assertThrows<EntityNotFoundException> { assessmentsService.getLatestAssessmentForOffender(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString(), null, null, null, null) }

        verify(exactly = 1) { offenderService.getOffenderIdByIdentifier(OffenderIdentifier.OASYS.value, oasysOffenderPk.toString())  }
        verify(exactly = 1) { assessmentRepository.getLatestAssessmentForOffender(oasysOffenderPk, null, null, null, null)  }
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
                oasysSections = emptySet(),
                oasysBcsParts = emptySet(),
                group = setupAssessmentGroup()
        )
    }

}