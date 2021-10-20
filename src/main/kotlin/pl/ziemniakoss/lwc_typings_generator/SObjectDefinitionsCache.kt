package pl.ziemniakoss.lwc_typings_generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class SObjectDefinitionsCache : ISObjectDefinitionsCache {
	private val jsonMapper = ObjectMapper().registerModule(KotlinModule())

	init {
		createCacheFolder()
	}

	override fun store(sObject: SObject) {
		val output = getPathForSObjectName(sObject.name).bufferedWriter()
		jsonMapper.writeValue(output, sObject)
		output.close()
	}

	override suspend fun fetch(sObjectName: String): Optional<SObject> {
		val pathWithCachedJson = getPathForSObjectName(sObjectName)
		if (pathWithCachedJson.exists()) {
			return jsonMapper.readValue(pathWithCachedJson.toFile())
		}
		return Optional.empty()
	}

	override suspend fun fetchAllExcluding(excludedSObjectNames: Set<String>): List<SObject> = coroutineScope {
		val excludedCachedFiles = excludedSObjectNames.map { "$it.json" }
		val pathsToParse = Paths.get(System.getProperty("user.home"), ".cache", "lwc-typings-generator", getDefaultSfdxUserName()).toFile()
			.walk()
			.drop(1)
			.filter { !excludedCachedFiles.contains(it.name) }
			.map { it.toPath() }

		val jobs = pathsToParse.map { async { fetchFromPath(it) } }
		val sObjects = mutableListOf<SObject>()
		for (job in jobs) {
			sObjects.add(job.await())
		}
		return@coroutineScope sObjects
	}

	private fun fetchFromPath(path: Path): SObject {
		return jsonMapper.readValue(path.toFile())
	}

	private fun getPathForSObjectName(sObjectName: String): Path {
		return Paths.get(System.getProperty("user.home"), ".cache", "lwc-typings-generator", getDefaultSfdxUserName(), "$sObjectName.json")
	}

	private fun getDefaultSfdxUserName():String {
		val sfdxConfg: SfdxConfig = jsonMapper.readValue(Paths.get(".sfdx", "sfdx-config.json").toFile())
		return sfdxConfg.defaultusername
	}
	private fun createCacheFolder() {
		val cacheFolder = Paths.get(System.getProperty("user.home"), ".cache", "lwc-typings-generator", getDefaultSfdxUserName())
		cacheFolder.createDirectories()
	}
}
