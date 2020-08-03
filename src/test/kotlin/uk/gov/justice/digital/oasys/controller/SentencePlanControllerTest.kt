package uk.gov.justice.digital.oasys.controller


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.BasicSentencePlanDto
import uk.gov.justice.digital.oasys.api.BasicSentencePlanItemDto
import uk.gov.justice.digital.oasys.api.RefElementDto
import java.time.LocalDate


@SqlGroup(
        Sql(scripts = ["classpath:sentencePlans/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:sentencePlans/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@AutoConfigureWebTestClient
class SentencePlanControllerTest : IntegrationTest() {

    private val oasysOffenderId = 100L
    private val pnc = "PNC100"
    private val crn = "XYZ100"
    private val nomis = "NOMIS100"
    private val booking = "B100"

    @Test
    fun `access forbidden when no authority`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/basicSentencePlans/")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun `access forbidden when no role`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/basicSentencePlans/")
                .headers(setAuthorisation())
                .exchange()
                .expectStatus().isForbidden
    }

    @Test
    fun `oasys offender PK returns list of Basic Sentence Plans`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<BasicSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(2)
                    assertThat(sentencePlans.map { s -> s.sentencePlanId }).containsExactlyInAnyOrder(100L, 300L)
                }
    }

    @Test
    fun `oasys offender PK returns not found`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/9999/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `oasys offender PK returns latest Basic Sentence Plan`() {

        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/basicSentencePlans/latest")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<BasicSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan).isEqualTo(setupBasicSentencePlan())
                }
    }


    @Test
    fun `offender CRN returns list of Basic Sentence Plans`() {

        webTestClient.get().uri("/offenders/crn/$crn/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<BasicSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(2)
                    assertThat(sentencePlans.map { s -> s.sentencePlanId }).containsExactlyInAnyOrder(100L, 300L)
                }
    }

    @Test
    fun `offender CRN returns not found`() {

        webTestClient.get().uri("/offenders/crn/9999/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender CRN returns latest Basic Sentence Plan`() {

        webTestClient.get().uri("/offenders/crn/$crn/basicSentencePlans/latest")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<BasicSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan).isEqualTo(setupBasicSentencePlan())
                }
    }

    @Test
    fun `offender PNC returns list of Basic Sentence Plans`() {

        webTestClient.get().uri("/offenders/pnc/$pnc/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<BasicSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(2)
                    assertThat(sentencePlans.map { s -> s.sentencePlanId }).containsExactlyInAnyOrder(100L, 300L)
                }
    }

    @Test
    fun `offender PNC returns not found`() {

        webTestClient.get().uri("/offenders/pnc/9999/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender PNC returns latest Basic Sentence Plan`() {

        webTestClient.get().uri("/offenders/pnc/$pnc/basicSentencePlans/latest")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<BasicSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan).isEqualTo(setupBasicSentencePlan())
                }
    }

    @Test
    fun `offender Nomis ID returns list of Basic Sentence Plans`() {

        webTestClient.get().uri("/offenders/nomisId/$nomis/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<BasicSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(2)
                    assertThat(sentencePlans.map { s -> s.sentencePlanId }).containsExactlyInAnyOrder(100L, 300L)
                }
    }

    @Test
    fun `offender Nomis ID returns not found`() {

        webTestClient.get().uri("/offenders/nomisId/9999/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Nomis ID returns latest Basic Sentence Plan`() {

        webTestClient.get().uri("/offenders/nomisId/$nomis/basicSentencePlans/latest")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<BasicSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan).isEqualTo(setupBasicSentencePlan())
                }
    }

    @Test
    fun `offender Booking ID returns list of Basic Sentence Plans`() {

        webTestClient.get().uri("/offenders/bookingId/$booking/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<BasicSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                    assertThat(sentencePlans).hasSize(2)
                    assertThat(sentencePlans.map { s -> s.sentencePlanId }).containsExactlyInAnyOrder(100L, 300L)
                }
    }

    @Test
    fun `offender Booking ID returns not found`() {

        webTestClient.get().uri("/offenders/bookingId/9999/basicSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun `offender Booking ID returns latest Basic Sentence Plan`() {

        webTestClient.get().uri("/offenders/bookingId/$booking/basicSentencePlans/latest")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<BasicSentencePlanDto>()
                .consumeWith {
                    val sentencePlan = it.responseBody
                    assertThat(sentencePlan).isEqualTo(setupBasicSentencePlan())
                }
    }

    fun setupBasicSentencePlan(): BasicSentencePlanDto {

        return BasicSentencePlanDto(
                sentencePlanId = 100L,
                createdDate = LocalDate.of(2018, 8, 19),
                basicSentencePlanItems = listOf(
                        BasicSentencePlanItemDto(
                                basicSentPlanObjId = 100L,
                                includeInPlan = true,
                                offenceBehaviourLink = RefElementDto(code = "ACCOMMODATION", description = "Accommodation"),
                                objectiveText = "Find somewhere to live",
                                measureText = "Has found accommodation",
                                whatWorkText = "Look for a hostel",
                                whoWillDoWorkText = "OFFENDER",
                                timescalesText = "2 weeks",
                                dateOpened = LocalDate.of(2018, 7, 26),
                                dateCompleted = null,
                                problemAreaCompInd = true
                        ),
                        BasicSentencePlanItemDto(
                                basicSentPlanObjId = 200,
                                includeInPlan = true,
                                offenceBehaviourLink = RefElementDto(code = "DRUG_MISUSE", description = "Drug misuse"),
                                objectiveText = "Attend support group",
                                measureText = "Attended 10 sessions",
                                whatWorkText = "Attend regular sessions",
                                whoWillDoWorkText = "OFFENDER",
                                timescalesText = "20 weeks",
                                dateOpened = LocalDate.of(2016, 7, 26),
                                dateCompleted = LocalDate.of(2018, 7, 26),
                                problemAreaCompInd = true
                        ),
                        BasicSentencePlanItemDto(
                                basicSentPlanObjId = 300,
                                includeInPlan = false,
                                offenceBehaviourLink = RefElementDto(code = "ATTITUDES", description = "Attitudes"),
                                objectiveText = "Attend support group",
                                measureText = "Not in plan objective",
                                whatWorkText = "Not in plan",
                                whoWillDoWorkText = "OFFENDER",
                                timescalesText = "1 year",
                                dateOpened = LocalDate.of(2016, 7, 26),
                                dateCompleted = LocalDate.of(2018, 7, 26),
                                problemAreaCompInd = false
                        )
                )
        )

    }

}