package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.RefAssessmentDto
import uk.gov.justice.digital.oasys.services.ReferenceAssessmentService

@RestController
@Api(description = "Reference assessment resources", tags = ["Reference Assessment"])
class ReferenceAssessmentController (private val refAssessmentService: ReferenceAssessmentService) {

    @GetMapping(path = ["/referenceassessments/type/{versionCode}/revision/{versionNumber}"])
    @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
    fun getReferenceAssessmentOf(@PathVariable("versionCode") versionCode: String?,
                                 @PathVariable("versionNumber") versionNumber: String?): RefAssessmentDto? {
        return refAssessmentService.getReferenceAssessmentOf(versionCode, versionNumber)
    }
}