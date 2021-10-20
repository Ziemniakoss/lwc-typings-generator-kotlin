package pl.ziemniakoss.lwc_typings_generator.schemaGeneration

import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject

interface ISObjectSchemaGenerator {
	fun generateSchema(sObject: SObject, allSObjectsMap: Map<String, SObject>)
}
