package uk.gov.justice.digital.oasys.jpa.entities

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "OBJECTIVE")
data class Objective (
        @Id
        @Column(name = "OBJECTIVE_CODE")
        val objectiveCode: String? = null,

        @Column(name = "OBJECTIVE_UK")
        private var objectiveUk: Long? = null,

        @Column(name = "OBJECTIVE_DESC")
        val objectiveDesc: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "OBJECTIVE_HEADING_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OBJECTIVE_HEADING_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        val objectiveHeading: RefElement? = null,

        @Column(name = "START_DATE")
        private val startDate: Time? = null,

        @Column(name = "END_DATE")
        private val endDate: Time? = null

) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Objective) return false
        return objectiveCode == o.objectiveCode
    }

    override fun hashCode(): Int {
        return 31
    }

}
