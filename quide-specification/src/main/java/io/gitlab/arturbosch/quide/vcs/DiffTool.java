package io.gitlab.arturbosch.quide.vcs;

/**
 * @author Artur Bosch
 */
public interface DiffTool<P extends Patch> {

	P createPatchFor(SourceFile oldFile, SourceFile newFile);

}
