package pl.ziemniakoss.lwc_typings_generator.subcommands

import kotlinx.cli.Subcommand
import pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration.IJsConfigGenerator

class JsConfigSubcommand(private val jsConfigGenerator: IJsConfigGenerator) : Subcommand("jsconfig", "Generate proper jsconfig") {
	override fun execute() {
		jsConfigGenerator.generateJsconfig();
	}
}
