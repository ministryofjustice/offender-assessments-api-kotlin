package uk.gov.justice.digital.oasys.jpa.repositories


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uk.gov.justice.digital.oasys.jpa.entities.OffenderLink
import java.util.*


interface OffenderLinkRepository : JpaRepository<OffenderLink?, Long?> {
    @Query("SELECT o FROM OffenderLink o where (o.decidingOffenderPK = ?1 OR o.initiatingOffenderPK = ?1) " +
            "AND (o.linkType.refElementCode = 'MERGE_RETAIN' OR o.linkType.refElementCode = 'MERGE_RELINQUISH') " +
            "AND o.mergedOffenderPK IS NOT NULL")
    fun findMergedOffenderOrNull(offenderPK: Long?): OffenderLink?
}
