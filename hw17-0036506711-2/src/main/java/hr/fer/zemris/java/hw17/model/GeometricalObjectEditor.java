package hr.fer.zemris.java.hw17.model;

import javax.swing.JPanel;

public abstract class GeometricalObjectEditor extends JPanel {

	private static final long serialVersionUID = 4358926910466347412L;

	public abstract void checkEditing();

	public abstract void acceptEditing();

}
