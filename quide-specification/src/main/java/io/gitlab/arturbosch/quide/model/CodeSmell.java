package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

/**
 * Interface for smells which can be connected to a version.
 *
 * @author Artur Bosch
 */
public interface CodeSmell extends Printable {

	default boolean isLocatedAt(Path path) {
		Validate.notNull(path);
		return path.toAbsolutePath().normalize().toString().equals(sourcePath());
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

	default void killedIn(Versionable versionable) {
		Validate.notNull(versionable);
		killedInVersions().put(versionable.versionNumber(), versionable);
		setAlive(false);
		setConsistent(false);
	}

	default void revivedIn(Versionable versionable) {
		Validate.notNull(versionable);
		revivedInVersions().put(versionable.versionNumber(), versionable);
		setAlive(true);
	}

	Versionable startVersion();

	void setStartVersion(Versionable versionable);

	Versionable endVersion();

	void setEndVersion(Versionable versionable);

	boolean isAlive();

	void setAlive(boolean alive);

	HashMap<Integer, Versionable> killedInVersions();

	HashMap<Integer, Versionable> revivedInVersions();

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

	/**
	 * @return a set of paths this smell was relocated to
	 */
	Set<String> relocations();

}
