package uk.gov.justice.digital.oasys.services

import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.AssessmentGroup
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Assessment Service Tests")
class AssessmentServiceRisksAndNeedsTest {

  private val assessmentRepository: AssessmentRepository = mockk()
  private val offenderService: OffenderService = mockk()
  private val sectionService: SectionService = mockk()

  private fun setupAssessmentGroup(): AssessmentGroup {
    return AssessmentGroup(historicStatus = "Current")
  }

  private fun setupVersion(): RefAssessmentVersion {
    return RefAssessmentVersion(refAssVersionUk = 1L, versionNumber = "Any Version", refAssVersionCode = "Any Ref Version Code", oasysScoringAlgVersion = 2L)
  }

  private fun setupAssessment(assessmentType: String): Assessment {
    val created = LocalDateTime.now()
    val completed = created.plusMonths(3)
    val voided = created.plusMonths(4)

    return Assessment(
      oasysSetPk = 1234L,
      assessorName = "Any Name",
      assessmentStatus = "STATUS",
      assessmentType = assessmentType,
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
