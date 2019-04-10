package hr.fer.zemris.lsystems.impl.commands;

import java.util.Objects;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface.
 * Its execute method removes the current state from the given {@code Context}.
 * 
 * @author Filip Husnjak
 */
public class PopCommand implements Command {

	/**
	 * {@inheritDoc}
	 * Removes the current state from the given {@code Context}.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 * @throws EmptyStackException if the given {@code Context} is empty
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Objects.requireNonNull(ctx, "Given context cannot be null!").popState();
	}
	
}
