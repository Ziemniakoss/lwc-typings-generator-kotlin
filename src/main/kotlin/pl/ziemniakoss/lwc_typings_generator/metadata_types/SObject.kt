package pl.ziemniakoss.lwc_typings_generator.metadata_types

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