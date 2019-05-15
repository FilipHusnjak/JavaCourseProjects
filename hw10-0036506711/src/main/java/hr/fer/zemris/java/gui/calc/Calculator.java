package hr.fer.zemris.java.gui.calc;

import java.awt.Container;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.buttons.BinaryOperationButton;
import hr.fer.zemris.java.gui.calc.model.buttons.ButtonProperties;
import hr.fer.zemris.java.gui.calc.model.buttons.CalcButton;
import hr.fer.zemris.java.gui.calc.model.buttons.InsertDigitButton;
import hr.fer.zemris.java.gui.calc.model.buttons.PowerOperationButton;
import hr.fer.zemris.java.gui.calc.model.buttons.UnaryOperationButton;
import hr.fer.zemris.java.gui.calc.model.screen.ScreenLabel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Represents simple calculator. Its using {@link CalcModelImpl} model to
 * perform proper calculations.
 * 
 * @author Filip Husnjak
 */
public class Calculator extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Model used by this instance of a {@link Calculator}
	 */
	private CalcModel model = new CalcModelImpl();
	
	/**
	 * Stack used by this calculator for push/pop actions
	 */
	private Deque<Double> stack = new ArrayDeque<>();
	
	/**
	 * Unary operations buttons
	 */
	private UnaryOperationButton[] unaryOperationBtns = new UnaryOperationButton[] {
		new UnaryOperationButton("1/x", "1/x", model, x -> 1 / x, x -> 1 / x),
		new UnaryOperationButton("sin", "arcsin", model, Math::sin, Math::asin),
		new UnaryOperationButton("log", "10^x", model, Math::log10, x -> Math.pow(10, x)),
		new UnaryOperationButton("cos", "arccos", model, Math::cos, Math::acos),
		new UnaryOperationButton("ln", "e^x", model, Math::log, x -> Math.pow(Math.E, x)),
		new UnaryOperationButton("tan", "arctan", model, Math::tan, Math::atan),
		new UnaryOperationButton("ctg", "arcctg", model, x -> 1 / Math.tan(x), x -> Math.atan(1 / x))
	};
	
	/**
	 * Binary operation buttons
	 */
	private BinaryOperationButton[] binaryOperationBtns = new BinaryOperationButton[] {
		new BinaryOperationButton("/", (x, y) -> x / y, model),
		new BinaryOperationButton("*", (x, y) -> x * y, model),
		new BinaryOperationButton("-", (x, y) -> x - y, model),
		new BinaryOperationButton("+", Double::sum, model)
	};
	
	/**
	 * Stack operation buttons
	 */
	private CalcButton[] stackOperationsBtns = new CalcButton[] {
		new CalcButton("push", e -> stack.push(model.getValue())),
		new CalcButton("pop", e -> {
			if (stack.isEmpty()) {
				throw new CalculatorInputException("Stack is empty!");
			}
			model.setValue(stack.pop());
		})
	};

	/**
	 * Constructs new {@link Calculator}, initializes GUI and sets window
	 * at the center of the screen.
	 */
	public Calculator() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Calculator");
        initGui();
        pack();
        setLocationRelativeTo(null);
	}

	/**
	 * Initializes GUI with all proper buttons and labels at right positions.
	 * It uses {@link CalcLayout} to position elements.
	 */
	private void initGui() {
		Container container = getContentPane();
		container.setLayout(new CalcLayout(3));
		
		// Create all needed components
		
		InsertDigitButton[] digitsBtns = new InsertDigitButton[10];
		for (int i = 0; i < digitsBtns.length; ++i) {
			digitsBtns[i] = new InsertDigitButton(i, model);
		}
		
		ScreenLabel screenLabel = new ScreenLabel("0");
		model.addCalcValueListener(v -> screenLabel.setText(v.toString()));
		
		CalcButton signBtn = new CalcButton("+/-");
		signBtn.addActionListener(e -> model.swapSign());
		
		CalcButton dotBtn = new CalcButton(".");
		dotBtn.addActionListener(e -> model.insertDecimalPoint());
		
		CalcButton equalsBtn = new CalcButton("=");
		equalsBtn.addActionListener(e -> {
			model.setValue(model.getPendingBinaryOperation()
					.applyAsDouble(model.getActiveOperand(), model.getValue()));
			model.clearActiveOperand();
		});
		
		CalcButton clearBtn = new CalcButton("clr");
		clearBtn.addActionListener(e -> model.clear());
		
		CalcButton resetBtn = new CalcButton("reset");
		resetBtn.addActionListener(e -> {
			model.clearAll();
			stack.clear();
		});
		
		PowerOperationButton powerOperationBtn = new PowerOperationButton(model);
		
		JCheckBox inverseCB = new JCheckBox("inv");
		inverseCB.addActionListener(l -> {
			for (UnaryOperationButton button: unaryOperationBtns) {
				if (inverseCB.isSelected()) {
					button.setInverse();
				} else {
					button.setNormal();
				}
			}
			if (inverseCB.isSelected()) {
				powerOperationBtn.setInverse();
			} else {
				powerOperationBtn.setNormal();
			}
		});
		inverseCB.setFont(inverseCB.getFont().deriveFont(ButtonProperties.buttonFont));
		
		// Fill the GUI with all created components
		
		container.add(screenLabel, "1,1");
		container.add(equalsBtn, "1,6");
		container.add(clearBtn, "1,7");
		
		container.add(unaryOperationBtns[6], "5,2");
		for (int i = 2, k = 0; i <= 4; ++i) {
			for (int j = 1; j <= 2; ++j, ++k) {
				container.add(unaryOperationBtns[k], new RCPosition(i, j));
			}
		}
		
		container.add(powerOperationBtn, "5,1");
		
		container.add(digitsBtns[0], "5,3");
		for (int i = 4, k = 1; i >= 2; --i) {
			for (int j = 3; j <= 5; ++j, ++k) {
				container.add(digitsBtns[k], new RCPosition(i, j));
			}
		}
		
		container.add(signBtn, "5,4");
		container.add(dotBtn, "5,5");
		
		for (int i = 2, j = 0; i <= 5; ++i, ++j) {
			container.add(binaryOperationBtns[j], new RCPosition(i, 6));
		}
		
		container.add(resetBtn, "2,7");
		
		container.add(stackOperationsBtns[0], "3,7");
		container.add(stackOperationsBtns[1], "4,7");

		container.add(inverseCB, "5,7");
	}
	
	/**
	 * Shows the calculator, takes no arguments.
	 * 
	 * @param args 
	 *        ignored
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}
	
}
