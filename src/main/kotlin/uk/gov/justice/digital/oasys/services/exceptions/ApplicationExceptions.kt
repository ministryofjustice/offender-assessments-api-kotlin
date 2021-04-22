package uk.gov.justice.digital.oasys.services.exceptions

class EntityNotFoundException(msg: String) : RuntimeException(msg)

class DuplicateOffenderRecordException(msg: String) : RuntimeException(msg)

class UserNotAuthorisedException(msg: String) : RuntimeException(msg)

class InvalidOasysPermissions(msg: String) : RuntimeException(msg)
