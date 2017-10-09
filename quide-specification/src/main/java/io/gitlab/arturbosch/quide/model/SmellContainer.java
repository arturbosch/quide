package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.validation.Validate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a container for smells.
 *
 * @author Artur Bosch
 */
public interface SmellContainer<T extends CodeSmell> {

	/**
	 * @return a list of all containing smells
	 */
	List<T> all();

	/**
	 * @param path the path used as a filter
	 * @return a subset of smells for given path
	 */
	default List<T> findBySourcePath(String path) {
		return all().stream()
				.filter(smell -> smell.isLocatedAt(path))
				.collect(Collectors.toList());
	}

	/**
	 * Size of the smell container eg. #smells.
	 *
	 * @return amount of smells
	 */
	default int size() {
		return all().size();
	}

	/**
	 * Filters the container for living smells/not killed within an evolutionary analysis.
	 *
	 * @return living smells
	 */
	default List<T> alive() {
		return all().stream()
				.filter(CodeSmell::isAlive)
				.collect(Collectors.toList());
	}

	/**
	 * Filters the container for dead smells/killed within an evolutionary analysis.
	 * Dead smells should also be considered in mapping as they can get revived.
	 *
	 * @return living smells
	 */

	default List<T> dead() {
		return all().stream()
				.filter(codeSmell -> !codeSmell.isAlive())
				.collect(Collectors.toList());
	}

	/**
	 * Combination of {@link #findBySourcePath(String)} and {@link #alive()}
	 *
	 * @param path path smells need to have
	 * @return smells in given path and which are alive
	 */
	default List<T> aliveInPath(String path) {
		return all().stream()
				.filter(smell -> smell.isLocatedAt(path))
				.filter(CodeSmell::isAlive)
				.collect(Collectors.toList());
	}

	/**
	 * Combination of {@link #findBySourcePath(String)} and {@link #dead()}
	 *
	 * @param path path smells need to have
	 * @return smells in given path and which are dead
	 */
	default List<T> deadInPath(String path) {
		return all().stream()
				.filter(smell -> smell.isLocatedAt(path))
				.filter(codeSmell -> !codeSmell.isAlive())
				.collect(Collectors.toList());
	}

	/**
	 * Adds a new smell to this container
	 *
	 * @param smell code smell to add
	 */
	default void addSmell(T smell) {
		Validate.notNull(smell);
		all().add(smell);
	}

	/**
	 * Adds a collection of smells to this container
	 *
	 * @param smells collection of smells
	 */
	default void addSmells(Collection<T> smells) {
		Validate.notNull(smells);
		all().addAll(smells);
	}
}
