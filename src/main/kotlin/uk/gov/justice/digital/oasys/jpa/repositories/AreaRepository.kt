package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.CtAreaEst

@Repository
interface AreaRepository : JpaRepository<CtAreaEst, String> {
  @Query(
    "SELECT a.areaEstName FROM CtAreaEst a where a.ctAreaEstCode IN ?1"
  )
  fun findCtAreaEstByCtAreaEstCodes(ctAreaEstCode: Set<String>): Set<String>
}
