package uk.gov.justice.digital.oasys.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.oasys.api.RefElementDto

@SqlGroup(
  Sql(scripts = ["classpath:referenceData/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:referenceData/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)

@AutoConfigureWebTestClient
class ReferenceDataControllerTest : IntegrationTest() {

  @Test
  fun `Category returns reference data`() {
    val category = "TEST_CATEGORY"
    webTestClient.get().uri("/referencedata/$category")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isOk
      .expectBody<Collection<RefElementDto>>()
      .consumeWith {
        assertThat(it.responseBody).isEqualTo(setupElementDtos())
      }
  }

  @Test
  fun `Invalid category returns not found`() {
    val category = "INVALID"
    webTestClient.get().uri("/referencedata/$category")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Category with null end date returns not found`() {
    val category = "NULL_CATEGORY"
    webTestClient.get().uri("/referencedata/$category")
      .headers(setAuthorisation(roles = listOf("ROLE_OASYS_READ_ONLY")))
      .exchange()
      .expectStatus().isNotFound
  }

  fun setupElementDtos(): List<RefElementDto> {
    return listOf(
      RefElementDto(
        code = "ELEMENT_1",
        shortDescription = "El 1",
        description = "Element 1"
      ),
      RefElementDto(
        code = "ELEMENT_2",
        shortDescription = "El 2",
        description = "Element 2"
      )
    )
  }
}
