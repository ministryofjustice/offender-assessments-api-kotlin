package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.RiskDto
import uk.gov.justice.digital.oasys.api.SectionAnswersDto
import uk.gov.justice.digital.oasys.services.RisksService
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

@RestController
@Api(value = "Offender SARA and ROSH risk resources", tags = ["Offender SARA, ROSH risk indicators"])
class RiskController(private val risksService: RisksService) {

  @GetMapping(path = ["/offenders/{identityType}/{identity}/risks"])
  @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
  fun getRisksForOasysOffenderId(
    @PathVariable("identityType") identityType: String,
    @PathVariable("identity") identity: String
  ): Collection<RiskDto>? {
    return risksService.getAllRisksForOffender(identityType, identity)
  }

  @GetMapping(path = ["/offenders/risks/assessment/{assessmentId}"])
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getRisksForOasysOffenderIdAndAssessmentId(
    @PathVariable("assessmentId") assessmentId: Long
  ): RiskDto? {
    return risksService.getRisksForAssessmentId(assessmentId)
  }

  @PostMapping(path = ["/offenders/risks/assessment/{assessmentId}/sections"])
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getSectionsForAGivenAssessment(
    @PathVariable("assessmentId") assessmentId: Long,
    @RequestBody sectionCodes: Set<SectionHeader>
  ): SectionAnswersDto? {
    return risksService.getRisksForAssessmentSections(assessmentId, sectionCodes)
  }
}
