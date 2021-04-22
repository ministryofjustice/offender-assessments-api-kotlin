package uk.gov.justice.digital.oasys.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.oasys.api.PermissionsDto
import uk.gov.justice.digital.oasys.api.PermissionsResponseDto
import uk.gov.justice.digital.oasys.services.PermissionsService

@RestController
@Api(value = "Permissions ", tags = ["Permissions"])
class PermissionsController(private val permissionsService: PermissionsService) {

  @RequestMapping(path = ["/authentication/user/{userCode}"], method = [RequestMethod.POST])
  @ApiOperation(value = "Checks the permissions of a user in oasys")
  @ApiResponses(
    ApiResponse(code = 200, message = "OK"),
    ApiResponse(code = 403, message = "Unauthorised"),
    ApiResponse(code = 404, message = "USer not found")
  )
  fun getPermissionsForUserCode(
    @PathVariable("userCode") userCode: String,
    @RequestBody permissions: PermissionsDto
  ): PermissionsResponseDto {
    return permissionsService.getPermissions(
      userCode,
      permissions.roleChecks,
      permissions.area,
      permissions.offenderPk,
      permissions.oasysSetPk,
      permissions.assessmentType,
      permissions.roleNames
    )
  }
}
