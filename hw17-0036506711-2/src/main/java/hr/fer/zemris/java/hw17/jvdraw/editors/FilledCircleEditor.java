package hr.fer.zemris.java.hw17.jvdraw.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;

/**
 * Implementation of {@link GeometricalObjectEditor} used to edit FilledCircles.
 * 
 * @author Filip Husnjak
 */
public class FilledCircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 2452239582413984834L;

	/** Text field for editing X coordinate of the center */
	private JTextField centerXField;
	
	/** Text field for editing Y coordinate of the center */
	private JTextField centerYField;
	
	/** Text field for editing radius */
	private JTextField radiusField;
	
	/** Text field for editing r component of the foreground color */
	private JTextField rField;
	
	/** Text field for editing g component of the foreground color */
	private JTextField gField;
	
	/** Text field for editing b component of the foreground color */
	private JTextField bField;
	
	/** Text field for editing r component of the background color */
	private JTextField rBField;
	
	/** Text field for editing g component of the background color */
	private JTextField gBField;
	
	/** Text field for editing b component of the background color */
	private JTextField bBField;
	
	/** {@link FilledCircle} object to be edited */
	private FilledCircle filledCircle;
	
	/**
	 * Constructs new {@link FilledCircleEditor} with the given filled circle object.
	 * 
	 * @param filledCircle
	 *        filled circle object to be edited
	 */
	public FilledCircleEditor(FilledCircle filledCircle) {
		this.filledCircle = filledCircle;
		initGui();
	}
	
	/**
	 * Initializes GUI of this JComponent.
	 */
	private void initGui() {
		centerXField = new JTextField(String.valueOf(filledCircle.getCenterX()));
		centerYField = new JTextField(String.valueOf(filledCircle.getCenterY()));
		radiusField = new JTextField(String.valueOf(filledCircle.getRadius()));
		rField = new JTextField(String.valueOf(filledCircle.getFgColor().getRed()));
		gField = new JTextField(String.valueOf(filledCircle.getFgColor().getGreen()));
		bField = new JTextField(String.valueOf(filledCircle.getFgColor().getBlue()));
		rBField = new JTextField(String.valueOf(filledCircle.getBgColor().getRed()));
		gBField = new JTextField(String.valueOf(filledCircle.getBgColor().getGreen()));
		bBField = new JTextField(String.valueOf(filledCircle.getBgColor().getBlue()));
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Center X: "));
		add(centerXField);
		add(new JLabel("Center Y: "));
		add(centerYField);
		add(new JLabel("Radius: "));
		add(radiusField);
		add(new JLabel("Foreground color"));
		add(new JPanel());
		add(new JLabel("R: "));
		add(rField);
		add(new JLabel("G: "));
		add(gField);
		add(new JLabel("B: "));
		add(bField);
		add(new JLabel("Background color"));
		add(new JPanel());
		add(new JLabel("R: "));
		add(rBField);
		add(new JLabel("G: "));
		add(gBField);
		add(new JLabel("B: "));
		add(bBField);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(centerXField.getText());
			Integer.parseInt(centerYField.getText());
			Integer.parseInt(radiusField.getText());
			checkColor(rField.getText());
			checkColor(bField.getText());
			checkColor(gField.getText());
			checkColor(rBField.getText());
			checkColor(bBField.getText());
			checkColor(gBField.getText());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Integer values expected!");
		}
	}

	/**
	 * Checks whether the given color is legal.
	 * 
	 * @param text
	 *        color to be checked
	 */
	private void checkColor(String text) {
		int c = Integer.parseInt(text);
		checkRange(c);
	}

	/**
	 * Checks range of the given value.
	 * 
	 * @param c
	 *        value whose range is to be checked
	 */
	private void checkRange(int c) {
		if (c < 0 || c > 255) {
			throw new IllegalArgumentException("Color number out of range [0, 255]!");
		}
	}

	@Override
	public void acceptEditing() {
		int centerX = Integer.parseInt(centerXField.getText());
		int centerY = Integer.parseInt(centerYField.getText());
		int radius = Integer.parseInt(radiusField.getText());
		int r = Integer.parseInt(rField.getText());
		int g = Integer.parseInt(gField.getText());
		int b = Integer.parseInt(bField.getText());
		int rB = Integer.parseInt(rBField.getText());
		int gB = Integer.parseInt(gBField.getText());
		int bB = Integer.parseInt(bBField.getText());
		filledCircle.setCenter(centerX, centerY);
		filledCircle.setRadius(radius);
		filledCircle.setFgColor(new Color(r, g, b));
		filledCircle.setBgColor(new Color(rB, gB, bB));
	}

}
