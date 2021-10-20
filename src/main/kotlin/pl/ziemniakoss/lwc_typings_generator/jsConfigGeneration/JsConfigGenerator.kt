package pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.writeText

class JsConfigGenerator : IJsConfigGenerator {
	private val mapper = ObjectMapper().registerModule(KotlinModule())

	override fun generateJsconfig() {
		val currentJsonConfig = readCurrentJsConfig();

		val compilerOptions = currentJsonConfig["compilerOptions"] as ObjectNode? ?: currentJsonConfig.objectNode()
		compilerOptions.put("experimentalDecorators", true)
		compilerOptions.put("baseUrl", ".")

		val pathsNode = compilerOptions["paths"] as ObjectNode? ?: compilerOptions.putObject("paths")

		removeExistingLwcComponentsPaths(pathsNode)
		createLwcComponentsPaths(pathsNode)
		saveNewJsConfig(currentJsonConfig)
	}

	private fun saveNewJsConfig(jsConfig: ObjectNode) {
		val outputPath = getPathToJsConfig()
		outputPath.writeText(jsConfig.toString())
	}

	private fun createLwcComponentsPaths(pathsNode: ObjectNode) {
		for(lwcComponentName in getLwcComponentNames()) {
			val componentNode = pathsNode.putArray("c/$lwcComponentName")
			componentNode.add("${lwcComponentName}${File.separator}${lwcComponentName}")
		}
	}

	private fun removeExistingLwcComponentsPaths(pathsNode: ObjectNode) {
		for(fieldName in pathsNode.fieldNames()) {
			if(fieldName.startsWith("c/")){
				pathsNode.remove(fieldName)
			}
		}
	}

	private fun getLwcComponentNames():List<String> {
		val folderWithLwcComponents  = Paths.get("force-app", "main", "default", "lwc")
		return folderWithLwcComponents.toFile().listFiles()
			.filter { it.isDirectory }
			.map { it.name }
	}

	private fun readCurrentJsConfig(): ObjectNode {
		val jsConfigPath =getPathToJsConfig()
		if(jsConfigPath.exists()) {
			return mapper.readTree(jsConfigPath.toFile()) as ObjectNode
		}
		return JsonNodeFactory.instance.objectNode()
	}

	private fun getPathToJsConfig():Path{
		return Paths.get("force-app", "main", "default", "lwc", "jsconfig.json")
	}
}
