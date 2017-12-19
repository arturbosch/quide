package io.gitlab.arturbosch.quide.model;

import io.gitlab.arturbosch.quide.vcs.FileChange;
import io.gitlab.arturbosch.quide.vcs.Revision;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.util.List;

/**
 * After the mapping the file change data is unnecessary and leads to permanent memory consumption is
 * a SmellContainer is not cleared. This implementation is used inside the BaseCodeSmell which manages
 * to use only lightweight versions.
 *
 * @author Artur Bosch
 */
public class LightweightVersion implements Versionable {

	private int number;
	private Revision revision;

	public LightweightVersion(int number, Revision revision) {
		this.number = number;
		this.revision = revision;
	}

	public static LightweightVersion from(Versionable other) {
		return new LightweightVersion(other.versionNumber(), other.revision());
	}

	@Override
	public int versionNumber() {
		return number;
	}

	@Override
	public String versionHash() {
		return revision.versionHash();
	}

	@Override
	public String parentHash() {
		return revision.parentHash();
	}

	@Override
	public Revision revision() {
		return revision;
	}

	@Override
	public List<FileChange> fileChanges() {
		throw new IllegalStateException("File changes are not supported in lightweight versions.");
	}

	@Override
	public int compareTo(Versionable other) {
		int otherVersion = other.versionNumber();
		return Integer.compare(number, otherVersion);
	}

	@Override
	public String asPrintable() {
		return String.format("%d - %s - %s - %s",
				number, versionHash(), revision.date().toString(), revision.message());
	}
}
