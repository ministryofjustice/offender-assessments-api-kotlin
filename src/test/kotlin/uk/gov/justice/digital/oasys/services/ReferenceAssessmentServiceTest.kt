package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.oasys.api.RefAssessmentDto
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersionPK
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceAssessmentRepository


@ExtendWith(MockKExtension::class)
@DisplayName("Reference Assessment Service Tests")
class ReferenceAssessmentServiceTest {

    private val referenceAssessmentRepository: ReferenceAssessmentRepository = mockk()
    private val referenceAssessmentService =  ReferenceAssessmentService(referenceAssessmentRepository)

    @Test
    fun `Return reference assessment for type and version`(){
        val refAssessmentVersion = setupRefAssessmentVersion()

        every {referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK("Any Code","Any Version"))} returns (refAssessmentVersion)

        val refAssessmentDto = referenceAssessmentService.getReferenceAssessmentOf("Any Code", "Any Version")

        verify(exactly = 1) { referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK("Any Code","Any Version")) }

        val validRefAssessmentDto = setupValidDto()
        assertThat(refAssessmentDto).isEqualTo(validRefAssessmentDto)
    }

    private fun setupRefAssessmentVersion(): RefAssessmentVersion{
        return RefAssessmentVersion(
                refAssVersionUk = 2L,
                refAssVersionCode = "Any Code",
                versionNumber = "Any Version",
                oasysScoringAlgVersion = 12L,
                refModuleCode = "Any Code",
                refSections = emptyList()
        )
    }

    private fun setupValidDto(): RefAssessmentDto{
        return RefAssessmentDto(
                refAssessmentVersionId = 2L,
                refAssVersionCode = "Any Code",
                versionNumber = "Any Version",
                oasysScoringAlgorithmVersion = 12L,
                refModuleCode = "Any Code",
                refSections = emptySet()
        )
    }
}