package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.Predictor
import uk.gov.justice.digital.oasys.services.PredictionService

@RestController
@Api(value = "Offender OGP, OGRs and OVP score resources", tags = ["Offender OGP, OGRs, OVP Predictors"])
class PredictorsController(private val predictionService: PredictionService) {

    @GetMapping(path = ["/offenders/{identityType}/{identity}/predictors"])
    @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
    fun getPredictorScoresForOasysOffenderId(@PathVariable("identityType") identityType: String,
                                             @PathVariable("identity") identity: String): Set<Predictor>? {
        return predictionService.getAllPredictorsForOffender(identityType, identity)
    }
}