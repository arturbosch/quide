package io.gitlab.arturbosch.quide.platform;

/**
 * @author Artur Bosch
 */
public interface QuideConstants {

	// PLATFORM
	String DEBUG = "platform.debug";
	String PLATFORM_ADDITIONAL_PROPERTIES = "platform.additional.properties";

	String PLATFORM_IGNORE_PLUGINS = "platform.ignore.plugins";

	// INPUT
	String PATHS_FILTERS_GLOBAL = "input.paths.filters.global";

	// OUTPUT
	String OUTPUT_CONSOLE = "output.console";
	String OUTPUT_FILE = "output.file";

	// SHELL
	String SHELL_IGNORE_COMMANDS = "shell.ignore.commands";

	// VCS
	String VCS_BRANCH = "vcs.branch";
	String VCS_RELATIVE_PATH = "vcs.relative.path";
	String VCS_START_COMMIT = "vcs.start.commit.version";
	String VCS_START_HASH = "vcs.start.commit.hash";
	String VCS_RANGE_SINCE = "vcs.range.since";
	String VCS_RANGE_UNTIL = "vcs.range.until";
	String VCS_OUTPUT = "vcs.output";
	String VCS_OUTPUT_PER_VERSION = "vcs.output.perversion";

}

