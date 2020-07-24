package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.OffenderDto
import uk.gov.justice.digital.oasys.services.OffenderService


@RestController
@Api(value = "Offender ", tags = ["Offenders"])
class OffenderController (private val offenderService: OffenderService) {

    @GetMapping(path = ["/offenders/{identityType}/{identity}"])
    @ApiOperation(value = "Gets an offender by its identity")
    @ApiResponses(ApiResponse(code = 404, message = "Offender not found"), ApiResponse(code = 200, message = "OK"))
    fun getOffenderByPk(@PathVariable ("identityType") @NonNull identityType: String, @PathVariable("identity") @NonNull identity: String): OffenderDto? {
        return offenderService.getOffender(identityType, identity)
    }
}