package uk.gov.justice.digital.oasys.services.domain

class RoshMapping {

  companion object {
    private val roshScreeningQuestions = setOf(
      "R2.1",
      "R2.2",
      "R2.2.1",
      "R3.1",
      "R3.2",
      "R3.3",
      "R3.4",
      "R4.1",
      "R4.2",
      "R4.3",
      "R4.4",
    )
    private val roshSummaryQuestions = setOf(
      "SUM1",
      "SUM2",
      "SUM3",
      "SUM4",
      "SUM5",
      "SUM6",
      "SUM6.1.1",
      "SUM6.1.2",
      "SUM6.2.1",
      "SUM6.2.2",
      "SUM6.3.1",
      "SUM6.3.2",
      "SUM6.4.1",
      "SUM6.4.2",
      "SUM6.5.1",
      "SUM6.5.2",
      "SUM7",
      "SUM8",
      "sum6.1",
      "sum6.2",
      "sum6.3",
      "sum6.4",
      "sum6.5",
    )
    private val roshFullQuestions = setOf(
      "FA1",
      "FA1.1",
      "FA10",
      "FA11",
      "FA12",
      "FA13",
      "FA14",
      "FA15",
      "FA16",
      "FA17",
      "FA18",
      "FA2",
      "FA26",
      "FA26.t",
      "FA27",
      "FA28",
      "FA29",
      "FA3",
      "FA30",
      "FA31",
      "FA32",
      "FA33",
      "FA34",
      "FA35",
      "FA36",
      "FA37",
      "FA38",
      "FA39",
      "FA4",
      "FA40",
      "FA41",
      "FA42",
      "FA43",
      "FA44",
      "FA45",
      "FA45.t",
      "FA47",
      "FA47.t",
      "FA49",
      "FA49.t",
      "FA5",
      "FA51",
      "FA51.t",
      "FA53",
      "FA53.t",
      "FA55",
      "FA55.t",
      "FA56",
      "FA56.t",
      "FA58",
      "FA58.t",
      "FA6",
      "FA6",
      "FA6.1",
      "FA60",
      "FA60.t",
      "FA7",
      "FA8",
      "FA9",
      "R8.1.1",
    )

    fun rosh(sectionCodes: Set<SectionHeader>): Map<String, Set<String>> {
      val mappings = mapOf(
        SectionHeader.ROSH_SCREENING.value to roshScreeningQuestions,
        SectionHeader.ROSH_FULL_ANALYSIS.value to roshFullQuestions,
        SectionHeader.ROSH_SUMMARY.value to roshSummaryQuestions
      )

      return sectionCodes.associate { it.value to mappings[it.value] } as Map<String, Set<String>>
    }
  }
}
