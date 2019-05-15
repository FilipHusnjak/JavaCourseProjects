package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Button used for power operation. Inversed operation is calculating power
 * of the active operand but with reciprocal value of the exponent.
 * 
 * @author Filip Husnjak
 */
public class PowerOperationButton extends CalcButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Normal text to be displayed
	 */
	private static String normalText = "x^n";
	
	/**
	 * Text to be displayed when this button is set to inversed operation
	 */
	private static String inverseText = "x^(1/n)";
	
	/**
	 * Model used for fetching data and doing calculations
	 */
	private CalcModel model;
	
	/**
	 * Listener used when button is set to normal state
	 */
	private ActionListener normalListener = e -> setOperation(model, Math::pow);
	
	/**
	 * Listener used when button is set to inversed state
	 */
	private ActionListener inverseListener = e -> setOperation(model, 
			(x, n) -> Math.pow(x, 1 / n));
	
	/**
	 * Calculates old operation if needed and sets the new one.
	 * 
	 * @param model
	 *        model whose operation is to be set
	 * @param operation
	 *        operation to be set
	 */
	private void setOperation(CalcModel model, DoubleBinaryOperator operation) {
		if (model.isActiveOperandSet()) {
			model.setValue(model.getPendingBinaryOperation()
					.applyAsDouble(model.getActiveOperand(), model.getValue()));
		}
		model.setActiveOperand(model.getValue());
		model.setPendingBinaryOperation(operation);
		model.clear();
	}

	/**
	 * Constructs new {@link PowerOperationButton} with specified {@link CalcModel}.
	 * 
	 * @param model
	 *        model used for calculations and data
	 * @throws NullPointerException if the given model is {@code null}
	 */
	public PowerOperationButton(CalcModel model) {
		super(normalText);
		this.model = Objects.requireNonNull(model, "Given calculator model cannot be null!");
		setNormal();
	}
	
	/**
	 * Sets this button to normal state.
	 */
	public void setNormal() {
		setText(normalText);
		removeActionListener(inverseListener);
		addActionListener(normalListener);
	}
	
	/**
	 * Sets this button to inversed state.
	 */
	public void setInverse() {
		setText(inverseText);
		removeActionListener(normalListener);
		addActionListener(inverseListener);
	}
	
}
