package uk.gov.justice.digital.oasys.services.domain

class CriminogenicNeedMapping {

  companion object {
    fun needs(): Map<SectionHeader, NeedConfiguration> {
      return mapOf(
        SectionHeader.ACCOMMODATION to NeedConfiguration(
          "3.98",
          "3.99",
          2,
          7,
          setOf("3.3", "3.4", "3.5", "3.6")
        ),
        SectionHeader.EDUCATION_TRAINING_AND_EMPLOYABILITY to NeedConfiguration(
          "4.96",
          "4.98",
          3,
          7,
          setOf("4.2", "4.3", "4.4", "4.5")
        ),
        // Financial is not a proper Criminogenic Need and so does not have a severity score
        SectionHeader.FINANCIAL_MANAGEMENT_AND_INCOME to NeedConfiguration(
          "5.98",
          "5.99",
          999,
          999,
          setOf()
        ),
        SectionHeader.RELATIONSHIPS to NeedConfiguration(
          "6.98",
          "6.99",
          2,
          5,
          setOf("6.1", "6.3", "6.6")
        ),
        SectionHeader.LIFESTYLE_AND_ASSOCIATES to NeedConfiguration(
          "7.98",
          "7.99",
          2,
          5,
          setOf("7.2", "7.3", "7.5")
        ),
        SectionHeader.DRUG_MISUSE to NeedConfiguration(
          "8.98",
          "8.99",
          1,
          2,
          setOf("8.4", "8.5", "8.6", "8.8", "8.9")
        ),
        SectionHeader.ALCOHOL_MISUSE to NeedConfiguration(
          "9.98",
          "9.99",
          1,
          4,
          setOf("9.1", "9.2", "9.3", "9.5")
        ),
        // Emotional Wellbeing is not a proper Criminogenic Need and so does not have a severity score
        SectionHeader.EMOTIONAL_WELL_BEING to NeedConfiguration(
          "10.98",
          "10.99",
          999,
          999,
          setOf()
        ),
        SectionHeader.THINKING_AND_BEHAVIOUR to NeedConfiguration(
          "11.98",
          "11.99",
          4,
          7,
          setOf("11.5", "11.6", "11.7", "11.9")
        ),
        SectionHeader.ATTITUDES to NeedConfiguration(
          "12.98",
          "12.99",
          1,
          4,
          setOf("12.1", "12.4", "12.5", "12.8")
        )
      )
    }

    fun getNeedsSectionHeadings(): Set<SectionHeader> {
      return needs().keys.toSet()
    }
  }
}
