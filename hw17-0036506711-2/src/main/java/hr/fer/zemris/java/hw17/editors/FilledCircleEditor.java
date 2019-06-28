package hr.fer.zemris.java.hw17.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.objects.FilledCircle;

public class FilledCircleEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = 2452239582413984834L;

	private JTextField centerXField;
	
	private JTextField centerYField;
	
	private JTextField radiusField;
	
	private JTextField rField;
	
	private JTextField gField;
	
	private JTextField bField;
	
	private JTextField rBField;
	
	private JTextField gBField;
	
	private JTextField bBField;
	
	private FilledCircle filledFilledCircle;
	
	public FilledCircleEditor(FilledCircle filledFilledCircle) {
		this.filledFilledCircle = filledFilledCircle;
		initGui();
	}
	
	private void initGui() {
		centerXField = new JTextField(String.valueOf(filledFilledCircle.getCenterX()));
		centerYField = new JTextField(String.valueOf(filledFilledCircle.getCenterY()));
		radiusField = new JTextField(String.valueOf(filledFilledCircle.getRadius()));
		rField = new JTextField(String.valueOf(filledFilledCircle.getFgColor().getRed()));
		gField = new JTextField(String.valueOf(filledFilledCircle.getFgColor().getGreen()));
		bField = new JTextField(String.valueOf(filledFilledCircle.getFgColor().getBlue()));
		rBField = new JTextField(String.valueOf(filledFilledCircle.getBgColor().getRed()));
		gBField = new JTextField(String.valueOf(filledFilledCircle.getBgColor().getGreen()));
		bBField = new JTextField(String.valueOf(filledFilledCircle.getBgColor().getBlue()));
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

	private void checkColor(String text) {
		int c = Integer.parseInt(text);
		checkRange(c);
	}

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
		filledFilledCircle.setCenter(centerX, centerY);
		filledFilledCircle.setRadius(radius);
		filledFilledCircle.setFgColor(new Color(r, g, b));
		filledFilledCircle.setBgColor(new Color(rB, gB, bB));
	}

}
