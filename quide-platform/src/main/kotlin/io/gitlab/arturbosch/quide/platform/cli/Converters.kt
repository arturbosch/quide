package io.gitlab.arturbosch.quide.platform.cli

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.ParameterException
import io.gitlab.arturbosch.kutils.isDirectory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ExistingPathConverter : IStringConverter<Path?> {
	override fun convert(value: String): Path? {
		val config = Paths.get(value)
		if (Files.notExists(config))
			throw ParameterException("Provided path '$value' does not exist!")
		return config
	}
}

class DirectoryPathConverter : IStringConverter<Path?> {
	override fun convert(value: String): Path? {
		return Paths.get(value).apply {
			Files.createDirectories(this)
			require(this.isDirectory()) { "Output path must be a directory to store all reports!" }
		}
	}
}
