package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OFFENCE_SENTENCE_DETAIL")
class OffenceSentenceDetail(
  @Id
  @Column(name = "OFFENCE_SENTENCE_DETAIL_PK")
  val offenceSentenceDetailPk: Long? = null,

  @Column(name = "DISPLAY_SORT")
  val displaySort: Long? = null,

  @Column(name = "CJA_UNPAID_HOURS")
  val cjaUnpaidHours: Long? = null,

  @Column(name = "CJA_SUPERVISION_MONTHS")
  val cjaSupervisionMonths: Long? = null,

  @Column(name = "ACTIVITY_DESC")
  val activityDesc: String? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "YES_NO_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "YES_NO_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val yesNo: RefElement? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "SENTENCE_ATTRIBUTE_CAT", referencedColumnName = "REF_CATEGORY_CODE"), JoinColumn(name = "SENTENCE_ATTRIBUTE_ELM", referencedColumnName = "REF_ELEMENT_CODE"))
  val sentenceAttribute: RefElement? = null,

  @Column(name = "CREATE_DATE")
  val createDate: LocalDateTime? = null,

  @OneToOne
  @JoinColumn(name = "OFFENCE_BLOCK_PK", referencedColumnName = "OFFENCE_BLOCK_PK")
  val offenceBlock: OffenceBlock? = null
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OffenceSentenceDetail) return false
    return offenceSentenceDetailPk == other.offenceSentenceDetailPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
