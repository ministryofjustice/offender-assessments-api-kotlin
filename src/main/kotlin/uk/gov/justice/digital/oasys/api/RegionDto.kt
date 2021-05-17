package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty

data class RegionDto(

  @ApiModelProperty(value = "Area Name", example = "Wakefield (HMP)")
  val name: String,

  @ApiModelProperty(value = "Area Code", example = "WWS")
  val code: String,

)
