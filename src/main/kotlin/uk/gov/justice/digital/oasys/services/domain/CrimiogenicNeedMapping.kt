package uk.gov.justice.digital.oasys.services.domain

class CrimiogenicNeedMapping {

    companion object {
        private val REOFFENDING_QUESTION = "reoffendingQuestion"
        private val HARM_QUESTION = "harmQuestion"

        fun getNeedsSectionHeadings(): Set<String>? {
            return needsSections().keys.toSet();
        }

        fun getHarmQuestion(section: String?): String? {
            return needsSections()[section]!![HARM_QUESTION]
        }

        fun getReoffendingQuestion(section: String?): String? {
            return needsSections()[section]!![REOFFENDING_QUESTION]
        }

        private fun needsSections(): Map<String, Map<String, String>> {
            return mapOf(

                    "3" to mapOf(
                            HARM_QUESTION to "3.98",
                            REOFFENDING_QUESTION to "3.99"),
                    "4" to mapOf(
                            HARM_QUESTION to "4.96",
                            REOFFENDING_QUESTION to "3.98"),
                    "5" to mapOf(
                            HARM_QUESTION to "5.98",
                            REOFFENDING_QUESTION to "5.99"),
                    "6" to mapOf(
                            HARM_QUESTION to "6.98",
                            REOFFENDING_QUESTION to "6.99"),
                    "7" to mapOf(
                            HARM_QUESTION to "7.98",
                            REOFFENDING_QUESTION to "7.99"),
                    "8" to mapOf(
                            HARM_QUESTION to "8.98",
                            REOFFENDING_QUESTION to "8.99"),
                    "9" to mapOf(
                            HARM_QUESTION to "9.98",
                            REOFFENDING_QUESTION to "9.99"),
                    "10" to mapOf(
                            HARM_QUESTION to "10.98",
                            REOFFENDING_QUESTION to "10.99"),
                    "11" to mapOf(
                            HARM_QUESTION to "11.98",
                            REOFFENDING_QUESTION to "11.99"),
                    "12" to mapOf(
                            HARM_QUESTION to "12.98",
                            REOFFENDING_QUESTION to "12.99"))
        }
    }
}