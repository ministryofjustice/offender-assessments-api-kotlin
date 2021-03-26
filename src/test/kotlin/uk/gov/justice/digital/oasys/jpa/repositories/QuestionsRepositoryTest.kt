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
  Sql(
    scripts = ["classpath:assessments/before-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:assessments/after-test-full.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
class QuestionsRepositoryTest(
  @Autowired
  private val entityManager: EntityManager
) : IntegrationTest() {

  private val questionRepository = QuestionRepository(entityManager)
  private val assessmentId = 5433L
  private val assessmentIdNoQuestions = 5435L

  @Test
  fun `returns only requested questions`() {
    val answers = questionRepository.getQuestionAnswersFromQuestionCodes(
      assessmentId,
      mapOf("10" to setOf("10.98"), "9" to setOf("9.99"))
    )
    assertThat(answers.map { a -> a.refQuestion?.refQuestionCode })
      .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))
    assertThat(answers.map { a -> a.oasysAnswers?.first()?.oasysAnswerPk }).containsExactlyInAnyOrderElementsOf(
      setOf(
        5343776,
        5343777
      )
    )
  }

  @Test
  fun `returns multiple answers`() {
    val answers = questionRepository.getQuestionAnswersFromQuestionCodes(
      assessmentId,
      mapOf("10" to setOf("10.98"), "9" to setOf("9.99"))
    )
    assertThat(answers.map { a -> a.refQuestion?.refQuestionCode })
      .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))
  }

  @Test
  fun `returns empty set when answers do not exist`() {
    val answers = questionRepository.getQuestionAnswersFromQuestionCodes(
      assessmentIdNoQuestions,
      mapOf("10" to setOf("10.98"), "9" to setOf("9.99"))
    )
    assertThat(answers).isEmpty()
  }
}
