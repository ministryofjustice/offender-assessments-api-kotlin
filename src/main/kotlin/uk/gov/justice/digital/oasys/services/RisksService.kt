package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository

@Service
class RisksService(
  private val offenderService: OffenderService,
  private val answerService: AnswerService,
  private val assessmentRepository: AssessmentRepository
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

//  fun getAllRisksForOffender(identityType: String, identity: String): Collection<RiskDto>? {
//    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
//    val assessments: Collection<Assessment>? = assessmentRepository.getAssessmentsForOffender(offenderId)
//    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
//    val answers = answerService.getAnswersForQuestions()
//
//    return RiskDto.from(assessments)
//  }

  fun getRisksForOffenderByAssessment(identityType: String, identity: String, assessmentId: String){
    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
    val answers = answerService.getAnswersForQuestions(assessmentId, )


  }
}
