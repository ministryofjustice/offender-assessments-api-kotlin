package uk.gov.justice.digital.oasys.api

object DtoUtils {

  fun String?.ynToBoolean(): Boolean? {
    return if (!(this.equals("Y", ignoreCase = true) || this.equals("N", ignoreCase = true))) {
      null
    } else this.equals("Y", ignoreCase = true)
  }
}
