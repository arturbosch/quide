package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
interface Detector<out S : SmellContainer<*>> : Executable<S>, Nameable
