package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

class RefAssessmentVersionPK(
  @Id
  @Column(name = "REF_ASS_VERSION_CODE")
  val refAssVersionCode: String? = null,

  @Id
  @Column(name = "VERSION_NUMBER")
  val versionNumber: String? = null
) : Serializable {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RefAssessmentVersionPK) return false
    return refAssVersionCode == other.refAssVersionCode &&
      versionNumber == other.versionNumber
  }

  override fun hashCode(): Int {
    return 31
  }
}
