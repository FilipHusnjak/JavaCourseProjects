package hr.fer.zemris.java.hw17.jvdraw.model;

import javax.swing.JPanel;

/**
 * Abstraction of GeometricalObjectEditor which defines which methods have to be
 * implemented.
 * 
 * @author Filip Husnjak
 */
public abstract class GeometricalObjectEditor extends JPanel {

	private static final long serialVersionUID = 4358926910466347412L;

	/**
	 * Checks whether editing is legal.
	 */
	public abstract void checkEditing();

	/**
	 * Modifies / edits the proper object.
	 */
	public abstract void acceptEditing();

}
