package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject
import java.io.BufferedWriter

interface ISObjectSchemaGenerator {
	fun generateSchema(sObject: SObject, output: BufferedWriter)
}
