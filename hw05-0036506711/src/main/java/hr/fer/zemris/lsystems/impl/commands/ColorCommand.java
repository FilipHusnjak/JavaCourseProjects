package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Implementation of {@link hr.fer.zemris.lsystems.impl.Command} interface. Its execute method changes
 * the color parameter of the current {@code TurtleState} in the given {@code Context}.
 * 
 * @author Filip Husnjak
 */
public class ColorCommand implements Command {

	/**
	 * The color the current state, in the given {@code Context} is to be set to. 
	 */
	private final Color color;

	/**
	 * Constructs {@code ColorCommand} object with specified color.
	 * 
	 * @param color
	 *        the color the current state is to be set to
	 * @throws NullPointerException if the given color is {@code null}
	 */
	public ColorCommand(Color color) {
		this.color = Objects.requireNonNull(color, "Given color cannot be null!");
	}

	/**
	 * {@inheritDoc}
	 * Changes the color of the current state in the given {@code Context} to the one
	 * specified by {@link ColorCommand#color}.
	 * 
	 * @throws NullPointerException if the given {@code Context} is {@code null}
	 * @throws EmptyStackException if the given {@code Context} is empty
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		Objects.requireNonNull(ctx, "Given context cannot be null!").getCurrentState().color = color;
	}
	
}
