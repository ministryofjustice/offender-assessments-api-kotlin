package uk.gov.justice.digital.oasys.jpa.repositories

import org.hibernate.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.sql.Connection
import java.sql.Types
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

interface ReferenceDataDao {
  fun findFilteredReferenceData(
    username: String,
    area: String,
    team: String? = null,
    assessor: String? = null,
    offenderPk: Long? = null,
    oasysSetPk: Long? = null,
    assessmentType: String? = null,
    section: String? = null,
    fieldName: String,
    parentList: Map<String, String>? = emptyMap(),
    stage: String? = "CONTINUE"
  ): String
}

@Repository
interface ReferenceDataRepository : JpaRepository<RefElement?, Long?>, ReferenceDataDao {
  @Query(
    """
 SELECT r FROM RefElement r where (r.refCategoryCode = ?1)
 AND (?2 BETWEEN r.startDate and r.endDate)
 """
  )
  fun findAllByRefCategoryCodeAndBetweenStartAndEndDate(
    refCategoryCode: String?,
    endDate: LocalDateTime?
  ): Collection<RefElement?>?
}

class ReferenceDataRepositoryImpl constructor(@PersistenceContext var em: EntityManager) {

  fun findFilteredReferenceData(
    username: String,
    area: String?,
    team: String?,
    assessor: String?,
    offenderPk: Long?,
    oasysSetPk: Long?,
    assessmentType: String?,
    section: String?,
    fieldName: String?,
    parentList: Map<String, String>? = emptyMap(),
    stage: String? = "CONTINUE"
  ): String {

    val session = em.unwrap(Session::class.java)

    val query =
      """DECLARE 
            |LV_RES VARCHAR2(4000); 
            |BEGIN 
            |LV_RES := ARN_RESTFUL_API_PKG.refdata( 
            |p_user => ?,
            |p_area => ?, 
            |p_team => ?, 
            |p_assessor => ?, 
            |p_offender_pk=> ?, 
            |p_oasys_set_pk => ?, 
            |p_assessment_type => ?, 
            |p_stage => ?, 
            |p_section => ?, 
            |p_fieldname => ?, 
            |p_parent_list => ?, 
            |p_parent_values => ?);
            |? := LV_RES; END;""".trimMargin()

    return session.doReturningWork { connection: Connection ->
      connection.prepareCall(query).use { function ->
        function.setString(1, username)
        function.setString(2, area)
        function.setString(3, team)
        function.setString(4, assessor)
        if (offenderPk == null) function.setNull(5, Types.BIGINT) else function.setLong(5, offenderPk)
        if (oasysSetPk == null) function.setNull(6, Types.BIGINT) else function.setLong(6, oasysSetPk)
        function.setString(7, assessmentType)
        function.setString(8, stage)
        function.setString(9, section)
        function.setString(10, fieldName)
        function.setString(11, parentList?.keys?.joinToString(","))
        function.setString(12, parentList?.values?.joinToString(","))
        function.registerOutParameter(13, Types.VARCHAR)
        function.execute()
        return@doReturningWork function.getString(13)
      }
    }
  }
}
