package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.*
import java.time.LocalDateTime

@DisplayName("Full Sentence Plan DTO Tests")
class FullSentencePlanDtoTest {

    private val date = LocalDateTime.now()

    @Test
    fun `builds Full Sentence Plan DTO from OASys Assessment`() {
        val assessment = setupAssessment()
        val sentencePlanDto = FullSentencePlanDto.from(assessment, Section())
        val validDtoNoQuestions = setupValidFullSentencePlanDto().copy(questions = mutableMapOf())
        assertThat(sentencePlanDto).isEqualTo(validDtoNoQuestions)
    }

    @Test
    fun `Builds Full Sentence Plan from Assessment and Section`() {
        val section = setupSectionWithQuestions()
        val assessment = setupAssessment()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan).isEqualTo(setupValidFullSentencePlanDto())
    }

    @Test
    fun `Builds null Full Sentence Plan DTO from null ISP or RSP Section and no Objectives`() {
        val assessment = setupAssessment().copy(sspObjectivesInSets = emptySet())
        val sentencePlan = FullSentencePlanDto.from(assessment, null)
        assertThat(sentencePlan).isNull()
    }

    @Test
    fun `Builds null Full Sentence Plan DTO from null ISP or RSP Section and null Objectives`() {
        val assessment = setupAssessment().copy(sspObjectivesInSets = null)
        val sentencePlan = FullSentencePlanDto.from(assessment, null)
        assertThat(sentencePlan).isNull()
    }

    @Test
    fun `Builds Full Sentence Plan from Section OasysQuestions and duplicate RefQuestion`() {
        val section = setupSectionWithQuestions().copy(
                refSection = RefSection(
                        refQuestions = listOf(RefQuestion(
                                        displaySort = 1L,
                                        refQuestionCode = "IP.1"),
                                RefQuestion(
                                        displaySort = 1L,
                                        refQuestionCode = "IP.2"))))
        val assessment = setupAssessment()
        val sentencePlan = FullSentencePlanDto.from(assessment, section)
        assertThat(sentencePlan).isEqualTo(setupValidFullSentencePlanDto())
    }

    @Test
    fun `Builds Full Sentence Plan from Section OasysQuestions and empty RefQuestions`() {
        val sectionWithEmptyRefSection = setupSectionWithQuestions().copy(refSection = RefSection())
        val assessment = setupAssessment()
        val sentencePlan = FullSentencePlanDto.from(assessment, sectionWithEmptyRefSection)
        val validDto = setupValidFullSentencePlanDto().copy(
                questions = mutableMapOf(
                        "IP.1" to QuestionDto(
                                refQuestionCode = "IP.1",
                                oasysQuestionId = 1,
                                displayOrder = 1,
                                answers = setOf(AnswerDto(
                                        refAnswerCode = "YES",
                                        staticText = "Yes")))))
        assertThat(sentencePlan).isEqualTo(validDto)
    }

    @Test
    fun `Builds Full Sentence Plan from Section RefQuestions and empty OasysQuestions`() {
        val sectionWithEmptyOasysQuestion = setupSectionWithQuestions().copy(oasysQuestions = emptySet())
        val assessment = setupAssessment()
        val sentencePlan = FullSentencePlanDto.from(assessment, sectionWithEmptyOasysQuestion)
        val validDto = setupValidFullSentencePlanDto().copy(
                questions = mutableMapOf(
                        "IP.2" to QuestionDto(
                                refQuestionCode = "IP.2",
                                displayOrder = 1)))
        assertThat(sentencePlan).isEqualTo(validDto)
    }

    private fun setupSectionWithQuestions(): Section {
        return Section(
                refSection = RefSection(
                        refSectionCode = "ISP",
                        refQuestions = listOf(RefQuestion(
                                displaySort = 1L,
                                refQuestionCode = "IP.2")),
                        sectionType = (RefElement(
                                refElementCode = "ISP",
                                refElementDesc = "Initial Sentence Plan",
                                refElementShortDesc = "Initial Sentence Plan"))),
                oasysQuestions = setupOasysQuestionSet())
    }

    private fun setupOasysQuestionSet():Set<OasysQuestion>{
        return setOf(OasysQuestion(
                oasysQuestionPk = 1L,
                refQuestion = RefQuestion(
                        displaySort = 1L,
                        refQuestionCode = "IP.1"),
                oasysAnswers = mutableSetOf(OasysAnswer(
                        refAnswer = (RefAnswer(
                                refAnswerCode = ("YES"),
                                refSectionAnswer = ("Yes")))))))
    }

    private fun setupAssessment():Assessment{
        return Assessment(
                oasysSetPk = 1L,
                createDate = date,
                dateCompleted = date.plusYears(1),
                sspObjectivesInSets = setOf(SspObjectivesInSet()))
    }

    private fun setupValidFullSentencePlanDto():FullSentencePlanDto{
        return FullSentencePlanDto(oasysSetId = 1, 
                createdDate = date, 
                completedDate = date.plusYears(1),
                objectives = setOf(ObjectiveDto(
                        criminogenicNeeds = emptySet(),
                        interventions = emptySet())),
                questions = mutableMapOf(
                        "IP.1" to QuestionDto(
                            refQuestionCode = "IP.1",
                            oasysQuestionId = 1,
                            displayOrder = 1,
                            answers = setOf(AnswerDto(
                                    refAnswerCode = "YES",
                                    staticText = "Yes"))),
                        "IP.2" to QuestionDto(
                                refQuestionCode = "IP.2",
                                displayOrder = 1)))
    }
}