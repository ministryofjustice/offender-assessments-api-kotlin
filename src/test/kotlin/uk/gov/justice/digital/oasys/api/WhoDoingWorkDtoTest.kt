package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.SspWhoDoWorkPivot

@DisplayName("Who Doing Work DTO Tests")
class WhoDoingWorkDtoTest {

  @Test
  fun `Build valid DTO from entity`() {
    val sspWhoDoWorkPivot = setupSspWhoDoWorkPivotEntity()
    val whoDoingWorkDto = WhoDoingWorkDto.from(sspWhoDoWorkPivot)
    assertThat(whoDoingWorkDto).isEqualTo(setupValidDto())
  }

  @Test
  fun `Build valid empty DTO set from null entity`() {
    val whoDoingWorkDto = WhoDoingWorkDto.from(null)
    assertThat(whoDoingWorkDto).isEqualTo(emptySet<WhoDoingWorkDto>())
  }

  @Test
  fun `Build valid empty DTO set from empty entity set`() {
    val whoDoingWorkDto = WhoDoingWorkDto.from(emptySet())
    assertThat(whoDoingWorkDto).isEqualTo(emptySet<WhoDoingWorkDto>())
  }

  private fun setupSspWhoDoWorkPivotEntity(): Set<SspWhoDoWorkPivot> {
    return setOf(
      SspWhoDoWorkPivot(
        whoDoWork = RefElement(
          refElementCode = "code",
          refElementDesc = "description"
        ),
        comments = "comments"
      )
    )
  }

  private fun setupValidDto(): Set<WhoDoingWorkDto> {
    return setOf(
      WhoDoingWorkDto(
        code = "code",
        description = "description",
        comments = "comments"
      )
    )
  }
}
