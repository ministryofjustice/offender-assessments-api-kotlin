package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto
import uk.gov.justice.digital.oasys.api.FullSentencePlanSummaryDto
import java.time.LocalDateTime

@SqlGroup(
        Sql(scripts = ["classpath:sentencePlans/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:sentencePlans/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@DisplayName("Full Sentence Plan and Summary Controller Tests")
@AutoConfigureWebTestClient(timeout = "60000")
class FullSentencePlanControllerTest: IntegrationTest() {

    private val oasysOffenderId = 100L
    private val pnc = "PNC100"
    private val crn = "XYZ100"
    private val nomis = "NOMIS100"
    private val booking = "B100"
    private val oasysSetPK = 100L

    @Test
    fun `access forbidden for fullSentencePlans when no authority`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden for fullSentencePlans when no role`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/")
                .headers(setAuthorisation())
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `oasys offender PK returns list of Full Sentence Plans`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(1)
                    assertThat(sentencePlans).extracting("oasysSetId").containsOnly(100L)
                }
    }

    @Test
    fun `oasys offender PK returns latest Full Sentence Plan`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/$oasysSetPK")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<FullSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan?.oasysSetId).isEqualTo(100L)                }
    }

    @Test
    fun `oasys offender PK returns not found`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/9999/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `offender CRN returns list of Full Sentence Plans`() {

        webTestClient.get().uri("/offenders/crn/$crn/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(1)
                    assertThat(sentencePlans).extracting("oasysSetId").containsOnly(100L)
                }
    }

    @Test
    fun `offender CRN returns not found`() {

        webTestClient.get().uri("/offenders/crn/9999/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `offender PNC returns list of Full Sentence Plans`() {

        webTestClient.get().uri("/offenders/pnc/$pnc/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(1)
                    assertThat(sentencePlans).extracting("oasysSetId").containsOnly(100L)
                }
    }

    @Test
    fun `offender PNC returns not found`() {

        webTestClient.get().uri("/offenders/pnc/9999/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Nomis ID returns list of Full Sentence Plans`() {

        webTestClient.get().uri("/offenders/nomisId/$nomis/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(1)
                    assertThat(sentencePlans).extracting("oasysSetId").containsOnly(100L)
                }
    }

    @Test
    fun `offender Nomis ID returns not found`() {

        webTestClient.get().uri("/offenders/nomisId/9999/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Booking ID returns list of Full Sentence Plans`() {

        webTestClient.get().uri("/offenders/bookingId/$booking/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(1)
                    assertThat(sentencePlans).extracting("oasysSetId").containsOnly(100L)
                }
    }

    @Test
    fun `offender Booking ID returns not found`() {

        webTestClient.get().uri("/offenders/bookingId/9999/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `access forbidden for fullSentencePlanSummary when no authority`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/summary")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden for fullSentencePlanSummary when no role`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/summary")
                .headers(setAuthorisation())
                .exchange()
                .expectStatus().isForbidden
    }




    @Test
    fun `oasys offender PK returns list of Full Sentence Plan Summaries`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanSummaryDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).isEqualTo(setupValidSummary())
                }
    }

    @Test
    fun `oasys offender PK Summaries returns not found for `() {

        webTestClient.get().uri("/offenders/oasysOffenderId/9999/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `offender CRN returns list of Full Sentence Plan Summaries`() {

        webTestClient.get().uri("/offenders/crn/$crn/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanSummaryDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).isEqualTo(setupValidSummary())
                }
    }

    @Test
    fun `offender CRN Summaries returns not found`() {

        webTestClient.get().uri("/offenders/crn/9999/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }


    @Test
    fun `offender PNC returns list of Full Sentence Plan Summaries`() {

        webTestClient.get().uri("/offenders/pnc/$pnc/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanSummaryDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).isEqualTo(setupValidSummary())
                }
    }

    @Test
    fun `offender PNC Summaries returns not found`() {

        webTestClient.get().uri("/offenders/pnc/9999/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Nomis ID returns list of Full Sentence Plan Summariess`() {

        webTestClient.get().uri("/offenders/nomisId/$nomis/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanSummaryDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).isEqualTo(setupValidSummary())
                }
    }

    @Test
    fun `offender Nomis ID Summaries returns not found`() {

        webTestClient.get().uri("/offenders/nomisId/9999/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Booking ID returns list of Full Sentence Plan Summariess`() {

        webTestClient.get().uri("/offenders/bookingId/$booking/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanSummaryDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).isEqualTo(setupValidSummary())
                }
    }

    @Test
    fun `offender Booking ID Summaries returns not found`() {

        webTestClient.get().uri("/offenders/bookingId/9999/fullSentencePlans/summary")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    private fun setupValidSummary():Collection<FullSentencePlanSummaryDto>{
        return listOf(FullSentencePlanSummaryDto(
                oasysSetId = 100,
                createdDate = LocalDateTime.of(2018, 8, 19, 23, 0, 9),
                completedDate = null ))
    }
}