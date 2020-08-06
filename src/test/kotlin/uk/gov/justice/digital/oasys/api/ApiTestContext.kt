package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.*
import java.time.LocalDateTime

object ApiTestContext {

     fun setupLayer3AssessmentWithFullSentencePlan(id: Long?): Assessment? {
        val sections: MutableSet<Section> = HashSet()
        sections.addAll(setupLayer3AssessmentSections())
        sections.add(setupSentencePlanSection())
        return Assessment(
                createDate = LocalDateTime.now().minusDays(1),
                assessmentType = "LAYER_3",
                group = AssessmentGroup(),
                oasysSections = sections,
                sspObjectivesInSets = setupObjectivesInSet(),
                assessmentStatus = "COMPLETED",
                oasysSetPk = id)
    }

    private fun setupLayer3AssessmentSections(): Collection<Section> {
        val section10 = Section(
                refSection = RefSection(crimNeedScoreThreshold = (5L),
                        refSectionCode = ("10"),
                        scoredForOgp = ("Y"),
                        scoredForOvp = ("Y"),
                        sectionType = RefElement(
                                refElementCode = ("10"),
                                refElementShortDesc = "Emotional Wellbeing")),
                sectOvpRawScore = (5L),
                sectOgpRawScore = (5L),
                lowScoreNeedAttnInd = ("YES"),
                sectOtherRawScore = (10L),
                oasysQuestions = setupOASysQuestions())
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
                                refSectionAnswer = ("Yes")))))
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
                                refSectionAnswer = ("No")))))
        val questionIP2 = OasysQuestion(
                oasysQuestionPk = 3L,
                freeFormatAnswer = ("Free form answer"),
                additionalNote = ("Additional note"),
                refQuestion = (RefQuestion(
                        displaySort = (3L),
                        refQuestionCode = ("IP.1"))))
        return mutableSetOf(question1098, question1099, questionIP2)
    }



    fun setupSentencePlanSection(): Section {
        val questionIP1 = OasysQuestion(
                oasysQuestionPk = 1L,
                refQuestion = RefQuestion(
                        displaySort = 1L,
                        refQuestionCode = "IP.1"))

        val questionIP2 = OasysQuestion(
                oasysQuestionPk = 2L,
                freeFormatAnswer = ("Free form answer"),
                additionalNote = ("Additional note"),
                refQuestion = (RefQuestion(
                        displaySort = (2L),
                        refQuestionCode = ("IP.2"))))

        val answerIP1 = OasysAnswer(
                refAnswer = (RefAnswer(
                        refAnswerCode = ("YES"),
                        refSectionAnswer = ("Yes"))))

        questionIP1.oasysAnswer = answerIP1
        answerIP1.oasysQuestion = questionIP1
        return Section(
                refSection = RefSection(
                         refSectionCode = "ISP",
                         refQuestions = listOf(RefQuestion(
                                refQuestionUk = 1L,
                                refQuestionCode = "IP.40",
                                displaySort = 1L,
                        refSectionQuestion = "Ref Question Text")),
                        sectionType = (setupRefElementFrom("ISP", "Initial Sentence Plan", "Initial Sentence Plan"))),
                oasysQuestions = (setOf(questionIP1, questionIP2)))
    }


    fun setupObjectivesInSet(): Set<SspObjectivesInSet> {
        val objective1 = SspObjectivesInSet(
                objectiveType = setupRefElementFrom("CURRENT", "Current", null),
                sspObjectiveMeasure = SspObjectiveMeasure(
                        objectiveStatus = setupRefElementFrom("R", "Ongoing", null),
                        objectiveStatusComments = "Status Comments",
                        sspObjectiveMeasurePk = 1L),
                howProgressMeasured = "Progress measured",
                sspObjIntervenePivots = setupInterventions(1L),
                sspObjective = SspObjective(
                        objectiveDesc = "Objective 1 description",
                        sspObjectivePk = 1L,
                        objective = setupObjective("100", "Objective 1", "Objective 1 Heading")),
                sspObjectivesInSetPk = 1L,
                sspCrimNeedObjPivots = setupNeeds())

        val objective2 = SspObjectivesInSet(objectiveType = setupRefElementFrom("CURRENT","Current", null),
                sspObjectiveMeasure = SspObjectiveMeasure(
                        objectiveStatus = setupRefElementFrom("R", "Ongoing", null),
                        objectiveStatusComments = "Status Comments",
                        sspObjectiveMeasurePk = 2L),
                howProgressMeasured = "Progress measured",
                sspObjIntervenePivots = setupInterventions(2L),
                sspObjective = (SspObjective(
                        objectiveDesc = "Objective 2 description",
                        sspObjectivePk = 2L,
                        objective = setupObjective("200", "Objective 2", "Objective 2 Heading"))),
                sspObjectivesInSetPk = 2L)
        return setOf(objective1, objective2)
    }
    
    
    private fun setupObjective(code: String?, description: String?, heading: String?): Objective? {
        return Objective(
                objectiveCode = code,
                objectiveDesc = description,
                objectiveHeading = (setupRefElementFrom(code, heading, heading)))
    }

    private fun setupRefElementFrom(code: String?, description: String?, shortDescription: String?): RefElement? {
        return RefElement(
                refElementCode = code,
                refElementDesc = description,
                refElementShortDesc = shortDescription)
    }

    private fun setupInterventions(objectiveInSetPK: Long): Set<SspObjIntervenePivot>? {
        val intervention1 = SspObjIntervenePivot(
                sspObjectivesInSetPk = objectiveInSetPK,
                sspObjIntervenePivotPk = 1L,
                sspInterventionInSet = setupInterventionInSetFrom(1L, "V2", "Intervention 1", "Inv 2"))
        val intervention2 = SspObjIntervenePivot(
                sspObjectivesInSetPk = (objectiveInSetPK),
                sspObjIntervenePivotPk = (2L),
                sspInterventionInSet = (setupInterventionInSetFrom(2L, "V2", "Intervention 2", "Inv 2")))
        return setOf(intervention1, intervention2)
    }

    private fun setupInterventionInSetFrom(sspInterventionInSetPk: Long, code: String?, description: String?, shortDescription: String?): SspInterventionInSet {

        return SspInterventionInSet(
                copiedForwardIndicator = "Y",
                sspInterventionInSetPk = sspInterventionInSetPk,
                interventionComment = "Intervention Comment",
                intervention = setupRefElementFrom(code, description, shortDescription),
                timescaleForIntervention = RefElement(refElementCode = ("ONE_MONTH"), refElementDesc = ("One Month")),
                sspInterventionMeasure = SspInterventionMeasure(),
                sspWhoDoWorkPivot = setOf(SspWhoDoWorkPivot(
                        sspWhoDoWorkPivotPk = 1L,
                        comments = "Who do work comment",
                        whoDoWork = setupRefElementFrom("IX1", "Offender", null))
                ))

    }
    private fun setupNeeds(): Set<SspCrimNeedObjPivot>? {
        return emptySet()
    }
}