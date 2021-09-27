package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.ChildRelationship
import pl.ziemniakoss.lwc_typings_generator.metadata_types.Field
import pl.ziemniakoss.lwc_typings_generator.metadata_types.FieldType
import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.io.BufferedWriter

class SObjectInterfaceGenerator : ISObjectInterfaceGenerator {
	override fun generateInterface(sObject: SObject, output: BufferedWriter, generateReferencesForSObjects: Set<String>) {
		generateReferencedSObjectsImports(sObject, output, generateReferencesForSObjects)
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

			write("export {")
			write(sObject.name)
			write("}")
			newLine()
		}
	}


	private fun generateReferencedSObjectsImports(sObject: SObject, output: BufferedWriter, generateRefereesFor: Set<String>) {
		val allReferencedSObjects = mutableSetOf<String>()
		val referenceFields = sObject.fields.filter { it.type == FieldType.reference}
		for(field in referenceFields) {
			for(referencedSObject in field.referenceTo!!) {
				if (referencedSObject != sObject.name) {
					allReferencedSObjects.add(referencedSObject)
				}
			}
		}
		for(childRelationship in sObject.childRelationships) {
			if(childRelationship.childSObject != sObject.name) {
				allReferencedSObjects.add(childRelationship.childSObject)
			}
		}
		allReferencedSObjects
			.filter { generateRefereesFor.contains(it) }
			.forEach {
			output.apply {
				write("type ")
				write(it)
				write("=import(\"./")
				write(it)
				write("\").")
				write(it)
				newLine()
			}
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
		if (field.type == FieldType.reference) {
			output.apply {
				write("string")
				newLine()
			}
			if(field.relationshipName != null) {
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
			output.write(getPicklistTypeName(sObject,field))
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