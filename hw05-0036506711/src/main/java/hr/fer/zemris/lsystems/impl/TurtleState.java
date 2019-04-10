package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.math.Vector2D;

/**
 * Represents the state of the turtle. State is defined with 4 parameters: 
 * <ul>
 *  <li> 
 *  {@code Vector2D} position - position of the turtle
 *  </li>
 *  <li> 
 *  {@code Vector2D} direction - the direction the turtle is looking at
 *  </li>
 *  <li> 
 *  {@code Color} color - color of the turtle
 *  </li>
 *  <li>
 *  {@code double} unitLength - unit length of the turtle translation
 *  </li>
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class TurtleState {

	/**
	 * Position of the turtle
	 */
	public Vector2D position;
	
	/**
	 * The direction the turtle is looking at
	 */
	public Vector2D direction;
	
	/**
	 * Color of the turtle
	 */
	public Color color;
	
	/**
	 * Unit length of the turtle translation
	 */
	public double unitLength;
	
	/**
	 * Constructs {@code TurtleState} object with given parameters.
	 * 
	 * @param position
	 * 		  position of the turtle
	 * @param direction
	 *        the direction the turtle is looking at
	 * @param color
	 *        color of the turtle
	 * @param unitLength
	 *        unit length of the turtle translation
	 * @throws NullPointerException if any of the given objects is {@code null}
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double unitLength) {
		this.position = Objects.requireNonNull(position, "Given position cannot be null!");
		this.direction = Objects.requireNonNull(direction, "Given direction cannot be null!");
		this.color = Objects.requireNonNull(color, "Given color cannot be null!");
		this.unitLength = unitLength;
	}
	
	/**
	 * Returns the copy of {@code this} TurtleState object. The copy occupies
	 * different memory space than {@code this} object.
	 * 
	 * @return the copy of {@code this} TurtleState object
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), direction.copy(), color, unitLength);
	}
	
}
