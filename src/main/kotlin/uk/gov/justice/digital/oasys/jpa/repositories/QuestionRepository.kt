package uk.gov.justice.digital.oasys.jpa.repositories

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

  fun getQuestionAnswersFromQuestionCodes(oasysSetId: Long, questionCodes: Collection<String>): List<OasysQuestion> {
    val answers = queryFactory.selectFrom(oasysQuestion)
      .innerJoin(oasysQuestion.section, section)
      .where(section.oasysSetPk.eq(oasysSetId))
      .where(oasysQuestion.refQuestion.refQuestionCode.`in`(questionCodes)).fetch()

    if (answers.isNullOrEmpty()) throw EntityNotFoundException("Assessment or question codes not found for assessment $oasysSetId")
    return answers
  }
}
