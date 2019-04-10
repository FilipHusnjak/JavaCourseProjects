package hr.fer.zemris.systems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

public class GenerateTest {

	@Test
	public void testGenerate() {
		assertEquals("F", createKochCurve(LSystemBuilderImpl::new).generate(0));
		assertEquals("F+F--F+F", createKochCurve(LSystemBuilderImpl::new).generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", createKochCurve(LSystemBuilderImpl::new).generate(2));
	}
	
	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder().registerCommand('F', "draw 1").setOrigin(0.05, 0.4).setAngle(0).setUnitLength(0.9)
				.setUnitLengthDegreeScaler(1.0 / 3.0).registerProduction('F', "F+F--F+F").setAxiom("F").build();
	}
	
}
