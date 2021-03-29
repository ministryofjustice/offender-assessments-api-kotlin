package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.PredictorDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository

@Service
class PredictorsService(
  private val offenderService: OffenderService,
  private val assessmentRepository: AssessmentRepository
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getAllPredictorsForOffender(identityType: String, identity: String): Collection<PredictorDto>? {
    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
    val assessments: Collection<Assessment>? = assessmentRepository.getAssessmentsForOffender(offenderId)
    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
    return PredictorDto.from(assessments)
  }
}
