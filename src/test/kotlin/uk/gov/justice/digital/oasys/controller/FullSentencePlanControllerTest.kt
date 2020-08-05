package uk.gov.justice.digital.oasys.controller

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.FullSentencePlanDto

@AutoConfigureWebTestClient
class FullSentencePlanControllerTest: IntegrationTest() {

    private val oasysOffenderId = 100L
    private val pnc = "PNC100"
    private val crn = "XYZ100"
    private val nomis = "NOMIS100"
    private val booking = "B100"

    @Test
    fun `oasys offender PK returns list of Full Sentence Plans`(){
        webTestClient.get().uri("/offenders/oasysOffenderId/$oasysOffenderId/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                }

    }

    @Test
    fun `offender pnc returns list of Full Sentence Plans`(){
        webTestClient.get().uri("/offenders/pnc/$pnc/fullSentencePlans/")
                .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
                .exchange()
                .expectStatus().isOk
                .expectBody<Collection<FullSentencePlanDto>>()
                .consumeWith {
                    val sentencePlans = it.responseBody
                }

    }

//    @Test
//    fun canGetFullSentencePlansSummaryForOffenderPk() {
//        val sentencePlans: Array<FullSentencePlanSummaryDto> = given()
//                .`when`()
//                .auth().oauth2(validOauthToken)
//                .get("/offenders/oasysOffenderId/{0}/fullSentencePlans/summary", 100L)
//                .then()
//                .statusCode(200)
//                .extract()
//                .body()
//                .`as`(Array<FullSentencePlanSummaryDto>::class.java)
//        Assertions.assertThat<Array<FullSentencePlanSummaryDto>>(sentencePlans).hasSize(1)
//        val plan: FullSentencePlanSummaryDto = sentencePlans[0]
//        assertThat(plan.getOasysSetId()).isEqualTo(100L)
//        assertThat(plan.getCreatedDate()).isEqualToIgnoringSeconds(LocalDateTime.of(2020, 3, 6, 9, 12))
//        assertThat(plan.getCompletedDate()).isEqualToIgnoringSeconds(LocalDateTime.of(2020, 6, 20, 23, 0))
//    }
}