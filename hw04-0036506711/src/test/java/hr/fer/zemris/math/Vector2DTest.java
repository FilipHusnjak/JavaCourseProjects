package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class Vector2DTest {
	
	Vector2D vector;
	Vector2D[] vectors = new Vector2D[] {
		new Vector2D(0, 1),
		new Vector2D(1, 0),
		new Vector2D(2, 2),
		new Vector2D(3, -1),
		new Vector2D(-1, -20)
	};;
	
	Vector2D[] translatedVectors = new Vector2D[] {
		new Vector2D(1, 3),
		new Vector2D(2, 3),
		new Vector2D(4, 5),
		new Vector2D(7, 4),
		new Vector2D(6, -16)
	};
	
	Vector2D[] rotatedVectors = new Vector2D[] {
		new Vector2D(1, 2),
		new Vector2D(-1, -2),
		new Vector2D(2, -1),
		new Vector2D(-0.707107, 2.12132),
		new Vector2D(-2.211277, 0.332044)
	};
	
	Vector2D[] scaledVectors = new Vector2D[] {
		new Vector2D(3.14, 6.28),
		new Vector2D(3.14, 6.28),
		new Vector2D(-6.908, -13.816),
		new Vector2D(-142.9956, -285.9912),
		new Vector2D(0, 0)
	};
	
	double[] angles = new double[] {
		0,
		Math.PI,
		Math.PI / 2,
		3 * Math.PI / 4,
		1.1
	};
	
	double[] scalars = new double[] {
		3.14,
		1,
		-2.2,
		20.7,
		0
	};
	
	@BeforeEach
	public void setup() {
		vector = new Vector2D(1, 2);
	}

	@Test
	public void testConstructor() {
		assertEquals(1, vector.getX());
		assertEquals(2, vector.getY());
	}
	
	@Test
	public void testTranslateNull() {
		assertThrows(NullPointerException.class, () -> vector.translate(null));
	}
	
	@Test
	public void testTranslateNormalVector() {
		for (int i = 0; i < vectors.length; ++i) {
			vector.translate(vectors[i]);
			assertEquals(translatedVectors[i], vector);
		}
	}
	
	@Test
	public void testTranslatedNull() {
		assertThrows(NullPointerException.class, () -> vector.translated(null));
	}
	
	@Test
	public void testTranslatedNormalVector() {
		for (int i = 0; i < vectors.length; ++i) {
			vector = vector.translated(vectors[i]);
			assertEquals(translatedVectors[i], vector);
		}
	}
	
	@Test
	public void testRotate() {
		for (int i = 0; i < angles.length; ++i) {
			vector.rotate(angles[i]);
			assertEquals(rotatedVectors[i], vector);
		}
	}
	
	@Test
	public void testRotated() {
		for (int i = 0; i < angles.length; ++i) {
			vector = vector.rotated(angles[i]);
			assertEquals(rotatedVectors[i], vector);
		}
	}
	
	@Test
	public void testScale() {
		for (int i = 0; i < scalars.length; ++i) {
			vector.scale(scalars[i]);
			assertEquals(scaledVectors[i], vector, "Expected: " + scaledVectors[i].getY() + " give: " + vector.getY());
		}
	}
	
	@Test
	public void testScaled() {
		for (int i = 0; i < scalars.length; ++i) {
			vector = vector.scaled(scalars[i]);
			assertEquals(scaledVectors[i], vector);
		}
	}
	
	@Test
	public void testCopy() {
		Vector2D vector2 = vector.copy();
		assertEquals(1, vector2.getX());
		assertEquals(2, vector2.getY());
	}
	
}
