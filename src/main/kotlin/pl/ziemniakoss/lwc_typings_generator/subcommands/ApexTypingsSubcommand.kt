package pl.ziemniakoss.lwc_typings_generator.subcommands

import kotlinx.cli.*

class ApexTypingsSubcommand : Subcommand("apex", "Generate typings for apex") {
	val generateForAll by option(ArgType.Boolean, "all", "a", "Generate typings for all apex classes").default(false)
	val apexClasses by argument(ArgType.String, "apexClasses", "Apex classes to generate ts typings for").vararg().optional()
	override fun execute() {
		TODO("Not implemented yet")
	}
}
