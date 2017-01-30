package io.gitlab.arturbosch.quide.java.mapping

import difflib.DiffUtils
import difflib.Patch

/**
 * @author Artur Bosch
 */
fun textDiff(file1: String, file2: String): Patch<String> {
	return DiffUtils.diff(file1.split("\n"), file2.split("\n"))
}