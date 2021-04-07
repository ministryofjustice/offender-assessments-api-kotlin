package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty

data class FilteredRefDataRequestDto(
  @ApiModelProperty(value = "OASys User Code", example = "TEST_USER")
  val oasysUserCode: String,

  @ApiModelProperty(value = "OASys Area Code", example = "WWS")
  val oasysAreaCode: String,

  @ApiModelProperty(value = "Team", example = "Team1")
  val team: String? = null,

  @ApiModelProperty(value = "Assessor", example = "TEST_USER")
  val assessor: String? = null,

  @ApiModelProperty(value = "OASys Offender PK", example = "123456")
  val offenderPk: Long? = null,

  @ApiModelProperty(value = "OASys Assessment PK", example = "123456")
  val oasysSetPk: Long? = null,

  @ApiModelProperty(value = "OASys Assessment Type", example = "SHORT_FORM_PSR")
  val assessmentType: String? = null,

  @ApiModelProperty(value = "OASys Assessment Type", example = "START, CONTINUE, COMPLETE")
  val assessmentStage: String? = null,

  @ApiModelProperty(value = "OASys Section Code", example = "RSR")
  val sectionCode: String? = null,

  @ApiModelProperty(value = "Section field name", example = "assessor_office")
  val fieldName: String,

  @ApiModelProperty(value = "Parent list", example = "assessor -> OASYS_ADMIN")
  val parentList: Map<String, String>? = emptyMap()

)
