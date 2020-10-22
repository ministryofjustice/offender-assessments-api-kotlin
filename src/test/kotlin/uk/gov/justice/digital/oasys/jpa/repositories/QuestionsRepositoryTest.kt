package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest
import uk.gov.justice.digital.oasys.services.exceptions.EntityNotFoundException
import javax.persistence.EntityManager

@SqlGroup(
        Sql(scripts = ["classpath:assessments/before-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:assessments/after-test-full.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class QuestionsRepositoryTest(@Autowired
                             private val entityManager: EntityManager) : IntegrationTest()  {

    private val questionRepository = QuestionRepository(entityManager)
    private val assessmentId = 5433L


    @Test
    fun `returns only requested questions`() {
        val answers = questionRepository.getQuestionAnswersFromQuestionCodes(assessmentId, setOf("10.98", "9.99"))
        assertThat(answers.map{ a -> a.refQuestion?.refQuestionCode})
                .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))
        assertThat(answers.map {a -> a.oasysAnswers?.first()?.oasysAnswerPk}).containsExactlyInAnyOrderElementsOf(setOf(2343784, 2343776))
    }

    @Test
    fun `returns multiple answers`() {
        val answers = questionRepository.getQuestionAnswersFromQuestionCodes(assessmentId, setOf("10.98", "9.99"))
        assertThat(answers.map{ a -> a.refQuestion?.refQuestionCode})
                .containsExactlyInAnyOrderElementsOf(setOf("10.98", "9.99"))

    }

    @Test
    fun `throws exception when assessment does not exist`() {
        val exception = assertThrows<EntityNotFoundException> { questionRepository.getQuestionAnswersFromQuestionCodes(12345L, setOf("10.98")) }
        assertThat(exception.message).isEqualTo("Assessment or question codes not found for assessment 12345")

    }

}