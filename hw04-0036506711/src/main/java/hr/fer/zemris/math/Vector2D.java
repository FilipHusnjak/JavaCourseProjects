package hr.fer.zemris.math;

import java.util.Objects;

/**
 * 2D vector model class. Vectors are represented with two real numbers (x, y). This
 * class provides basic math operations with vectors.
 * 
 * @author Filip Husnjak
 *
 */
public class Vector2D {
	
	/**
	 * The precision when comparing 2 double values.
	 */
	private static final double PRECISION = Math.pow(10, -6);
	
	/**
	 * X value of the vector
	 */
	private double x;
	
	/**
	 * Y value of the vector
	 */
	private double y;
	
	/**
	 * Constructs a vector object with specified x and y values.
	 * 
	 * @param x
	 *        x value of this vector object
	 * @param y
	 *        y value of this vector object
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the X value of this vector.
	 *  
	 * @return the X value of this vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y value of this vector.
	 * 
	 * @return the Y value of this vector
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Translates {@code this} Vector2D object in the direction of the given
	 * Vector2D object.
	 * 
	 * @param offset
	 *        the Vector2D object that represents the direction of the translation
	 *        of {@code this} Vector2D object
	 * @throws NullPointerException if the given vector object is {@code null}
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset, "Given vector object cannot be null!");
		x += offset.getX();
		y += offset.getY();
	}
	
	/**
	 * Returns the new {@code Vector2D} object as the result of the translation of
	 * {@code this} vector in the direction of the given vector. {@code This} vector
	 * object does not change.
	 * 
	 * @param offset
	 *        the direction of the translation
	 * @return new {@code Vector2D} object as the result of the translation of
	 *         {@code this} vector in the direction of the given vector
	 * @throws NullPointerException if the given vector object is {@code null}
	 */
	public Vector2D translated(Vector2D offset) {
		Objects.requireNonNull(offset, "Given vector object cannot be null!");
		return new Vector2D(x + offset.getX(), y + offset.getY());
	}
	
	/**
	 * Rotates {@code this} Vector2D object by a specified angle.
	 * 
	 * @param angle
	 *        angle in radians by which the vector should be rotated
	 */
	public void rotate(double angle) {
		Vector2D newVec = rotated(angle);
		x = newVec.getX();
		y = newVec.getY();
	}
	
	/**
	 * Returns the new {@code Vector2D} object as the result of the rotation
	 * of this vector by the specified angle. {@code This} vector does not change.
	 * 
	 * @param angle
	 *        angle in radians by which the vector should be rotated 
	 * @return the new {@code Vector2D} object as the result of the rotation
	 *         of this vector by the specified angle
	 */
	public Vector2D rotated(double angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		return new Vector2D(x * cos - y * sin, x * sin + y * cos);
	}
	
	/**
	 * Scales {@code this} Vector2D object by the specified factor. 
	 * 
	 * @param scaler
	 *        scaling factor
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Returns the new {@code Vector2D} object as the result of the scaling {@code this}
	 * vector by the specified factor. {@code This} vector does not change. 
	 * 
	 * @param scaler
	 *        scaling factor
	 * @return the new {@code Vector2D} object as the result of the scaling {@code this}
	 *         vector by the specified factor
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}
	
	/**
	 * Returns the copy of {@code this} Vector2D object.
	 * 
	 * @return the copy of {@code this} Vector2D object
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector2D))
			return false;
		Vector2D other = (Vector2D) obj;
		return Math.abs(x - other.x) < PRECISION
				&& Math.abs(y - other.y) < PRECISION;
	}
	
}
