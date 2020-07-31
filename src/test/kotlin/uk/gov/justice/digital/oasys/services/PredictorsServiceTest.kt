package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.*
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Predictor Service Tests")
class PredictorsServiceTest {

    private val offenderService = mockk<OffenderService>()
    private val assessmentRepository: AssessmentRepository = mockk()
    private val predictorsService: PredictorsService = PredictorsService(offenderService, assessmentRepository)
    private val version = setupVersion()

    @Test
    fun `should get all Predictors For Offender`() {
        val assessment = setupAssessment()

        every { offenderService.getOffenderIdByIdentifier("OASYS", "1")} returns (1L)
        every { assessmentRepository.getAssessmentsForOffender(1L)} returns(setOf(assessment))

        val predictors = predictorsService.getAllPredictorsForOffender("OASYS", "1")

        verify(exactly = 1) { assessmentRepository.getAssessmentsForOffender(1L) }
        verify(exactly = 1) { offenderService.getOffenderIdByIdentifier("OASYS", "1") }

        val validPredictor = setUpValidPredictor(assessment)
        assertThat(predictors?.first()).isEqualTo(validPredictor)
    }

    private fun setupVersion(): RefAssessmentVersion {
        return RefAssessmentVersion( refAssVersionUk = 1L,
                versionNumber = "Any Version",
                refAssVersionCode  = "Any Ref Version Code")
    }

    private fun setupAssessment(): Assessment {
        val created = LocalDateTime.now()
        val completed = created.plusMonths(3)
        val voided = created.plusMonths(4)

        return Assessment( oasysSetPk = 1234L,
                assessmentStatus = "STATUS",
                assessmentType = "LAYER_3",
                createDate = created,
                dateCompleted = completed,
                assessmentVoidedDate = voided,
                assessmentVersion = version,
                otherRiskRecon = RefElement( refElementDesc="Low", refElementCode = "L"),

                ogpStWesc = BigDecimal.valueOf(1),
                ogpDyWesc = BigDecimal.valueOf(2),
                ogpTotWesc = BigDecimal.valueOf(3),
                ogp1Year = BigDecimal.valueOf(4),
                ogp2Year = BigDecimal.valueOf(5),
                ogpRiskRecon = RefElement( refElementDesc="Low", refElementCode = "L"),

                ovp1Year = BigDecimal.valueOf(1),
                ovp2Year = BigDecimal.valueOf(2),
                ovpAgeWesc = BigDecimal.valueOf(3),
                ovpDyWesc = BigDecimal.valueOf(4),
                ovpNonVioWesc = BigDecimal.valueOf(5),
                ovpPrevWesc = BigDecimal.valueOf(6),
                ovpSexWesc = BigDecimal.valueOf(7),
                ovpStWesc = BigDecimal.valueOf(8),
                ovpTotWesc = BigDecimal.valueOf(9),
                ovpVioWesc = BigDecimal.valueOf(10),
                ovpRiskRecon = RefElement( refElementDesc="Low", refElementCode = "L"),

                ogrs31Year = BigDecimal.valueOf(4),
                ogrs32Year = BigDecimal.valueOf(5),
                ogrs3RiskRecon = RefElement( refElementDesc="Low", refElementCode = "L"))
    }

    private fun setUpValidPredictor(assessment:Assessment): PredictorDto {

        //OGP
        val ogp = OgpDto(ogpStaticWeightedScore = assessment.ogpStWesc,
                ogpDynamicWeightedScore = assessment.ogpDyWesc,
                ogpTotalWeightedScore = assessment.ogpTotWesc,
                ogp1Year = assessment.ogp1Year,
                ogp2Year = assessment.ogp2Year,
                ogpRisk  = RefElementDto(code = "L", description = assessment.ogpRiskRecon?.refElementDesc))

        val ovp = OvpDto(ovpStaticWeightedScore = assessment.ovpStWesc,
                ovpDynamicWeightedScore = assessment.ovpDyWesc,
                ovpTotalWeightedScore = assessment.ovpTotWesc,
                ovp1Year = assessment.ovp1Year,
                ovp2Year = assessment.ovp2Year,
                ovpRisk = RefElementDto (code = "L", description = assessment.ovpRiskRecon?.refElementDesc),
                ovpPreviousWeightedScore = assessment.ovpPrevWesc,
                ovpViolentWeightedScore = assessment.ovpVioWesc,
                ovpNonViolentWeightedScore = assessment.ovpNonVioWesc,
                ovpAgeWeightedScore = assessment.ovpAgeWesc,
                ovpSexWeightedScore = assessment.ovpSexWesc )

        val ogrs3 = Ogrs3Dto(ogrs3_1Year = assessment.ogrs31Year,
                ogrs3_2Year = assessment.ogrs32Year,
                reconvictionRisk = RefElementDto(code = "L", description = assessment.ogrs3RiskRecon?.refElementDesc))

        return PredictorDto(oasysSetId = assessment.oasysSetPk,
                refAssessmentVersionCode = version.refAssVersionCode,
                refAssessmentVersionNumber = version.versionNumber,
                refAssessmentId = version.refAssVersionUk,
                completedDate = assessment.dateCompleted,
                voidedDateTime = assessment.assessmentVoidedDate,
                assessmentCompleted = true,
                otherRisk = RefElementDto(code="L", shortDescription=null, description="Low"),
                ogp = ogp,
                ovp = ovp,
                ogr3 = ogrs3)
    }
}