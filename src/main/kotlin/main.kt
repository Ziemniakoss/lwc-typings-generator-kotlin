import pl.ziemniakoss.lwc_typings_generator.*

fun main(args: Array<String>) {
	Main(
		args,
		definitionsFetcher = SObjectDefinitionFetcher(),
		definitionsCache = SObjectDefinitionsCache(),
		sObjectInterfaceGenerator = SObjectInterfaceGenerator(),
		sObjectSchemaGenerator = SObjectSchemaGenerator()
	).run()
}
