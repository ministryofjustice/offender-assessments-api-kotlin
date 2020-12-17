package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.RefElementDto
import uk.gov.justice.digital.oasys.jpa.repositories.ReferenceDataRepository
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import java.time.LocalDateTime

@Service
class ReferenceDataService(private val referenceDataRepository: ReferenceDataRepository) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getActiveReferenceDataOfCategory(categoryCode: String?): Collection<RefElementDto?>? {
    log.info("Retrieving reference data of category: $categoryCode")
    val referenceData = referenceDataRepository.findAllByRefCategoryCodeAndBetweenStartAndEndDate(categoryCode, LocalDateTime.now())
    if (referenceData.isNullOrEmpty()) {
      throw EntityNotFoundException("Category $categoryCode, not found")
    }
    log.info("Found reference data of category: $categoryCode")
    return referenceData.map { RefElementDto.from(it) }
  }
}
