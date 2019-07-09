package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.editors.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectVisitor;

/**
 * Represents Circle as {@link GeometricalObject}.
 * 
 * @author Filip Husnjak
 */
public class Circle extends GeometricalObject {
	
	/** X coordinate of the center */
	private int centerX;
	
	/** Y coordinate of the center */
	private int centerY;
	
	/** Radius of the circle */
	private int radius;
	
	/** Foreground color of the circle */
	private Color fgColor;
	
	/**
	 * Constructs new {@link Circle} with the given center coordinates and
	 * foreground color.
	 * 
	 * @param centerX
	 *        X coordinate of the center
	 * @param centerY
	 *        Y coordinate of the center
	 * @param radius
	 *        radius of the circle
	 * @param fgColor
	 *        foreground color of the circle
	 */
	public Circle(int centerX, int centerY, int radius, Color fgColor) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.fgColor = fgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	/**
	 * Sets center coordinates of this circle
	 * 
	 * @param centerX
	 *        X coordinate of the center
	 * @param centerY
	 *        Y coordinate of the center
	 */
	public void setCenter(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		notifyListeners();
	}
	
	/**
	 * Sets radius of this circle
	 * 
	 * @param radius
	 *        circle radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	/**
	 * Returns X coordinate of the center.
	 * 
	 * @return X coordinate of the center
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * Returns Y coordinate of the center.
	 * 
	 * @return Y coordinate of the center
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * Returns radius of this circle.
	 * 
	 * @return radius of this circle
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Sets foreground color of this circle.
	 * 
	 * @param fgColor
	 *        new foreground color
	 */
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}
	
	/**
	 * Returns foreground color of this circle.
	 * 
	 * @return foreground color of this circle
	 */
	public Color getFgColor() {
		return fgColor;
	}
	
	@Override
	public String toString() {
		return String.format("Circle (%d,%d), %d", centerX, centerY, radius);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

}
