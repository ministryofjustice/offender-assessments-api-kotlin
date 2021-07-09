package uk.gov.justice.digital.oasys.utils.offenderStubResource

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Profile("offenderstub")
@RestController
class OffenderStubController(val offenderStubService: OffenderStubService) {

  @GetMapping(path = ["/offender/stub"])
  fun getOffenderStubs(): List<OffenderStubDto>? {
    return offenderStubService.getOffenderStubs()
  }
}
