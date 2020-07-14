package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.Predictor
import uk.gov.justice.digital.oasys.services.PredictionService

@RestController
@Api(value = "Offender OGP, OGRs and OVP score resources", tags = ["Offender OGP, OGRs, OVP Predictors"])
class PredictorsController(private val predictionService: PredictionService) {

    @RequestMapping(path = ["/offenders/{identityType}/{identity}/predictors"], method = [RequestMethod.GET])
    @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
    fun getPredictorScoresForOasysOffenderId(@PathVariable("identityType") identityType: String,
                                             @PathVariable("identity") identity: String): ResponseEntity<Set<Predictor?>> {
        val predictors =  predictionService.getAllPredictorsForOffender(identityType, identity)
        return ResponseEntity.ok(predictors)
    }
}