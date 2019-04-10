package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Functional interface that represents a single command that is executed upon
 * given {@code Context} and {@code Painter} objects.
 * 
 * @author Filip Husnjak
 */
@FunctionalInterface
public interface Command {

	/**
	 * Executes command upon given {@code Context} and {@code Painter} objects.
	 * 
	 * @param ctx
	 *        {@code Context} object upon which this command is executed
	 * @param painter
	 *        {@code Painter} object upon which this command is executed
	 */
	void execute(Context ctx, Painter painter);
	
}
