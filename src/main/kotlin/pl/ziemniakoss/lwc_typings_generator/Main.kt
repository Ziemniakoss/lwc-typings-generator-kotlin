package pl.ziemniakoss.lwc_typings_generator

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

class Main(
	private val args: Array<String>,
	private val definitionsFetcher: ISObjectDefinitionFetcher,
	private val definitionsCache: ISObjectDefinitionsCache,
	private val sObjectInterfaceGenerator: ISObjectInterfaceGenerator,
	private val sObjectSchemaGenerator: ISObjectSchemaGenerator,
) {
	fun run() = runBlocking {
		val fetchingFromRemoteJob = async { fetchSObjectDefinitions() }
		val parsingLocalJob = async { loadLocalSObjetsDefinitions() }

		val sObjectsMap = mutableMapOf<String, SObject>()
		for (fetchedSObject in fetchingFromRemoteJob.await()) {
			sObjectsMap[fetchedSObject.name] = fetchedSObject
			definitionsCache.store(fetchedSObject)
		}
		for (parsedSObject in parsingLocalJob.await()) {
			sObjectsMap[parsedSObject.name] = parsedSObject
		}

		sObjectsMap.values.forEach { sObjectInterfaceGenerator.generateInterface(it) }
		sObjectsMap.values.forEach { sObjectSchemaGenerator.generateSchema(it) }
		sObjectsMap.values.forEach{ println("HAS ${it.name}")}

	}

	private suspend fun fetchSObjectDefinitions(): List<SObject> = definitionsFetcher.fetchSObjectsDefinitions(args.toSet())

	private suspend fun loadLocalSObjetsDefinitions(): List<SObject> = definitionsCache.fetchAllExcluding(args.toSet())


}