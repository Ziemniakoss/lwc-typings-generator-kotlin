package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.ChildRelationship
import pl.ziemniakoss.lwc_typings_generator.metadata_types.Field
import pl.ziemniakoss.lwc_typings_generator.metadata_types.FieldType
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.io.BufferedWriter
import java.nio.file.Paths
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories

class SObjectInterfaceGenerator : ISObjectInterfaceGenerator {
	init {
		Paths.get(".sfdx", "typings", "lwc", "sobjects").createDirectories()
	}

	override fun generateInterface(sObject: SObject) {
		val output = Paths.get(".sfdx", "typings", "lwc", "sobjects", "${sObject.name}.d.ts").bufferedWriter()
		generatePicklistTypes(sObject, output)
		output.apply {
			write("declare interface ")
			write(sObject.name)
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
		if (field.type == FieldType.reference) {
			output.apply {
				write("string")
				newLine()
			}
			if (field.relationshipName != null) {
				output.apply {
					write("\t")
					write(field.relationshipName)
					write(":")
					output.write(field.referenceTo?.joinToString("|") ?: "any")
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