package uk.gov.justice.digital.oasys.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.oasys.jpa.entities.RefElement

@DisplayName("Ref Element DTO Tests")
class RefElementDtoTest {

    @Test
    fun `Builds Valid RefElement DTO from Entity`() {

        val refElement = RefElement(
            refElementCode = "ELEMENT_CODE",
            refElementShortDesc = "short description",
            refElementDesc = "long description")

        val refElementDto = RefElementDto.from(refElement)
       assertThat(refElementDto?.description).isEqualTo(refElement.refElementDesc)
       assertThat(refElementDto?.shortDescription).isEqualTo(refElement.refElementShortDesc)
    }

    @Test
    fun `Builds valid RefElement DTO Null`() {
        var refElementDto = RefElementDto.from(null)
        assertThat(refElementDto).isNull()
    }
}