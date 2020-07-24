package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.BasicSentencePlanObj
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.time.LocalDate
import java.time.LocalDateTime

@DisplayName("Basic Sentence Plan Items DTO Tests")
class BasicSentencePlanItemDtoTest {

    @Test
    fun `Builds valid Basic Sentence Plan Item DTO from Entity`() {

        val basicSentencePlanItem = setupBasicSentencePlanObj()
        val basicSentencePlanItemDto = BasicSentencePlanItemDto.from(basicSentencePlanItem)

        SoftAssertions().apply {
            with(basicSentencePlanItemDto) {
                assertThat(this?.basicSentPlanObjId).isEqualTo(basicSentencePlanItem?.basicSentPlanObjPk)
                assertThat(this?.includeInPlan).isTrue()
                assertThat(this?.objectiveText).isEqualTo(basicSentencePlanItem?.objectiveText)
                assertThat(this?.measureText).isEqualTo(basicSentencePlanItem?.measureText)
                assertThat(this?.whatWorkText).isEqualTo(basicSentencePlanItem?.whatWorkText)
                assertThat(this?.whoWillDoWorkText).isEqualTo(basicSentencePlanItem?.whoWillDoWorkText)
                assertThat(this?.timescalesText).isEqualTo(basicSentencePlanItem?.timescalesText)
                assertThat(this?.dateOpened).isEqualTo( LocalDate.of(2020, 2, 1))
                assertThat(this?.dateCompleted).isEqualTo(LocalDate.of(2020, 12, 1))
                assertThat(this?.problemAreaCompInd).isTrue()
                assertThat(this?.offenceBehaviourLink?.code).isEqualTo(basicSentencePlanItem?.offenceBehaviourLink?.refElementCode)
                assertThat(this?.offenceBehaviourLink?.description).isEqualTo(basicSentencePlanItem?.offenceBehaviourLink?.refElementDesc)
            }
        }.assertAll()
    }

    @Test
    fun `Builds valid Basic Sentence Plan Item DTO from Entity with problem area True`() {
        val basicSentencePlanItem = setupBasicSentencePlanObj()
        val basicSentencePlanItemDto =  BasicSentencePlanItemDto.from(basicSentencePlanItem)
        assertThat(basicSentencePlanItemDto?.problemAreaCompInd).isTrue()
    }

    @Test
    fun `Builds valid Basic Sentence Plan Item DTO from Entity with problem area False`() {
        val basicSentencePlanItem = setupBasicSentencePlanObj().copy(problemAreaCompInd = "N")
        val basicSentencePlanItemDto =  BasicSentencePlanItemDto.from(basicSentencePlanItem)
        assertThat(basicSentencePlanItemDto?.problemAreaCompInd).isFalse()
    }

    @Test
    fun `Builds valid Basic Sentence Plan Item DTO from Entity with Include in Plan True`() {
        val basicSentencePlanItem = setupBasicSentencePlanObj()
        val basicSentencePlanItemDto =  BasicSentencePlanItemDto.from(basicSentencePlanItem)
        assertThat(basicSentencePlanItemDto?.includeInPlan).isTrue()
    }

    @Test
    fun `Builds valid Basic Sentence Plan Item DTO from Entity with Include in Plan False`() {
        val basicSentencePlanItem = setupBasicSentencePlanObj().copy(includeInPlanInd = "N")
        val basicSentencePlanItemDto =  BasicSentencePlanItemDto.from(basicSentencePlanItem)
        assertThat(basicSentencePlanItemDto?.includeInPlan).isFalse()
    }

    private fun setupBasicSentencePlanObj(): BasicSentencePlanObj {
        val created = LocalDateTime.of(2020, 1, 1, 14, 0)
        val dateOpenend = LocalDateTime.of(2020, 2, 1, 14, 0)
        val dateCompleted = LocalDateTime.of(2020, 12, 1, 14, 0)

                      return BasicSentencePlanObj(
                                basicSentPlanObjPk = 1L,
                                includeInPlanInd = "Y",
                                offenceBehaviourLink = RefElement(refElementCode = "RefCode", refElementDesc = "Ref Description"),
                                objectiveText = "Objective text",
                                measureText = "Measure text",
                                whatWorkText = "What Work",
                                whoWillDoWorkText = "Who do work",
                                timescalesText = "Timescales",
                                dateOpened = dateOpenend,
                                dateCompleted = dateCompleted,
                                problemAreaCompInd = "Y",
                                createDate = created

        )
    }
}
