package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.Offender

@DisplayName("Offender DTO Tests")
class OffenderDtoTest {

    @Test
    fun `Builds valid Offender DTO from Entity`() {
        val offender: Offender = setupOffender()
        val offenderDto = OffenderDto.from(offender)

        assertThat(offenderDto.oasysOffenderId).isEqualTo(offender.offenderPk)
        assertThat(offenderDto.familyName).isEqualTo(offender.familyName)
        assertThat(offenderDto.forename1).isEqualTo(offender.forename1)
        assertThat(offenderDto.forename2).isEqualTo(offender.forename2)
        assertThat(offenderDto.forename3).isEqualTo(offender.forename3)
        assertThat(offenderDto.riskToOthers).isEqualTo(offender.riskToOthers)
        assertThat(offenderDto.riskToSelf).isEqualTo(offender.riskToSelf)
        assertThat(offenderDto.pnc).isEqualTo(offender.pnc)
        assertThat(offenderDto.crn).isEqualTo(offender.cmsProbNumber)
        assertThat(offenderDto.nomisId).isEqualTo(offender.cmsPrisNumber)
        assertThat(offenderDto.legacyCmsProbNumber).isEqualTo(offender.legacyCmsProbNumber)
        assertThat(offenderDto.croNumber).isEqualTo(offender.croNumber)
        assertThat(offenderDto.bookingNumber).isEqualTo(offender.prisonNumber)
        assertThat(offenderDto.mergePncNumber).isEqualTo(offender.mergePncNumber)
        assertThat(offenderDto.limitedAccessOffender).isTrue()

    }

    private fun setupOffender() : Offender {
        return Offender(12L, "Y", "Any PNC", "Any CMS Prob",
                "Any CMS Pris", "Any Leg Prob", "Any cro",
                "Any prison number", "Any Merge PNC", "Family",
                "Any f1", "Any f2", "Any f3", "N", null, "YES")
    }
}