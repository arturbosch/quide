package io.gitlab.arturbosch.quide.platform;

/**
 * @author Artur Bosch
 */
public final class QuideConstants {

	private QuideConstants() {
	}

	// PLATFORM
	public static final String DEBUG = "platform.debug";
	public static final String PLATFORM_ADDITIONAL_PROPERTIES = "platform.additional.properties";

	public static final String PLATFORM_IGNORE_PLUGINS = "platform.ignore.plugins";

	// INPUT
	public static final String PATHS_FILTERS_GLOBAL = "input.paths.filters.global";

	// OUTPUT
	public static final String OUTPUT_CONSOLE = "output.console";
	public static final String OUTPUT_FILE = "output.file";

	// SHELL
	public static final String SHELL_IGNORE_COMMANDS = "shell.ignore.commands";

	// VCS
	public static final String VCS_BRANCH = "vcs.branch";
	public static final String VCS_RELATIVE_PATH = "vcs.relative.path";
	public static final String VCS_OUTPUT = "vcs.output";
	public static final String VCS_OUTPUT_PER_VERSION = "vcs.output.perversion";
}

