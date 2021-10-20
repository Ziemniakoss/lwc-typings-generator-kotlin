package pl.ziemniakoss.lwc_typings_generator.jsConfigGeneration

interface IJsConfigGenerator {
	/**
	 * Generate jsconfig file with c/componentName paths
	 */
	fun generateJsconfig();
}