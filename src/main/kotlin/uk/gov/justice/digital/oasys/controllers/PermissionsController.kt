package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto

class PermissionsController {

  @RequestMapping(path = ["/authentication/user/{userCode}"], method = [RequestMethod.POST])
  @ApiOperation(value = "Checks the permissions of a user in oasys")
  @ApiResponses(
    ApiResponse(code = 200, message = "OK"),
    ApiResponse(code = 403, message = "Unauthorised"),
    ApiResponse(code = 404, message = "USer not found")
  )
  fun getPermissionsForUserCode(
    @PathVariable("userCode") userCode: Long,
    @RequestBody permissions: PermissionsDto
  ): PermissionsResponseDto {
    throw NotImplementedError()
  }
}
