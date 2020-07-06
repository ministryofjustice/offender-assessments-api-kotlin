package uk.gov.justice.digital.oasys.services.domain

enum class SectionHeader(private val value: String) {
    OFFENDING_INFO("1"),
    ANALYSIS_OF_OFFENCES("2"),
    ACCOMMODATION("3"),
    EDUCATION_TRAINING_AND_EMPLOYABILITY("4"),
    FINANCIAL_MANAGEMENT_AND_INCOME("5"),
    RELATIONSHIPS("6"),
    LIFESTYLE_AND_ASSOCIATES("7"),
    DRUG_MISUSE("8"),
    ALCOHOL_MISUSE("9"),
    EMOTIONAL_WELL_BEING("10"),
    THINKING_AND_BEHAVIOUR("11"),
    ATTITUDES("12"),
    HEATH_AND_OTHER_CONSIDERATIONS("13");

    companion object {
        fun findByValue(value: String?): SectionHeader? {
            for (section in values()) {
                if (section.value == value) {
                    return section
                }
            }
            return null
        }
    }

}