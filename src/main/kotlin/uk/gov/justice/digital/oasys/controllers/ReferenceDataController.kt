package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.FilteredRefDataRequestDto
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.services.ReferenceDataService

@RestController
@Api(description = "Reference resources", tags = ["Reference Data"])
class ReferenceDataController(private val referenceDataService: ReferenceDataService) {

  @GetMapping(path = ["/referencedata/{category}"])
  @ApiOperation(value = "Gets reference data for a category")
  @ApiResponses(ApiResponse(code = 200, message = "OK"))
  fun getReferenceDataByCategoryCode(@PathVariable("category") refDataCategoryCode: String?): Collection<RefElementDto?>? {
    return referenceDataService.getActiveReferenceDataOfCategory(refDataCategoryCode)
  }

  @PostMapping(path = ["/referencedata/filtered"])
  @ApiOperation(value = "Gets filtered reference data", hidden = true)
  @ApiResponses(ApiResponse(code = 200, message = "OK"))
  fun getFilteredReferenceData(@RequestBody filteredRefDataRequest: FilteredRefDataRequestDto): Map<String, Collection<RefElementDto>> {
    return referenceDataService.getFilteredReferenceData(filteredRefDataRequest)
  }
}
