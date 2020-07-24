package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.*
import uk.gov.justice.digital.oasys.api.BasicSentencePlanDto
import uk.gov.justice.digital.oasys.services.SentencePlanService

@RestController
@Api(value = "Sentence Plans", tags = ["SentencePlans"])
class SentencePlanController (private val sentencePlanService: SentencePlanService) {


    @RequestMapping(path = ["/offenders/{identityType}/{identity}/basicSentencePlans/latest"], method = [RequestMethod.GET])
    @ApiResponses(ApiResponse(code = 404, message = "Sentence Plan or Offender not found"), ApiResponse(code = 200, message = "OK"))
    fun getLatestBasicSentencePlanForOffender(@PathVariable("identityType") identityType: String,
                                              @PathVariable("identity") identity: String,
                                              @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
                                              @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
                                              @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
                                              @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?): BasicSentencePlanDto {
        return sentencePlanService.getLatestBasicSentencePlanForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    }


    @RequestMapping(path = ["/offenders/{identityType}/{identity}/basicSentencePlans"], method = [RequestMethod.GET])
    @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
    fun getSentenceBasicPlansForOffender(@PathVariable("identityType") identityType: String,
                                         @PathVariable("identity") identity: String,
                                         @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
                                         @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
                                         @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
                                         @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?): Collection<BasicSentencePlanDto> {
        return sentencePlanService.getBasicSentencePlansForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    }


}
