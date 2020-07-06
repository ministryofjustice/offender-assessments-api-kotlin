package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "OASYS_ASSESSMENT_GROUP")
@Entity
data class AssessmentGroup (
    @Id
    @Column(name = "OASYS_ASSESSMENT_GROUP_PK")
    val oasysAssessmentGroupPk: Long? = null,

    @Column(name = "OFFENDER_PK")
    val offenderPk: Long? = null,

    @Column(name = "ASSESSMENT_DATE_CLOSED")
    val assessmentDateClosed: LocalDateTime? = null,

    @Column(name = "HISTORIC_STATUS_ELM")
    val historicStatus: String? = null,

    @OneToMany
    @JoinColumn(name = "OASYS_ASSESSMENT_GROUP_PK", referencedColumnName = "OASYS_ASSESSMENT_GROUP_PK")
    val assessments: Set<Assessment>? = null

) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is AssessmentGroup) return false
        return oasysAssessmentGroupPk == o.oasysAssessmentGroupPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
