package uk.gov.justice.digital.oasys.controllers.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import uk.gov.justice.digital.oasys.api.ErrorResponse
import uk.gov.justice.digital.oasys.services.exceptions.DuplicateOffenderRecordException
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import uk.gov.justice.digital.oasys.services.exceptions.UserNotAuthorisedException
import uk.gov.justice.digital.oasys.services.exceptions.UserPermissionsChecksFailedException

@ControllerAdvice
class ControllerAdvice {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  @ExceptionHandler(EntityNotFoundException::class)
  fun handle(e: EntityNotFoundException): ResponseEntity<ErrorResponse?> {
    log.info("EntityNotFoundException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 404, developerMessage = e.message), HttpStatus.NOT_FOUND)
  }

  @ExceptionHandler(DuplicateOffenderRecordException::class)
  fun handle(e: DuplicateOffenderRecordException): ResponseEntity<ErrorResponse?> {
    log.error("DuplicateOffenderRecordException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 409, developerMessage = e.message, userMessage = e.message), HttpStatus.CONFLICT)
  }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handle(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse?> {
    log.info("MethodArgumentNotValidException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 400, developerMessage = e.message), HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(HttpMessageConversionException::class)
  fun handle(e: HttpMessageConversionException): ResponseEntity<*> {
    log.error("HttpMessageConversionException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 400, developerMessage = e.message), HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handle(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse?> {
    log.error("HttpMessageNotReadableException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 400, developerMessage = e.message), HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(IllegalArgumentException::class)
  fun handle(e: IllegalArgumentException): ResponseEntity<ErrorResponse?> {
    log.error("IllegalArgumentException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 400, developerMessage = e.message), HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(UserNotAuthorisedException::class)
  fun handle(e: UserNotAuthorisedException): ResponseEntity<ErrorResponse?> {
    log.error("UserNotAuthorisedException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 401, developerMessage = e.message), HttpStatus.UNAUTHORIZED)
  }

  @ExceptionHandler(UserPermissionsChecksFailedException::class)
  fun handle(e: UserPermissionsChecksFailedException): ResponseEntity<ErrorResponse?> {
    log.error("UserPermissionsChecksFailedException: {}", e.message)
    return ResponseEntity(ErrorResponse(status = 403, developerMessage = e.message, payload = e.permissions.toString()), HttpStatus.FORBIDDEN)
  }

  @ExceptionHandler(Exception::class)
  fun handle(e: Exception): ResponseEntity<ErrorResponse?> {
    log.error("Exception: {}", e.message)
    return ResponseEntity(
      ErrorResponse(
        status = 500,
        developerMessage = "Internal Server Error. Check Logs",
        userMessage = "An unexpected error has occurred"
      ),
      HttpStatus.INTERNAL_SERVER_ERROR
    )
  }
}
