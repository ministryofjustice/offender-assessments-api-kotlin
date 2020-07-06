package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Id

data class RefElementPK (
    @Column(name = "REF_CATEGORY_CODE")
    @Id
    private val refCategoryCode: String? = null,

    @Column(name = "REF_ELEMENT_CODE")
    @Id
    private val refElementCode: String? = null
)  : Serializable {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is RefElementPK) return false
        return  refCategoryCode == o.refCategoryCode &&
                refElementCode == o.refElementCode
    }

    override fun hashCode(): Int {
        return 31
    }
}