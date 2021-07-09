package uk.gov.justice.digital.oasys.utils.offenderStubResource

import java.time.LocalDateTime

class OffenderStubDto(
  val offenderPk: Long,
  val crn: String?,
  val pnc: String?,
  val familyName: String?,
  val forename1: String?,
  val laoIndicator: String?,
  val areaCode: String?,
  val createDate: LocalDateTime?,
  val createUser: String?,
  val updatedDate: LocalDateTime?,
  val updatedUser: String?
) {

  companion object {
    fun from(offenderStubEntities: List<OffenderStubEntity>): List<OffenderStubDto> {
      return offenderStubEntities.map { from(it) }
    }

    fun from(offenderStubEntity: OffenderStubEntity): OffenderStubDto {
      with(offenderStubEntity) {
        return OffenderStubDto(
          offenderPk = offenderPk,
          crn = crn,
          pnc = pnc,
          familyName = familyName,
          forename1 = forename1,
          laoIndicator = laoIndicator,
          areaCode = areaCode,
          createDate = createDate,
          createUser = createUser,
          updatedDate = updatedDate,
          updatedUser = updatedUser
        )
      }
    }
  }
}
