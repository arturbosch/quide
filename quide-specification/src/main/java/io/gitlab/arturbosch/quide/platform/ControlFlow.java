package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.Detector;
import io.gitlab.arturbosch.quide.mapping.SmellMapping;
import io.gitlab.arturbosch.quide.model.CodeSmell;

import java.util.Comparator;
import java.util.List;

/**
 * @author Artur Bosch
 */
public interface ControlFlow {

	enum InjectionPoint {
		BeforeDetection, AfterDetection, AfterAnalysis
	}

	interface State {
		Detector<CodeSmell> detector();

		List<Processor> processors();

		SmellMapping<CodeSmell> mapping();

		UserData userData();
	}

	State state();

	default void executeDetection(UserData userData) {
		detector().execute(userData);
	}

	default void executeMapping(UserData userData) {
		mapping().execute(userData);
	}

	default void execute() {
		UserData data = userData();
		executeProcessors(data, InjectionPoint.BeforeDetection);
		executeDetection(data);
		executeProcessors(data, InjectionPoint.AfterDetection);
		executeMapping(data);
		executeProcessors(data, InjectionPoint.AfterAnalysis);
	}

	default void executeProcessors(UserData userData, InjectionPoint injectionPoint) {
		processors().stream()
				.filter(processor -> processor.injectionPoint().equals(injectionPoint))
				.sorted(Comparator.comparingInt(Processor::priority))
				.forEach(processor -> processor.execute(userData));
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

	default List<Processor> processors() {
		return state().processors();
	}

}
