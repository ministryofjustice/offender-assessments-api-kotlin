package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.jpa.entities.FilteredRefDataEntity
import uk.gov.justice.digital.oasys.jpa.entities.RefElement

data class RefElementDto(

  @ApiModelProperty(value = "Reference Code", example = "LAYER_3")
  val code: String? = null,

  @ApiModelProperty(value = "Reference short Description", example = "Layer 3")
  val shortDescription: String? = null,

  @ApiModelProperty(value = "Reference short Description", example = "Full (Layer 3)")
  val description: String? = null
) {
  companion object {
    fun from(refElement: RefElement?): RefElementDto? {
      if (refElement == null) {
        return null
      }
      return RefElementDto(refElement.refElementCode, refElement.refElementShortDesc, refElement.refElementDesc)
    }

    fun from(refElement: FilteredRefDataEntity): RefElementDto {
      return RefElementDto(code = refElement.returnValue, description = refElement.displayValue)
    }
  }
}
