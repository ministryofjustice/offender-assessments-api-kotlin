package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.BasicSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanSummaryDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException

@Service
class SentencePlanService(
  private val offenderService: OffenderService,
  private val assessmentRepository: AssessmentRepository,
  private val sectionService: SectionService
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @Deprecated("Latest is now deprecated.")
  fun getLatestBasicSentencePlanForOffender(identityType: String?, identity: String?, filterGroupStatus: String? = null, filterAssessmentType: String? = null, filterVoided: Boolean? = null, filterAssessmentStatus: String? = null): BasicSentencePlanDto {
    val offenderId = getOffenderIdByIdentifier(identityType, identity)
    val assessment = assessmentRepository.getLatestAssessmentForOffender(offenderId, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
      ?: throw EntityNotFoundException("Latest assessment for Offender $offenderId, not found")

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

  fun getFullSentencePlansForOffender(identityType: String?, identity: String?, filterGroupStatus: String?, filterAssessmentType: String?, filterVoided: Boolean?, filterAssessmentStatus: String?): Collection<FullSentencePlanDto?>? {
    val offenderId = getOffenderIdByIdentifier(identityType, identity)
    val assessments = assessmentRepository.getAssessmentsForOffender(offenderId, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
    return fullSentencePlansFrom(assessments)
  }

  fun getFullSentencePlanSummariesForOffender(identityType: String?, identity: String?, filterGroupStatus: String?, filterAssessmentType: String?, filterVoided: Boolean?, filterAssessmentStatus: String?): Collection<FullSentencePlanSummaryDto> {
    val offenderId = getOffenderIdByIdentifier(identityType, identity)
    val assessments = assessmentRepository.getAssessmentsForOffender(offenderId, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
    return fullSentencePlanSummariesFrom(assessments)
  }

  fun getFullSentencePlan(oasysSetPk: Long?): FullSentencePlanDto? {
    val assessment = assessmentRepository.getAssessment(oasysSetPk)
      ?: throw EntityNotFoundException("Full Sentence Plan $oasysSetPk, not found!")
    log.info("Found Latest Assessment: ${assessment.assessmentType} for OASYS id $oasysSetPk")
    return fullSentencePlanFrom(assessment)
  }

  private fun getOffenderIdByIdentifier(identityType: String?, identity: String?): Long? {
    return offenderService.getOffenderIdByIdentifier(identityType, identity)
  }

  private fun fullSentencePlansFrom(assessments: Collection<Assessment>?): Set<FullSentencePlanDto?>? {

    val list = assessments?.mapNotNull { assessment ->
      fullSentencePlanFrom(assessment)
    }
    return list?.toSet().orEmpty()
  }

  private fun fullSentencePlanFrom(assessment: Assessment): FullSentencePlanDto? {
    val section = getFullSentencePlanSection(assessment)
    return FullSentencePlanDto.from(assessment, section)
  }

  private fun fullSentencePlanSummariesFrom(assessments: Collection<Assessment?>?): Set<FullSentencePlanSummaryDto> {
    return assessments?.filterNotNull()?.mapNotNull { assessment ->
      val section = getFullSentencePlanSection(assessment)
      FullSentencePlanSummaryDto.from(assessment, section)
    }?.toSet().orEmpty()
  }

  private fun getFullSentencePlanSection(assessment: Assessment): Section? {
    return sectionService.getSectionsForAssessment(assessment.oasysSetPk, setOf(SectionHeader.INITIAL_SENTENCE_PLAN, SectionHeader.REVIEW_SENTENCE_PLAN)).firstOrNull()
  }
}
