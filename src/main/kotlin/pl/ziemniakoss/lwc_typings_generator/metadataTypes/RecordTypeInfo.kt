package pl.ziemniakoss.lwc_typings_generator.metadataTypes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RecordTypeInfo(
	val developerName: String,
	val master: Boolean
) {
}