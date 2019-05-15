package hr.fer.zemris.math;

import java.util.Objects;

/**
 * 3D vector model class. Vectors are represented by three real numbers (x, y, z). 
 * This class also provides basic math operations with vectors.
 * 
 * @author Filip Husnjak
 */
public class Vector3 {

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
	 * Z value of the vector
	 */
	private double z;

	/**
	 * Constructs new {@link #Vector3} object with specified parameters.
	 * 
	 * @param x
	 *        x value of the vector object
	 * @param y
	 *        y value of the vector object
	 * @param z
	 *        z value of the vector object
	 */
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Returns the magnitude of {@code this} {@link #Vector3} object.
	 * 
	 * @return the magnitude of {@code this} {@link #Vector3} object
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns new {@link #Vector3} object that represent normalized version of
	 * {@code this} {@link #Vector3} object. In other words it has same direction
	 * as {@code this} vector but its magnitude is equal to {@code one}.
	 * 
	 * @return new {@link #Vector3} object that represent normalized version of
	 *         {@code this} {@link #Vector3} object
	 */
	public Vector3 normalized() {
		return scale(1.0 / norm());
	}
	
	/**
	 * Adds given vector to {@code this} {@code Vector3} object and returns the
	 * new {@code Vector3} object as the result.
	 * 
	 * @param other
	 *        {@code Vector3} object to be added
	 * @return new {@code Vector3} object as the result of addition
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other, "Given Vector3 object cannot be null!");
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	 * Subtracts given {@code Vector3} object from {@code this} vector and returns
	 * new {@code Vector3} object as the result.
	 * 
	 * @param other
	 *        {@code Vector3} object to be subtracted from {@code this} vector
	 * @return new {@code Vector3} object as the result of subtraction
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other, "Given Vector3 object cannot be null!");
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	 * Returns dot product of {@code this} Vector3 object and the given one.
	 * 
	 * @param other
	 *        argument used for calculating dot product
	 * @return dot product of {@code this} Vector3 object and the given one
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other, "Given Vector3 object cannot be null!");
		return x * other.x + y * other.y + z * other.z;
	}
	
	/**
	 * Returns cross product of {@code this} Vector3 object and the given one as
	 * new {@code Vector3} object.
	 * 
	 * @param other
	 *        argument used for calculating cross product
	 * @return new Vector3 object as the result of cross product of {@code this} 
	 *         Vector3 object and the given one
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other, "Given Vector3 object cannot be null!");
		double newX = y * other.z - z * other.y;
		double newY = z * other.x - x * other.z;
		double newZ = x * other.y - y * other.x;
		return new Vector3(newX, newY, newZ);
	}
	
	/**
	 * Returns the new {@code Vector3} object as the result of the scaling {@code this}
	 * vector by the specified factor. {@code This} vector does not change. 
	 * 
	 * @param s
	 *        scaling factor
	 * @return the new {@code Vector3} object as the result of the scaling {@code this}
	 *         vector by the specified factor
	 */
	public Vector3 scale(double s) {
		return new Vector3(s * x, s * y, s * z);
	}
	
	/**
	 * Returns cosine of an angle between {@code this} vector and the given one.
	 * 
	 * @param other
	 *        vector used for calculating a cosine of an angle
	 * @return cosine of an angle between {@code this} vector and the given one
	 * @throws NullPointerException if the given object is {@code null}
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other, "Given Vector3 object cannot be null!");
		return dot(other) / (norm() * other.norm());
	}

	/**
	 * Returns X value of this vector.
	 * 
	 * @return X value of this vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns Y value of this vector.
	 * 
	 * @return Y value of this vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns Z value of this vector.
	 * 
	 * @return Z value of this vector
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Returns x, y and z values stored in an array. X value is at position
	 * 0, y at 1 and z at 2.
	 * 
	 * @return x, y and z values stored in an array
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector3))
			return false;
		Vector3 other = (Vector3) obj;
		return Math.abs(x - other.x) < PRECISION
				&& Math.abs(y - other.y) < PRECISION
				&& Math.abs(z - other.z) < PRECISION;
	}
	
}
