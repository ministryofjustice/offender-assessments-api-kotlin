package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.api.RiskDto

@Service
class RisksService(
  private val answerService: AnswerService,
  private val assessmentService: AssessmentService
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val riskQuestions = mapOf<String, Collection<String>>(
      "SARA" to listOf("SR76.1.1", "SR77.1.1 "),
      "ROSHSUM" to listOf("SUM6.1.2", "SUM6.2.1", "SUM6.2.2", "SUM6.3.1", "SUM6.3.2", "SUM6.4.1", "SUM6.4.2", "SUM6.5.2"),
      "ROSHFULL" to listOf("FA31", "FA32")
    )
  }

  fun getAllRisksForOffender(identityType: String, identity: String): Collection<RiskDto>? {
    val assessments = assessmentService.getAssessmentsForOffender(identityType, identity, null, null, null, null)
    return assessments.map { assessment ->
      val answers = getRiskAnswersByAssessmentId(assessment.assessmentId!!)
      RiskDto.from(assessment, answers) }
  }

  fun getRiskAnswersByAssessmentId(assessmentId: Long): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(assessmentId, riskQuestions).also {
      log.info("Found Risks for assessment ID: ($assessmentId)")
    }
  }

  fun getRisksForOffenderByAssessmentId(assessmentId: Long): RiskDto {
    val assessment = assessmentService.getAssessment(assessmentId)
    val answers = getRiskAnswersByAssessmentId(assessmentId)
    return RiskDto.from(assessment, answers)
  }
}
