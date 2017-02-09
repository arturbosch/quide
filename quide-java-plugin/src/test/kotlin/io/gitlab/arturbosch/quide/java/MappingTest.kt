package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.mapping.ASTMapping
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.SourceFile
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.smells.longparam.LongParameterList
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class MappingTest {

	val storage = object : UserData() {}

	data class SFile(val path: String, val content: String) : SourceFile {
		override fun path() = path
		override fun content() = content

		constructor(file: File) : this(file.canonicalPath, file.readText())
	}

	data class SFileChange(val type: FileChange.Type, val oldFile: SFile, val newFile: SFile) : FileChange {
		override fun type(): FileChange.Type = type
		override fun oldFile(): SourceFile = oldFile
		override fun newFile(): SourceFile = newFile
	}

	data class SVersion(val number: Int, val changes: MutableList<FileChange>) : Versionable {
		override fun versionNumber(): Int = number
		override fun revision(): Revision {
			throw UnsupportedOperationException("not implemented")
		}

		override fun fileChanges(): MutableList<FileChange> = changes
	}

	companion object {
		private var number = 1
		private var fileChanges = mutableListOf<FileChange>()

		fun nextVersion(): Versionable {
			if (number == 1) {
				return SVersion(number++, fileChanges)
			} else {
				val lastVersion = "repo/version${number - 1}/Version.java"
				val thisVersion = "repo/version$number/Version.java"
				fileChanges = mutableListOf(SFileChange(FileChange.Type.MODIFICATION,
						SFile(lastVersion.asResourceStringPath(), lastVersion.content()),
						SFile(thisVersion.asResourceStringPath(), thisVersion.content())
				))
			}
			return SVersion(number++, fileChanges.toMutableList())
		}
	}

	@Test
	fun test() {
		val mapping = ASTMapping()

		// Version 1
		println("Version $number")
		val containerOne = "repo/version$number/Version.java".lint()
		val versionOne = nextVersion()
		storage.put(UserData.CURRENT_VERSION, versionOne)
		storage.put(UserData.CURRENT_CONTAINER, containerOne)
		mapping.execute(storage)
		val mapOne = currentContainer()
		assert(mapOne.size() == 12)
		assert(mapOne.all().all { it.startVersion().versionNumber() == 1 })
		assert(mapOne.all().all { it.endVersion().versionNumber() == 1 })

		// Version 2
		println("Version $number")
		val containerTwo = "repo/version$number/Version.java".lint()
		val versionTwo = nextVersion()
		storage.put(UserData.LAST_VERSION, versionOne)
		storage.put(UserData.LAST_CONTAINER, mapOne)
		storage.put(UserData.CURRENT_VERSION, versionTwo)
		storage.put(UserData.CURRENT_CONTAINER, containerTwo)
		mapping.execute(storage)
		val mapTwo = currentContainer()
		assert(mapTwo.size() == 12)
		assert(mapTwo.all().all { it.startVersion().versionNumber() == 1 })
		assert(mapTwo.all().all { it.endVersion().versionNumber() == 2 })

		// Version 3
		println("Version $number")
		val containerThree = "repo/version$number/Version.java".lint()
		val versionThree = nextVersion()
		storage.put(UserData.LAST_VERSION, versionTwo)
		storage.put(UserData.LAST_CONTAINER, mapTwo)
		storage.put(UserData.CURRENT_VERSION, versionThree)
		storage.put(UserData.CURRENT_CONTAINER, containerThree)
		mapping.execute(storage)
		val mapThree = currentContainer()
		mapThree.all().forEach { println(it) }
		assert(mapThree.size() == 12) // Three smells marked as dead, containers do not shrink!
		assert(mapThree.all().all { it.startVersion().versionNumber() == 1 })
		assert(mapThree.all().all { it.endVersion().versionNumber() == 3 })
		assert(mapThree.alive().size == 9)

		// Version 4
		println("Version $number")
		val containerFour = "repo/version$number/Version.java".lint()
		val versionFour = nextVersion()
		storage.put(UserData.LAST_VERSION, versionThree)
		storage.put(UserData.LAST_CONTAINER, mapThree)
		storage.put(UserData.CURRENT_VERSION, versionFour)
		storage.put(UserData.CURRENT_CONTAINER, containerFour)
		mapping.execute(storage)
		val mapFour = currentContainer()
		mapFour.all().forEach { println(it) }
		assert(mapFour.size() == 12) // no new smells in this version
		assert(mapFour.alive().all { it.startVersion().versionNumber() == 1 })
		assert(mapFour.alive().all { it.endVersion().versionNumber() == 4 })
		assert(mapFour.alive().size == 4) // 9 - 5 DeadCodes - 1 LPL + 1 LM = 4
		assert(mapFour.all().find { it.smell is LongParameterList }!!.isAlive.not())
	}

	private fun currentContainer() = storage.currentContainer<JavaSmellContainer, JavaCodeSmell>().get()
}

