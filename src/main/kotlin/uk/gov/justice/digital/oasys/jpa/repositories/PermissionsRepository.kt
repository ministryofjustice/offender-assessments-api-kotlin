package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.Types
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PermissionsRepository(
  @PersistenceContext
  private val em: EntityManager
) {
  fun getPermissions(
    userCode: String,
    roleChecks: List<String>,
    area: String,
    offenderPk: Long?,
    oasysSetPk: Long?
  ): String {
    val connection = em.unwrap(Connection::class.java)

    val query =
      """DECLARE 
            |LV_RES VARCHAR2(4000); 
            |BEGIN 
            |LV_RES := ARN_RESTFUL_API_PKG.allowed_operations(
            |p_checks_required => ?, p_user => ?, p_area => ?, p_offender_pk => ?, p_oasys_set_pk => ?);
            |? := LV_RES; END;""".trimMargin()

    connection.prepareCall(query).use { function ->
      function.setString(1, roleChecks.joinToString(prefix = "'", postfix = "'"))
      function.setString(2, userCode)
      function.setString(3, area)
      offenderPk?.let {
        function.setLong(4, offenderPk)
      }
      oasysSetPk?.let {
        function.setLong(5, oasysSetPk)
      }
      function.registerOutParameter(6, Types.VARCHAR)
      function.execute()
      return function.getString(6)
    }
  }
}
