package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.OffenceBlock
import uk.gov.justice.digital.oasys.jpa.entities.OffenceSentenceDetail
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.entities.Sentence
import java.time.LocalDate

@DisplayName("Sentence DTO Tests")
class SentenceDtoTest {

  @Test
  fun `Builds valid Sentence DTO from Offence Block Entity`() {

    val sentenceDto = SentenceDto.from(setOf(setupOffenceBlockFull()))?.first()

    assertThat(sentenceDto?.sentenceCode).isEqualTo("sentenceCode")
    assertThat(sentenceDto?.sentenceDescription).isEqualTo("sentenceDesc")
    assertThat(sentenceDto?.custodial).isTrue()
    assertThat(sentenceDto?.cja).isTrue()
    assertThat(sentenceDto?.offenceDate).isEqualTo(LocalDate.MIN)
    assertThat(sentenceDto?.sentenceDate).isEqualTo(LocalDate.MAX)
    assertThat(sentenceDto?.sentenceLengthCustodyDays).isEqualTo(100)
    assertThat(sentenceDto?.orderType?.code).isEqualTo("typeCode")
    assertThat(sentenceDto?.orderType?.description).isEqualTo("orderType")
    assertThat(sentenceDto?.cjaSupervisionMonths).isEqualTo(15L)
    assertThat(sentenceDto?.cjaUnpaidHours).isEqualTo(10L)
    assertThat(sentenceDto?.activity).isEqualTo("activity")
    assertThat(sentenceDto?.offenceBlockType?.code).isEqualTo("typeCode")
    assertThat(sentenceDto?.offenceBlockType?.description).isEqualTo("offenceType")
  }

  @Test
  fun `Builds valid Sentence DTO from Offence Block Entity with Null Sentence Detail`() {

    val sentenceDto = SentenceDto.from(setOf(setupOffenceBlockNullDetail()))?.first()

    assertThat(sentenceDto?.sentenceCode).isEqualTo("sentenceCode")
    assertThat(sentenceDto?.sentenceDescription).isEqualTo("sentenceDesc")
    assertThat(sentenceDto?.custodial).isTrue()
    assertThat(sentenceDto?.cja).isTrue()
    assertThat(sentenceDto?.offenceDate).isEqualTo(LocalDate.MIN)
    assertThat(sentenceDto?.sentenceDate).isEqualTo(LocalDate.MAX)
    assertThat(sentenceDto?.sentenceLengthCustodyDays).isEqualTo(100)
    assertThat(sentenceDto?.orderType?.code).isEqualTo("typeCode")
    assertThat(sentenceDto?.orderType?.description).isEqualTo("orderType")
    assertThat(sentenceDto?.cjaSupervisionMonths).isNull()
    assertThat(sentenceDto?.cjaUnpaidHours).isNull()
    assertThat(sentenceDto?.activity).isNull()
    assertThat(sentenceDto?.offenceBlockType?.code).isEqualTo("typeCode")
    assertThat(sentenceDto?.offenceBlockType?.description).isEqualTo("offenceType")
  }

  @Test
  fun `Builds valid Sentence DTO from Offence Block Entity with Null Sentence`() {

    val sentenceDto = SentenceDto.from(setOf(setupOffenceBlockNullSentence()))?.first()

    assertThat(sentenceDto?.sentenceCode).isNull()
    assertThat(sentenceDto?.sentenceDescription).isNull()
    assertThat(sentenceDto?.custodial).isNull()
    assertThat(sentenceDto?.cja).isNull()
    assertThat(sentenceDto?.offenceDate).isEqualTo(LocalDate.MIN)
    assertThat(sentenceDto?.sentenceDate).isEqualTo(LocalDate.MAX)
    assertThat(sentenceDto?.sentenceLengthCustodyDays).isEqualTo(100)
    assertThat(sentenceDto?.orderType).isNull()
    assertThat(sentenceDto?.cjaSupervisionMonths).isEqualTo(15L)
    assertThat(sentenceDto?.cjaUnpaidHours).isEqualTo(10L)
    assertThat(sentenceDto?.activity).isEqualTo("activity")
  }

  private fun setupOffenceBlockFull(): OffenceBlock {

    return OffenceBlock(
      offenceBlockPk = 1L,
      offenceBlockType = RefElement(refElementCode = "typeCode", refElementDesc = "offenceType"),
      offenceSentenceDetail = OffenceSentenceDetail(
        activityDesc = "activity",
        cjaSupervisionMonths = 15L,
        cjaUnpaidHours = 10L
      ),
      sentenceDate = LocalDate.MAX,
      offenceDate = LocalDate.MIN,
      sentLengthCustDays = 100L,
      sentence = Sentence(
        cjaInd = "Y",
        custodialInd = "Y",
        orderType = RefElement(refElementCode = "typeCode", refElementDesc = "orderType"),
        sentenceCode = "sentenceCode",
        sentenceDesc = "sentenceDesc"
      )
    )
  }

  private fun setupOffenceBlockNullDetail(): OffenceBlock {

    return OffenceBlock(
      offenceBlockPk = 1L,
      offenceBlockType = RefElement(refElementCode = "typeCode", refElementDesc = "offenceType"),
      offenceSentenceDetail = null,
      sentenceDate = LocalDate.MAX,
      offenceDate = LocalDate.MIN,
      sentLengthCustDays = 100L,
      sentence = Sentence(
        cjaInd = "Y",
        custodialInd = "Y",
        orderType = RefElement(refElementCode = "typeCode", refElementDesc = "orderType"),
        sentenceCode = "sentenceCode",
        sentenceDesc = "sentenceDesc"
      )
    )
  }

  private fun setupOffenceBlockNullSentence(): OffenceBlock {
    return OffenceBlock(
      offenceBlockPk = 1L,
      sentenceDate = LocalDate.MAX,
      offenceDate = LocalDate.MIN,
      sentLengthCustDays = 100L,
      sentence = null,
      offenceSentenceDetail = OffenceSentenceDetail(
        activityDesc = "activity",
        cjaSupervisionMonths = 15L,
        cjaUnpaidHours = 10L
      )
    )
  }
}
