package uk.gov.justice.digital.oasys.services.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Rosh Mapping Tests")
class RoshMappingTest {
  @Test
  fun `should get map with only ROSH questions if only ROSH section requested`() {
    val rosh = RoshMapping.rosh(setOf(SectionHeader.ROSH_SCREENING))

    assertThat(rosh).hasSize(1)
  }

  @Test
  fun `should get map with all ROSH questions if all ROSH sections requested`() {
    val rosh = RoshMapping.rosh(
      setOf(
        SectionHeader.ROSH_SCREENING,
        SectionHeader.ROSH_FULL_ANALYSIS,
        SectionHeader.ROSH_SUMMARY
      )
    )

    assertThat(rosh).hasSize(3)
  }
}
