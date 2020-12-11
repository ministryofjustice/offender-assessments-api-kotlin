package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.jpa.entities.*
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import uk.gov.justice.digital.oasys.services.domain.NeedSeverity
import uk.gov.justice.digital.oasys.services.domain.SectionHeader
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Assessment Service Tests")
class AssessmentServiceNeedsSeverityTest {
    private val oasysSetPk = 1234L
    private val assessmentRepository: AssessmentRepository = mockk()
    private val offenderService: OffenderService = mockk()
    private val sectionService: SectionService = mockk()
    private val assessmentsService = AssessmentService(assessmentRepository, offenderService, sectionService)

    @Test
    fun `needs should return all needs where section exists`() {
        val assessment = setupAssessment("LAYER_3")
        val oasysSetPk = 1234L
        val accommodationRefSection = RefSection(
                refSectionCode = "3",
                crimNeedScoreThreshold = 10L,
                sectionType = RefElement(refElementShortDesc = "Accommodation"))
        val accommodationSection = Section(
                oasysSectionPk = 1L,
                oasysQuestions = emptySet(),
                sectOtherRawScore = 20L,
                refSection = accommodationRefSection)

        //no indicator, under threshold
        val relationshipsRefSection = RefSection(
                refSectionCode = "6",
                crimNeedScoreThreshold = 10L,
                sectionType = RefElement(refElementShortDesc = "Relationships"))
        val relationshipsSection = Section(
                oasysSectionPk = 3L,
                oasysQuestions = emptySet(),
                sectOtherRawScore = 1L,
                refSection = relationshipsRefSection)

        every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
        every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
        every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(accommodationSection, relationshipsSection)

        val needs = assessmentsService.getAssessmentNeeds(oasysSetPk)
        assertThat(needs).hasSize(2)
    }


    @Test
    fun `should not fail when answers are null`() {
        setupAssessmentWithQuestions(mapOf("3.3" to null, "3.4" to null, "3.5" to null, "3.6" to null), "3", "Accommodation")
        val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
        assertThat(need.section).isEqualTo(SectionHeader.ACCOMMODATION)
        assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
    }

