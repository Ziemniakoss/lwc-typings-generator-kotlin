package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

interface ISObjectDefinitionFetcher {
	suspend fun fetchSObjectDefinition(sObjectName: String): SObject

	suspend fun fetchSObjectsDefinitions(sObjectNames: Set<String>): List<SObject>
}
