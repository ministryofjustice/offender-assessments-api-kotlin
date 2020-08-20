package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeedMapping
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException


@Service
@Transactional(readOnly = true)
class AssessmentService constructor(
        private val assessmentRepository: AssessmentRepository,
        private val offenderService: OffenderService,
        private val sectionService: SectionService
) {

    private val LAYER_3 = "LAYER_3"
    private val ROSH_SECTION = "ROSH"
    private val POSITIVE_ANSWERS = setOf("YES", "Y")

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getAssessmentsForOffender(identityType : String?, identity: String?, filterGroupStatus: String?, filterAssessmentType: String?, filterVoided: Boolean?, filterAssessmentStatus: String?): Collection<AssessmentSummaryDto> {
        val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
        val assessments = assessmentRepository.getAssessmentsForOffender(offenderId,filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
        log.info("Found ${assessments?.size} Assessments for identity: ($identity, $identityType)")
        return AssessmentSummaryDto.from(assessments)
    }

    fun getLatestAssessmentForOffender(identityType : String?, identity: String?, filterGroupStatus: String?, filterAssessmentType: String?, filterVoided: Boolean?, filterAssessmentStatus: String?): AssessmentDto {
        val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
        val assessment = assessmentRepository.getLatestAssessmentForOffender(offenderId,filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus) ?:
                throw EntityNotFoundException("Assessment for Oasys Offender $offenderId, not found")
        log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for identity: ($identity, $identityType)")
        return populateAssessmentDto(assessment)
    }

    fun getAssessment(oasysSetId: Long?): AssessmentDto {
        if(oasysSetId == null) throw IllegalArgumentException("OasysSetPK cannot be null")
        val assessment = assessmentRepository.getAssessment(oasysSetId) ?:
                throw EntityNotFoundException("Assessment for OasysSetId ${oasysSetId}, not found!")
        log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for oasysSetId: $oasysSetId")
        return populateAssessmentDto(assessment)
    }

    private fun populateAssessmentDto(assessment: Assessment?): AssessmentDto {
        val childSafeguardingIndicated = calculateChildSafeguardingIndicated(assessment?.oasysSetPk)
        val needs= calculateNeeds(assessment?.assessmentType, assessment?.oasysSetPk)
        return AssessmentDto.from(assessment, childSafeguardingIndicated, needs)
    }

    private fun calculateChildSafeguardingIndicated(oasysSetId: Long?) : Boolean? {
        val roshSection = sectionService.getSectionForAssessment(oasysSetId, ROSH_SECTION)
        val answers = roshSection?.getRefAnswers(setOf("R2.1", "R2.2"))
        if (answers.isNullOrEmpty()) {
            return null
        }
        return anyAnswersArePositive(answers.values)
    }

    private fun calculateNeeds(assessmentType: String?, oasysSetId: Long?): Collection<CriminogenicNeed> {
        if (LAYER_3 != assessmentType) {
            return emptySet()
        }
        val needsSections: Collection<Section> = sectionService.getSectionsForAssessment(oasysSetId, CriminogenicNeedMapping.getNeedsSectionHeadings())
        return needsSections.map { checkRiskAndThresholdLevels(it) }.filter(CriminogenicNeed::anyRiskFlagged)

    }

    private fun checkRiskAndThresholdLevels(section: Section?): CriminogenicNeed {

        val sectionCode = section?.refSection?.refSectionCode
        val shortDescription = section?.refSection?.sectionType?.refElementShortDesc
        val sectionName = SectionHeader.findByValue(sectionCode)

        val answers = section?.getRefAnswers(setOf(CriminogenicNeedMapping.getHarmQuestion(sectionCode),
                CriminogenicNeedMapping.getReoffendingQuestion(sectionCode)))

        val riskHarm = isPositiveAnswer(answers?.get(CriminogenicNeedMapping.getHarmQuestion(sectionCode)))
        val riskReoffending = isPositiveAnswer(answers?.get(CriminogenicNeedMapping.getReoffendingQuestion(sectionCode)))
        val overThreshold = sectionIsOverThreshold(section)
        val flaggedAsNeed = isPositiveAnswer(section?.lowScoreNeedAttnInd)

        return CriminogenicNeed(sectionName, shortDescription, riskHarm, riskReoffending, overThreshold, flaggedAsNeed)
    }


    private fun isPositiveAnswer(answer: String?): Boolean {
        return POSITIVE_ANSWERS.contains(answer?.toUpperCase())
    }

    private fun sectionIsOverThreshold(section: Section?): Boolean {
        val rawScore: Long = section?.sectOtherRawScore ?: 0L
        val threshold: Long = section?.refSection?.crimNeedScoreThreshold ?: Long.MAX_VALUE
        return rawScore >= threshold
    }

    private fun anyAnswersArePositive(answers: Collection<String?>?): Boolean {
        return POSITIVE_ANSWERS.any{ answers?.contains(it) ?: false }
    }

}