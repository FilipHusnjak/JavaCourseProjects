package hr.fer.zemris.java.hw17.trazilica.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents Vector of N double components. 
 * 
 * @author Filip Husnjak
 */
public class VectorN {
	
	/** Array of vector components */
	private double[] components;
	
	/**
	 * Constructs new {@link VectorN} with the gien components.
	 * 
	 * @param components
	 *        components of the new {@link VectorN}
	 */
	public VectorN(double[] components) {
		this.components = Arrays.copyOf(components, components.length);
	}
	
	/**
	 * Returns scalar product of this vector and given vector which has to have
	 * equal size.
	 * 
	 * @param other
	 *        second vector used when calculating scalar product
	 * @return scalar product of this vector and given vector 
	 * @throws IllegalArgumentException if the given {@link VectorN} does not have
	 *         the same size as this instance
	 */
	public double scalar(VectorN other) {
		if (other.size() != components.length) {
			throw new IllegalArgumentException("Vectors have to have same sizes!");
		}
		double res = 0;
		for (int i = 0; i < components.length; ++i) {
			res += components[i] * other.components[i];
		}
		return res;
	}
	
	/**
	 * Multiplies this vector with the given vector and returns the result.
	 * Result is newly created VectorN so this one nor the given one do not change.
	 * 
	 * @param other
	 *        {@link VectorN} used in multiplication
	 * @return result of multiplication
	 */
	public VectorN multiply(VectorN other) {
		if (other.size() != components.length) {
			throw new IllegalArgumentException("Vectors have to have same sizes!");
		}
		VectorN newVec = new VectorN(components);
		for (int i = 0; i < components.length; ++i) {
			newVec.components[i] *= other.components[i];
		}
		return newVec;
	}
	
	/**
	 * Returns magnitude of this vector.
	 * 
	 * @return magnitude of this vector
	 */
	public double magnitude() {
		double sumSquares = 0;
		for (int i = 0; i < components.length; ++i) {
			sumSquares += components[i] * components[i];
		}
		return Math.sqrt(sumSquares);
	}
	
	/**
	 * Returns component at the given index in this vector.
	 * 
	 * @param index
	 *        index of the component to be returned
	 * @return component at the given index in this vector
	 */
	public double get(int index) {
		Objects.checkIndex(index, components.length);
		return components[index];
	}
	
	/**
	 * Returns cosine of the angle between this vector and the given vector.
	 * 
	 * @param other
	 *        vector used in calculation
	 * @return cosine of the angle between this vector and the given vector
	 */
	public double cos(VectorN other) {
		return this.scalar(other) / (this.magnitude() * other.magnitude());
	}
	
	/**
	 * Returns size of this vector.
	 * 
	 * @return size of this vector
	 */
	public int size() {
		return components.length;
	}
	
}
