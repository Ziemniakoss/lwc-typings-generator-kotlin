package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadata_types.SObject

interface ISObjectInterfaceGenerator {
	fun generateInterface(sObject: SObject)
}
