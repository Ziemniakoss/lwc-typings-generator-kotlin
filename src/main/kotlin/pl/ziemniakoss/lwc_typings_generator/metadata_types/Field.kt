package pl.ziemniakoss.lwc_typings_generator.metadata_types

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Field (
	val name: String,
	val nillable: Boolean,
	val picklistValues: List<PicklistEntry>?,
	val relationshipName: String?,
	val referenceTo: List<String>?,
	val type: FieldType
	){
}