package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.BasicSentencePlanObj
import java.time.LocalDate
import java.time.LocalDateTime

@DisplayName("Basic Sentence Plan DTO Tests")
class BasicSentencePlanDtoTest {

  @Test
  fun `Builds valid Basic Sentence Plan DTO from Entity`() {
    val assessment = setupAssessment()
    val basicSentencePlan = BasicSentencePlanDto.from(assessment)

    SoftAssertions().apply {
      assertThat(basicSentencePlan).isNotNull()
      assertThat(basicSentencePlan?.basicSentencePlanItems).hasSize(1)
      assertThat(basicSentencePlan?.sentencePlanId).isEqualTo(1234L)
      assertThat(basicSentencePlan?.createdDate).isEqualTo(LocalDate.of(2020, 1, 1))
    }.assertAll()
  }

  private fun setupAssessment(): Assessment {
    val created = LocalDateTime.of(2020, 1, 1, 14, 0)
    return Assessment(
      oasysSetPk = 1234L,
      assessmentStatus = "STATUS",
      assessmentType = "LAYER_3",
      createDate = created,
      basicSentencePlanList = setOf(BasicSentencePlanObj())
    )
  }
}
