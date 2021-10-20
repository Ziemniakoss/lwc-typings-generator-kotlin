package pl.ziemniakoss.lwc_typings_generator.metadataTypes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ChildRelationship(
	val childSObject: String,
	val field: String,
	val relationshipName: String?
) {
}