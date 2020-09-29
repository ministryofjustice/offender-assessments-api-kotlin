package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "REF_ELEMENT")
@IdClass(RefElementPK::class)
data class RefElement (
    @Column(name = "REF_ELEMENT_UK")
    val refElementUk: Long? = null,

    @Id
    @Column(name = "REF_CATEGORY_CODE")
    val refCategoryCode: String? = null,

    @Id
    @Column(name = "REF_ELEMENT_CODE")
    val refElementCode: String? = null,

    @Column(name = "REF_ELEMENT_CTID")
    val refElementCtid: String? = null,

    @Column(name = "REF_ELEMENT_SHORT_DESC")
    val refElementShortDesc: String? = null,

    @Column(name = "REF_ELEMENT_DESC")
    val refElementDesc: String? = null,

    @Column(name = "DISPLAY_SORT")
    val displaySort: Long? = null,

    @Column(name = "START_DATE")
    val startDate: LocalDateTime? = null,

    @Column(name = "END_DATE")
    val endDate: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefElement) return false
        return refCategoryCode == other.refCategoryCode &&
                refElementCode == other.refElementCode
    }

    override fun hashCode(): Int {
        return 31
    }
}