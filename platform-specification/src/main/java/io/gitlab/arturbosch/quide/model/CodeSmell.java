package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;

/**
 * Interface for smells which can be connected to a version.
 *
 * @author Artur Bosch
 */
public interface CodeSmell {

	default boolean isLocatedAt(Path path) {
		Validate.notNull(path);
		return path.toAbsolutePath().toString().equals(sourcePath());
	}

	default boolean isLocatedAt(String path) {
		Validate.notNull(path);
		return path.equals(sourcePath());
	}

	default void applyVersion(Versionable versionable) {
		Validate.notNull(versionable);
		setStartVersion(versionable);
		setEndVersion(versionable);
	}

	Versionable startVersion();

	void setStartVersion(Versionable versionable);

	Versionable endVersion();

	void setEndVersion(Versionable versionable);

	/**
	 * The smell existence is continuous starting from startVersion to EndVersion.
	 *
	 * @return true if smell did not disappear even one time
	 */
	boolean isConsistent();

	/**
	 * Sets if the smell is consistent e.g. do not disappear within history.
	 *
	 * @param consistent true if consistent
	 */
	void setConsistent(boolean consistent);

	/**
	 * @return weight of a smell
	 */
	int weight();

	/**
	 * Adds weight to a smell.
	 *
	 * @param amount a int value
	 */
	void addWeight(int amount);

	/**
	 * @return path to the file this smell occurred
	 */
	String sourcePath();
}
