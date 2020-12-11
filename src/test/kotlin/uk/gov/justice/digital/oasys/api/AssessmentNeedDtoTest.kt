package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.services.domain.CriminogenicNeed
import uk.gov.justice.digital.oasys.services.domain.NeedSeverity
import uk.gov.justice.digital.oasys.services.domain.SectionHeader

@DisplayName("Assessment DTO Tests")
class AssessmentNeedDtoTest {


    @Test
    fun `Builds valid Assessment Need DTO`() {
        val assessmentNeed = setupAssessmentNeed()
        val assessmentNeedDto = AssessmentNeedDto.from(setOf(assessmentNeed)).first()

        assertThat(assessmentNeedDto.name).isEqualTo(assessmentNeed.name)
        assertThat(assessmentNeedDto.overThreshold).isEqualTo(assessmentNeed.overThreshold)
        assertThat(assessmentNeedDto.riskOfHarm).isEqualTo(assessmentNeed.riskOfHarm)
        assertThat(assessmentNeedDto.riskOfReoffending).isEqualTo(assessmentNeed.riskOfReoffending)
        assertThat(assessmentNeedDto.flaggedAsNeed).isEqualTo(assessmentNeed.flaggedAsNeed)
        assertThat(assessmentNeedDto.severity).isEqualTo(assessmentNeed.severity)
    }

    @Test
    fun `Builds valid Assessment Need DTO Null`() {
        val assessmentNeedDto = AssessmentNeedDto.from(null)
        assertThat(assessmentNeedDto).isEmpty()
    }

    @Test
    fun `Builds valid Assessment Need DTO empty set`() {
        val assessmentNeedDto = AssessmentNeedDto.from(emptySet())
        assertThat(assessmentNeedDto).isEmpty()
    }

    @Test
    fun `Builds valid Assessment Need DTO null values in set set`() {
        val assessmentNeedDto = AssessmentNeedDto.from(setOf(null))
        assertThat(assessmentNeedDto).isEmpty()
    }

    fun setupAssessmentNeed(): CriminogenicNeed {
        return CriminogenicNeed(
                SectionHeader.DRUG_MISUSE,
                "Drug Misuse",
                false,
                true,
                true,
                true,
                NeedSeverity.SEVERE)
    }
}