package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentNeedDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto.Companion.toAssessmentSummaryDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto.Companion.toAssessmentsSummaryDto
import uk.gov.justice.digital.oasys.api.PeriodUnit
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.AssessmentType
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeedMapping
import uk.gov.justice.digital.oasys.services.domain.NeedConfiguration
import uk.gov.justice.digital.oasys.services.domain.NeedSeverity
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class AssessmentService constructor(
  private val assessmentRepository: AssessmentRepository,
  private val offenderService: OffenderService,
  private val sectionService: SectionService,
) {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val LAYER_3 = AssessmentType.LAYER3.value
    val POSITIVE_ANSWERS = setOf("YES", "Y")
  }

  fun getAssessmentsForOffender(
    identityType: String?,
    identity: String?,
    filterGroupStatus: String?,
    filterAssessmentType: String?,
    filterVoided: Boolean?,
    filterAssessmentStatus: String?
  ): Collection<AssessmentSummaryDto> {
    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
    val assessments = assessmentRepository.getAssessmentsForOffender(
      offenderId,
      filterGroupStatus,
      filterAssessmentType,
      filterVoided,
      filterAssessmentStatus
    )
    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
    return assessments.toAssessmentsSummaryDto()
  }

  fun getLatestAssessmentsForOffenderInPeriod(
    identityType: String?,
    identity: String?,
    filterAssessmentType: Set<String>?,
    filterAssessmentStatus: String?,
    period: PeriodUnit,
    periodUnits: Long
  ): AssessmentSummaryDto {
    val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)

    val assessment = assessmentRepository.getLatestAssessmentsForOffenderInPeriod(
      offenderId,
      filterAssessmentType,
      filterAssessmentStatus,
      LocalDateTime.now().minus(periodUnits, period.chronoUnit)
    )
      ?: throw EntityNotFoundException("Latest $filterAssessmentStatus with types $filterAssessmentType type not found for $identityType, $identity")
    log.info("Found ${assessment?.oasysSetPk} Assessment for identity: ($identity, $identityType)")
    return assessment.toAssessmentSummaryDto()
  }

  fun getAssessment(oasysSetId: Long?): AssessmentDto {
    if (oasysSetId == null) throw IllegalArgumentException("OasysSetPK cannot be null")
    val assessment = assessmentRepository.getAssessment(oasysSetId)
      ?: throw EntityNotFoundException("Assessment for OasysSetId $oasysSetId, not found")
    log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for oasysSetId: $oasysSetId")
    return AssessmentDto.from(assessment)
  }

  private fun calculateNeeds(assessmentType: String?, oasysSetId: Long?): Collection<CriminogenicNeed> {
    if (LAYER_3 != assessmentType) {
      return emptySet()
    }
    val needsSections: Collection<Section> =
      sectionService.getSectionsForAssessment(oasysSetId, CriminogenicNeedMapping.getNeedsSectionHeadings())
    return needsSections.map { checkRiskAndThresholdLevels(it) }
  }

  private fun checkRiskAndThresholdLevels(section: Section?): CriminogenicNeed {

    val sectionCode = section?.refSection?.refSectionCode
    val shortDescription = section?.refSection?.sectionType?.refElementShortDesc
    val sectionName = SectionHeader.findByValue(sectionCode)
    val needConfig = CriminogenicNeedMapping.needs()[sectionName]

    val answers = section?.getRefAnswersValues(needConfig?.allQuestionCodes() ?: emptySet())

    val riskHarm = isPositiveAnswer(answers?.get(needConfig?.harmQuestion)?.getOrNull(0))
    val riskReoffending = isPositiveAnswer(answers?.get(needConfig?.reoffendingQuestion)?.getOrNull(0))
    val overThreshold = sectionIsOverThreshold(section)
    val flaggedAsNeed = isPositiveAnswer(section?.lowScoreNeedAttnInd)
    val severity = calculateNeedSeverity(section, needConfig)

    return CriminogenicNeed(
      sectionName,
      shortDescription,
      riskHarm,
      riskReoffending,
      overThreshold,
      flaggedAsNeed,
      severity
    )
  }

  private fun calculateNeedSeverity(section: Section?, needConfig: NeedConfiguration?): NeedSeverity {
    val answers = section?.getRefAnswersScores(needConfig?.allQuestionCodes() ?: emptySet())

    val sumOfAnswers = answers?.filter { needConfig?.severityQuestions?.contains(it.key) == true }
      ?.mapNotNull { it.value?.getOrElse(0) { 0 } }?.sum() ?: 0

    return when {
      sumOfAnswers >= needConfig?.severeSeverityThreshold ?: 0 -> NeedSeverity.SEVERE
      sumOfAnswers >= needConfig?.standardSeverityThreshold ?: 0 -> NeedSeverity.STANDARD
      else -> NeedSeverity.NO_NEED
    }
  }

  private fun isPositiveAnswer(answer: String?): Boolean {
    return POSITIVE_ANSWERS.contains(answer?.toUpperCase())
  }

  private fun sectionIsOverThreshold(section: Section?): Boolean {
    val rawScore: Long = section?.sectOtherRawScore ?: 0L
    val threshold: Long = section?.refSection?.crimNeedScoreThreshold ?: Long.MAX_VALUE
    return rawScore >= threshold
  }

  fun getAssessmentNeeds(oasysSetId: Long?): Collection<AssessmentNeedDto> {
    if (oasysSetId == null) throw IllegalArgumentException("OasysSetPK cannot be null")
    val assessment = assessmentRepository.getAssessment(oasysSetId)
      ?: throw EntityNotFoundException("Assessment for OasysSetId $oasysSetId, not found")
    // return all needs even when risk is not flagged
    return AssessmentNeedDto.from(calculateNeeds(assessment.assessmentType, oasysSetId))
  }
}