    @Nested
    @DisplayName("Accommodation Tests")
    inner class AccommodationTests {
        @Test
        fun ` accommodation should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("3.3" to 2, "3.4" to 2, "3.5" to 2, "3.6" to 1), "3", "Accommodation")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ACCOMMODATION)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `accommodation should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("3.3" to 0, "3.4" to 2, "3.5" to 1, "3.6" to 1), "3", "Accommodation")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ACCOMMODATION)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `accommodation should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("3.3" to 0, "3.4" to 0, "3.5" to 0, "3.6" to 1), "3", "Accommodation")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ACCOMMODATION)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("ETE Tests")
    inner class ETETests {
        @Test
        fun `ETE should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("4.2" to 2, "4.3" to 2, "4.4" to 2, "4.5" to 1), "4", "ETE")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.EDUCATION_TRAINING_AND_EMPLOYABILITY)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `ETE should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("4.2" to 1, "4.3" to 1, "4.4" to 1, "4.5" to 0), "4", "ETE")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.EDUCATION_TRAINING_AND_EMPLOYABILITY)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `ETE should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("4.2" to 1, "4.3" to 1, "4.4" to 0, "4.5" to 0), "4", "ETE")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.EDUCATION_TRAINING_AND_EMPLOYABILITY)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Financial Tests")
    inner class FinancialTests {
        @Test
        fun `Financial should be NO_NEED`() {
            setupAssessmentWithQuestions(mapOf(), "5", "Financial Management")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.FINANCIAL_MANAGEMENT_AND_INCOME)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Relationships Tests")
    inner class RelationshipTests {
        @Test
        fun `Relationships should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("6.1" to 2, "6.3" to 2, "6.6" to 1), "6", "Relationships")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.RELATIONSHIPS)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Relationships should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("6.1" to 2, "6.3" to 1, "6.6" to 1), "6", "Relationships")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.RELATIONSHIPS)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Relationships should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("6.1" to 1, "6.3" to 0, "6.6" to 0), "6", "Relationships")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.RELATIONSHIPS)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Lifestyle and Associates Tests")
    inner class LifestyleTests {
        @Test
        fun `Lifestyle should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("7.2" to 2, "7.3" to 2, "7.5" to 1), "7", "Lifestyle and Associates")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.LIFESTYLE_AND_ASSOCIATES)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Lifestyle should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("7.2" to 1, "7.3" to 1, "7.5" to 0), "7", "Lifestyle and Associates")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.LIFESTYLE_AND_ASSOCIATES)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Lifestyle should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("7.2" to 1, "7.3" to 0, "7.5" to 0), "7", "Lifestyle and Associates")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.LIFESTYLE_AND_ASSOCIATES)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Drug Misuse Tests")
    inner class DrugMisuseTests {
        @Test
        fun `Drug Misuse should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("8.4" to 1, "8.5" to 1, "8.6" to 0, "8.8" to 0, "8.9" to 0), "8", "Drug Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.DRUG_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Drug Misuse should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("8.4" to 1, "8.5" to 0, "8.6" to 0, "8.8" to 0, "8.9" to 0), "8", "Drug Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.DRUG_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Drug Misuse should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("8.4" to 0, "8.5" to 0, "8.6" to 0, "8.8" to 0, "8.9" to 0), "8", "Drug Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.DRUG_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Alcohol Misuse Tests")
    inner class AlcoholTests {
        @Test
        fun `Alcohol should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("9.1" to 1, "9.2" to 1, "9.3" to 1, "9.5" to 1), "9", "Alcohol Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ALCOHOL_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Alcohol should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("9.1" to 1, "9.2" to 0, "9.3" to 0, "9.5" to 0), "9", "Alcohol Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ALCOHOL_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Alcohol should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("9.1" to 0, "9.2" to 0, "9.3" to 0, "9.5" to 0), "9", "Alcohol Misuse")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ALCOHOL_MISUSE)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Attitudes Tests")
    inner class AttitudesTests {
        @Test
        fun `Attitudes should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("12.1" to 1, "12.4" to 1, "12.5" to 1, "12.8" to 1), "12", "Attitudes")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ATTITUDES)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Attitudes should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("12.1" to 0, "12.4" to 0, "12.5" to 0, "12.8" to 1), "12", "Attitudes")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ATTITUDES)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Attitudes should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("12.1" to 0, "12.4" to 0, "12.5" to 0, "12.8" to 0), "12", "Attitudes")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.ATTITUDES)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Emotional Wellbeing Tests")
    inner class EmotionalTests {
        @Test
        fun `Emotional should be NO_NEED`() {
            setupAssessmentWithQuestions(mapOf(), "10", "Emotional Wellbeing")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.EMOTIONAL_WELL_BEING)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }

    @Nested
    @DisplayName("Thinking and Behaviour Tests")
    inner class ThinkingTests {
        @Test
        fun `Thinking should return SEVERE if over severe threshold`() {
            setupAssessmentWithQuestions(mapOf("11.5" to 2, "11.6" to 2, "11.7" to 2, "11.9" to 1), "11", "Thinking and Behaviour")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.THINKING_AND_BEHAVIOUR)
            assertThat(need.severity).isEqualTo(NeedSeverity.SEVERE)
        }

        @Test
        fun `Thinking should return STANDARD if over standard threshold but under severe`() {
            setupAssessmentWithQuestions(mapOf("11.5" to 1, "11.6" to 1, "11.7" to 1, "11.9" to 1), "11", "Thinking and Behaviour")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.THINKING_AND_BEHAVIOUR)
            assertThat(need.severity).isEqualTo(NeedSeverity.STANDARD)
        }

        @Test
        fun `Thinking should return NO_NEED if under standard threshold `() {
            setupAssessmentWithQuestions(mapOf("11.5" to 1, "11.6" to 1, "11.7" to 1, "11.9" to 0), "11", "Thinking and Behaviour")
            val need = assessmentsService.getAssessmentNeeds(oasysSetPk).toList()[0]
            assertThat(need.section).isEqualTo(SectionHeader.THINKING_AND_BEHAVIOUR)
            assertThat(need.severity).isEqualTo(NeedSeverity.NO_NEED)
        }
    }


    private fun setupAssessmentWithQuestions(questions: Map<String, Int?>, sectionCode: String, sectionName: String) {
        val assessment = setupAssessment("LAYER_3")

        val refSection = RefSection(
                refSectionCode = sectionCode,
                crimNeedScoreThreshold = 10L,
                sectionType = RefElement(refElementShortDesc = sectionName))
        val section = Section(
                oasysSectionPk = 1L,
                oasysQuestions = setupQuestions(questions),
                sectOtherRawScore = 20L,
                refSection = refSection)


        every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
        every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
        every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)
    }

    private fun setupQuestions(questions: Map<String, Int?> ) : Set<OasysQuestion> {

        return questions.entries.mapIndexed{ index, question -> OasysQuestion(
                oasysQuestionPk = index.toLong(),
                refQuestion = RefQuestion(
                        refQuestionCode = question.key),
                oasysAnswers = mutableSetOf(OasysAnswer(
                        refAnswer = RefAnswer(defaultDisplayScore = question.value)))) }
                .toSet()
    }

    private fun setupAssessmentGroup() : AssessmentGroup {
       return AssessmentGroup(historicStatus = "Current")
    }

    private fun setupVersion(): RefAssessmentVersion {
        return RefAssessmentVersion(refAssVersionUk = 1L, versionNumber = "Any Version", refAssVersionCode = "Any Ref Version Code", oasysScoringAlgVersion = 2L)
    }

    private fun setupAssessment(assessmentType: String): Assessment {
        val created = LocalDateTime.now()
        val completed = created.plusMonths(3)
        val voided = created.plusMonths(4)

        return Assessment(oasysSetPk = 1234L,
                assessorName = "Any Name",
                assessmentStatus = "STATUS",
                assessmentType = assessmentType,
                createDate = created,
                dateCompleted = completed,
                assessmentVoidedDate = voided,
                assessmentVersion = setupVersion(),
                oasysSections = emptySet(),
                oasysBcsParts = emptySet(),
                group = setupAssessmentGroup()
        )
    }

}