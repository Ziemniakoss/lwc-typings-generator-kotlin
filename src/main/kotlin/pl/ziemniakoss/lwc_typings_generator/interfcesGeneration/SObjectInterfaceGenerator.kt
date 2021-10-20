package pl.ziemniakoss.lwc_typings_generator.interfcesGeneration

import pl.ziemniakoss.lwc_typings_generator.metadataTypes.ChildRelationship
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.Field
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.FieldType
import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import java.io.BufferedWriter
import java.nio.file.AccessDeniedException
import java.nio.file.Paths
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories

class SObjectInterfaceGenerator : ISObjectInterfaceGenerator {
	init {
		Paths.get(".sfdx", "typings", "lwc", "sobjects").createDirectories()
		deleteExistingFilesInSObjectFolder()
	}

	private fun deleteExistingFilesInSObjectFolder() {
		val allFiles = Paths.get(".sfdx", "typings", "lwc", "sobjects").toFile().listFiles()
		for (file in allFiles!!) {
			file.delete()
		}
	}

	override fun generateInterface(sObject: SObject) {
		val outputPath = Paths.get(".sfdx", "typings", "lwc", "sobjects", "${sObject.name}.d.ts")
		try {
			val output = outputPath.bufferedWriter()
			generateRecordTypeTypes(sObject, output)
			generatePicklistTypes(sObject, output)
			output.apply {
				write("declare interface ")
				write(sObject.name)
				if(sObject.name == "RecordType") {
					write("<T>")
				}
				write(" {")
				newLine()
			}
			sObject.fields.forEach { generateTypingsForField(sObject, it, output) }
			generateTypingsForChildRelationships(sObject.childRelationships, output)
			output.apply {
				write("}")
				newLine()
			}
			output.close()
		} catch (e: AccessDeniedException) {
			println("Error occurred while generating interfaces for ${sObject.name}: Make sure that you have write access to ${outputPath.toUri()}")
		} catch (e: Exception) {
			println("Error occurred while generating interfaces for ${sObject.name}: ${e.message}")
		}
	}

	private fun generateRecordTypeTypes(sObject: SObject, output: BufferedWriter) {
		val nonMasterRecordTypesDevNames = sObject.recordTypeInfos.filter { !it.master }
			.map { "\"${it.developerName}\"" }
			.sorted()
		if (nonMasterRecordTypesDevNames.isEmpty()) {
			return
		}
		output.apply {
			write("type ")
			write(sObject.name)
			write("__RecordType__DevName=")
			write(nonMasterRecordTypesDevNames.joinToString("|"))
			newLine()
		}
	}

	private fun generatePicklistTypes(sObject: SObject, output: BufferedWriter) {
		sObject.fields
			.filter { it.type == FieldType.picklist || it.type == FieldType.multipicklist }
			.filter { it.picklistValues != null }
			.forEach { generatePicklistType(sObject, it, output) }
	}

	private fun generatePicklistType(sObject: SObject, picklistField: Field, output: BufferedWriter) {
		output.write("declare type ")
		output.write(getPicklistTypeName(sObject, picklistField))
		output.write("=")
		output.write(picklistField.picklistValues
		!!.map { "\"${it.value.replace("\"", "\\\"")}\"" }
			.joinToString("|"))
		output.newLine()
	}

	private fun generateTypingsForField(sObject: SObject, field: Field, output: BufferedWriter) {
		output.apply {
			write("\t")
			write(field.name)
			write(":")
		}
		if (sObject.name == "RecordType" && field.name == "DeveloperName") {
			output.apply {
				write("T")
				newLine()
			}
		} else if (field.type == FieldType.reference) {
			output.apply {
				write("string")
				newLine()
			}
			if (field.relationshipName != null) {
				output.apply {
					write("\t")
					write(field.relationshipName)
					write(":")
					val joinedReferenceTo = field.referenceTo
						?.map {
							if (it == "RecordType") {
								return@map "RecordType<${sObject.name}__RecordType__DevName"
							}
							return@map it
						}
						?.joinToString("|") ?: "any"
					output.write(joinedReferenceTo)
				}
			}
		} else if (field.type == FieldType.reference) {
			output.write("string")
		} else if (field.type == FieldType.picklist || field.type == FieldType.multipicklist) {
			output.write(getPicklistTypeName(sObject, field))
		} else {
			output.write(field.type.getJsType())
		}
		output.newLine()
	}

	private fun generateTypingsForChildRelationships(childRelationships: List<ChildRelationship>, output: BufferedWriter) {
		childRelationships.filter { it.relationshipName != null }
			.forEach { generateTypingsForChildRelationship(it, output) }
	}

	private fun generateTypingsForChildRelationship(childRelationship: ChildRelationship, output: BufferedWriter) {
		output.apply {
			write("\t")
			write(childRelationship.relationshipName)
			write(":")
			write(childRelationship.childSObject)
			write("[]")
			newLine()
		}
	}

	private fun getPicklistTypeName(sObject: SObject, field: Field): String = "${sObject.name}__${field.name}"

}