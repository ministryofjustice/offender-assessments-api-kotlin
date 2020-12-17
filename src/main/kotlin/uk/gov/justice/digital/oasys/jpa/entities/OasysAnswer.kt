package uk.gov.justice.digital.oasys.jpa.entities

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "OASYS_ANSWER")
data class OasysAnswer(

  @Id
  @Column(name = "OASYS_ANSWER_PK")
  val oasysAnswerPk: Long? = null,

  @ManyToOne
  @JoinColumn(name = "OASYS_QUESTION_PK")
  var oasysQuestion: OasysQuestion? = null,

  @OneToOne
  @JoinColumns(JoinColumn(name = "REF_ASS_VERSION_CODE", referencedColumnName = "REF_ASS_VERSION_CODE"), JoinColumn(name = "VERSION_NUMBER", referencedColumnName = "VERSION_NUMBER"), JoinColumn(name = "REF_SECTION_CODE", referencedColumnName = "REF_SECTION_CODE"), JoinColumn(name = "REF_QUESTION_CODE", referencedColumnName = "REF_QUESTION_CODE"), JoinColumn(name = "REF_ANSWER_CODE", referencedColumnName = "REF_ANSWER_CODE"))
  val refAnswer: RefAnswer? = null,

  @Column(name = "CREATE_DATE")
  var createDate: LocalDateTime? = null,

  @Column(name = "CREATE_USER")
  val createUser: String? = null,

  @Column(name = "LASTUPD_DATE")
  val lastupdDate: LocalDateTime? = null,

  @Column(name = "LASTUPD_USER")
  val lastupdUser: String? = null
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OasysAnswer) return false
    return oasysAnswerPk == other.oasysAnswerPk
  }

  override fun hashCode(): Int {
    return 31
  }
}
