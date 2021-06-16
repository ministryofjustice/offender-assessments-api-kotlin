package uk.gov.justice.digital.oasys.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.AssessmentAnswersDto
import uk.gov.justice.digital.oasys.api.SectionAnswersDto
import uk.gov.justice.digital.oasys.jpa.entities.OasysQuestion
import uk.gov.justice.digital.oasys.jpa.repositories.QuestionRepository

@Service
class AnswerService(private val questionRepository: QuestionRepository) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun getAnswersForQuestions(oasysSetPk: Long, questionCodes: Map<String, Collection<String>>): AssessmentAnswersDto {
    val questionAnswers = getAnswers(oasysSetPk, questionCodes)
    return AssessmentAnswersDto.from(oasysSetPk, questionAnswers)
  }

  fun getSectionAnswersForQuestions(oasysSetPk: Long, questionCodes: Map<String, Collection<String>>): SectionAnswersDto {
    val questionAnswers = getAnswers(oasysSetPk, questionCodes)
    return SectionAnswersDto.from(oasysSetPk, questionAnswers)
  }

  private fun getAnswers(
    oasysSetPk: Long,
    questionCodes: Map<String, Collection<String>>
  ): List<OasysQuestion> {
    val questionAnswers = questionRepository.getQuestionAnswersFromQuestionCodes(oasysSetPk, questionCodes)
    log.info("Found ${questionAnswers.size} answers for oasys assessment: $oasysSetPk")
    return questionAnswers
  }
}
