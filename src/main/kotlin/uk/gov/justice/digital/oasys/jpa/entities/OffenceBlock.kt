package uk.gov.justice.digital.oasys.jpa.entities

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "OFFENCE_BLOCK")
data class OffenceBlock (

    @Id
    @Column(name = "OFFENCE_BLOCK_PK")
    val offenceBlockPk: Long? = null,

    @Column(name = "DISPLAY_SORT")
    val displaySort: Long? = null,

    @Column(name = "OFFENCE_DATE")
    val offenceDate: LocalDate? = null,

    @Column(name = "RESENTENCE_FOR_BREACH_IND")
    val resentenceForBreachInd: String? = null,

    @Column(name = "SENTENCE_DATE")
    val sentenceDate: LocalDate? = null,

    @Column(name = "SENTENCE_LENGTH_CPO_HOURS")
    val sentenceLengthCpoHours: Long? = null,

    @Column(name = "SENTENCE_LENGTH_CRO_M")
    val sentenceLengthCroM: Long? = null,

    @Column(name = "SENTENCE_OTHER_FREE_TEXT")
    val sentenceOtherFreeText: String? = null,

    @Column(name = "SENT_LENGTH_CUST_DAYS")
    val sentLengthCustDays: Long? = null,

    @Column(name = "SNTNC_EXTND_LNGTH_MNTHS")
    val sntncExtndLngthMnths: Long? = null,

    @Column(name = "SENTENCE_COURT_RECOM")
    val sentenceCourtRecom: String? = null,

    @Column(name = "DISQUALIFICATION_ORDER")
    val disqualificationOrder: String? = null,

    @Column(name = "ORDER_AMENDED_IND")
    val orderAmendedInd: String? = null,

    @Column(name = "ORDER_AMENDED_DATE")
    val orderAmendedDate: LocalDateTime? = null,

    @OneToOne
    @JoinColumns(JoinColumn(name = "OFFENCE_BLOCK_TYPE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "OFFENCE_BLOCK_TYPE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
    val offenceBlockType: RefElement? = null,

    @OneToOne
    @JoinColumns(JoinColumn(name = "LEVEL_OF_SERIOUSNESS_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "LEVEL_OF_SERIOUSNESS_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
    val levelOfSeriousness: RefElement? = null,

    @OneToOne
    @JoinColumn(name = "COURT_PK", referencedColumnName = "COURT_PK")
    val court: Court? = null,

    @Column(name = "COURT_OTHER_TEXT")
    val courtOtherText: String? = null,

    @Column(name = "CREATE_DATE")
    val createDate: LocalDateTime? = null,

    @OneToOne(mappedBy = "offenceBlock")
    val offenceSentenceDetail: OffenceSentenceDetail? = null,

    @OneToOne
    @JoinColumn(name = "SENTENCE_CODE", referencedColumnName = "SENTENCE_CODE")
    val sentence: Sentence? = null

) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OffenceBlock) return false
        return offenceBlockPk == other.offenceBlockPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
