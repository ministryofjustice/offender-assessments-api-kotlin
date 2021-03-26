package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.RiskDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException

@Service
class RisksService(
  private val answerService: AnswerService,
  private val assessmentRepository: AssessmentRepository,
  private val offenderService: OffenderService,
) {

  fun getAllRisksForOffender(identityType: String, identity: String): Collection<RiskDto>? {
    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
    val assessments = assessmentRepository.getAssessmentsForOffender(offenderId)
    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
    return assessments?.mapNotNull { risksByAssessment(it) }
  }

  fun getRisksForAssessmentId(assessmentId: Long): RiskDto? {
    val assessment = assessmentRepository.getAssessment(assessmentId)
      ?: throw EntityNotFoundException("Assessment for OasysSetId $assessmentId, not found")
    log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for oasysSetId: ${assessment.oasysSetPk}")
    return risksByAssessment(assessment)
  }

  fun risksByAssessment(assessment: Assessment): RiskDto? {
    val answers = getRiskAnswersByAssessmentId(assessment.oasysSetPk!!)
    if (assessment.assessmentType.equals("SARA")) {
      return RiskDto.fromSara(assessment, answers)
    } else {
      val sara = assessment.childAssessments.find { it?.assessmentType.equals("SARA") }
        ?: return RiskDto.fromRosha(assessment, answers)
      val saraAnswers = getRiskAnswersByAssessmentId(sara.oasysSetPk!!)
      return RiskDto.fromRoshaWithSara(assessment, answers, saraAnswers)
    }
  }

  fun getRiskAnswersByAssessmentId(assessmentId: Long): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(assessmentId, riskQuestions).also {
      log.info("Found Risks for assessment ID: ($assessmentId)")
    }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val riskQuestions = mapOf<String, Collection<String>>(
      "SARA" to listOf("SR76.1.1", "SR77.1.1"),
      "ROSHSUM" to listOf("SUM6.1.2", "SUM6.2.1", "SUM6.2.2", "SUM6.3.1", "SUM6.3.2", "SUM6.4.1", "SUM6.4.2", "SUM6.5.2"),
      "ROSHFULL" to listOf("FA31", "FA32")
    )
  }
}
