package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.BasicSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanSummaryDto
import uk.gov.justice.digital.oasys.services.SentencePlanService

@RestController
@Api(value = "Sentence Plans", tags = ["SentencePlans"])
class SentencePlanController(private val sentencePlanService: SentencePlanService) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @RequestMapping(path = ["/offenders/{identityType}/{identity}/basicSentencePlans/latest"], method = [RequestMethod.GET])
  @ApiResponses(ApiResponse(code = 404, message = "Sentence Plan or Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getLatestBasicSentencePlanForOffender(
    @PathVariable("identityType") identityType: String,
    @PathVariable("identity") identity: String,
    @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
    @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
    @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
    @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?
  ): BasicSentencePlanDto {
    return sentencePlanService.getLatestBasicSentencePlanForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
  }

  @RequestMapping(path = ["/offenders/{identityType}/{identity}/basicSentencePlans"], method = [RequestMethod.GET])
  @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getSentenceBasicPlansForOffender(
    @PathVariable("identityType") identityType: String,
    @PathVariable("identity") identity: String,
    @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
    @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
    @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
    @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?
  ): Collection<BasicSentencePlanDto> {
    return sentencePlanService.getBasicSentencePlansForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
  }

  @RequestMapping(path = ["/offenders/{identityType}/{identity}/fullSentencePlans"], method = [RequestMethod.GET])
  @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getFullSentencePlansForOffender(
    @PathVariable("identityType") identityType: String?,
    @PathVariable("identity") identity: String?,
    @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
    @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
    @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
    @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?
  ): Collection<FullSentencePlanDto?>? {
    log.info("Retrieving Full Sentence Plans for Identity {}, {}", identityType, identity)
    log.info("Found Full Sentence Plans for Identity {}, {}", identityType, identity)
    return sentencePlanService.getFullSentencePlansForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
  }

  @RequestMapping(path = ["/offenders/{identityType}/{identity}/fullSentencePlans/{oasysSetPk}"], method = [RequestMethod.GET])
  @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getFullSentencePlanForOffender(
    @PathVariable("identityType") identityType: String?,
    @PathVariable("identity") identity: String?,
    @PathVariable("oasysSetPk") oasysSetPk: Long?
  ): FullSentencePlanDto? {
    log.info("Retrieving Full Sentence Plan for Identity {}, {}", identityType, identity)
    log.info("Found Full Sentence Plan for Identity {}, {}", identityType, identity)
    return sentencePlanService.getFullSentencePlan(oasysSetPk)
  }

  @RequestMapping(path = ["/offenders/{identityType}/{identity}/fullSentencePlans/summary"], method = [RequestMethod.GET])
  @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getSummarySentencePlansForOffender(
    @PathVariable("identityType") identityType: String?,
    @PathVariable("identity") identity: String?,
    @RequestParam(value = "historicStatus", required = false) filterGroupStatus: String?,
    @RequestParam(value = "assessmentType", required = false) filterAssessmentType: String?,
    @RequestParam(value = "voided", required = false) filterVoided: Boolean?,
    @RequestParam(value = "assessmentStatus", required = false) filterAssessmentStatus: String?
  ): Collection<FullSentencePlanSummaryDto> {
    log.info("Retrieving Summary Sentence Plans for Identity {}, {}", identityType, identity)
    log.info("Found Summary Sentence Plans for Identity {}, {}", identityType, identity)
    return sentencePlanService.getFullSentencePlanSummariesForOffender(identityType, identity, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
  }
}
