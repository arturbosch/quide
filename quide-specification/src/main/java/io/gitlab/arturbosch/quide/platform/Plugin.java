package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.Detector;
import io.gitlab.arturbosch.quide.mapping.SmellMapping;
import io.gitlab.arturbosch.quide.model.CodeSmell;

import java.util.List;

/**
 * @author Artur Bosch
 */
public interface Plugin {
	<T extends CodeSmell> Detector<T> detector();

	List<Processor> processors();

	<T extends CodeSmell> SmellMapping<T> mapping();

	UserData userData();
}
