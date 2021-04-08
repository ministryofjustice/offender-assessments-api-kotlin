package uk.gov.justice.digital.oasys.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.FilteredRefDataRequestDto
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.jpa.entities.ReferenceDataEntity
import uk.gov.justice.digital.oasys.jpa.entities.ReferenceDataFailureEntity
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceDataRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import java.time.LocalDateTime

@Service
class ReferenceDataService(private val referenceDataRepository: ReferenceDataRepository) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  private val objectMapper: ObjectMapper = jacksonObjectMapper()

  init {
    objectMapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  }

  fun getActiveReferenceDataOfCategory(categoryCode: String?): Collection<RefElementDto?>? {
    log.info("Retrieving reference data of category: $categoryCode")
    val referenceData =
      referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(categoryCode, LocalDateTime.now())
    if (referenceData.isNullOrEmpty()) {
      throw EntityNotFoundException("Category $categoryCode, not found")
    }
    log.info("Found reference data of category: $categoryCode")
    return referenceData.map { RefElementDto.from(it) }
  }

  fun getFilteredReferenceData(filteredRefDataRequest: FilteredRefDataRequestDto): Map<String, Collection<RefElementDto>> {
    log.info("Retrieving reference data for field ${filteredRefDataRequest.fieldName}")
    with(filteredRefDataRequest) {
      val response = referenceDataRepository.findFilteredReferenceData(
        oasysUserCode,
        oasysAreaCode,
        team,
        assessor,
        offenderPk,
        oasysSetPk,
        assessmentType,
        sectionCode,
        fieldName,
        parentList
      )

      val result: ReferenceDataEntity = objectMapper.readValue(response)
      logErrors(response)
      when (result.state) {
        "SUCCESS" -> {
          return result.referenceData?.entries?.associate {
            it.key to it.value.map { item ->
              RefElementDto.from(item)
            }
          }.orEmpty().also { log.info("Returned ${it.size} items of reference data") }
        }
        "MAPPING_ERROR" -> throw IllegalArgumentException("Invalid parameters passed for filtered reference data $fieldName, check logs")
        "OASYS_FAIL" -> throw Exception("Internal OASys error when calling create_assessment function for offender $offenderPk, check logs")
        "SYSTEM_ERROR" -> throw Exception("Unknown OASys error when calling create_assessment function for offender $offenderPk, check logs")
        else -> throw Exception(
          "Unknown STATE returned by update assessment function. " +
            "User: $oasysUserCode, areaCode $oasysAreaCode, " +
            "Offender: $offenderPk, AssessmentType: $assessmentType"
        )
      }
    }
  }

  private fun logErrors(response: String) {

    val result: ReferenceDataFailureEntity = objectMapper.readValue(response)
    result?.errorDetail?.failures?.forEach {
      it.errors.forEach { err ->
        log.error("OASYs Validation Error: $err")
      }
    }
  }
}
