package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.api.FilteredRefDataRequestDto
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
    every {
      referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(
        "TEST_CATEGORY",
        any()
      )
    } returns refElements

    assertThat(referenceDataService.getActiveReferenceDataOfCategory(category))
      .isEqualTo(setUpElementDtos())
  }

  @Test
  fun `throws not found when no entries found for category`() {
    every {
      referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(
        "TEST_CATEGORY",
        any()
      )
    } returns emptyList()

    val exception = assertThrows<EntityNotFoundException> { referenceDataService.getActiveReferenceDataOfCategory(category) }
    assertThat(exception.message).isEqualTo("Category $category, not found")
  }

  @Test
  fun `returns filtered reference data for `() {
    every {
      referenceDataRepository.findFilteredReferenceData(
        username = "TEST_USER",
        area = "WWS",
        assessmentType = "SHORT_FORM_PSR",
        fieldName = "ethnicity"
      )
    } returns """{"STATE":"SUCCESS",
                  "DETAIL":{"ethnicity":[
                  {
                  "displayValue":"Chinese or other ethnic group"
                  ,"returnValue":"O1"
                  }
                  ,{
                  "displayValue":"White - British English Welsh Scottish Northern Irish"
                  ,"returnValue":"W1"
                  }
                  ]
                  }}"""

    val result = referenceDataService.getFilteredReferenceData(
      FilteredRefDataRequestDto(
        oasysUserCode = "TEST_USER",
        oasysAreaCode = "WWS",
        assessmentType = "SHORT_FORM_PSR",
        fieldName = "ethnicity"
      )
    )

    assertThat(result["ethnicity"]).containsExactlyInAnyOrder(
      RefElementDto("O1", null, "Chinese or other ethnic group"),
      RefElementDto("W1", null, "White - British English Welsh Scottish Northern Irish")
    )
  }

  @Test
  fun `throws exception when invalid state returned from OASys filtered reference data`() {
    every {
      referenceDataRepository.findFilteredReferenceData(
        username = "TEST_USER",
        area = "WWS",
        assessmentType = "SHORT_FORM_PSR",
        fieldName = "ethnicity"
      )
    } returns """{"STATE":"MAPPING_ERROR","DETAIL":
      |{"Failures":[{"sectionCode":"ASSESSMENT","errors":
      |[{"failureType":"PARAMETER_CHECK","questionCode":"ethnicity","errorName":"JSON_Mapping_Error","oasysErrorLogId":61,"message":"Section / fieldname not mapped"},
      |{"failureType":"PROCESSING_RESULT","questionCode":"ethnicity","errorName":"refdata_execution_error","oasysErrorLogId":64,"message":"Section / fieldname not mapped"},
      |{"failureType":"PARAMETER_CHECK","questionCode":"ethnicity","errorName":"JSON_Mapping_Error","oasysErrorLogId":63,"message":"Section / fieldname not mapped"},
      |{"failureType":"PROCESSING_RESULT","questionCode":"ethnicity","errorName":"refdata_execution_error","oasysErrorLogId":62,"message":"Section / fieldname not mapped"}]}]}}""".trimMargin()

    val exception = assertThrows<IllegalArgumentException> {
      referenceDataService.getFilteredReferenceData(
        FilteredRefDataRequestDto(
          oasysUserCode = "TEST_USER",
          oasysAreaCode = "WWS",
          assessmentType = "SHORT_FORM_PSR",
          fieldName = "ethnicity"
        )
      )
    }

    assertThat(exception.message).isEqualTo("Invalid parameters passed for filtered reference data ethnicity, check logs")
  }

  @Test
  fun `throws exception when unknown error returned from OASys filtered reference data`() {
    every {
      referenceDataRepository.findFilteredReferenceData(
        username = "TEST_USER",
        area = "WWS",
        assessmentType = "SHORT_FORM_PSR",
        fieldName = "ethnicity"
      )
    } returns """{"STATE":"OASYS_FAIL"}""".trimMargin()

    assertThrows<Exception> {
      referenceDataService.getFilteredReferenceData(
        FilteredRefDataRequestDto(
          oasysUserCode = "TEST_USER",
          oasysAreaCode = "WWS",
          assessmentType = "SHORT_FORM_PSR",
          fieldName = "ethnicity"
        )
      )
    }
  }

  @Test
  fun `throws not found exception when fieldname does not exist in OASys filtered reference data`() {
    every {
      referenceDataRepository.findFilteredReferenceData(
        username = "TEST_USER",
        area = "WWS",
        assessmentType = "SHORT_FORM_PSR",
        fieldName = "rsr_offence_code"
      )
    } returns """{"STATE":"REFDATA_FAILURE"}""".trimMargin()

    val exception = assertThrows<EntityNotFoundException> {
      referenceDataService.getFilteredReferenceData(
        FilteredRefDataRequestDto(
          oasysUserCode = "TEST_USER",
          oasysAreaCode = "WWS",
          assessmentType = "SHORT_FORM_PSR",
          fieldName = "rsr_offence_code"
        )
      )
    }
    assertThat(exception.message).isEqualTo("Ref Data item not found for rsr_offence_code, check logs")
  }

  private fun setupElements(): Collection<RefElement?> {
    return listOf(
      RefElement(
        refElementCode = "ELEMENT_1",
        refElementShortDesc = "El 1",
        refElementDesc = "Element 1"
      ),
      RefElement(
        refElementCode = "ELEMENT_2",
        refElementShortDesc = "El 2",
        refElementDesc = "Element 2"
      )
    )
  }

  private fun setUpElementDtos(): Collection<RefElementDto> {
    return listOf(
      RefElementDto(
        code = "ELEMENT_1",
        shortDescription = "El 1",
        description = "Element 1"
      ),
      RefElementDto(
        code = "ELEMENT_2",
        shortDescription = "El 2",
        description = "Element 2"
      )
    )
  }
}
