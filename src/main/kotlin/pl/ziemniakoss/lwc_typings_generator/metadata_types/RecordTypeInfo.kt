package pl.ziemniakoss.lwc_typings_generator.metadata_types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RecordTypeInfo(
	val active: Boolean,
	val developerName: String,
	val name: String,
	val recordTypeId: String,
) {
}