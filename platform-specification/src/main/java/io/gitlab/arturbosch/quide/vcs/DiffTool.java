package io.gitlab.arturbosch.quide.vcs;

/**
 * @author Artur Bosch
 */
public interface DiffTool {

	Patch createPatchFor(SourceFile oldFile, SourceFile newFile);

}
