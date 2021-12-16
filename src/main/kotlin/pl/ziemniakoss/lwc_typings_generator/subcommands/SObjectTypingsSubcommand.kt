package pl.ziemniakoss.lwc_typings_generator.subcommands

import kotlinx.cli.ArgType
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.vararg
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.ziemniakoss.lwc_typings_generator.ISObjectDefinitionFetcher
import pl.ziemniakoss.lwc_typings_generator.ISObjectDefinitionsCache
import pl.ziemniakoss.lwc_typings_generator.interfcesGeneration.ISObjectInterfaceGenerator
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import pl.ziemniakoss.lwc_typings_generator.schemaGeneration.ISObjectSchemaGenerator

class SObjectTypingsSubcommand(
	private val schemaGenerator: ISObjectSchemaGenerator,
	private val interfacesGenerator: ISObjectInterfaceGenerator,
	private val sObjectCache: ISObjectDefinitionsCache,
	private val sObjectDefinitionsFetcher: ISObjectDefinitionFetcher,
) : Subcommand("sobject", "Generate typings for sobjects") {
	private val sObjects by argument(ArgType.String, "sObjects", "SObjects for type generation").vararg()
	private val maxSchemaDepth by option(ArgType.Int, "max-schema-depth", description = "Max schema types depth, should be number from 1 to 5").default(5)
	private val skipInterfacesGeneration by option(ArgType.Boolean, "no-interfaces", description = "Skip interfaces generation, generate only schema").default(false)

	override fun execute() = runBlocking {
		val fetchingFromRemoteJob = async { sObjectDefinitionsFetcher.fetchSObjectsDefinitions(sObjects.toSet()) }
		val parsingLocalJob = async { sObjectCache.fetchAllExcluding(sObjects.toSet()) }
		val sObjectsMap = mutableMapOf<String, SObject>()
		for (sObject in fetchingFromRemoteJob.await()) {
			sObjectsMap[sObject.name] = sObject;
		}
		for (sObject in parsingLocalJob.await()) {
			sObjectsMap[sObject.name] = sObject
		}

		if (!skipInterfacesGeneration) {
			generateInterfaces(sObjectsMap)
		}
		generateSchema(sObjectsMap);
	}

	private suspend fun generateInterfaces(sObjectsMap: Map<String, SObject>) = runBlocking {
		val interfacesGenerationJobs = sObjectsMap.values.map { async { interfacesGenerator.generateInterface(it) } }
		for (interfaceGenerationJob in interfacesGenerationJobs) {
			interfaceGenerationJob.await()
		}
	}

	private fun generateSchema(sObjectsMap: Map<String, SObject>) = runBlocking {
		val schemaGenerationJobs = sObjectsMap.values.map { async { schemaGenerator.generateSchema(it, sObjectsMap) } }
		for (schemaGenerationJob in schemaGenerationJobs) {
			schemaGenerationJob.await()
		}
	}
}
