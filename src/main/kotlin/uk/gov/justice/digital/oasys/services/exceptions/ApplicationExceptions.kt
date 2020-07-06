package uk.gov.justice.digital.oasys.services.exceptions

class EntityNotFoundException(msg: String) : RuntimeException(msg)

class DuplicateOffenderRecordException(msg: String) : RuntimeException(msg)
