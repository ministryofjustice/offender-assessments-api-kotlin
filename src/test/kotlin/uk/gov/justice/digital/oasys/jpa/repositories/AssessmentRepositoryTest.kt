package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest
import javax.persistence.EntityManager

@SqlGroup(
  Sql(scripts = ["classpath:assessments/before-test-summary.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:assessments/after-test-summary.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class AssessmentRepositoryTest(
  @Autowired
  private val entityManager: EntityManager
) : IntegrationTest() {

  private val assessmentRepository = AssessmentRepository(entityManager)
  private val oasysOffenderId = 1234L
  private val layerOneAssessmentId = 5434L
  private val openAssessmentId = 5433L
  private val completeAssessmentId = 5432L
  private val voidedAssessmentId = 5431L
  private val historicAssessmentId = 5430L
  private val deletedAssessmentId = 5435L

  @Test
  fun `return only voided assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, null, true, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(voidedAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentVoidedDate }).doesNotContainNull()
  }

  @Test
  fun `return only not voided assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, null, false, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId, openAssessmentId, completeAssessmentId, layerOneAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentVoidedDate }).containsOnlyNulls()
  }

  @Test
  fun `return only type LAYER 3 assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, "LAYER_3", null, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId, openAssessmentId, completeAssessmentId, voidedAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentType }).containsOnly("LAYER_3")
  }

  @Test
  fun `return only type LAYER 1 assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, "LAYER_1", null, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(layerOneAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentType }).containsOnly("LAYER_1")
  }

  @Test
  fun `return only COMPLETE assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, null, null, "COMPLETE")
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId, voidedAssessmentId, completeAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentStatus }).containsOnly("COMPLETE")
  }

  @Test
  fun `do not return deleted assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, null, null, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId, openAssessmentId, completeAssessmentId, voidedAssessmentId, layerOneAssessmentId))
    assertThat(summaries?.map { a -> a.assessmentStatus }).containsOnly("COMPLETE", "COMPLETE", "COMPLETE", "OPEN", "OPEN")
  }

  @Test
  fun `return only OPEN assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, null, null, null, "OPEN")
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(openAssessmentId, layerOneAssessmentId))
    assertThat(summaries?.map { a -> a.deletedDate }).containsOnly(null)
  }

  @Test
  fun `return only CURRENT assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, "CURRENT", null, null, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(openAssessmentId, completeAssessmentId, voidedAssessmentId, layerOneAssessmentId))
    assertThat(summaries?.map { a -> a.group?.historicStatus }).containsOnly("CURRENT")
  }

  @Test
  fun `return only OTHER assessments`() {
    val summaries = assessmentRepository.getAssessmentsForOffender(oasysOffenderId, "OTHER", null, null, null)
    assertThat(summaries?.map { a -> a.oasysSetPk })
      .containsExactlyInAnyOrderElementsOf(setOf(historicAssessmentId))
    assertThat(summaries?.map { a -> a.group?.historicStatus }).containsOnly("OTHER")
  }
}
