package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.io.BufferedWriter

interface ISObjectInterfaceGenerator {
	fun generateInterface(sObject: SObject, output: BufferedWriter, generateReferencesForSObjects: Set<String>)
}