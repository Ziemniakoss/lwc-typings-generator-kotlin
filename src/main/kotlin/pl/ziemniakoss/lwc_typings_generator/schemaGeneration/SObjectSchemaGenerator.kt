package pl.ziemniakoss.lwc_typings_generator.schemaGeneration

import pl.ziemniakoss.lwc_typings_generator.metadataTypes.Field
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import java.io.BufferedWriter
import java.nio.file.Paths
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories

class SObjectSchemaGenerator : ISObjectSchemaGenerator {
	init {
		createSchemasFolder()
		deleteAllFilesInSchemaFolder()
	}

	private fun deleteAllFilesInSchemaFolder() {
		val allFiles = Paths.get(".sfdx", "typings", "lwc", "schema").toFile().listFiles()
		for(file in allFiles!!) {
			file.delete()
		}
	}
	private fun createSchemasFolder() {
		val schemaFolder = Paths.get(".sfdx", "typings", "lwc", "schema")
		schemaFolder.createDirectories()
	}

	override fun generateSchema(sObject: SObject, allSObjectsMap: Map<String, SObject>) {
		val output = Paths.get(".sfdx", "typings", "lwc", "schema", "${sObject.name}.d.ts").bufferedWriter()
		output.apply {
			write("""type FieldId = import("@salesforce/schema").FieldId""")
			newLine()

			write("""type ObjectId=import("@salesforce/schema").ObjectId""")
			newLine()
		}
		generateSchemaForSObject(sObject, output)
		sObject.fields.forEach { generateSchemaForField(sObject, it, output) }
		output.close()
	}

	private fun generateSchemaForField(sObject: SObject, field: Field, output: BufferedWriter) {
		output.apply {
			write("declare module \"@salesforce/schema/")
			write(sObject.name)
			write(".")
			write(field.name)
			write("\"{")
			newLine()

			write("\tconst ")
			write(field.name)
			write(":FieldId")
			newLine()

			write("\texport default ")
			write(field.name)
			newLine()

			write("}")
			newLine()
		}
	}

	private fun generateSchemaForSObject(sObject: SObject, output: BufferedWriter) {
		output.apply {
			write("declare module \"@salesforce/schema/")
			write(sObject.name)
			write("\"{")
			newLine()

			write("\tconst ")
			write(sObject.name)
			write(":ObjectId")
			newLine()

			write("\texport default ")
			write(sObject.name)
			write("\n}\n")
		}
	}
}
