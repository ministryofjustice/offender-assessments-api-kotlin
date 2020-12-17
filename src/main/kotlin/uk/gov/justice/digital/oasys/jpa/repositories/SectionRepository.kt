package uk.gov.justice.digital.oasys.jpa.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.QSection.section
import uk.gov.justice.digital.oasys.jpa.entities.Section
import javax.persistence.EntityManager

@Repository
class SectionRepository(
  entityManager: EntityManager,
  private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getSectionForAssessment(oasysSetId: Long?, sectionCode: String?): Section? {
    val query = queryFactory.selectFrom(section)
    query.where(section.oasysSetPk.eq(oasysSetId))
    query.where(section.refSection.refSectionCode.eq(sectionCode))
    // Section shouldn't throw not found as we use it to calculate things, which if null we can ignore.
    log.run {
      query.where(section.oasysSetPk.eq(oasysSetId))
      query.where(section.refSection.refSectionCode.eq(sectionCode))
      // Section shouldn't throw not found as we use it to calculate things, which if null we can ignore.
      warn("Section $sectionCode for OasysSetId $oasysSetId, not found")
    }
    return query.fetchFirst()
  }

  fun getSectionsForAssessment(oasysSetId: Long?, sectionCodes: List<String>): Collection<Section>? {
    val query = queryFactory.selectFrom(section)
    query.where(section.oasysSetPk.eq(oasysSetId))
    query.where(section.refSection.refSectionCode.`in`(sectionCodes))
    return query.fetch()
  }
}
