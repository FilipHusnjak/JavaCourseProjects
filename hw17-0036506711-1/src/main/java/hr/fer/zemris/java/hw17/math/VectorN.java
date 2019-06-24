package hr.fer.zemris.java.hw17.math;

import java.util.Arrays;
import java.util.Objects;

public class VectorN {
	
	private double[] components;
	
	public VectorN(double[] components) {
		this.components = Arrays.copyOf(components, components.length);
	}
	
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
	
	public double magnitude() {
		double sumSquares = 0;
		for (int i = 0; i < components.length; ++i) {
			sumSquares += components[i] * components[i];
		}
		return Math.sqrt(sumSquares);
	}
	
	public double get(int index) {
		Objects.checkIndex(index, components.length);
		return components[index];
	}
	
	public double cos(VectorN other) {
		return this.scalar(other) / (this.magnitude() * other.magnitude());
	}
	
	public int size() {
		return components.length;
	}
	
}
