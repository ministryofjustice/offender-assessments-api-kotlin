package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "SENTENCE")
data class Sentence(

        @Id
        @Column(name = "SENTENCE_CODE")
        val sentenceCode: String? = null,

        @Column(name = "SENTENCE_UK")
        val sentenceUk: Long? = null,

        @Column(name = "SENTENCE_CTID")
        val sentenceCtid: String? = null,

        @Column(name = "SENTENCE_DESC")
        val sentenceDesc: String? = null,

        @Column(name = "DISPLAY_SORT")
        val displaySort: Long? = null,

        @Column(name = "CUSTODIAL_IND")
        val custodialInd: String? = null,

        @Column(name = "CJA_IND")
        val cjaInd: String? = null,

        @OneToOne
        @JoinColumns(JoinColumn(name = "ORDER_TYPE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "ORDER_TYPE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
        val orderType: RefElement? = null,

        @Column(name = "CREATE_DATE")
        val createDate: LocalDateTime? = null

) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Sentence) return false
        return sentenceCode == o.sentenceCode
    }

    override fun hashCode(): Int {
        return 31
    }

}
