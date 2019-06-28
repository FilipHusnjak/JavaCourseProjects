package hr.fer.zemris.java.hw17.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.objects.Line;

public class LineEditor extends GeometricalObjectEditor {

	private static final long serialVersionUID = -1148076639400732749L;

	private JTextField startXField;
	
	private JTextField startYField;
	
	private JTextField endXField;
	
	private JTextField endYField;
	
	private JTextField rField;
	
	private JTextField gField;
	
	private JTextField bField;
	
	private Line line;
	
	public LineEditor(Line line) {
		this.line = line;
		initGui();
	}
	
	private void initGui() {
		startXField = new JTextField(String.valueOf(line.getStartX()));
		startYField = new JTextField(String.valueOf(line.getStartY()));
		endXField = new JTextField(String.valueOf(line.getEndX()));
		endYField = new JTextField(String.valueOf(line.getEndY()));
		rField = new JTextField(String.valueOf(line.getFgColor().getRed()));
		gField = new JTextField(String.valueOf(line.getFgColor().getGreen()));
		bField = new JTextField(String.valueOf(line.getFgColor().getBlue()));
		setLayout(new GridLayout(0, 2));
		add(new JLabel("Start X: "));
		add(startXField);
		add(new JLabel("Start Y: "));
		add(startYField);
		add(new JLabel("End X: "));
		add(endXField);
		add(new JLabel("End Y: "));
		add(endYField);
		add(new JLabel("R: "));
		add(rField);
		add(new JLabel("G: "));
		add(gField);
		add(new JLabel("B: "));
		add(bField);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(startXField.getText());
			Integer.parseInt(startYField.getText());
			Integer.parseInt(endXField.getText());
			Integer.parseInt(endYField.getText());
			int r = Integer.parseInt(rField.getText());
			int g = Integer.parseInt(gField.getText());
			int b = Integer.parseInt(bField.getText());
			checkRange(r);
			checkRange(g);
			checkRange(b);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Integer values expected!");
		}
	}

	private void checkRange(int c) {
		if (c < 0 || c > 255) {
			throw new IllegalArgumentException("Color number out of range [0, 255]!");
		}
	}

	@Override
	public void acceptEditing() {
		int startX = Integer.parseInt(startXField.getText());
		int startY = Integer.parseInt(startYField.getText());
		int endX = Integer.parseInt(endXField.getText());
		int endY = Integer.parseInt(endYField.getText());
		int r = Integer.parseInt(rField.getText());
		int g = Integer.parseInt(gField.getText());
		int b = Integer.parseInt(bField.getText());
		line.setStart(startX, startY);
		line.setEnd(endX, endY);
		line.setFgColor(new Color(r, g, b));
	}

}
