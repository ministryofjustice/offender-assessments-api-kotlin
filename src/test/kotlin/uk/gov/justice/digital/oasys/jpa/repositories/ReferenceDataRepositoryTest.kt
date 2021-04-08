package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.time.LocalDateTime

@SqlGroup(
  Sql(
    scripts = ["classpath:referenceData/before-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:referenceData/after-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
@DisplayName("Reference Data Repository Tests")
class ReferenceDataRepositoryTest(@Autowired private val repository: ReferenceDataRepository) : IntegrationTest() {

  val betweenStartAndEndDate: LocalDateTime = LocalDateTime.of(2010, 1, 1, 0, 0)
  val beforeStartDate: LocalDateTime = LocalDateTime.of(2009, 12, 31, 23, 59)
  val afterEndDate: LocalDateTime = LocalDateTime.of(2100, 1, 1, 0, 1)

  val category = "TEST_CATEGORY"

  @Test
  fun `return data by category and valid date`() {
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(category, betweenStartAndEndDate)
    assertThat(refData).isEqualTo(setupElements())
  }

  @Test
  fun `return data by category and null end date`() {
    val nullCategory = "NULL_DATE"
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(nullCategory, betweenStartAndEndDate)
    assertThat(refData).isEqualTo(
      listOf(
        RefElement(
          refElementUk = 9997,
          refCategoryCode = "NULL_DATE",
          refElementCode = "ELEMENT_1",
          refElementCtid = "9400",
          refElementShortDesc = "El 1",
          refElementDesc = "Element 1",
          displaySort = 940L,
          startDate = LocalDateTime.of(2010, 1, 1, 0, 0)
        )
      )
    )
  }

  @Test
  fun `return empty collection for invalid category`() {
    val invalidCategory = "INVALID"
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(invalidCategory, betweenStartAndEndDate)
    assertThat(refData).isEmpty()
  }

  @Test
  fun `return empty collection for expired end date`() {
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(category, afterEndDate)
    assertThat(refData).isEmpty()
  }

  @Test
  fun `return empty collection for future start date`() {
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(category, beforeStartDate)
    assertThat(refData).isEmpty()
  }

  @Test
  fun `return empty collection for future start date and null end date`() {
    val nullCategory = "NULL_DATE"
    val refData = repository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(nullCategory, beforeStartDate)
    assertThat(refData).isEmpty()
  }

  private fun setupElements(): Collection<RefElement> {
    return listOf(
      RefElement(
        refElementUk = 9997,
        refCategoryCode = "TEST_CATEGORY",
        refElementCode = "ELEMENT_1",
        refElementCtid = "9400",
        refElementShortDesc = "El 1",
        refElementDesc = "Element 1",
        displaySort = 940L,
        startDate = LocalDateTime.of(2010, 1, 1, 0, 0),
        endDate = LocalDateTime.of(2010, 1, 1, 0, 0)
      ),
      RefElement(
        refElementUk = 9998,
        refCategoryCode = "TEST_CATEGORY",
        refElementCode = "ELEMENT_2",
        refElementCtid = "9400",
        refElementShortDesc = "El 2",
        refElementDesc = "Element 2",
        displaySort = 940L,
        startDate = LocalDateTime.of(2010, 1, 1, 0, 0),
        endDate = LocalDateTime.of(2010, 1, 1, 0, 0)
      )
    )
  }
}
