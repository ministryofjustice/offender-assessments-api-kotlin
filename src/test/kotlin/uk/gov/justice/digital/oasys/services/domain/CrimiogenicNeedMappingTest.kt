package uk.gov.justice.digital.oasys.services.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Crimiogenic Need Mapping Tests")
class CrimiogenicNeedMappingTest {

    private val section = SectionHeader.DRUG_MISUSE
    private val name = "Drug Misuse"


    @Test
    fun `Any Risk Flag is True when Risk of Harm set`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                true,
                null,
                null,
                null)

        assertThat(assessmentNeed.riskOfHarm).isTrue()
        assertThat(assessmentNeed.riskOfReoffending).isNull()
        assertThat(assessmentNeed.overThreshold).isNull()
        assertThat(assessmentNeed.flaggedAsNeed).isNull()

        assertThat(assessmentNeed.anyRiskFlagged()).isTrue()
    }

    @Test
    fun `Any Risk Flag is True when Risk of Reoffending set`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                null,
                true,
                null,
                null)

        assertThat(assessmentNeed.riskOfHarm).isNull()
        assertThat(assessmentNeed.riskOfReoffending).isTrue()
        assertThat(assessmentNeed.overThreshold).isNull()
        assertThat(assessmentNeed.flaggedAsNeed).isNull()

        assertThat(assessmentNeed.anyRiskFlagged()).isTrue()
    }

    @Test
    fun `Any Risk Flag is True when over threshold set`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                null,
                null,
                true,
                null)

        assertThat(assessmentNeed.riskOfHarm).isNull()
        assertThat(assessmentNeed.riskOfReoffending).isNull()
        assertThat(assessmentNeed.overThreshold).isTrue()
        assertThat(assessmentNeed.flaggedAsNeed).isNull()

        assertThat(assessmentNeed.anyRiskFlagged()).isTrue()
    }

    @Test
    fun `Any Risk Flag is True when flagged`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                false,
                true,
                null,
                null)

        assertThat(assessmentNeed.riskOfHarm).isFalse()
        assertThat(assessmentNeed.riskOfReoffending).isTrue()
        assertThat(assessmentNeed.overThreshold).isNull()
        assertThat(assessmentNeed.flaggedAsNeed).isNull()

        assertThat(assessmentNeed.anyRiskFlagged()).isTrue()
    }

    @Test
    fun `Any Risk Flag is True when multiple set`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                false,
                true,
                true,
                true)

        assertThat(assessmentNeed.riskOfHarm).isFalse()
        assertThat(assessmentNeed.riskOfReoffending).isTrue()
        assertThat(assessmentNeed.overThreshold).isTrue()
        assertThat(assessmentNeed.flaggedAsNeed).isTrue()

        assertThat(assessmentNeed.anyRiskFlagged()).isTrue()
    }

    @Test
    fun `Any Risk Flag is False when all unset`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                null,
                null,
                null,
                null)

        assertThat(assessmentNeed.riskOfHarm).isNull()
        assertThat(assessmentNeed.riskOfReoffending).isNull()
        assertThat(assessmentNeed.overThreshold).isNull()
        assertThat(assessmentNeed.flaggedAsNeed).isNull()

        assertThat(assessmentNeed.anyRiskFlagged()).isFalse()
    }

    @Test
    fun `Any Risk Flag is False when all false`() {
        val assessmentNeed = CrimiogenicNeed(
                section,
                name,
                false,
                false,
                false,
                false)

        assertThat(assessmentNeed.riskOfHarm).isFalse()
        assertThat(assessmentNeed.riskOfReoffending).isFalse()
        assertThat(assessmentNeed.overThreshold).isFalse()
        assertThat(assessmentNeed.flaggedAsNeed).isFalse()

        assertThat(assessmentNeed.anyRiskFlagged()).isFalse()
    }

}