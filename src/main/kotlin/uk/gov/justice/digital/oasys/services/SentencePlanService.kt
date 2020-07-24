package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.BasicSentencePlanDto
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException

@Service
class SentencePlanService (
        private val offenderService: OffenderService,
        private val assessmentRepository: AssessmentRepository,

        private val sectionService: SectionService
) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getLatestBasicSentencePlanForOffender(identityType: String?, identity: String?, filterGroupStatus: String? = null, filterAssessmentType: String? = null, filterVoided: Boolean? = null, filterAssessmentStatus: String? = null): BasicSentencePlanDto {
        val offenderId = getOffenderIdByIdentifier(identityType, identity)
        val assessment = assessmentRepository.getLatestAssessmentForOffender(offenderId, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
                ?: throw EntityNotFoundException("Latest Basic Sentence Plan for Offender $offenderId, not found")

        log.info("Found Latest Assessment type: ${assessment.assessmentType}status: ${assessment.assessmentStatus} for identity: ($identity,$identityType)")
        return BasicSentencePlanDto.from(assessment)
                ?: throw EntityNotFoundException("Latest Basic Sentence Plan for Offender $offenderId, not found")

    }

    fun getBasicSentencePlansForOffender(identityType: String?, identity: String?, filterGroupStatus: String? = null, filterAssessmentType: String? = null, filterVoided: Boolean? = null, filterAssessmentStatus: String? = null): Collection<BasicSentencePlanDto> {
        val offenderId = getOffenderIdByIdentifier(identityType, identity)
        val assessments = assessmentRepository.getAssessmentsForOffender(offenderId, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
        log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identity)")
        return BasicSentencePlanDto.from(assessments)
    }

    private fun getOffenderIdByIdentifier(identityType: String?, identity: String?): Long? {
        return offenderService.getOffenderIdByIdentifier(identityType, identity)
    }

}
