package io.gitlab.arturbosch.quide.detection;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.Executable;
import io.gitlab.arturbosch.quide.platform.UserData;

/**
 * @author Artur Bosch
 */
public interface Detector<T extends CodeSmell> extends Tool, Executable {

}
