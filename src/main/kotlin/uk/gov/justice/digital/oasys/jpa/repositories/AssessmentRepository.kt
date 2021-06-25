package uk.gov.justice.digital.oasys.jpa.repositories

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.QAssessment.assessment
import java.time.LocalDateTime
import javax.persistence.EntityManager

@Repository
class AssessmentRepository constructor(entityManager: EntityManager) {

  private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)

  fun getAssessment(oasysSetId: Long?): Assessment? {
    return queryFactory.selectFrom(assessment).where(assessment.oasysSetPk.eq(oasysSetId)).fetchFirst()
  }

  fun getAssessmentsForOffender(
    offenderId: Long?,
    filterGroupStatus: String? = null,
    filterAssessmentType: String? = null,
    filterVoided: Boolean? = null,
    filterAssessmentStatus: String? = null
  ): Collection<Assessment> {
    val query = getAssessmentsQueryForOffender(offenderId)
    filterQuery(query, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
    val assessments = query.fetch()
    return assessments ?: emptySet()
  }

  fun getLatestAssessmentsForOffenderInPeriod(
    offenderId: Long?,
    filterAssessmentType: Set<String>?,
    filterAssessmentStatus: String?,
    dateCompletedFrom: LocalDateTime
  ): Assessment? {
    val query = queryFactory.selectFrom(assessment)
    query
      .where(assessment.group.offenderPk.eq(offenderId))
      .where(assessment.deletedDate.isNull)
    if (filterAssessmentStatus?.isNotEmpty() == true) {
      query.where(assessment.assessmentStatus.equalsIgnoreCase(filterAssessmentStatus))
    }
    if (filterAssessmentType?.isNotEmpty() == true && !filterAssessmentType?.isNullOrEmpty()) {
      query.where(assessment.assessmentType.`in`(filterAssessmentType))
    }
    query
      .where(assessment.dateCompleted.after(dateCompletedFrom))
      .orderBy(assessment.dateCompleted.desc())
    return query.fetchFirst()
  }

  fun getAssessmentsForOffender(offenderId: Long?): Collection<Assessment>? {
    return getAssessmentsQueryForOffender(offenderId).fetch()
  }

  private fun getAssessmentsQueryForOffender(offenderId: Long?): JPAQuery<Assessment> {
    val query = queryFactory.selectFrom(assessment)
    return query.where(assessment.group.offenderPk.eq(offenderId)).where(assessment.deletedDate.isNull)
  }

  private fun filterQuery(
    query: JPAQuery<Assessment>,
    filterGroupStatus: String?,
    filterAssessmentType: String?,
    filterVoided: Boolean?,
    filterAssessmentStatus: String?
  ) {
    if (filterAssessmentStatus?.isNotEmpty() == true) {
      query.where(assessment.assessmentStatus.equalsIgnoreCase(filterAssessmentStatus))
    }
    if (filterAssessmentType?.isNotEmpty() == true) {
      query.where(assessment.assessmentType.equalsIgnoreCase(filterAssessmentType))
    }
    if (filterGroupStatus?.isNotEmpty() == true) {
      query.where(assessment.group.historicStatus.equalsIgnoreCase(filterGroupStatus))
    }
    if (filterVoided != null) {
      if (filterVoided) {
        query.where(assessment.assessmentVoidedDate.isNotNull)
      } else {
        query.where(assessment.assessmentVoidedDate.isNull)
      }
    }
  }
}
