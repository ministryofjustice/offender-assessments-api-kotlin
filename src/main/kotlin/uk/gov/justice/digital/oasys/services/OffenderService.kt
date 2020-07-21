package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.OffenderDto
import uk.gov.justice.digital.oasys.jpa.entities.Offender
import uk.gov.justice.digital.oasys.jpa.entities.OffenderLink
import uk.gov.justice.digital.oasys.jpa.repositories.OffenderLinkRepository
import uk.gov.justice.digital.oasys.jpa.repositories.OffenderRepository

@Service
class OffenderService(private val offenderRepository: OffenderRepository,
                      private val offenderLinkRepository: OffenderLinkRepository) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun getOffenderIdByIdentifier(identityType: String?, identity: String?) : Long? {
        return getOffenderFromRepository(identityType, identity)?.offenderPk
    }

    fun getOffender(identityType: String?, identity: String?) : OffenderDto {
        val offender = OffenderDto.from(getOffenderFromRepository(identityType, identity))
        AssessmentService.log.info("Found Offender for identity: $identity ,$identityType)")
        return offender
    }

    private fun getOffenderFromRepository(identityType : String?, identity: String?) : Offender? {
        return checkForOffenderMerge(offenderRepository.getOffender(identityType, identity))

    }

    private fun checkForOffenderMerge(offender : Offender?) : Offender? {
        if(offender?.mergeIndicated.equals("Y")) {
            val linkedOffender = offenderLinkRepository.findMergedOffenderOrNull(offender?.offenderPk)
            if (linkedOffender != null) {
                val mergedOffenderPK = findMergedOffenderPK(linkedOffender)
                val mergedOffender = getOffenderFromRepository("oasysOffenderId", mergedOffenderPK.toString())
                mergedOffender?.mergedOffenderPK = offender?.offenderPk
                return mergedOffender
            }
        }
        return offender
    }

    private fun findMergedOffenderPK(mergedOffender : OffenderLink?) : Long? {
            val linkedOffender = offenderLinkRepository.findMergedOffenderOrNull(mergedOffender?.mergedOffenderPK)
            if (linkedOffender != null) {
                return findMergedOffenderPK(linkedOffender)
            }
            return mergedOffender?.mergedOffenderPK
        }
}