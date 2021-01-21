package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.AuthenticationDto
import uk.gov.justice.digital.oasys.api.AuthorisationDto
import uk.gov.justice.digital.oasys.api.OasysUserAuthenticationDto
import uk.gov.justice.digital.oasys.api.OffenderPermissionResource
import uk.gov.justice.digital.oasys.api.UserDto
import uk.gov.justice.digital.oasys.api.UserRequestByEmail
import uk.gov.justice.digital.oasys.api.ValidateUserRequest
import uk.gov.justice.digital.oasys.services.AuthenticationService

@RestController
@Api(value = "Authentication resources", tags = ["Authentication"])
class AuthenticationController(private val authenticationService: AuthenticationService) {

  @RequestMapping(path = ["/authentication/user/{oasysUserId}"], method = [RequestMethod.GET])
  @ApiOperation(value = "Gets a user by its user code")
  @ApiResponses(ApiResponse(code = 404, message = "User not found"), ApiResponse(code = 200, message = "OK"))
  fun getUserByUserId(@PathVariable("oasysUserId") oasysUserId: String?): OasysUserAuthenticationDto {
    return authenticationService.getUserByUserId(oasysUserId)
  }

  @RequestMapping(path = ["/authentication/user/{oasysUserId}/offender/{offenderId}/{resource}"], method = [RequestMethod.GET])
  @ApiOperation(value = "Verifies a user has access to an offender")
  @ApiResponses(ApiResponse(code = 200, message = "OK"))
  fun getUserAuthorisedForOffenderId(
    @PathVariable("oasysUserId") oasysUserId: String?,
    @PathVariable("offenderId") offenderId: Long?,
    @PathVariable("resource") resource: OffenderPermissionResource?,
    @RequestParam("sessionId") sessionId: Long?
  ): AuthorisationDto {
    return authenticationService.userCanAccessOffenderRecord(oasysUserId, offenderId, sessionId, resource)
  }

  @RequestMapping(path = ["/authentication/user/validate"], method = [RequestMethod.POST])
  @ApiOperation(value = "Validates a users credentials")
  @ApiResponses(ApiResponse(code = 401, message = "Incorrect Credentials"), ApiResponse(code = 200, message = "OK"))
  fun validateUser(@RequestBody request: ValidateUserRequest): AuthenticationDto {
    return authenticationService.validateUserCredentials(request.user, request.password)
  }

  @RequestMapping(path = ["/authentication/user/email"], method = [RequestMethod.POST])
  @ApiOperation(value = "Gets a OASYS user code by its email address")
  @ApiResponses(ApiResponse(code = 200, message = "OK"))
  fun getUserCodeByEmail(@RequestBody email: UserRequestByEmail): UserDto {
    return authenticationService.getUserCodeByEmail(email.email)
  }
}
