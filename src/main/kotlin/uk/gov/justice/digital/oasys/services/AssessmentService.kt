package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentNeedDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.*
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException


@Service
@Transactional(readOnly = true)
class AssessmentService constructor(
        private val assessmentRepository: AssessmentRepository,
        private val offenderService: OffenderService,
        private val sectionService: SectionService,
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
                throw EntityNotFoundException("Assessment for OasysSetId ${oasysSetId}, not found")
        log.info("Found Assessment type: ${assessment.assessmentType} status: ${assessment.assessmentStatus} for oasysSetId: $oasysSetId")
        return populateAssessmentDto(assessment)
    }

    private fun populateAssessmentDto(assessment: Assessment?): AssessmentDto {
        val childSafeguardingIndicated = calculateChildSafeguardingIndicated(assessment?.oasysSetPk)
        //only return needs where a risk has been flagged when returning full assessment
        val needs= calculateNeeds(assessment?.assessmentType, assessment?.oasysSetPk).filter(CriminogenicNeed::anyRiskFlagged)
        return AssessmentDto.from(assessment, childSafeguardingIndicated, needs)
    }

    private fun calculateChildSafeguardingIndicated(oasysSetId: Long?) : Boolean? {
        val roshSection = sectionService.getSectionForAssessment(oasysSetId, ROSH_SECTION)
        val answers = roshSection?.getRefAnswers(setOf("R2.1", "R2.2"))
        if (answers.isNullOrEmpty()) {
            return null
        }
        return anySingleAnswersArePositive(answers.values)
    }

    private fun calculateNeeds(assessmentType: String?, oasysSetId: Long?): Collection<CriminogenicNeed> {
        if (LAYER_3 != assessmentType) {
            return emptySet()
        }
        val needsSections: Collection<Section> = sectionService.getSectionsForAssessment(oasysSetId, CriminogenicNeedMapping.getNeedsSectionHeadings())
        return needsSections.map { checkRiskAndThresholdLevels(it) }

    }

    private fun checkRiskAndThresholdLevels(section: Section?): CriminogenicNeed {

        val sectionCode = section?.refSection?.refSectionCode
        val shortDescription = section?.refSection?.sectionType?.refElementShortDesc
        val sectionName = SectionHeader.findByValue(sectionCode)
        val needConfig = CriminogenicNeedMapping.needs()[sectionName]


        val answers = section?.getRefAnswers(needConfig?.allQuestionCodes() ?: emptySet())

        val riskHarm = isPositiveAnswer(answers?.get(needConfig?.harmQuestion)?.getOrNull(0))
        val riskReoffending = isPositiveAnswer(answers?.get(needConfig?.reoffendingQuestion)?.getOrNull(0))
        val overThreshold = sectionIsOverThreshold(section)
        val flaggedAsNeed = isPositiveAnswer(section?.lowScoreNeedAttnInd)
        val severity = calculateNeedSeverity(answers, needConfig)


        return CriminogenicNeed(sectionName, shortDescription, riskHarm, riskReoffending, overThreshold, flaggedAsNeed, severity)
    }

    private fun calculateNeedSeverity(answers: Map<String?, List<String>?>?, needConfig: NeedConfiguration?): NeedSeverity {
        return NeedSeverity.NO_NEED
    }


    private fun isPositiveAnswer(answer: String?): Boolean {
        return POSITIVE_ANSWERS.contains(answer?.toUpperCase())
    }

    private fun sectionIsOverThreshold(section: Section?): Boolean {
        val rawScore: Long = section?.sectOtherRawScore ?: 0L
        val threshold: Long = section?.refSection?.crimNeedScoreThreshold ?: Long.MAX_VALUE
        return rawScore >= threshold
    }

    private fun anySingleAnswersArePositive(answers: Collection<List<String>?>?): Boolean {
        return POSITIVE_ANSWERS.any{ answers?.map { a -> a?.getOrNull(0) }?.contains(it)   ?: false }
    }

    fun getAssessmentNeeds(oasysSetId: Long?): Collection<AssessmentNeedDto> {
        if(oasysSetId == null) throw IllegalArgumentException("OasysSetPK cannot be null")
        val assessment = assessmentRepository.getAssessment(oasysSetId) ?:
        throw EntityNotFoundException("Assessment for OasysSetId ${oasysSetId}, not found")
        //return all needs even when risk is not flagged
        return AssessmentNeedDto.from(calculateNeeds(assessment.assessmentType, oasysSetId))
    }

}