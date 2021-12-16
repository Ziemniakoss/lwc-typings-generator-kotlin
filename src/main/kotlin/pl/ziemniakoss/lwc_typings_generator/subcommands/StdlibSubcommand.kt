package pl.ziemniakoss.lwc_typings_generator.subcommands

import kotlinx.cli.ArgType
import kotlinx.cli.Subcommand
import kotlinx.cli.default

class StdlibSubcommand : Subcommand("stdlib", "Fetch standard library") {
	val dontUseCache by option(ArgType.Boolean, "--no-cache", description = "Dont use cached standard lib, fetch new version").default(false)
	override fun execute() {
		TODO("Not implemented yet")
	}
}
