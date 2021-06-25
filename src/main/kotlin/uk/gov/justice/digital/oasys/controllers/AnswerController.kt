package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.PeriodUnit
import uk.gov.justice.digital.oasys.api.SectionAnswersDto
import uk.gov.justice.digital.oasys.api.SectionCodesDto
import uk.gov.justice.digital.oasys.services.AnswerService
import uk.gov.justice.digital.oasys.services.AssessmentService

@RestController
@Api(value = "Assessments ", tags = ["Assessments"])
class AnswerController(
  private val answerService: AnswerService,
  private val assessmentService: AssessmentService
) {

  @PostMapping(path = ["/assessments/oasysSetId/{oasysSetId}/answers"])
  @ApiOperation(value = "Gets a list of answers for a given list of question codes")
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getAssessment(
    @PathVariable("oasysSetId") oasysSetId: Long,
    @RequestBody questionCodes: Map<String, Collection<String>>
  ): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(oasysSetId, questionCodes)
  }

  @PostMapping(path = ["/assessments/oasysSetId/{oasysSetId}/sections/answers"])
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getSectionsForAGivenAssessment(
    @PathVariable("oasysSetId") assessmentId: Long,
    @RequestBody sections: SectionCodesDto
  ): SectionAnswersDto? {
    return answerService.getRisksForAssessmentSections(assessmentId, sections.sectionCodes)
  }

  @PostMapping(path = ["/assessments/{identityType}/{identity}/sections/answers"])
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getSectionsForLatestGivenAssessment(
    @PathVariable("identityType") identityType: String,
    @PathVariable("identity") identity: String,
    @RequestParam(value = "assessmentStatus", required = false) assessmentStatus: String?,
    @RequestParam(value = "assessmentTypes", required = false) assessmentTypes: Set<String>?,
    @RequestParam(value = "period", required = true) period: PeriodUnit,
    @RequestParam(value = "periodUnits", required = true) periodUnits: Long,
    @RequestBody sections: SectionCodesDto
  ): SectionAnswersDto? {
    val latestAssessmentsForOffenderInPeriod = assessmentService.getLatestAssessmentsForOffenderInPeriod(
      identityType,
      identity,
      assessmentTypes,
      assessmentStatus,
      period,
      periodUnits
    )
    return answerService.getRisksForAssessmentSections(latestAssessmentsForOffenderInPeriod.assessmentId, sections.sectionCodes)
  }
}
