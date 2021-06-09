package uk.gov.justice.digital.oasys.utils.offenderStubResource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OffenderStubController(val offenderStubService: OffenderStubService) {

  @GetMapping(path = ["/offender/stub"])
  fun getAssessment(): List<OffenderStub>? {
    return offenderStubService.getOffenderStubs()
  }
}
