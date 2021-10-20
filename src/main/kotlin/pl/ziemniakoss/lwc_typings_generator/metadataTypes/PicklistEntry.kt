package pl.ziemniakoss.lwc_typings_generator.metadataTypes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PicklistEntry(
	val value: String,
)
