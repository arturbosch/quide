package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.reflect.TypeReference
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Artur Bosch
 */
@Suppress("UNCHECKED_CAST")
class Storage : UserData {

	private val userData: Map<String, Any> = ConcurrentHashMap()

	override fun lastVersion(): Versionable {
		return get("lastVersion", TypeReference.get(Versionable::class.java)) ?: DefaultVersion
	}

	override fun lastContainer(): SmellContainer<CodeSmell>? {
		val type = object : TypeReference<SmellContainer<CodeSmell>>() {}
		return get("TypeReference", type) ?: DefaultContainer
	}

	override fun currentVersion(): Versionable? {
		return get("currentVersion", TypeReference.get(Versionable::class.java)) ?: DefaultVersion
	}

	override fun projectPath(): Path? {
		return get("currentVersion", TypeReference.get(Path::class.java))

	}

	private fun <T> get(key: String, type: TypeReference<T>): T? {
		val typeToken = type.type
		val any = userData[key]
		return if (any != null && any.javaClass == typeToken) {
			any as T
		} else {
			null
		}
	}

}