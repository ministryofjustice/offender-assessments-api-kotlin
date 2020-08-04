package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceDataRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException


@ExtendWith(MockKExtension::class)
@DisplayName("Reference Data Service Tests")
class ReferenceDataServiceTest {

    private val referenceDataRepository: ReferenceDataRepository = mockk()
    private val referenceDataService = ReferenceDataService(referenceDataRepository)
    private val category = "TEST_CATEGORY"

    @Test
    fun `returns active Reference Elements for category`() {
        val refElements = setupElements()
        every { referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(
                "TEST_CATEGORY", any()) } returns refElements

        assertThat(referenceDataService.getActiveReferenceDataOfCategory(category))
                .isEqualTo(setUpElementDtos())
    }

    @Test
    fun `throws not found when no entries found for category`() {
        every { referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(
                "TEST_CATEGORY", any()) } returns emptyList()

        val exception = assertThrows<EntityNotFoundException> { referenceDataService.getActiveReferenceDataOfCategory(category) }
        assertThat(exception.message).isEqualTo("Category $category, not found")
    }

    private fun setupElements(): Collection<RefElement?>? {
        return listOf(
                RefElement(
                        refElementCode = "ELEMENT_1",
                        refElementShortDesc = "El 1",
                        refElementDesc = "Element 1"),
                RefElement(
                        refElementCode = "ELEMENT_2",
                        refElementShortDesc = "El 2",
                        refElementDesc = "Element 2"))
    }

    private fun setUpElementDtos(): Collection<RefElementDto>{
        return listOf(
                RefElementDto(
                        code = "ELEMENT_1",
                        shortDescription = "El 1",
                        description = "Element 1"),
                RefElementDto(
                        code = "ELEMENT_2",
                        shortDescription = "El 2",
                        description = "Element 2"))
    }
}