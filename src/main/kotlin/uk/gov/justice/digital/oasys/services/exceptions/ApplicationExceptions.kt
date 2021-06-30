package uk.gov.justice.digital.oasys.services.exceptions

import uk.gov.justice.digital.oasys.api.ErrorDetailsDto
import uk.gov.justice.digital.oasys.api.PermissionsDetailsDto

class EntityNotFoundException(msg: String) : RuntimeException(msg)

class DuplicateOffenderRecordException(msg: String) : RuntimeException(msg)

class UserNotAuthorisedException(msg: String) : RuntimeException(msg)

class InvalidOasysRequestException(msg: String) : RuntimeException(msg)

class UserPermissionsChecksFailedException(msg: String, val permissions: PermissionsDetailsDto) : RuntimeException(msg)

class UserPermissionsBadRequestException(msg: String, val errors: List<ErrorDetailsDto>? = null) : RuntimeException(msg)
