package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AnswerDto
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshFullQuestionIds
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshQuestionIds
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshSumQuestionIds
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.saraQuestionIds
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
      ?: throw EntityNotFoundException("No Assessments found for Identity Type: $identityType, Identity: $identity ")
    log.info("Found ${assessments.size} Assessments for identity: ($identity, $identityType)")
    return assessments.map { getRisksForAssessment(it) }
  }

  fun getRisksForAssessmentId(assessmentId: Long): RiskDto? {
    val assessment = assessmentRepository.getAssessment(assessmentId)
      ?: throw EntityNotFoundException("Assessment for OasysSetId $assessmentId, not found")
    log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for oasysSetId: ${assessment.oasysSetPk}")
    return getRisksForAssessment(assessment)
  }

  fun getRisksForAssessment(assessment: Assessment): RiskDto {
    val answers = getRiskAnswersByAssessmentId(assessment.oasysSetPk!!)
    val childSafeguardingIndicated = calculateChildSafeguardingIndicated(answers)
    if (assessment.assessmentType.equals("SARA")) {
      return RiskDto.fromSara(assessment, answers, childSafeguardingIndicated)
    } else {
      val sara = assessment.childAssessments.find { it?.assessmentType.equals("SARA") }
        ?: return RiskDto.fromRosha(assessment, answers, childSafeguardingIndicated)
      val saraAnswers = getRiskAnswersByAssessmentId(sara.oasysSetPk!!)
      return RiskDto.fromRoshaWithSara(assessment, answers, saraAnswers, childSafeguardingIndicated)
    }
  }

  private fun getRiskAnswersByAssessmentId(assessmentId: Long): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(assessmentId, riskQuestions).also {
      log.info("Found ${it.questionAnswers.size} risk Answers for Assessment ID: $assessmentId")
    }
  }

  private fun calculateChildSafeguardingIndicated(answers: AssessmentAnswersDto): Boolean? {
    val riskQuestions = answers.questionAnswers.filter { roshQuestionIds.contains(it.refQuestionCode) }
    return if (riskQuestions.isNullOrEmpty() || riskQuestions?.map { it.answers }.flatten().isEmpty()) null
    else riskQuestions?.any {
      anySingleAnswersArePositive(
        it.answers
      )
    }
  }

  private fun anySingleAnswersArePositive(answers: Collection<AnswerDto>): Boolean {
    return POSITIVE_ANSWERS.any { answers?.map { a -> a.refAnswerCode }?.contains(it) }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val riskQuestions = mapOf(
      "SARA" to saraQuestionIds,
      "ROSHSUM" to roshSumQuestionIds,
      "ROSHFULL" to roshFullQuestionIds,
      "ROSH" to roshQuestionIds
    )
    val POSITIVE_ANSWERS = setOf("YES", "Y")
  }
}
