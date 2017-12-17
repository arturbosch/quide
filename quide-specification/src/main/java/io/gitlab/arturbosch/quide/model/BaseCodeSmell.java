package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements the default behaviour of a code smell.
 *
 * @author Artur Bosch
 */
public abstract class BaseCodeSmell implements CodeSmell {

	protected String sourcePath;
	protected Versionable startVersion;
	protected Versionable endVersion;
	protected boolean consistent = true;
	protected int weight = 0;
	protected boolean alive = true;
	protected boolean dirty = false;
	protected boolean introducedOnFirstCommit = false;
	protected HashMap<Integer, Versionable> revivedInVersions = new HashMap<>();
	protected HashMap<Integer, Versionable> killedInVersions = new HashMap<>();
	protected Set<String> relocations = new HashSet<>();

	/**
	 * Copies all version related information from given other smell.
	 * This method can be useful in smell mappings.
	 *
	 * @param other code smell
	 */
	public void copyVersionInformationFrom(CodeSmell other) {
		startVersion = other.startVersion();
		endVersion = other.endVersion();
		weight = other.weight();
		consistent = other.isConsistent();
		alive = other.isAlive();
		revivedInVersions = other.revivedInVersions();
		killedInVersions = other.killedInVersions();
		if (!sourcePath.equals(other.sourcePath())) {
			relocations.add(other.sourcePath());
		}
	}

	@Override
	public Set<String> relocations() {
		return relocations;
	}

	@Override
	public HashMap<Integer, Versionable> killedInVersions() {
		return killedInVersions;
	}

	@Override
	public HashMap<Integer, Versionable> revivedInVersions() {
		return revivedInVersions;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void markDirty() {
		dirty = true;
	}

	@Override
	public Versionable startVersion() {
		return startVersion;
	}

	@Override
	public void setStartVersion(Versionable versionable) {
		Validate.notNull(versionable);
		startVersion = versionable;
	}

	@Override
	public Versionable endVersion() {
		return endVersion;
	}

	@Override
	public void setEndVersion(Versionable versionable) {
		Validate.notNull(versionable);
		endVersion = versionable;
	}

	@Override
	public boolean isConsistent() {
		return consistent;
	}

	@Override
	public void setConsistent(boolean consistent) {
		this.consistent = consistent;
	}

	@Override
	public int weight() {
		return weight;
	}

	@Override
	public void addWeight(int amount) {
		weight += amount;
	}

	@Override
	public String sourcePath() {
		return sourcePath;
	}

	@Override
	public void setIntroducedOnFirstCheckin() {
		introducedOnFirstCommit = true;
	}

	@Override
	public boolean isIntroducedOnFirstCheckin() {
		return introducedOnFirstCommit;
	}

	@Override
	public String toString() {
		return "VersionInfo{" +
				"startVersion=" + (startVersion == null ? "-1" : startVersion.versionNumber()) +
				", endVersion=" + (startVersion == null ? "-1" : endVersion.versionNumber()) +
				", consistent=" + consistent +
				", weight=" + weight +
				", alive=" + alive +
				", revivedInVersions=" + revivedInVersions.keySet() +
				", killedInVersions=" + killedInVersions.keySet() +
				'}';
	}
}
