package uk.gov.justice.digital.oasys.jpa.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.api.OffenderIdentifier
import uk.gov.justice.digital.oasys.jpa.entities.Offender
import uk.gov.justice.digital.oasys.jpa.entities.QOffender.offender
import uk.gov.justice.digital.oasys.services.exceptions.DuplicateOffenderRecordException
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import javax.persistence.EntityManager

@Repository
class OffenderRepository constructor(entityManager: EntityManager) {
  private val queryFactory = JPAQueryFactory(entityManager)

  fun getOffender(identifierType: String?, identifier: String?): Offender {
    val offenderIdentifier = OffenderIdentifier.fromString(identifierType)
      ?: throw IllegalArgumentException("Identifier type not found for $identifierType, $identifier")
    return getOffenderByIdentifier(offenderIdentifier, identifier)
  }

  private fun getOffenderByIdentifier(identityType: OffenderIdentifier?, identity: String?): Offender {
    if (identity == null) { throw IllegalArgumentException("Identifier id is null") }
    val query = queryFactory.selectFrom(offender)
    when (identityType) {
      OffenderIdentifier.CRN -> {
        query.where(
          offender.cmsProbNumber.toUpperCase().eq(identity.uppercase())
            .and(offender.deletedDate.isNull)
        )
      }
      OffenderIdentifier.PNC -> {
        query.where(
          offender.pnc.toUpperCase().eq(identity.uppercase())
            .and(offender.deletedDate.isNull)
        )
      }
      OffenderIdentifier.NOMIS -> {
        query.where(
          offender.cmsPrisNumber.toUpperCase().eq(identity.uppercase())
            .and(offender.deletedDate.isNull)
        )
      }
      OffenderIdentifier.OASYS -> query.where(offender.offenderPk.eq(identity.toLong()))
      OffenderIdentifier.BOOKING -> {
        query.where(
          offender.prisonNumber.toUpperCase().eq(identity.uppercase())
            .and(offender.deletedDate.isNull)
        )
      }
      else -> throw EntityNotFoundException("Offender not found for $identityType, $identity")
    }
    val result = query.fetch()

    when {
      result.size == 1 -> return result[0]
      result.size > 1 -> throw DuplicateOffenderRecordException("Duplicate offender found for $identityType $identity")
      else -> throw EntityNotFoundException("Offender not found for $identityType, $identity")
    }
  }
}
