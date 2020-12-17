package uk.gov.justice.digital.oasys.jpa.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersion
import uk.gov.justice.digital.oasys.jpa.entities.RefAssessmentVersionPK

@Repository
interface ReferenceAssessmentRepository : JpaRepository<RefAssessmentVersion?, RefAssessmentVersionPK?>
