package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.jpa.entities.Section
import uk.gov.justice.digital.oasys.jpa.repositories.SectionRepository

@Service
class SectionService (private val sectionRepository: SectionRepository) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getSectionForAssessment(oasysSetId: Long?, sectionId: String): Section? {
        val section = sectionRepository.getSectionForAssessment(oasysSetId,sectionId)
        log.info("Found Section id: $sectionId for oasysSetId:$ {oasysSetId}")
        return section
    }

    fun getSectionsForAssessment(oasysSetId: Long?, sectionIds: Set<String>?): Collection<Section> {
        val sections = sectionRepository.getSectionsForAssessment(oasysSetId, sectionIds)
        log.info("Found ${sections?.size} Sections for oasysSetId: $oasysSetId")
        return sections.orEmpty()
    }

}
