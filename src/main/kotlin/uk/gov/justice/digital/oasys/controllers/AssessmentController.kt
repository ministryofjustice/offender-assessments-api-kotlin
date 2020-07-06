package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.AssessmentDto
import uk.gov.justice.digital.oasys.api.AssessmentSummaryDto
import uk.gov.justice.digital.oasys.services.AssessmentService

@RestController
@Api(value = "Assessments ", tags = ["Assessments"])
class AssessmentController (private val assessmentService: AssessmentService) {

    @GetMapping(path = ["/assessments/oasysSetId/{oasysSetId}"])
    @ApiOperation(value = "Gets an Assessment by its identity")
    @ApiResponses(*[ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK")])
    fun getAssessment(@PathVariable("oasysSetId") oasysSetId: Long?): AssessmentDto? {
         return assessmentService.getAssessment(oasysSetId);
    }


    @GetMapping(path = ["/offenders/{identityType}/{identity}/assessments/summary"])
    @ApiOperation(value = "Gets a list of assessments for an offender")
    @ApiResponses(*[
        ApiResponse(code = 404, message = "Offender not found"),
        ApiResponse(code = 200, message = "OK")]
    )
    fun getAssessmentsForOffender(@PathVariable("identityType") identityType: String,
                                  @PathVariable("identity") identity: String,
                                  @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
                                  @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
                                  @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
                                  @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?): Collection<AssessmentSummaryDto?>? {
        return assessmentService.getAssessmentsForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    }

    @GetMapping(path = ["/offenders/{identityType}/{identity}/assessments/latest"])
    @ApiOperation(value = "Gets the latest assessment for an offender")
    @ApiResponses(*[ApiResponse(code = 404, message = "Assessment or Offender not found"), ApiResponse(code = 200, message = "OK")])
    fun getAssessment(@PathVariable("identityType") identityType: String,
                      @PathVariable("identity") identity: String,
                      @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
                      @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
                      @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
                      @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?): AssessmentDto? {
        return  assessmentService.getLatestAssessmentForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    }
}