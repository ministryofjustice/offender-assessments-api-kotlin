package uk.gov.justice.digital.oasys.api

class PermissionsDto(
  val userCode: String,
  val roleChecks: Set<Roles>,
  val area: String,
  val offenderPk: Long? = null,
  val oasysSetPk: Long? = null,
  val assessmentType: AssessmentType? = null,
  val roleNames: RoleNames? = null
)

enum class RoleNames(val rbacName: String) {
  CREATE_BASIC_ASSESSMENT("Create Basic Assessment"),
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
