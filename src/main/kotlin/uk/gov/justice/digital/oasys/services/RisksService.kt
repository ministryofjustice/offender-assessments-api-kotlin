package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AnswerDto
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshFullQuestionCodes
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.roshSumQuestionCodes
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.rsrQuestionCodes
import uk.gov.justice.digital.oasys.api.QuestionDto.Companion.saraQuestionCodes
import uk.gov.justice.digital.oasys.api.RiskDto
import uk.gov.justice.digital.oasys.api.SectionAnswersDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.AssessmentPurposeType
import uk.gov.justice.digital.oasys.services.domain.AssessmentType
import uk.gov.justice.digital.oasys.services.domain.RoshMapping
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
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
    val isRsrOnly = isRsrOnlyAssessment(assessment, answers)
    if (assessment.assessmentType?.equals(AssessmentType.SARA.value)!!) {
      return RiskDto.fromSara(assessment, answers, isRsrOnly)
    } else {
      val sara = assessment.childAssessments.find { it?.assessmentType?.equals(AssessmentType.SARA.value)!! }
        ?: return RiskDto.fromRosha(assessment, answers, isRsrOnly)
      val saraAnswers = getRiskAnswersByAssessmentId(sara.oasysSetPk!!)
      return RiskDto.fromRoshaWithSara(assessment, answers, saraAnswers, isRsrOnly)
    }
  }

  private fun isRsrOnlyAssessment(assessment: Assessment, answers: AssessmentAnswersDto): Boolean? {
    return when (assessment?.assessmentType.let { AssessmentType.fromString(it) }) {
      AssessmentType.LAYER1 -> {
        assessment?.assessmentVersion?.versionNumber?.equals(VERSION_2).let {
          areAnyOfTheQuestionAnswersNegative(rsrQuestionCodes, answers)
        }
      }
      AssessmentType.LAYER3 -> {
        AssessmentPurposeType.fromString(assessment.assessmentPurposeType)?.equals(AssessmentPurposeType.RSR_ONLY)
      }
      else -> null
    }
  }

  private fun getRiskAnswersByAssessmentId(assessmentId: Long): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(assessmentId, riskQuestions).also {
      log.info("Found ${it.questionAnswers.size} risk Answers for Assessment ID: $assessmentId")
    }
  }

  private fun areAnyOfTheQuestionAnswersNegative(questionCodes: Set<String>, answers: AssessmentAnswersDto): Boolean? {
    return areAnyOfTheAnswersOfType(questionCodes, answers, NEGATIVE_ANSWERS)
  }

  private fun areAnyOfTheAnswersOfType(
    questionCodes: Set<String>,
    answers: AssessmentAnswersDto,
    answerTypes: Set<String>
  ): Boolean? {
    val riskQuestions = answers.questionAnswers.filter { questionCodes.contains(it.refQuestionCode) }
    return if (riskQuestions.isNullOrEmpty() || riskQuestions?.map { it.answers }.flatten().isEmpty()) null
    else riskQuestions?.any {
      anySingleAnswersMatch(
        answerTypes,
        it.answers
      )
    }
  }

  private fun anySingleAnswersMatch(answerTypes: Set<String>, answers: Collection<AnswerDto>): Boolean {
    return answerTypes.any { answers?.map { a -> a.refAnswerCode }?.contains(it) }
  }

  fun getRiskSections(assessmentId: Long, sectionCodes: Set<SectionHeader>): SectionAnswersDto {
    return answerService.getSectionAnswersForQuestions(assessmentId, RoshMapping.rosh(sectionCodes))
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val riskQuestions = mapOf(
      "SARA" to saraQuestionCodes,
      "ROSHSUM" to roshSumQuestionCodes,
      "ROSHFULL" to roshFullQuestionCodes,
      "RSR" to rsrQuestionCodes
    )
    val NEGATIVE_ANSWERS = setOf("NO", "N")
    val VERSION_2 = "2"
  }
}
