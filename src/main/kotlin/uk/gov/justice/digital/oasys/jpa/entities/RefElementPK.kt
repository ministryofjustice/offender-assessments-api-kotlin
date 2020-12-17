package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

data class RefElementPK(
  @Column(name = "REF_CATEGORY_CODE")
  @Id
  private val refCategoryCode: String? = null,

  @Column(name = "REF_ELEMENT_CODE")
  @Id
  private val refElementCode: String? = null
) : Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RefElementPK) return false
    return refCategoryCode == other.refCategoryCode &&
      refElementCode == other.refElementCode
  }

  override fun hashCode(): Int {
    return 31
  }
}
