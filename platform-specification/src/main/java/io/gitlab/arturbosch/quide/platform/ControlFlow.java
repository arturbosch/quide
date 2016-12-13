package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.Detector;
import io.gitlab.arturbosch.quide.mapping.SmellMapping;
import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Artur Bosch
 */
public interface ControlFlow {

	enum InjectionPoint {
		AfterAnalysis, AfterMapping
	}

	interface State {
		List<PreProcessor> preProcessors();

		Detector<CodeSmell> detector();

		List<PostProcessor> postProcessors();

		SmellMapping<CodeSmell> mapping();

		UserData userData();
	}

	State state();

	SmellContainer executeDetection(Detector<CodeSmell> detector);

	SmellContainer executeMapping(SmellMapping<CodeSmell> mapping);

	default void execute() {
		executePreProcessors(userData());
		SmellContainer container = executeDetection(detector());
		executePostProcessors(container, InjectionPoint.AfterAnalysis);
		container = executeMapping(mapping());
		executePostProcessors(container, InjectionPoint.AfterMapping);
	}

	default void executePreProcessors(UserData userData) {
		Versionable version = userData.currentVersion();
		Path projectPath = userData.projectPath();
		preProcessors().forEach(preProcessor -> preProcessor.process(version, projectPath, userData));
	}

	default void executePostProcessors(SmellContainer container, InjectionPoint injectionPoint) {
		postProcessors().stream()
				.filter(postProcessor -> postProcessor.flow().equals(injectionPoint))
				.forEach(postProcessor -> postProcessor.process(container, userData()));
	}

	default UserData userData() {
		return state().userData();
	}

	default Detector<CodeSmell> detector() {
		return state().detector();
	}

	default SmellMapping<CodeSmell> mapping() {
		return state().mapping();
	}

	default List<PreProcessor> preProcessors() {
		return state().preProcessors();
	}

	default List<PostProcessor> postProcessors() {
		return state().postProcessors();
	}

}
