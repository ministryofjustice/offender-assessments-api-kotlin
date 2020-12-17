package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.entities.SspObjectivesInSet
import java.time.LocalDateTime

@DisplayName("Full Sentence Plan Summary Tests")
class FullSentencePlanSummaryTest {

  private val date = LocalDateTime.now()

  @Test
  fun `Build valid Full Sentence Plan Summary DTO from Assessment and Service entities`() {
    val assessment = setupAssessmentEntity()
    val fullSentencePlanSummaryDto = FullSentencePlanSummaryDto.from(assessment, Section())
    assertThat(fullSentencePlanSummaryDto).isEqualTo(setupValidFullSentencePlanSummaryDto())
  }

  @Test
  fun `Build valid Full Sentence Plan Summary DTO from Assessment and null Service entities`() {
    val assessment = setupAssessmentEntity()
    val fullSentencePlanSummaryDto = FullSentencePlanSummaryDto.from(assessment, null)
    assertThat(fullSentencePlanSummaryDto).isEqualTo(setupValidFullSentencePlanSummaryDto())
  }

  @Test
  fun `Build null Full Sentence Plan Summary DTO from null Service and Assessment with empty Objectives set`() {
    val assessment = setupAssessmentEntity().copy(sspObjectivesInSets = emptySet())
    val fullSentencePlanSummaryDto = FullSentencePlanSummaryDto.from(assessment, null)
    assertThat(fullSentencePlanSummaryDto).isNull()
  }

  @Test
  fun `Build null Full Sentence Plan Summary DTO from null Service and Assessment with null Objectives`() {
    val assessment = setupAssessmentEntity().copy(sspObjectivesInSets = null)
    val fullSentencePlanSummaryDto = FullSentencePlanSummaryDto.from(assessment, null)
    assertThat(fullSentencePlanSummaryDto).isNull()
  }

  private fun setupAssessmentEntity(): Assessment {
    return Assessment(
      oasysSetPk = 1L,
      createDate = date,
      dateCompleted = date.plusYears(1),
      sspObjectivesInSets = setOf(SspObjectivesInSet())
    )
  }

  private fun setupValidFullSentencePlanSummaryDto(): FullSentencePlanSummaryDto {
    return FullSentencePlanSummaryDto(
      oasysSetId = 1L,
      createdDate = date,
      completedDate = date.plusYears(1)
    )
  }
}
