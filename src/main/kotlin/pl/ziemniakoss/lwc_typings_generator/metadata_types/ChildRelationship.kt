package pl.ziemniakoss.lwc_typings_generator.metadata_types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ChildRelationship(
	val childSObject: String,
	val field: String,
	val relationshipName: String?
) {
}