package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.services.AnswerService

@RestController
@Api(value = "Assessments ", tags = ["Assessments"])
class AnswerController(private val answerService: AnswerService) {

  @PostMapping(path = ["/assessments/oasysSetId/{oasysSetId}/answers"])
  @ApiOperation(value = "Gets a list of answers for a given list of question codes")
  @ApiResponses(ApiResponse(code = 404, message = "Assessment not found"), ApiResponse(code = 200, message = "OK"))
  fun getAssessment(@PathVariable("oasysSetId") oasysSetId: Long, @RequestBody questionCodes: Map<String, Collection<String>>): AssessmentAnswersDto {
    return answerService.getAnswersForQuestions(oasysSetId, questionCodes)
  }
}
