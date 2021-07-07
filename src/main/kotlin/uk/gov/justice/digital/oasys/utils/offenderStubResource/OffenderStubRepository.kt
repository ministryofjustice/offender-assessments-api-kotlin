package uk.gov.justice.digital.oasys.utils.offenderStubResource

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.utils.offenderStubResource.QOffenderStub.offenderStub
import javax.persistence.EntityManager

@Repository
class OffenderStubRepository constructor(entityManager: EntityManager) {
  private val queryFactory: JPAQueryFactory = JPAQueryFactory(entityManager)

  fun getOffenderStubs(): List<OffenderStubEntity> {
    return queryFactory.selectFrom(offenderStub)
      .fetch()
  }
}
