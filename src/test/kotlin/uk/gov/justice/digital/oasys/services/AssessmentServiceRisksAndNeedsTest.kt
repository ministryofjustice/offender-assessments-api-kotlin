package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.jpa.entities.*
import uk.gov.justice.digital.oasys.jpa.repositories.AssessmentRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Assessment Service Tests")
class AssessmentServiceRisksAndNeedsTest {

    private val assessmentRepository: AssessmentRepository = mockk()
    private val offenderService: OffenderService = mockk()
    private val sectionService: SectionService = mockk()
    private val assessmentsService = AssessmentService(assessmentRepository, offenderService, sectionService)

    @Nested
    @DisplayName("Child safeguarding tests")
    inner class ChildSafeguardingTest {

        @Test
        fun `should not return child safeguarding flag if ROSH section not present`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isNull()
        }

        @Test
        fun `should not return child safeguarding flag if ROSH section has no answers`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val section = Section()

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isNull()
        }

        @Test
        fun `should return child safeguarding flag True if ROSH R2-1 is Positive`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "Y")
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isTrue()
        }

        @Test
        fun `should return child safeguarding flag True if ROSH R2-1 is Positive and R2-2 is null`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "Y", "R2.2" to null)
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isTrue()
        }

        @Test
        fun `should return child safeguarding flag True if ROSH R2-2 is Positive`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "N", "R2.2" to "Y")
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isTrue()
        }

        @Test
        fun `should return child safeguarding flag True if both ROSH R2-1 and R2-2 are Positive`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "Y", "R2.2" to "Y")
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isTrue()
        }

        @Test
        fun `should return child safeguarding flag False if ROSH R2-1 is Negative and R2-2 is null`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "N", "R2.2" to null)
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isFalse()
        }

        @Test
        fun `should return child safeguarding flag False if both ROSH R2-1 and R2-2 are Negative`() {
            val assessment = setupAssessment("LAYER_1")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("R2.1" to "N", "R2.2" to "N")
            val section = Section(oasysQuestions = setupQuestions(questions))

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns section
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.childSafeguardingIndicated).isFalse()
        }
    }

    @Nested
    @DisplayName("Criminogenic Needs tests")
    inner class  CriminogenicNeedsTest {

        @Test
        fun `should return empty Needs when no sections completed`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns emptySet()

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).isEmpty()
        }

        @Test
        fun `should not return needs for non layer 3 assessments`() {
            val assessment = setupAssessment("LAYER_2")
            val oasysSetPk = 1234L

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).isEmpty()
            verify(exactly = 0) { sectionService.getSectionsForAssessment(oasysSetPk, any()) }

        }

        @Test
        fun `should return needs when risk of harm identified`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("3.98" to "Y")
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = setupQuestions(questions),
                    sectOtherRawScore = 0L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.riskOfHarm).isTrue()
            assertThat(needDto?.riskOfReoffending).isFalse()
            assertThat(needDto?.flaggedAsNeed).isFalse()
            assertThat(needDto?.overThreshold).isFalse()
            assertThat(needDto?.name).isEqualTo("Accommodation")
            assertThat(needDto?.section?.name).isEqualTo("ACCOMMODATION")
        }

        @Test
        fun `should return needs when risk of reoffending identified`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val questions: Map<String, String?> = mapOf("3.99" to "Y")
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = setupQuestions(questions),
                    sectOtherRawScore = 0L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.riskOfHarm).isFalse()
            assertThat(needDto?.riskOfReoffending).isTrue()
            assertThat(needDto?.flaggedAsNeed).isFalse()
            assertThat(needDto?.overThreshold).isFalse()
        }

        @Test
        fun `should return needs when flagged as low scoring need`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 0L,
                    refSection = refSection,
                    lowScoreNeedAttnInd = "Y")

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.riskOfHarm).isFalse()
            assertThat(needDto?.riskOfReoffending).isFalse()
            assertThat(needDto?.flaggedAsNeed).isTrue()
            assertThat(needDto?.overThreshold).isFalse()
        }

        @Test
        fun `should not return needs when flagged as low scoring need is N`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 0L,
                    refSection = refSection,
                    lowScoreNeedAttnInd = "N")

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).isEmpty()
        }

        @Test
        fun `should return needs when over threshold`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 20L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.riskOfHarm).isFalse()
            assertThat(needDto?.riskOfReoffending).isFalse()
            assertThat(needDto?.flaggedAsNeed).isFalse()
            assertThat(needDto?.overThreshold).isTrue()
        }

        @Test
        fun `should return needs when over threshold boundary`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 11L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.overThreshold).isTrue()

        }

        @Test
        fun `should return needs when at threshold boundary`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 10L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.overThreshold).isTrue()

        }

        @Test
        fun `should not return needs when below threshold boundary`() {
            val assessment = setupAssessment("LAYER_3")
            val oasysSetPk = 1234L
            val refSection = RefSection(
                    refSectionCode = "3",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Accommodation"))
            val section = Section(
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 9L,
                    refSection = refSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(section)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).isEmpty()
        }

        @Test
        fun `should return multiple needs over threshold`() {
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

            val drugsRefSection = RefSection(
                    refSectionCode = "8",
                    crimNeedScoreThreshold = 10L,
                    sectionType = RefElement(refElementShortDesc = "Drugs Misuse"))
            val drugsSection = Section(
                    oasysSectionPk = 2L,
                    oasysQuestions = emptySet(),
                    sectOtherRawScore = 20L,
                    refSection = drugsRefSection)

            every { assessmentRepository.getAssessment(oasysSetPk) } returns assessment
            every { sectionService.getSectionForAssessment(oasysSetPk, "ROSH") } returns null
            every { sectionService.getSectionsForAssessment(oasysSetPk, any()) } returns setOf(accommodationSection, drugsSection)

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(2)
        }


        @Test
        fun `should not return needs for sections without indicators`() {
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

            val assessmentDto = assessmentsService.getAssessment(oasysSetPk)
            assertThat(assessmentDto?.layer3SentencePlanNeeds).hasSize(1)
            val needDto = assessmentDto?.layer3SentencePlanNeeds?.first()
            assertThat(needDto?.riskOfHarm).isFalse()
            assertThat(needDto?.riskOfReoffending).isFalse()
            assertThat(needDto?.flaggedAsNeed).isFalse()
            assertThat(needDto?.overThreshold).isTrue()
            assertThat(needDto?.name).isEqualTo("Accommodation")
            assertThat(needDto?.section?.name).isEqualTo("ACCOMMODATION")
        }
    }

    private fun setupQuestions(questions: Map<String, String?> ) : Set<OasysQuestion> {

        return questions.entries.mapIndexed{ index, question -> OasysQuestion(
                oasysQuestionPk = index.toLong(),
                refQuestion = RefQuestion(
                        refQuestionCode = question.key),
                oasysAnswer = OasysAnswer(
                        refAnswer = RefAnswer(refAnswerCode = question.value))) }
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