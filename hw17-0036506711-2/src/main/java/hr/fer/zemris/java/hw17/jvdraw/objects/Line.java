package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.editors.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectVisitor;

/**
 * Represents Line as {@link GeometricalObject}.
 * 
 * @author Filip Husnjak
 */
public class Line extends GeometricalObject {
	
	/** X coordinate of the start point */
	private int startX;
	
	/** Y coordinate of the start point */
	private int startY;
	
	/** X coordinate of the end point */
	private int endX;
	
	/** Y coordinate of the end point */
	private int endY;
	
	/** Foreground color of this line */
	private Color fgColor;

	/**
	 * Constructs new {@link Line} with given parameters.
	 *
	 * @param startX
	 *        X coordinate of the start point
	 * @param startY
	 *        Y coordinate of the start point
	 * @param endX
	 *        X coordinate of the end point
	 * @param endY
	 *        Y coordinate of the end point
	 * @param fgColor
	 *        foreground color of this line
	 */
	public Line(int startX, int startY, int endX, int endY, Color fgColor) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.fgColor = fgColor;
	}
	
	/**
	 * Sets start point of this line.
	 * 
	 * @param startX
	 *        X coordinate of the start point
	 * @param startY
	 *        Y coordinate of the start point
	 */
	public void setStart(int startX, int startY) {
		this.startX = startX;
		this.startY = startY;
	}
	
	/**
	 * Returns X coordinate of the start point.
	 * 
	 * @return X coordinate of the start point
	 */
	public int getStartX() {
		return startX;
	}
	
	/**
	 * Returns Y coordinate of the start point.
	 * 
	 * @return Y coordinate of the start point
	 */
	public int getStartY() {
		return startY;
	}
	
	/**
	 * Sets end point of this line.
	 * 
	 * @param startX
	 *        X coordinate of the end point
	 * @param startY
	 *        Y coordinate of the end point
	 */
	public void setEnd(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	/**
	 * Returns X coordinate of the end point.
	 * 
	 * @return X coordinate of the end point
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Returns Y coordinate of the end point.
	 * 
	 * @return Y coordinate of the end point
	 */
	public int getEndY() {
		return endY;
	}
	
	/**
	 * Sets foreground color of this line.
	 * 
	 * @param fgColor
	 *        new foreground color
	 */
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	/**
	 * Returns foreground color of this line.
	 * 
	 * @return foreground color of this line
	 */
	public Color getFgColor() {
		return fgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public String toString() {
		return String.format("Line (%d,%d)-(%d,%d)", startX, startY, endX, endY);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

}
