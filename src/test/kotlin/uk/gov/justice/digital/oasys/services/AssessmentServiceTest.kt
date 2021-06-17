package uk.gov.justice.digital.oasys.services

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
import uk.gov.justice.digital.oasys.api.PeriodUnit
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
    verify(exactly = 1) { assessmentRepository.getAssessment(oasysSetPk) }
  }

  @Test
  fun `throws not found exception when null assessment returned`() {
    setupAssessment()
    val oasysSetPk = 1234L
    every { assessmentRepository.getAssessment(oasysSetPk) } returns null
    every { sectionService.getSectionForAssessment(oasysSetPk, any()) } returns null
    every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

    assertThrows<EntityNotFoundException> { assessmentsService.getAssessment(oasysSetPk) }

    verify(exactly = 1) { assessmentRepository.getAssessment(oasysSetPk) }
    verify(exactly = 0) { sectionService.getSectionForAssessment(oasysSetPk, any()) }
    verify(exactly = 0) { sectionService.getSectionsForAssessment(oasysSetPk, any()) }
  }

  @Test
  fun `return assessments for OASys Offender ID`() {
    val assessment = setupAssessment()
    val oasysOffenderPk = 1L
    every {
      offenderService.getOffenderIdByIdentifier(
        OffenderIdentifier.OASYS.value,
        oasysOffenderPk.toString()
      )
    } returns oasysOffenderPk
    every { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) } returns setOf(
      assessment
    )

    val assessments = assessmentsService.getAssessmentsForOffender(
      OffenderIdentifier.OASYS.value,
      oasysOffenderPk.toString(),
      null,
      null,
      null,
      null
    )

    assertThat(assessments).hasSize(1)
    verify(exactly = 1) {
      offenderService.getOffenderIdByIdentifier(
        OffenderIdentifier.OASYS.value,
        oasysOffenderPk.toString()
      )
    }
    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) }
  }

  @Test
  fun `return empty collection of assessments for OASys Offender ID`() {
    val oasysOffenderPk = 1L
    every {
      offenderService.getOffenderIdByIdentifier(
        OffenderIdentifier.OASYS.value,
        oasysOffenderPk.toString()
      )
    } returns oasysOffenderPk
    every { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) } returns emptySet()

    val assessments = assessmentsService.getAssessmentsForOffender(
      OffenderIdentifier.OASYS.value,
      oasysOffenderPk.toString(),
      null,
      null,
      null,
      null
    )

    assertThat(assessments).hasSize(0)
    verify(exactly = 1) {
      offenderService.getOffenderIdByIdentifier(
        OffenderIdentifier.OASYS.value,
        oasysOffenderPk.toString()
      )
    }
    verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(oasysOffenderPk, null, null, null, null) }
  }

  @Test
  fun `get latest assessment in period returns assessment`() {
    val oasysOffenderPk = 1L
    val filterAssessmentType = setOf("LAYER_1")
    val filterAssessmentStatus = "COMPLETE"
    every {
      offenderService.getOffenderIdByIdentifier(
        OffenderIdentifier.CRN.value,
        oasysOffenderPk.toString()
      )
    } returns oasysOffenderPk
    val assessment = Assessment(oasysSetPk = 123)
    every {
      assessmentRepository.getLatestAssessmentsForOffenderInPeriod(
        oasysOffenderPk,
        filterAssessmentType,
        filterAssessmentStatus,
        any()
      )
    } returns assessment

    val result = assessmentsService.getLatestAssessmentsForOffenderInPeriod(
      OffenderIdentifier.CRN.value,
      oasysOffenderPk.toString(),
      filterAssessmentType,
      filterAssessmentStatus,
      PeriodUnit.YEAR,
      1
    )

    // assertThat(result).isEqualTo(assessmentSummary)
  }

  private fun setupAssessmentGroup(): AssessmentGroup {
    return AssessmentGroup(historicStatus = "Current")
  }

  private fun setupVersion(): RefAssessmentVersion {
    return RefAssessmentVersion(
      refAssVersionUk = 1L,
      versionNumber = "Any Version",
      refAssVersionCode = "Any Ref Version Code",
      oasysScoringAlgVersion = 2L
    )
  }

  private fun setupAssessment(): Assessment {
    val created = LocalDateTime.now()
    val completed = created.plusMonths(3)
    val voided = created.plusMonths(4)

    return Assessment(
      oasysSetPk = 1234L,
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
