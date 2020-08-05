package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.*
import java.time.LocalDateTime
import java.util.*

object ApiTestContext {

     fun setupLayer3AssessmentWithFullSentencePlan(id: Long?): Assessment? {
        val sections: MutableSet<Section> = HashSet()
        sections.addAll(setupLayer3AssessmentSections())
        sections.add(getSentencePlanSection())
        return Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                group = AssessmentGroup(),
                oasysSections = sections,
                sspObjectivesInSets = getObjectivesInSet(),
                assessmentStatus = "COMPLETED",
                oasysSetPk = id
        )
    }

    fun setupLayer3AssessmentSections(): Collection<Section> {
        val section10 = Section(
                refSection = RefSection(crimNeedScoreThreshold = (5L),
                        refSectionCode = ("10"),
                        scoredForOgp = ("Y"),
                        scoredForOvp = ("Y"),
                        sectionType = RefElement(
                                refElementCode = ("10"),
                                refElementShortDesc = "Emotional Wellbeing"
                        )),
                sectOvpRawScore = (5L),
                sectOgpRawScore = (5L),
                lowScoreNeedAttnInd = ("YES"),
                sectOtherRawScore = (10L),
                oasysQuestions = setupOASysQuestions()
        )
        return setOf(section10)
    }


    fun setupOASysQuestions(): MutableSet<OasysQuestion>? {
        val question1098 = OasysQuestion(
                oasysQuestionPk = (1L),
                displayScore = (1L),
                refQuestion = ( RefQuestion (
                        refQuestionUk = (1L),
                        refSectionQuestion = ("Question 10.98"),
                        refQuestionCode = ("10.98"),
                        displaySort = (1L))
                        ),
                oasysAnswer = OasysAnswer(
                        refAnswer = (RefAnswer(
                                refAnswerCode = ("YES"),
                                defaultDisplayScore = (1L),
                                displaySort = (1L),
                                refSectionAnswer = ("Yes"))
                                )
                )
        )
        val question1099 = OasysQuestion(
                oasysQuestionPk = 2L,
                displayScore = 2L,
                refQuestion = (RefQuestion(
                        refQuestionUk = (2L),
                        refSectionQuestion = ("Question 10.99"),
                        refQuestionCode = ("10.99"),
                        displaySort = (2L))
                        ),
                oasysAnswer = OasysAnswer(
                        refAnswer = ( RefAnswer(
                                refAnswerCode = ("NO"),
                                defaultDisplayScore = (2L),
                                displaySort = (2L),
                                ogpScore = (1L),
                                ovpScore = (2L),
                                qaRawScore = (3L),
                                refSectionAnswer = ("No"))
                                )
                )
        )
        val questionIP2 = OasysQuestion(
                freeFormatAnswer = ("Free form answer"),
                additionalNote = ("Additional note"),
                refQuestion = (RefQuestion(
                        displaySort = (3L),
                        refQuestionCode = ("IP.1"))
                        )
        )
        return mutableSetOf(question1098, question1099, questionIP2)
    }



    fun getSentencePlanSection(): Section {
        val questionIP1: OasysQuestion = OasysQuestion
                .refQuestion(RefQuestion
                        .displaySort(1L)
                        .refQuestionCode("IP.1")
                )

        val questionIP2: OasysQuestion = OasysQuestion.freeFormatAnswer("Free form answer")
                .additionalNote("Additional note")
                .refQuestion(RefQuestion
                        .displaySort(2L)
                        .refQuestionCode("IP.2")
                )


        val answerIP1: OasysAnswer = OasysAnswer.refAnswer(RefAnswer.refAnswerCode("YES").refSectionAnswer("Yes"))

        questionIP1.setOasysAnswer(answerIP1)
        answerIP1.setOasysQuestion(questionIP1)

        return Section
                .refSection(RefSection
                        .refSectionCode("ISP")
                        .refQuestions(List.of(RefQuestion
                                .refQuestionUk(1L)
                                .refQuestionCode("IP.40")
                                .displaySort(1L)
                                .refSectionQuestion("Ref Question Text")))
                        .sectionType(uk.gov.justice.digital.oasys.api.ApiTestContext.refElementFrom("ISP", "Initial Sentence Plan", "Initial Sentence Plan")))
                .oasysQuestions(Set.of(questionIP1, questionIP2))
    }


    fun getObjectivesInSet(): Set<SspObjectivesInSet>? {
        val objective1: SspObjectivesInSet = SspObjectivesInSet.builder().objectiveType(ApiTestContext.refElementFrom("CURRENT",
                "Current", null))
                .sspObjectiveMeasure(SspObjectiveMeasure.builder().objectiveStatus(
                        ApiTestContext.refElementFrom("R", "Ongoing", null))
                        .objectiveStatusComments("Status Comments")
                        .sspObjectiveMeasurePk(1L).build())
                .howProgressMeasured("Progress measured")
                .sspObjIntervenePivots(ApiTestContext.getInterventions(1L))
                .sspObjective(SspObjective.builder().objectiveDesc("Objective 1 description")
                        .sspObjectivePk(1L)
                        .objective(ApiTestContext.getObjective("100", "Objective 1", "Objective 1 Heading")).build())
                .sspObjectivesInSetPk(1L)
                .createDate(LocalDateTime.of(2019, 12, 28, 9, 0))
                .sspCrimNeedObjPivots(ApiTestContext.getNeeds()).build()
        val objective2: SspObjectivesInSet = SspObjectivesInSet.builder().objectiveType(ApiTestContext.refElementFrom("CURRENT",
                "Current", null))
                .sspObjectiveMeasure(SspObjectiveMeasure.builder().objectiveStatus(
                        ApiTestContext.refElementFrom("R", "Ongoing", null))
                        .objectiveStatusComments("Status Comments")
                        .sspObjectiveMeasurePk(2L).build())
                .howProgressMeasured("Progress measured")
                .sspObjIntervenePivots(ApiTestContext.getInterventions(2L))
                .sspObjective(SspObjective.builder().objectiveDesc("Objective 2 description")
                        .sspObjectivePk(2L)
                        .objective(ApiTestContext.getObjective("200", "Objective 2", "Objective 2 Heading")).build())
                .sspObjIntervenePivots(ApiTestContext.getInterventions(2L))
                .createDate(LocalDateTime.of(2019, 11, 28, 9, 0))
                .sspObjectivesInSetPk(2L).build()
        return java.util.Set.of(objective1, objective2)
    }

}