package uk.gov.justice.digital.oasys.utils.offenderStubResource

import org.springframework.stereotype.Service

@Service
class OffenderStubService(val offenderStubRepository: OffenderStubRepository) {

  fun getOffenderStubs(): List<OffenderStubDto> {
    val offenderStubEntities = offenderStubRepository.getOffenderStubs()
    return OffenderStubDto.from(offenderStubEntities)
  }
}
