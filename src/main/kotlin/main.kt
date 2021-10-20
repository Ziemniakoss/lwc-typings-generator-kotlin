import pl.ziemniakoss.lwc_typings_generator.*
import pl.ziemniakoss.lwc_typings_generator.interfcesGeneration.SObjectInterfaceGenerator
import pl.ziemniakoss.lwc_typings_generator.schemaGeneration.SObjectSchemaGenerator

fun main(args: Array<String>) {
	Main(
		args,
		definitionsFetcher = SObjectDefinitionFetcher(),
		definitionsCache = SObjectDefinitionsCache(),
		sObjectInterfaceGenerator = SObjectInterfaceGenerator(),
		sObjectSchemaGenerator = SObjectSchemaGenerator()
	).run()
}
