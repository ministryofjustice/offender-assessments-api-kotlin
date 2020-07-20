package uk.gov.justice.digital.oasys.services

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.oasys.jpa.entities.Offender
import uk.gov.justice.digital.oasys.jpa.entities.OffenderLink
import uk.gov.justice.digital.oasys.jpa.repositories.OffenderLinkRepository
import uk.gov.justice.digital.oasys.jpa.repositories.OffenderRepository

@ExtendWith(MockKExtension::class)
@DisplayName("Offender Service Tests")
class OffenderServiceTest  {
    private val offenderRepository: OffenderRepository = mockk()
    private val offenderLinkRepository: OffenderLinkRepository = mockk()

    private val service = OffenderService(offenderRepository, offenderLinkRepository)

    @Test
    fun `return empty merged offender PK when offender has not been merged`() {
        val offender = setupOffender()
        every { offenderRepository.getOffender("OASYS", "1") } returns offender
        every { offenderLinkRepository.findMergedOffenderOrNull(1L) } returns null

        var result = service.getOffender("OASYS", "1");

        assertThat(result.oasysOffenderId).isEqualTo(1L)
        assertThat(result.mergedOasysOffenderId).isNull()
    }

    @Test
    fun `return merged offender when requested offender has been merged`() {
        val expectedMergedOffender = 2L
        var offender = setupOffender().copy(mergeIndicated = "Y", offenderPk = 1L)
        var mergedOffender = setupOffender().copy(mergeIndicated = "N", offenderPk = expectedMergedOffender)
        var linkedOffender = OffenderLink(mergedOffenderPK = 2L)

        every { offenderRepository.getOffender("oasysOffenderId", "1") } returns offender
        every { offenderRepository.getOffender("oasysOffenderId", expectedMergedOffender.toString()) } returns mergedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(1L) } returns linkedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(expectedMergedOffender) } returns null

        val result = service.getOffender("oasysOffenderId", "1");

        assertThat(result.oasysOffenderId).isEqualTo(expectedMergedOffender);
        assertThat(result.mergedOasysOffenderId).isEqualTo(1L);

        verify(exactly = 1) { offenderRepository.getOffender("oasysOffenderId", "1")  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(1L)  }

    }

    @Test
    fun `return merged offender when requested offender has been merged twice`() {
        val expectedMergedOffender = 3L
        var offender = setupOffender().copy(mergeIndicated = "Y", offenderPk = 1L)
        var mergedOffender = setupOffender().copy(mergeIndicated = "N", offenderPk = expectedMergedOffender)
        var linkedOffender = OffenderLink(mergedOffenderPK = 2L)
        var linkedOffender2 = OffenderLink(mergedOffenderPK = 3L)

        every { offenderRepository.getOffender("oasysOffenderId", "1") } returns offender
        every { offenderRepository.getOffender("oasysOffenderId", expectedMergedOffender.toString()) } returns mergedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(1L) } returns linkedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(2L) } returns linkedOffender2
        every { offenderLinkRepository.findMergedOffenderOrNull(expectedMergedOffender) } returns null

        val result = service.getOffender("oasysOffenderId", "1");

        assertThat(result.oasysOffenderId).isEqualTo(expectedMergedOffender);
        assertThat(result.mergedOasysOffenderId).isEqualTo(1L);

        verify(exactly = 1) { offenderRepository.getOffender("oasysOffenderId", "1")  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(1L)  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(2L)  }

    }

    @Test
    fun `return merged offender when requested offender has been merged three times`() {
        val expectedMergedOffender = 4L
        var offender = setupOffender().copy(mergeIndicated = "Y", offenderPk = 1L)
        var mergedOffender = setupOffender().copy(mergeIndicated = "N", offenderPk = expectedMergedOffender)
        var linkedOffender = OffenderLink(mergedOffenderPK = 2L)
        var linkedOffender2 = OffenderLink(mergedOffenderPK = 3L)
        var linkedOffender3 = OffenderLink(mergedOffenderPK = 4L)

        every { offenderRepository.getOffender("oasysOffenderId", "1") } returns offender
        every { offenderRepository.getOffender("oasysOffenderId", "3") } returns mergedOffender
        every { offenderRepository.getOffender("oasysOffenderId", expectedMergedOffender.toString()) } returns mergedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(1L) } returns linkedOffender
        every { offenderLinkRepository.findMergedOffenderOrNull(2L) } returns linkedOffender2
        every { offenderLinkRepository.findMergedOffenderOrNull(3L) } returns linkedOffender3
        every { offenderLinkRepository.findMergedOffenderOrNull(expectedMergedOffender) } returns null

        val result = service.getOffender("oasysOffenderId", "1");

        assertThat(result.oasysOffenderId).isEqualTo(expectedMergedOffender);
        assertThat(result.mergedOasysOffenderId).isEqualTo(1L);

        verify(exactly = 1) { offenderRepository.getOffender("oasysOffenderId", "1")  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(1L)  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(2L)  }
        verify(exactly = 1) { offenderLinkRepository.findMergedOffenderOrNull(3L)  }

    }


    private fun setupOffender() : Offender {

        return Offender(1L, "Y", "Any PNC", "Any CMS Prob",
                "Any CMS Pris", "Any Leg Prob", "Any cro",
                "Any prison number", "Any Merge PNC", "Family",
                "Any f1", "Any f2", "Any f3", "N", null, "YES")
    }
}