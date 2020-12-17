package uk.gov.justice.digital.oasys.services.domain

class NeedConfiguration (
            val harmQuestion: String,
            val reoffendingQuestion: String,
            val standardSeverityThreshold: Int,
            val severeSeverityThreshold: Int,
            val severityQuestions: Set<String>
)
{
    fun allQuestionCodes() : Set<String> {
        return setOf(harmQuestion, reoffendingQuestion).plus(severityQuestions)
    }
}