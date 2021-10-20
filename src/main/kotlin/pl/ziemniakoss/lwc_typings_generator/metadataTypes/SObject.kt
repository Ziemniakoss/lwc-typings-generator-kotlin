package pl.ziemniakoss.lwc_typings_generator.metadataTypes

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SObject(
	val fields: List<Field>,
	val childRelationships: List<ChildRelationship>,
	val keyPrefix: String,
	val name: String,
	val recordTypeInfos: List<RecordTypeInfo>
) {

}