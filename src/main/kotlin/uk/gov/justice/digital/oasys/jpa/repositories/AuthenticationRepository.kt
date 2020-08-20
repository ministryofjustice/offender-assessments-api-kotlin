package uk.gov.justice.digital.oasys.jpa.repositories

import org.hibernate.Session
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.Types
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AuthenticationRepository(@PersistenceContext
                                    private val em: EntityManager) {

    fun validateCredentials(username: String, password: String): String? {
        val session = em.unwrap(Session::class.java)

        // The function indirectly updates the database and so must be called within an anonymous block
        // We therefore have to call the function using the raw PL SQL
        val query = """DECLARE 
            |LV_RES VARCHAR2(4000); 
            |BEGIN 
            |LV_RES := RESTFUL_API_PKG.USER_LOGIN(P_USER => ?, P_PASSWORD => ?);
            |? := LV_RES; END;""".trimMargin()

        return session.doReturningWork { connection: Connection ->
            connection.prepareCall(query).use { function ->
                function.setString(1, username)
                function.setString(2, password)
                function.registerOutParameter(3, Types.VARCHAR)
                function.execute()
                return@doReturningWork function.getString(3)
            }
        }
    }

    fun validateUserSentencePlanAccessWithSession(username: String, oasysOffenderId: Long, sessionId: Long): String? {
        val session = em.unwrap(Session::class.java)

        val query = """DECLARE 
            |LV_RES VARCHAR2(4000);
            |BEGIN LV_RES := RESTFUL_API_PKG.SENTENCE_PLAN(P_USER => ?, P_OFFENDER_PK => ?, P_SESSION_SNAPSHOT_PK => ?);
            |? := LV_RES; END;""".trimMargin()

        return session.doReturningWork { connection: Connection ->
            connection.prepareCall(query).use { function ->
                function.setString(1, username)
                function.setLong(2, oasysOffenderId)
                function.setLong(3, sessionId)
                function.registerOutParameter(4, Types.VARCHAR)
                function.execute()
                return@doReturningWork function.getString(4)
            }
        }
    }
}