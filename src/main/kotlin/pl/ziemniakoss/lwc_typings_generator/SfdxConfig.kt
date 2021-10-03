package pl.ziemniakoss.lwc_typings_generator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Info read from ".sfx/sfdx-config.json file
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class SfdxConfig(val defaultusername: String)
