package uk.gov.justice.digital.oasys.api

import uk.gov.justice.digital.oasys.jpa.entities.RefElement

data class RefElementDto (

    val code: String? = null,
    val shortDescription: String? = null,
    val description: String? = null
)
{
    companion object {
        fun from(refElement: RefElement?): RefElementDto? {
            if (refElement == null) {
                return null
            }
            return RefElementDto(refElement?.refElementCode, refElement?.refElementShortDesc, refElement?.refElementDesc)
        }
    }
}
