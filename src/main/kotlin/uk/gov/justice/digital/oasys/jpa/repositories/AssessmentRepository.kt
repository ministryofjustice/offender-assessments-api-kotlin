package uk.gov.justice.digital.oasys.jpa.repositories

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.entities.QAssessment.assessment
import javax.persistence.EntityManager

@Repository
class AssessmentRepository constructor(entityManager: EntityManager) {

    private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)

    fun getAssessment(oasysSetId: Long?): Assessment? {
        return queryFactory.selectFrom(assessment).where(assessment.oasysSetPk.eq(oasysSetId)).fetchFirst()
    }

    fun getLatestAssessmentForOffender(offenderId: Long?, filterGroupStatus: String? = null, filterAssessmentType: String? = null, filterVoided: Boolean? = null, filterAssessmentStatus: String? = null): Assessment? {
        val query = getAssessmentsQueryForOffender(offenderId)
        filterQuery(query, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
         return query.orderBy(assessment.createDate.desc())?.fetchFirst()
    }

    fun getAssessmentsForOffender(offenderId: Long?, filterGroupStatus: String? = null, filterAssessmentType: String? = null, filterVoided: Boolean? = null, filterAssessmentStatus: String? = null): Collection<Assessment>? {
        val query = getAssessmentsQueryForOffender(offenderId)
        filterQuery(query, filterGroupStatus, filterAssessmentType, filterVoided, filterAssessmentStatus)
        return query.fetch()
    }

    fun getAssessmentsForOffender(offenderId: Long?): Collection<Assessment>? {
        return getAssessmentsQueryForOffender(offenderId).fetch()
    }

    private fun getAssessmentsQueryForOffender(offenderId: Long?): JPAQuery<Assessment> {
        val query = queryFactory.selectFrom(assessment)
        return query.where(assessment.group.offenderPk.eq(offenderId))
    }

    private fun filterQuery(query: JPAQuery<Assessment>, filterGroupStatus: String?, filterAssessmentType: String?, filterVoided: Boolean?, filterAssessmentStatus: String?) {
        if(filterAssessmentStatus?.isNotEmpty() == true) {
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