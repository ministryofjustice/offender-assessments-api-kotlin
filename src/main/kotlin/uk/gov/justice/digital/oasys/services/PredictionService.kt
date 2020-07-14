package uk.gov.justice.digital.oasys.services


import org.springframework.stereotype.Service
import uk.gov.justice.digital.oasys.api.Predictor
import uk.gov.justice.digital.oasys.jpa.entities.Assessment
import uk.gov.justice.digital.oasys.jpa.repositories.SimpleAssessmentRepository

@Service
class PredictionService constructor(
        private val  offenderService: OffenderService,
        private val  simpleAssessmentRepository: SimpleAssessmentRepository
){

    fun getAllPredictorsForOffender(identityType:String, identity: String):Set<Predictor?> {
        val offenderId = offenderService.getOffenderIdByIdentifier(identityType, identity)
        val assessments:Collection<Assessment?> = simpleAssessmentRepository.getAssessmentsForOffender(offenderId)
        return assessments.map { Predictor.from(it) }.toSet()
    }
}
