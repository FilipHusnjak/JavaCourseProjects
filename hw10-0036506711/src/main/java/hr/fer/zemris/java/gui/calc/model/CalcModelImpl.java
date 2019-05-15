package hr.fer.zemris.java.gui.calc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * Proper implementation of {@link CalcModel}. 
 * 
 * @author Filip Husnjak
 */
public class CalcModelImpl implements CalcModel {
	
	/**
	 * Tells whether {@code this} model is editable in its current state
	 */
	private boolean editable = true;
	
	/**
	 * Tells whether the current value is positive
	 */
	private boolean positive = true;
	
	/**
	 * {@link String} representation of absolute value of current value
	 */
	private String input = "";
	
	/**
	 * {@link Double} representation of absolute value of current value
	 */
	private double number = 0.0;
	
	/**
	 * Tells whether value was just set
	 */
	private boolean valueSet;
	
	/**
	 * Currently active operand
	 */
	private double activeOperand;
	
	/**
	 * Tells whether active operand is set, in other words tells whether
	 * {@link #activeOperand} is valid.
	 */
	private boolean activeOperandSet;
	
	/**
	 * Operation to be executed upon {@link #activeOperand} and current value
	 */
	private DoubleBinaryOperator pendingOperation;
	
	/**
	 * Listeners that are notified when any property in this model changes.
	 */
	private List<CalcValueListener> listeners = new LinkedList<>();

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(Objects.requireNonNull(l, "Given listener cannot be null!"));
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners in {@link #listeners} list.
	 */
	private void notifyListeners() {
		for (CalcValueListener listener: listeners) {
			listener.valueChanged(this);
		}
	}

	@Override
	public double getValue() {
		return positive ? number : -number;
	}

	@Override
	public void setValue(double value) {
		editable = false;
		valueSet = true;
		positive = value >= 0;
		number = Math.abs(value);
		input = Double.toString(number);
		notifyListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		valueSet = false;
		positive = true;
		input = "";
		number = 0.0;
		editable = true;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		clearActiveOperand();
		pendingOperation = null;
		clear();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException("Calculator is not editable in its current state!");
		}
		positive = !positive;
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException("Calculator is not editable in its current state!");
		}
		if (input.contains(".")) {
			throw new CalculatorInputException("Number already contains decimal point!");
		}
		if (!valueSet) {
			throw new CalculatorInputException("No numbers were given!");
		}
		input += input.isEmpty() ? "0." : ".";
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (!editable) {
			throw new CalculatorInputException("Calculator is not editable in its current state!");
		}
		String tmp = input + Integer.toString(digit);
		try {
			// Check whether new value is finite
			Double num = Double.parseDouble(tmp);
			if (num.isInfinite()) {
				throw new CalculatorInputException("Given number cannot be interpreted as finite double!");
			}
			valueSet = true;
			number = num;
			// Ignore leading zeros
			if (num == 0 && !input.contains(".")) return; 
			input = tmp;
			notifyListeners();
		} catch (NumberFormatException e) {
			throw new CalculatorInputException("Given number cannot be interpreted as double!");
		}
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperandSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!isActiveOperandSet()) {
			throw new IllegalStateException("Active operand is not set!");
		}
		return activeOperand;	
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		activeOperandSet = true;
		editable = false;
		notifyListeners();
	}

	@Override
	public void clearActiveOperand() {
		activeOperandSet = false;
		notifyListeners();
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		String sign = positive ? "" : "-";
		return input.isEmpty() ? sign + "0" : sign + input;
	}

}
