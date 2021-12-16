package pl.ziemniakoss.lwc_typings_generator

import kotlinx.cli.ArgParser
import pl.ziemniakoss.lwc_typings_generator.interfcesGeneration.SObjectInterfaceGenerator
import pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration.JsConfigGenerator
import pl.ziemniakoss.lwc_typings_generator.schemaGeneration.SObjectSchemaGenerator
import pl.ziemniakoss.lwc_typings_generator.subcommands.ApexTypingsSubcommand
import pl.ziemniakoss.lwc_typings_generator.subcommands.JsConfigSubcommand
import pl.ziemniakoss.lwc_typings_generator.subcommands.SObjectTypingsSubcommand
import pl.ziemniakoss.lwc_typings_generator.subcommands.StdlibSubcommand

class ArgumentParser : IArgumentParser {
	override fun parseAndRun(args: Array<String>) {
		val parser = ArgParser("lwc-typings-generator")
		parser.subcommands(
			JsConfigSubcommand(JsConfigGenerator()),
			ApexTypingsSubcommand(),
			SObjectTypingsSubcommand(SObjectSchemaGenerator(), SObjectInterfaceGenerator(), SObjectDefinitionsCache(), SObjectDefinitionFetcher()),
			StdlibSubcommand()
		)
		parser.parse(args)
	}
}
