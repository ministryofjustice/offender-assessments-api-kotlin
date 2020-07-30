package uk.gov.justice.digital.oasys.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.RefAssessmentDto
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersionPK
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceAssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException

@Service
class ReferenceAssessmentService (private val refAssessmentRepository: ReferenceAssessmentRepository) {

    fun getReferenceAssessmentOf(versionCode: String?, versionNumber: String?): RefAssessmentDto? {
        val refAssessmentVersion = refAssessmentRepository.findByIdOrNull(RefAssessmentVersionPK(versionCode, versionNumber))
                ?: throw EntityNotFoundException("Reference Assessment for version code ${versionCode}, version number ${versionNumber}, not found!")
        return RefAssessmentDto.from(refAssessmentVersion)
    }
}