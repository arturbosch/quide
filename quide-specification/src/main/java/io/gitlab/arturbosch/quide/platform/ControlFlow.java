package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.SmellContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Specifies the execution control flow of a detection platform.
 *
 * @author Artur Bosch
 */
public interface ControlFlow extends Runnable {

	Logger LOGGER = LoggerFactory.getLogger(ControlFlow.class.getSimpleName());

	/**
	 * Specifies when to run a processor in the analysis timeline.
	 */
	enum InjectionPoint {
		BeforeDetection, AfterDetection, BeforeAnalysis, AfterAnalysis
	}

	/**
	 * Override this method to return loaded plugins.
	 *
	 * @return a list of plugins
	 */
	List<Plugin> plugins();

	/**
	 * Should be run before an analysis to setup environment and useful variables inside user data.
	 *
	 * @param context analysis context object which should provide minimal environment about for quide
	 */
	default void beforeAnalysis(AnalysisContext context) {
		for (Plugin plugin : plugins()) {
			UserData data = plugin.userData();
			String detectorName = plugin.detector().name();

			data.put(UserData.TOOL_NAME, detectorName);
			data.put(UserData.PROJECT_PATH, context.projectPath());
			context.outputPath().ifPresent(outputPath -> data.put(UserData.OUTPUT_PATH, outputPath));
			data.put(UserData.QUIDE_DIRECTORY, context.quideDirectory());

			executeProcessors(plugin.processors(), plugin.userData(), InjectionPoint.BeforeAnalysis);
		}
	}

	/**
	 * Easiest way to run the platform.
	 * <p>
	 * Attention: this method runs all detectors synchronously!
	 */
	default void run(AnalysisContext context) {
		beforeAnalysis(context);
		plugins().forEach(this::execute);
		afterAnalysis();
	}

	/**
	 * Should be called after setting the environment for quide.
	 * Runs processors and the detector of a given plugin.
	 * Smell containers are cached for you in the user data of the plugin.
	 * <p>
	 * This method pays attention when to run each specified processor by looking at the {@see InjectionPoint}.
	 * <p>
	 * Preferably use this method in an asynchronous platform.
	 *
	 * @param plugin the plugin to execute
	 */
	default void execute(Plugin plugin) {
		UserData data = plugin.userData();
		String detectorName = plugin.detector().name();

		List<Processor> processors = plugin.processors();
		executeProcessors(processors, data, InjectionPoint.BeforeDetection);

		LOGGER.info("Starting '" + detectorName + "' ...");

		data.currentContainer().ifPresent(currentContainer ->
				data.put(UserData.LAST_CONTAINER, currentContainer));
		SmellContainer container = plugin.detector().execute(data);
		data.put(UserData.CURRENT_CONTAINER, container);

		executeProcessors(processors, data, InjectionPoint.AfterDetection);
	}

	/**
	 * Should be run after the analysis to execute remaining processors.
	 */
	default void afterAnalysis() {
		for (Plugin plugin : plugins()) {
			executeProcessors(plugin.processors(), plugin.userData(), InjectionPoint.AfterAnalysis);
		}
	}

	/**
	 * Executors given processors filtered by the given injection point.
	 *
	 * @param processors     processors from a plugin
	 * @param userData       a plugin's user data
	 * @param injectionPoint the point in the analysis when to execute the processors
	 */
	default void executeProcessors(List<Processor> processors,
								   UserData userData,
								   InjectionPoint injectionPoint) {
		processors.stream()
				.filter(processor -> processor.injectionPoint().equals(injectionPoint))
				.sorted(Comparator.comparingInt(Processor::priority))
				.forEach(processor -> {
					String name = processor.name();
					LOGGER.info("Executing processor '" + name + "'");
					processor.execute(userData);
				});
	}

}
