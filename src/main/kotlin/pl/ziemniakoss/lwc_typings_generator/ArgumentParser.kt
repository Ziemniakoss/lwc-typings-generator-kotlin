package pl.ziemniakoss.lwc_typings_generator

import com.sun.org.apache.xpath.internal.Arg
import kotlinx.cli.*

//https://github.com/Kotlin/kotlinx-cli
class ArgumentParser : IArgumentParser{
	override fun parseAndRun(args: Array<String>){
		val parser = ArgParser("lwc-typings-generator")
		parser.subcommands(JsConfigSubcommand(), ApexTypingsSubcommand(), SObjectTypingsSubcommand(), StdlibSubcommand())
		parser.parse(args)
	}
}

class JsConfigSubcommand : Subcommand("jsconfig", "Generate proper jsconfig") {
	override fun execute() {
		println("Hellop form commad")
	}

}

class ApexTypingsSubcommand : Subcommand("apex", "Generate typings for apex") {
	val generateForAll by option(ArgType.Boolean, "all", "a", "Generate typings for all apex classes").default(false)
	val apexClasses by argument(ArgType.String, "apexClasses", "Apex classes to generate ts typings for").vararg().optional()
	override fun execute() {
		println("Apex generator")
		println(generateForAll)
		println(apexClasses)
	}
}

class SObjectTypingsSubcommand : Subcommand("sobject", "Generate typings for sobjects") {
	val generateForAll by option(ArgType.Boolean, "all", "a", "Generate for all sobjects in org, HIGLY DISCOURAGED")
	val sObjects by argument(ArgType.String, "sObjects", "SObjects for type generation")
	val maxSchemaDepth by option(ArgType.Int, "max-schema-depth", description = "Max schema types depth, should be number from 1 to 5").default(5)
	val skipInterfacesGeneration by option(ArgType.Boolean, "no-interfaces", description = "Skip interfaces generation, generate only schema").default(false)

	override fun execute() {
		println("Genertate typings for sobject")
	}
}

class StdlibSubcommand : Subcommand("stdlib", "Fetch standard library") {
	val dontUseCache by option(ArgType.Boolean, "--no-cache", description = "Dont use cached standard lib, fetch new version").default(false)
	override fun execute() {
		println("Fetch stdlib")
	}
}