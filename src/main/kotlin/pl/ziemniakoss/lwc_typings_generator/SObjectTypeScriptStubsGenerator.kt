package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories

class SObjectTypeScriptStubsGenerator(
	private val interfacesGenerator: ISObjectInterfaceGenerator,
	private val schemaGenerator: ISObjectSchemaGenerator,
	private val generateSObjectReferencesFor: Set<String>,
) {
	fun generateStubs(sObject: SObject) {
		createFolders()
		val outputPath = Paths.get(".sfdx", "typings", "lwc", "sobjects", "${sObject.name}.d.ts")

		val output = outputPath.bufferedWriter()


		val schemaOutput = Paths.get(".sfdx", "typings", "lwc", "schema", "${sObject.name}.d.ts").bufferedWriter()
		schemaOutput.apply {
			write("type SObject = import(\"./Commons\").SObject")
			newLine()

			write("type Field = import(\"./Commons\").Field")
			newLine()
		}
		interfacesGenerator.generateInterface(sObject, output, generateSObjectReferencesFor)
		schemaGenerator.generateSchema(sObject, schemaOutput)
		output.close()
		schemaOutput.close()
	}

	private fun createFolders() {
		Paths.get(".sfdx", "typings", "lwc", "schema").createDirectories()
		Paths.get(".sfdx", "typings", "lwc", "sobjects").createDirectories()
	}
}