package uk.gov.justice.digital.oasys.api

import io.swagger.annotations.ApiModelProperty
import uk.gov.justice.digital.oasys.api.DtoUtils.ynToBoolean
import uk.gov.justice.digital.oasys.jpa.entities.Offender

data class OffenderDto(

        @ApiModelProperty(value = "OASys Offender PK", example = "123456")
        val oasysOffenderId: Long?,

        @ApiModelProperty(value = "Limited Access Offender", example = "true")
        val limitedAccessOffender: Boolean? = null,

        @ApiModelProperty(value = "Family Name", example = "Smith")
        val familyName: String? = null,

        @ApiModelProperty(value = "Forename", example = "John")
        val forename1: String? = null,

        @ApiModelProperty(value = "Middle Name", example = "James")
        val forename2: String? = null,

        @ApiModelProperty(value = "Middle Name", example = "Andrew")
        val forename3: String? = null,

        @ApiModelProperty(value = "Risk to Others", example = "High")
        val riskToOthers: String? = null,

        @ApiModelProperty(value = "Risk to Others", example = "Low")
        val riskToSelf: String? = null,

        @ApiModelProperty(value = "PNC Number", example = "AB123456")
        val pnc: String? = null,

        @ApiModelProperty(value = "Delius CRN", example = "123456")
        val crn: String? = null,

        @ApiModelProperty(value = "NOMIS ID", example = "123456")
        val nomisId: String? = null,

        @ApiModelProperty(value = "Legacy Case ID", example = "123456")
        val legacyCmsProbNumber: String? = null,

        @ApiModelProperty(value = "CRO Number", example = "123456")
        val croNumber: String? = null,

        @ApiModelProperty(value = "NOMIS Booking Number", example = "123456")
        val bookingNumber: String? = null,

        @ApiModelProperty(value = "Merged PNC Number", example = "AB123456")
        val mergePncNumber: String? = null,

        @ApiModelProperty(value = "Merged From Offender PK", example = "123456")
        val mergedOasysOffenderId: Long? = null) {

    companion object {
        fun from(offender: Offender?): OffenderDto {
            return OffenderDto(
                    offender?.offenderPk,
                    offender?.limitedAccessOffender.ynToBoolean(),
                    offender?.familyName,
                    offender?.forename1,
                    offender?.forename2,
                    offender?.forename3,
                    offender?.riskToOthers,
                    offender?.riskToSelf,
                    offender?.pnc,
                    offender?.cmsProbNumber,
                    offender?.cmsPrisNumber,
                    offender?.legacyCmsProbNumber,
                    offender?.croNumber,
                    offender?.prisonNumber,
                    offender?.mergePncNumber,
                    offender?.mergedOffenderPK
            )
        }
    }


}