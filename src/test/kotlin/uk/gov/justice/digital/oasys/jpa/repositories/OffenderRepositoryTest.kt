package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest
import uk.gov.justice.digital.oasys.jpa.entities.Offender
import uk.gov.justice.digital.oasys.services.exceptions.DuplicateOffenderRecordException
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import javax.persistence.EntityManager


@SqlGroup(
        Sql(scripts = ["classpath:offender/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:offender/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class OffenderRepositoryTest(@Autowired
                             private val entityManager: EntityManager) : IntegrationTest() {

    private val offenderRepository = OffenderRepository(entityManager)
    private val invalidId = "invalid"

    private val crn = "crn"
    private val crnType = "CRN"
    private val pnc = "pnc"
    private val pncType = "PNC"
    private val nomis = "nomisId"
    private val nomisType = "NOMIS"
    private val booking = "bookingId"
    private val bookingType = "BOOKING"
    private val oasys = "oasysOffenderId"

    @Test
    fun `return CRN offender`() {
        val offender = offenderRepository.getOffender(crn, "CRN")
        assertThat(offender).isEqualTo(Offender(1234L))
    }

    @Test
    fun `throws exception for CRN offender with invalid identifier`() {
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(crn, invalidId) }
        assertThat(exception.message).isEqualTo("Offender not found for $crnType, $invalidId")
    }

    @Test
    fun `throws exception for deleted CRN offender`() {
        val deletedCrn = "DCRN"
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(crn, deletedCrn) }
        assertThat(exception.message).isEqualTo("Offender not found for $crnType, $deletedCrn")
    }

    @Test
    fun `throws exception for duplicate CRN offender`() {
        val duplicateCrn = "CRND"
        val exception = assertThrows<DuplicateOffenderRecordException> { offenderRepository.getOffender(crn, duplicateCrn) }
        assertThat(exception.message).isEqualTo("Duplicate offender found for $crnType $duplicateCrn")
    }




    @Test
    fun `return PNC offender`() {
        val offender = offenderRepository.getOffender(pnc, "PNC")
        assertThat(offender).isEqualTo(Offender(1234L))
    }

    @Test
    fun `throws exception for PNC offender with invalid identifier`() {
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(pnc, invalidId) }
        assertThat(exception.message).isEqualTo("Offender not found for $pncType, $invalidId")
    }

    @Test
    fun `throws exception for deleted PNC offender`() {
        val deletedPnc = "DPNC"
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(pnc, deletedPnc) }
        assertThat(exception.message).isEqualTo("Offender not found for $pncType, $deletedPnc")
    }

    @Test
    fun `throws exception for duplicate PNC offender`() {
        val duplicatedPnc = "PNCD"
        val exception = assertThrows<DuplicateOffenderRecordException> { offenderRepository.getOffender(pnc, duplicatedPnc) }
        assertThat(exception.message).isEqualTo("Duplicate offender found for $pncType $duplicatedPnc")
    }




    @Test
    fun `return NOMIS offender`() {
        val offender = offenderRepository.getOffender(nomis, "NOMIS")
        assertThat(offender).isEqualTo(Offender(1234L))
    }

    @Test
    fun `throws exception for NOMIS offender with invalid identifier`() {
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(nomis, invalidId) }
        assertThat(exception.message).isEqualTo("Offender not found for $nomisType, $invalidId")
    }

    @Test
    fun `throws exception for deleted NOMIS offender`() {
        val deletedNomis = "DNOMIS"
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(nomis, deletedNomis) }
        assertThat(exception.message).isEqualTo("Offender not found for $nomisType, $deletedNomis")
    }

    @Test
    fun `throws exception for duplicate NOMIS offender`() {
        val duplicatedNomis = "NOMISD"
        val exception = assertThrows<DuplicateOffenderRecordException> { offenderRepository.getOffender(nomis, duplicatedNomis) }
        assertThat(exception.message).isEqualTo("Duplicate offender found for $nomisType $duplicatedNomis")
    }




    @Test
    fun `return BOOKING offender with valid identifier`() {
        val offender = offenderRepository.getOffender(booking, "BOOKIN")
        assertThat(offender).isEqualTo(Offender(1234L))
    }

    @Test
    fun `throws exception for Booking offender with invalid identifier`() {
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(booking, invalidId) }
        assertThat(exception.message).isEqualTo("Offender not found for $bookingType, $invalidId")
    }

    @Test
    fun `throws exception for deleted Booking offender`() {
        val deletedBooking = "DBOOK"
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(booking, deletedBooking) }
        assertThat(exception.message).isEqualTo("Offender not found for $bookingType, $deletedBooking")
    }

    @Test
    fun `throws exception for duplicate Booking offender`() {
        val duplicatedBooking = "BOOKD"
        val exception = assertThrows<DuplicateOffenderRecordException> { offenderRepository.getOffender(booking, duplicatedBooking) }
        assertThat(exception.message).isEqualTo("Duplicate offender found for $bookingType $duplicatedBooking")
    }



    @Test
    fun `return OASYS offender`() {
        val offender = offenderRepository.getOffender(oasys, "1234")
        assertThat(offender).isEqualTo(Offender(1234L))
    }

    @Test
    fun `throws exception for OASYS offender with invalid identifier`() {
        val exception = assertThrows<EntityNotFoundException> { offenderRepository.getOffender(oasys, "0000") }
        assertThat(exception.message).isEqualTo("Offender not found for OASYS, 0000")
    }
}