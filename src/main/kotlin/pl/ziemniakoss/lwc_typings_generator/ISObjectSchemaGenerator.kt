package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

interface ISObjectSchemaGenerator {
	fun generateSchema(sObject: SObject, allSObjectsMap: Map<String, SObject>)
}
