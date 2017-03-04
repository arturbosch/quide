package io.gitlab.arturbosch.quide.shell;

import io.gitlab.arturbosch.quide.platform.HomeFolder;
import io.gitlab.arturbosch.quide.shell.compiler.JavaStringCompiler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Artur Bosch
 */
public class AdditionalCommandLoader implements CommandLoader {

	private static final Logger logger = LoggerFactory.getLogger(AdditionalCommandLoader.class.getSimpleName());

	private final JavaStringCompiler compiler = new JavaStringCompiler();

	@NotNull
	@Override
	public Map<String, Command> load() {
		try {
			Path root = HomeFolder.INSTANCE.resolve("shell/commands");
			List<Path> scripts = Files.walk(root)
					.filter(path -> Files.isRegularFile(path))
					.filter(path -> path.getFileName().toString().endsWith(".java"))
					.collect(Collectors.toList());

			Map<String, String> scriptsAsStrings = scripts.stream().collect(Collectors
					.toMap(this::readFileName, this::readScript));
			Map<String, byte[]> compile = compiler.compile(scriptsAsStrings);

			return scriptsAsStrings.keySet().stream()
					.map(javaName -> javaName.substring(0, javaName.lastIndexOf(".")))
					.map(fileName -> loadClass(fileName, compile))
					.map(this::transformToCommand)
					.filter(Objects::nonNull)
					.collect(Collectors.toMap(Command::getId, Function.identity()));
		} catch (IOException e) {
			return Collections.emptyMap();
		}
	}

	private Command transformToCommand(Class<?> aClass) {
		try {
			if (Command.class.isAssignableFrom(aClass)) {
				return (Command) aClass.newInstance();
			}
		} catch (Throwable ignored) {
			logger.error("Error while loading command class: " + aClass.getSimpleName(), ignored);
		}
		return null;
	}

	private Class<?> loadClass(String className, Map<String, byte[]> classData) {
		try {
			return compiler.loadClass(className, classData);
		} catch (ClassNotFoundException | IOException e) {
			throw new IllegalStateException();
		}
	}

	private String readFileName(Path path) {
		return path.getFileName().toString();
	}

	private String readScript(Path path) {
		try {
			return new String(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
}
