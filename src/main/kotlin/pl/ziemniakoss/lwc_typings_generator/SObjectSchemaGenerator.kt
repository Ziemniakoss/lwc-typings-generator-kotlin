package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.Field
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.io.BufferedWriter
import java.nio.file.Paths
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile

class SObjectSchemaGenerator : ISObjectSchemaGenerator {
	override fun generateSchema(sObject: SObject, output: BufferedWriter) {
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
			write(":Field")
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
			write(":SObject")
			newLine()

			write("\texport default ")
			write(sObject.name)
			write("\n}\n")
		}
	}
}
