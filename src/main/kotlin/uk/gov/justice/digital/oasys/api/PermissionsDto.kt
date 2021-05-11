package uk.gov.justice.digital.oasys.api

class PermissionsDto(
  val userCode: String,
  val roleChecks: Set<Roles>,
  val area: String,
  val offenderPk: Long? = null,
  val oasysSetPk: Long? = null,
  val assessmentType: AssessmentType? = null,
  val roleNames: Set<RoleNames>? = emptySet()
)

enum class RoleNames(val rbacName: String) {
  CREATE_BASIC_ASSESSMENT("Create Basic Assessment"),
  CREATE_FULL_ASSESSMENT("Create Full Assessment"),
  CREATE_STANDARD_ASSESSMENT("Create Standard Assessment"),
  EDIT_SARA("Edit SARA"),
  EDIT_SIGN_AND_LOCK_THE_ASSESSMENT("Edit Sign And Lock The Assessment"),
  OPEN_OFFENDER_RECORD("Open Offender Record"),
  OPEN_SARA("Open SARA"),
  CREATE_OFFENDER("Create Offender"),
  CREATE_RISK_OF_HARM_ASSESSMENT("Create Risk of Harm Assessment");
  companion object {
    fun findByRbacName(rbacName: String): RoleNames? {
      return values().firstOrNull { value -> value.rbacName == rbacName }
    }
  }
}

enum class AssessmentType {
  SHORT_FORM_PSR,
  LONG_FORM_PSR
}

enum class Roles {
  RBAC_OTHER,
  ASSESSMENT_READ,
  ASSESSMENT_EDIT,
  RBAC_SARA_EDIT,
  RBAC_SARA_CREATE,
  OFF_ASSESSMENT_CREATE
}
