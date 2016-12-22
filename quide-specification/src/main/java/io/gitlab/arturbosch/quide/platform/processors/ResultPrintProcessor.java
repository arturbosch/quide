package io.gitlab.arturbosch.quide.platform.processors;

import io.gitlab.arturbosch.quide.platform.ControlFlow;
import io.gitlab.arturbosch.quide.platform.Processor;
import io.gitlab.arturbosch.quide.platform.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author Artur Bosch
 */
public class ResultPrintProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultPrintProcessor.class.getSimpleName());

	@Override
	public <U extends UserData> void execute(U data) {
		data.currentContainer().ifPresent(container -> {
			Path outputPath = data.outputPath().orElse(data.projectPath());

			String tool = data.toolName();
			Path file = filePath(tool, outputPath);
			LOGGER.info("Saving " + tool + " result at " + file);

			String smellString = container.all().stream()
					.map(Object::toString)
					.collect(Collectors.joining("\n"));

			if (smellString.isEmpty()) return;

			try {
				Files.write(file, smellString.getBytes());
			} catch (IOException e) {
				LOGGER.warn("The result of " + tool + "could not be saved.", e);
			}
		});
	}

	private Path filePath(String tool, Path output) {
		String date = LocalDateTime.now().toString();
		String ending = ".quide";
		String filename = tool + "_" + date + ending;
		return output.resolve(filename);
	}

	@Override
	public ControlFlow.InjectionPoint injectionPoint() {
		return ControlFlow.InjectionPoint.AfterAnalysis;
	}

	@Override
	public int priority() {
		return 100;
	}

}
