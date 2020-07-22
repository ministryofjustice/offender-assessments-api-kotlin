package uk.gov.justice.digital.oasys.jpa.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.oasys.controller.IntegrationTest
import uk.gov.justice.digital.oasys.jpa.entities.OffenderLink
import uk.gov.justice.digital.oasys.jpa.entities.RefElement
import java.time.LocalDateTime

@SqlGroup(
        Sql(scripts = ["classpath:offender/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:offender/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
@DisplayName("Offender Link Repository Tests")
 class OffenderLinkRepositoryTest(@Autowired private val offenderLinkRepository: OffenderLinkRepository) : IntegrationTest() {

    @Test
    fun `return merged Offender by deciding offender PK`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(100L)
        assertThat(offenderLink?.decidingOffenderPK).isEqualTo(100L)
        assertThat(offenderLink).isEqualTo(setupOffenderLinkMergeRetain())
    }

    @Test
    fun `return merged Offender by initiating offender PK`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(200L)
        assertThat(offenderLink?.initiatingOffenderPK).isEqualTo(200L)
        assertThat(offenderLink).isEqualTo(setupOffenderLinkMergeRetain())
    }

    @Test
    fun `return merged Offender with MERGE_RETAIN`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(200L)
        assertThat(offenderLink?.linkType?.refElementCode).isEqualTo("MERGE_RETAIN")
        assertThat(offenderLink).isEqualTo(setupOffenderLinkMergeRetain())
    }

    @Test
    fun `return merged Offender with MERGE_RELINQUISH`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(400L)
        assertThat(offenderLink?.linkType?.refElementCode).isEqualTo("MERGE_RELINQUISH")
        assertThat(offenderLink).isEqualTo(setupOffenderLinkMergeRelinquish())
    }

    @Test
    fun `return null for null offender PK`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(900L)
        assertThat(offenderLink).isNull()
    }

    @Test
    fun `return null for invalid PK`(){
        val offenderLink = offenderLinkRepository.findMergedOffenderOrNull(1000L)
        assertThat(offenderLink).isNull()
    }

    fun setupOffenderLinkMergeRetain():OffenderLink{
        return OffenderLink(offenderLinkPk=100,
                initiatingOffenderPK=200,
                decidingOffenderPK=100,
                mergedOffenderPK=300,
                reversed=false,
                reversalAllowed=true,
                originalInitiatingPnc=null,
                deciderCancel=false,
                lastUpdateDate=null,
                createDate=null,
                linkType= RefElement(
                        refElementUk=-170L,
                        refCategoryCode="LINK_TYPE",
                        refElementCode="MERGE_RETAIN",
                        refElementCtid="200",
                        refElementShortDesc=null,
                        refElementDesc="Merge retain offender",
                        displaySort=20,
                        startDate= LocalDateTime.of(2010, 1, 1,0,0),
                        endDate=null))
    }

    fun setupOffenderLinkMergeRelinquish():OffenderLink{
        return OffenderLink(offenderLinkPk=200,
                initiatingOffenderPK=400,
                decidingOffenderPK=500,
                mergedOffenderPK=600,
                reversed=false,
                reversalAllowed=true,
                originalInitiatingPnc=null,
                deciderCancel=false,
                lastUpdateDate=null,
                createDate=null,
                linkType= RefElement(
                        refElementUk=-170L,
                        refCategoryCode="LINK_TYPE",
                        refElementCode="MERGE_RELINQUISH",
                        refElementCtid="200",
                        refElementShortDesc=null,
                        refElementDesc="Merge relinquish offender",
                        displaySort=20,
                        startDate= LocalDateTime.of(2010, 1, 1,0,0),
                        endDate=null))
    }
}