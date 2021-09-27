package pl.ziemniakoss.lwc_typings_generator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

class SObjectRepository :ISObjectRepository{
	override fun get(sObjectName: String) : SObject {
		val result = getSObjectDescribe(sObjectName)
		if(result.status == 0) {
			return result.result!!
		}
		throw SfdxOperationException("${result.name}: ${result.message}")
	}

	private fun getSObjectDescribe(sObjectName: String): SfdxOperationResult {
		val proc = ProcessBuilder(listOf("sfdx", "force:schema:sobject:describe", "--json", "-s", sObjectName))
			.redirectOutput(ProcessBuilder.Redirect.PIPE)
			.redirectError(ProcessBuilder.Redirect.PIPE)
			.start()

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