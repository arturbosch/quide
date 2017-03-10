package io.gitlab.arturbosch.quide.format.evolution

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.CodeSmell

/**
 * @author Artur Bosch
 */
interface CodeSmellParser {

	def toXml(CodeSmell smell, MarkupBuilder mb)

}