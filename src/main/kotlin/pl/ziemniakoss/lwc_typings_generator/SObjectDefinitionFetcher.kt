package pl.ziemniakoss.lwc_typings_generator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

class SObjectDefinitionFetcher :ISObjectDefinitionFetcher{
	override suspend fun fetchSObjectDefinition(sObjectName: String) : SObject {
		val result = getSObjectDescribe(sObjectName)
		if(result.status == 0) {
			return result.result!!
		}
		throw SfdxOperationException("${result.name}: ${result.message}")
	}

	override suspend fun fetchSObjectsDefinitions(sObjectNames: Set<String>): List<SObject> = coroutineScope{
		val jobs = sObjectNames.map { async { fetchSObjectDefinition(it) } }
		val sObjects = mutableListOf<SObject>()
		for(job in jobs) {
			sObjects.add(job.await())
		}
		return@coroutineScope sObjects
	}

	private fun getSObjectDescribe(sObjectName: String): SfdxOperationResult {
		val proc = ProcessBuilder(listOf("sfdx", "force:schema:sobject:describe", "--json", "-s", sObjectName))
			.redirectOutput(ProcessBuilder.Redirect.PIPE)
			.redirectError(ProcessBuilder.Redirect.PIPE)
			.start()
		//TODO ERROR handling

		val resultAsJson = proc.inputStream.bufferedReader().readText()
		return ObjectMapper().registerModule(KotlinModule()).readValue(resultAsJson)

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	class SfdxOperationResult(
		val status: Int,
		val result: SObject?,
		val message: String?,
		val stack: String?,
		val name: String?
	)
}