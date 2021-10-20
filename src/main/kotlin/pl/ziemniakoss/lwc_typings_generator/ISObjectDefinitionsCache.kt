package pl.ziemniakoss.lwc_typings_generator

import pl.ziemniakoss.lwc_typings_generator.metadataTypes.SObject
import java.util.*

interface ISObjectDefinitionsCache {
	fun store(sObject: SObject)

	suspend fun fetch(sObjectName: String): Optional<SObject>

	suspend fun fetchAllExcluding(excludedSObjectNames: Set<String>): List<SObject>
}