package io.gitlab.arturbosch.quide.shell.compiler;

import io.gitlab.arturbosch.quide.shell.Command;
import io.gitlab.arturbosch.quide.shell.QuideShellException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Artur Bosch
 */
public final class JavaCommandExecutor {

	private JavaCommandExecutor() {
	}

	public static String run(Command command) {
		try {
			MethodType methodType = MethodType.methodType(String.class, String.class);
			MethodHandle handle = MethodHandles.lookup().findVirtual(command.getClass(), "run", methodType);
			return (String) handle.invoke(command, "");
		} catch (Throwable throwable) {
			throw new QuideShellException("Could not execute 'run' method of " + command.getClass().getSimpleName(), throwable);
		}
	}

}
