package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.ErrorResponse
import uk.gov.justice.digital.oasys.api.OffenderDto
import uk.gov.justice.digital.oasys.api.OffenderIdentifier

@SqlGroup(
        Sql(scripts = ["classpath:offender/before-test.sql"], config = SqlConfig(transactionMode = TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:offender/after-test.sql"], config = SqlConfig(transactionMode = TransactionMode.ISOLATED), executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient
class OffenderControllerTest : IntegrationTest() {

    private val oasysOffenderId = 1234L
    private val deletedOasysOffenderId = 4321L
    private val pnc = "PNC"
    private val crn = "CRN"
    private val nomis = "NOMIS"
    private val booking = "BOOKIN"
    private val deletedPnc = "DPNC"
    private val deletedCrn = "DCRN"
    private val deletedNomis = "DNOMIS"
    private val deletedBooking = "DBOOK"
    private val duplicatePnc = "PNCD"
    private val duplicateCrn = "CRND"
    private val duplicateNomis = "NOMISD"

    @Test
    fun `access forbidden when no authority`() {

        webTestClient.get().uri("/offenders/oasysoffenderpk/${oasysOffenderId}")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden when no role`() {

        webTestClient.get().uri("/offenders/oasysoffenderpk/${oasysOffenderId}")
                .headers(setAuthorisation())
                .exchange()
                .expectStatus().isForbidden
    }


    @Test
    fun `oasys offender PK returns offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/${oasysOffenderId}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {validateOffender(it.responseBody)}
    }

    @Test
    fun `oasys offender PK returns not found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/${oasysOffenderId}1")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `oasys offender PK returns deleted offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/${deletedOasysOffenderId}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {
                    assertThat( it.responseBody?.oasysOffenderId).isEqualTo(deletedOasysOffenderId)
                }
    }


    @Test
    fun `Delius CRN returns offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/${crn}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {validateOffender(it.responseBody)}
    }

    @Test
    fun `Delius CRN returns  not found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/${crn}n")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `Delius CRN does not return deleted offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/${deletedCrn}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `PNC returns offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/${pnc}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {validateOffender(it.responseBody)}
    }

    @Test
    fun `PNC returns  not found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/${pnc}n")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `PNC does not return deleted offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/${deletedPnc}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `NOMIS ID returns offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/${nomis}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {validateOffender(it.responseBody)}
    }

    @Test
    fun `NOMIS ID returns  not found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/${nomis}n")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `NOMIS ID does not return deleted offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/${deletedNomis}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `NOMIS Booking ID returns offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.BOOKING.value}/${booking}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {validateOffender(it.responseBody)}
    }

    @Test
    fun `NOMIS Booking ID returns  not found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.BOOKING.value}/${booking}n")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `NOMIS Booking ID does not return deleted offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.BOOKING.value}/${deletedBooking}")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `OASys Offender PK returns merged offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/100")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {
                    assertThat(it.responseBody?.oasysOffenderId).isEqualTo(300)
                    assertThat(it.responseBody?.mergedOasysOffenderId).isEqualTo(100)
                }
    }

    @Test
    fun `OASys Offender PK returns twice merged offender`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.OASYS.value}/400")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<OffenderDto>()
                .consumeWith {
                    assertThat(it.responseBody?.oasysOffenderId).isEqualTo(800)
                    assertThat(it.responseBody?.mergedOasysOffenderId).isEqualTo(400)
                }
    }

    @Test
    fun `PNC returns error when duplicate record found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.PNC.value}/$duplicatePnc")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().is4xxClientError
                .expectBody<ErrorResponse>()
                .consumeWith {
                    assertThat(it.responseBody?.developerMessage).contains("Duplicate offender found for PNC $duplicatePnc")
                    assertThat(it.responseBody?.userMessage).contains("Duplicate offender found for PNC $duplicatePnc")
                    assertThat(it.responseBody?.status).isEqualTo(409)
                }
    }

    @Test
    fun `Delius CRN returns error when duplicate record found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.CRN.value}/$duplicateCrn")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().is4xxClientError
                .expectBody<ErrorResponse>()
                .consumeWith {
                    assertThat(it.responseBody?.developerMessage).contains("Duplicate offender found for CRN $duplicateCrn")
                    assertThat(it.responseBody?.userMessage).contains("Duplicate offender found for CRN $duplicateCrn")
                    assertThat(it.responseBody?.status).isEqualTo(409)
                }
    }

    @Test
    fun `NOMIS ID returns error when duplicate record found`() {

        webTestClient.get().uri("/offenders/${OffenderIdentifier.NOMIS.value}/$duplicateNomis")
                .headers(setAuthorisation(roles=listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().is4xxClientError
                .expectBody<ErrorResponse>()
                .consumeWith {
                    assertThat(it.responseBody?.developerMessage).contains("Duplicate offender found for NOMIS $duplicateNomis")
                    assertThat(it.responseBody?.userMessage).contains("Duplicate offender found for NOMIS $duplicateNomis")
                    assertThat(it.responseBody?.status).isEqualTo(409)
                }
    }

    private fun validateOffender(offender: OffenderDto?) {
        assertThat(offender?.oasysOffenderId).isEqualTo(oasysOffenderId)
        assertThat(offender?.limitedAccessOffender).isTrue()
        assertThat(offender?.familyName).isEqualTo("Offender")
        assertThat(offender?.forename1).isEqualTo("Mike")
        assertThat(offender?.forename2).isEqualTo("Tom")
        assertThat(offender?.forename3).isEqualTo("Steve")
        assertThat(offender?.riskToOthers).isEqualTo("Y")
        assertThat(offender?.riskToSelf).isEqualTo("N")
        assertThat(offender?.pnc).isEqualTo(pnc)
        assertThat(offender?.crn).isEqualTo(crn)
        assertThat(offender?.nomisId).isEqualTo(nomis)
        assertThat(offender?.legacyCmsProbNumber).isEqualTo("LEGACYCMS")
        assertThat(offender?.croNumber).isEqualTo("CRO")
        assertThat(offender?.bookingNumber).isEqualTo(booking)
        assertThat(offender?.mergePncNumber).isEqualTo("MPNC")
    }

}