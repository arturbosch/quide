package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.TestFileSystem
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
class FileSystemTest : Spek({

	describe("file system representation of resources/baseDir") {

		val fs = TestFileSystem

		it("base and work dir should exist") {
			assertThat(fs.projectDir.exists()).isTrue()
			assertThat(fs.workDir.exists()).isTrue()
		}

		it("must contain one java file") {
			val javaFile = fs.inputFile { it.ending == "java" }
			val javaFiles = fs.inputFiles { it.ending == "java" }.toList()
			assertThat(javaFile?.name).isEqualTo("TestJavaFile")
			assertThat(javaFile?.ending).isEqualTo("java")
			assertThat(javaFiles).hasSize(1)
		}

		it("fs should allow traversing the files multiple times") {
			val java = fs.inputFile { it.ending == "java" }
			val kotlin = fs.inputFile { it.ending == "kt" }
			assertThat(java?.ending).isEqualTo("java")
			assertThat(kotlin?.ending).isEqualTo("kt")
		}

		it("should contain one dir with name resources") {
			val file = fs.resolvePath("resources")
			val resourcesDir = fs.inputDir(file)
			val filesOfDir = file.listFiles()
			val configTxt = filesOfDir[0]
			val configFile = fs.inputFile(configTxt)
			assertThat(resourcesDir?.name).isEqualTo("resources")
			assertThat(configFile?.name).isEqualTo("config")
			assertThat(configFile?.ending).isEqualTo("txt")
		}
	}
})
