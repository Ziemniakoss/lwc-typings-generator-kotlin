package pl.ziemniakoss.lwc_typings_generator

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.ziemniakoss.lwc_typings_generator.interfcesGeneration.ISObjectInterfaceGenerator
import pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration.IJsConfigGenerator
import pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration.JsConfigGenerator
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import pl.ziemniakoss.lwc_typings_generator.schemaGeneration.ISObjectSchemaGenerator

class Main(
	private val args: Array<String>,
	private val definitionsFetcher: ISObjectDefinitionFetcher,
	private val definitionsCache: ISObjectDefinitionsCache,
	private val sObjectInterfaceGenerator: ISObjectInterfaceGenerator,
	private val sObjectSchemaGenerator: ISObjectSchemaGenerator,
	private val jsConfigGenerator: IJsConfigGenerator = JsConfigGenerator()
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

		val schemaGenerationJobs = sObjectsMap.values.map { async { sObjectSchemaGenerator.generateSchema(it, sObjectsMap) } }
		val interfacesGenerationJobs = sObjectsMap.values.map{ async { sObjectInterfaceGenerator.generateInterface(it) }}

		for(schemaGenerationJob in schemaGenerationJobs) {
			schemaGenerationJob.await()
		}
		for(interfaceGenerationJob in interfacesGenerationJobs) {
			interfaceGenerationJob.await()
		}
		jsConfigGenerator.generateJsconfig()
	}

	private suspend fun fetchSObjectDefinitions(): List<SObject> = definitionsFetcher.fetchSObjectsDefinitions(args.toSet())

	private suspend fun loadLocalSObjetsDefinitions(): List<SObject> = definitionsCache.fetchAllExcluding(args.toSet())


}