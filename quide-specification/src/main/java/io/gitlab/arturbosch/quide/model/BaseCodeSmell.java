package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.util.HashMap;

/**
 * Implements the default behaviour of a code smell.
 *
 * @author Artur Bosch
 */
public abstract class BaseCodeSmell implements CodeSmell {

	protected String sourcePath;
	protected Versionable startVersion;
	protected Versionable endVersion;
	protected boolean consistent;
	protected int weight;
	protected boolean alive;
	protected HashMap<Integer, Versionable> revivedInVersions;
	protected HashMap<Integer, Versionable> killedInVersions;

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
}
