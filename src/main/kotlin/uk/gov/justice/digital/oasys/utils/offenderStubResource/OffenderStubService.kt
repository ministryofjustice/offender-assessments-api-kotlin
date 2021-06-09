package uk.gov.justice.digital.oasys.utils.offenderStubResource

import org.springframework.stereotype.Service

@Service
class OffenderStubService(val offenderStubRepository: OffenderStubRepository) {

  fun getOffenderStubs(): List<OffenderStub>? {
    return offenderStubRepository.getOffenderStubs()
  }
}
