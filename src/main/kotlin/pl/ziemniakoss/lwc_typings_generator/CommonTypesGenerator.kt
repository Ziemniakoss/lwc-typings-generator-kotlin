package pl.ziemniakoss.lwc_typings_generator

import java.nio.file.Paths
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories

class CommonTypesGenerator {
	fun generateCommonTypes() {
		Paths.get(".sfdx", "typings", "lwc", "schema").createDirectories()
		val path = Paths.get(".sfdx", "typings", "lwc", "schema" ,"Commons.d.ts")
		val output = path.bufferedWriter()
		output.apply {
			write("export interface SObject{")
			newLine()

			write("\tobjectApiName:string")
			newLine()

			write("}")
			newLine()

			write("export interface Field{")
			newLine()

			write("\tobjectApiName:string")
			newLine()

			write("\tfieldApiName:string")
			newLine()

			write("}")
			newLine()
		}
		output.close()
	}
}