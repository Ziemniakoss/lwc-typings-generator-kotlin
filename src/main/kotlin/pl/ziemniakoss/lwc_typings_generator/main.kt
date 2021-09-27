import pl.ziemniakoss.lwc_typings_generator.*
import java.lang.Exception
import java.nio.file.AccessDeniedException
import java.nio.file.Paths
import java.util.logging.Logger

fun main(args: Array<String>) {
	println("Hello")
	val sObjectRepository = SObjectRepository()
	val path = Paths.get(".sfdx","typings", "lwc", "sobjects")
	val sObjectTypesAfterGeneration = path.toFile().walk()
		.filter { it.isFile }
		.map{ it.name.substring(0, it.name.length-5)}
		.filter { it != "Commons" }
		.toMutableSet()
	sObjectTypesAfterGeneration.addAll(args)
	val tsTypingsGenerator = SObjectTypeScriptStubsGenerator(
		interfacesGenerator = SObjectInterfaceGenerator(),
		schemaGenerator = SObjectSchemaGenerator(),
		generateSObjectReferencesFor = sObjectTypesAfterGeneration
	)
	val log = Logger.getAnonymousLogger()

	try {
		CommonTypesGenerator().generateCommonTypes()
	} catch (e :Exception) {
		log.severe("Error while generating common types: ${e.message}")
	}
	for (sObjectName in args) {
		try {
			tsTypingsGenerator.generateStubs(sObjectRepository.get(sObjectName))
		} catch (e: AccessDeniedException) {
			log.severe("Error while writing types for $sObjectName, Make sure you have write access to ${e.file}, please grant yourself write access or delete file")
		} catch (e: SfdxOperationException) {
			log.severe("Error while fetching $sObjectName describe: ${e.message}")
		}
	}
}