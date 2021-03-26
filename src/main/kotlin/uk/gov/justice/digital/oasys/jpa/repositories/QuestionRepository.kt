package uk.gov.justice.digital.oasys.jpa.repositories

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.QOasysQuestion.oasysQuestion
import uk.gov.justice.digital.oasys.jpa.entities.QSection.section
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import javax.persistence.EntityManager

@Repository
class QuestionRepository(
  entityManager: EntityManager,
  private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getQuestionAnswersFromQuestionCodes(
    oasysSetId: Long,
    questionCodes: Map<String, Collection<String>>
  ): List<OasysQuestion> {

    val where = BooleanBuilder()

    for (questionCode in questionCodes) {
      where.or(
        oasysQuestion.refQuestion.refQuestionCode.`in`(questionCode.value)
          .and(section.refSection.refSectionCode.eq(questionCode.key))
      )
    }

    val query = queryFactory.selectFrom(oasysQuestion)
      .innerJoin(oasysQuestion.section, section)
      .where(section.oasysSetPk.eq(oasysSetId))
      .where(where)

    return query.fetch()
  }
}
