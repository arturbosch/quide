package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.CodeSmellDetector;

import java.util.List;

/**
 * @author Artur Bosch
 */
public interface Plugin extends Nameable {

	CodeSmellDetector detector();

	List<Processor> processors();

	UserData userData();
}
