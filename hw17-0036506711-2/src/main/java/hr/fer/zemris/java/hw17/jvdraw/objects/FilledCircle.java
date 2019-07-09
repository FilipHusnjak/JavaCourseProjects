package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.editors.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectVisitor;

/**
 * Represents Filled Circle as {@link GeometricalObject}.
 * 
 * @author Filip Husnjak
 */
public class FilledCircle extends GeometricalObject {

	/** X coordinate of the center */
	private int centerX;
	
	/** Y coordinate of the center */
	private int centerY;
	
	/** Radius of the filled circle */
	private int radius;
	
	/** Foreground color of the filled circle */
	private Color fgColor;

	/** Background color of this filled circle */
	private Color bgColor;

	/**
	 * Constructs new {@link FilledCircle} with the given center coordinates,
	 * foreground color and background color.
	 * 
	 * @param centerX
	 *        X coordinate of the center
	 * @param centerY
	 *        Y coordinate of the center
	 * @param radius
	 *        radius of the filled circle
	 * @param fgColor
	 *        foreground color of the filled circle
	 * @param bgColor
	 *        background color of the filled circle
	 */
	public FilledCircle(int centerX, int centerY, int radius, Color fgColor, 
			Color bgColor) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * Sets center coordinates of this filled circle
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
	
	/**
	 * Sets background color of this circle.
	 * 
	 * @param bgColor
	 *        new background color
	 */
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	/**
	 * Returns background color of this circle.
	 * 
	 * @return background color of this circle
	 */
	public Color getBgColor() {
		return bgColor;
	}
	
	@Override
	public String toString() {
		return String.format("Filled Circle (%d,%d), %d, 0x%08X", 
				centerX, centerY, radius, bgColor.getRGB());
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

}
