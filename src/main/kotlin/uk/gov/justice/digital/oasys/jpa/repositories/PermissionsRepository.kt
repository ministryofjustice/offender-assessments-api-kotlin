package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.stereotype.Repository
import java.sql.Types
import javax.sql.DataSource

@Repository
class PermissionsRepository(
  val dataSource: DataSource
) {
  fun getPermissions(
    userCode: String,
    roleChecks: List<String>,
    area: String,
    offenderPk: Long?,
    oasysSetPk: Long?
  ): String {

    val query =
      """DECLARE 
            |LV_RES VARCHAR2(4000); 
            |BEGIN 
            |LV_RES := ARN_RESTFUL_API_PKG.allowed_operations(
            |p_checks_required => ?, p_user => ?, p_area => ?, p_offender_pk => ?, p_oasys_set_pk => ?);
            |? := LV_RES; END;""".trimMargin()

    dataSource.connection.prepareCall(query).use { function ->
      function.setString(1, roleChecks.joinToString())
      function.setString(2, userCode)
      function.setString(3, area)
      function.setString(4, offenderPk.toString())
      function.setString(5, oasysSetPk.toString())

      function.registerOutParameter(6, Types.VARCHAR)
      function.execute()
      return function.getString(6)
    }
  }
}
