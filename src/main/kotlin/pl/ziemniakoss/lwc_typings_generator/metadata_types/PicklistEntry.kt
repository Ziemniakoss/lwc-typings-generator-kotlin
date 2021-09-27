package pl.ziemniakoss.lwc_typings_generator.metadata_types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PicklistEntry(
	val value: String,
)
