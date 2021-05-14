package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.OasysUser

@Repository
interface UserRepository : CrudRepository<OasysUser?, String?> {

  fun findOasysUserByOasysUserCodeIgnoreCase(userCode: String): OasysUser?

  @Query(
    value =
    """SELECT SESSION_SNAPSHOT_PK 
      FROM SESSION_SNAPSHOT 
      WHERE SESSION_STATE_KEY='OFFENDER_PK' AND SESSION_STATE_VALUE=?1 AND SESSION_SNAPSHOT_PK IN (
          SELECT SESSION_SNAPSHOT_PK from SESSION_SNAPSHOT 
          WHERE SESSION_STATE_KEY='UNAME' AND SESSION_STATE_VALUE=?2) 
      AND ROWNUM <= 1
      ORDER BY TIME_STAMP DESC""",
    nativeQuery = true
  )
  fun findCurrentUserSessionForOffender(oasysOffenderId: Long, userCode: String): Long?

  fun findOasysUserByEmailAddressIgnoreCase(emailAddress: String): OasysUser?
}
