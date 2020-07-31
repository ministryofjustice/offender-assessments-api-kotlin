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
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.oasys.api.RefAssessmentDto
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersionPK
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceAssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException


@ExtendWith(MockKExtension::class)
@DisplayName("Reference Assessment Service Tests")
class ReferenceAssessmentServiceTest {

    private val referenceAssessmentRepository: ReferenceAssessmentRepository = mockk()
    private val referenceAssessmentService =  ReferenceAssessmentService(referenceAssessmentRepository)

    @Test
    fun `Return reference assessment for type and version`(){
        val versionCode = "Any Code"
        val versionNumber = "Any Version"
        val refAssessmentVersion = setupRefAssessmentVersion()
        every {referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK(versionCode, versionNumber))} returns (refAssessmentVersion)

        val refAssessmentDto = referenceAssessmentService.getReferenceAssessmentOf(versionCode, versionNumber)
        verify(exactly = 1) { referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK(versionCode,versionNumber)) }
        assertThat(refAssessmentDto).isEqualTo(setupValidDto())
    }

    @Test
    fun `Throws not found for invalid type and version`(){
        val invalidString = "invalid"
        every {referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK(invalidString,invalidString))} returns (null)

        assertThrows<EntityNotFoundException> { referenceAssessmentService.getReferenceAssessmentOf(invalidString, invalidString) }
        verify(exactly = 1) { referenceAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK(invalidString,invalidString)) }
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