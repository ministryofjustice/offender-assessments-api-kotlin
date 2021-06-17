package uk.gov.justice.digital.oasys.api

import java.time.temporal.ChronoUnit

enum class PeriodUnit(val chronoUnit: ChronoUnit) {
  DAY(ChronoUnit.DAYS), MONTH(ChronoUnit.MONTHS), YEAR(ChronoUnit.YEARS)
}
