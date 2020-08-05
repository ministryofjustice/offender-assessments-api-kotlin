package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.time.LocalDateTime

@Repository
interface ReferenceDataRepository : JpaRepository<RefElement?, Long?> {
    @Query("""
            SELECT r FROM RefElement r where (r.refCategoryCode = ?1)
            AND (?2 BETWEEN r.startDate and r.endDate)
            """)
    fun findAllByRefCategoryCodeAndBetweenStartAndEndDate(refCategoryCode: String?, endDate: LocalDateTime?): Collection<RefElement?>?
}