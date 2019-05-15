package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program that displays bar chart defined in file. Filename is given through
 * the program parameters.
 * 
 * @author Filip Husnjak
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Width of a window.
	 */
	private static int WINDOW_WIDTH = 1000;
	
	/**
	 * Height of a window.
	 */
	private static int WINDOW_HEIGHT = 1000;
	
	/**
	 * Bar chart whose properties are to be displayed.
	 */
	private BarChart chart;
	
	/**
	 * Name of the file used for loading bar chart properties.
	 */
	private String file;
	
	/**
	 * Constructs new {@link BarChartDemo} with specified chart to be displayed
	 * and file.
	 * 
	 * @param chart
	 *        chart to be displayed
	 * @param filename
	 *        path of the file used to load chart parameters
	 */
	public BarChartDemo(BarChart chart, String file) {
		this.chart = chart;
		this.file = file;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		initGuit();
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes GUI with label that displays path to the file and 
	 * {@link BarChartComponent} which displays chart.
	 */
	private void initGuit() {
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		JLabel fileLabel = new JLabel(file, SwingConstants.CENTER);
		fileLabel.setFont(new Font("Arial", Font.BOLD, 20));
		container.add(fileLabel, BorderLayout.NORTH);
		container.add(new BarChartComponent(chart), BorderLayout.CENTER);
	}

	/**
	 * Program takes 1 argument that should be the path to the file. File should
	 * contain at least 6 rows each defining:
	 * <ul>
	 * <li> X axis description
	 * <li> Y axis description
	 * <li> bars whose x and y values are separated by comma and each bar is 
	 *      separated by spaces
	 * <li> Y min value
	 * <li> Y max value
	 * <li> resolution of the y axis
	 * </ul>
	 * 
	 * @param args
	 *        path to the file
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of arguments!");
			return;
		}
		String file = args[0];
		try {
			Path path = Paths.get(file);
			List<String> lines = Files.readAllLines(path);
			if (lines.size() < 6) {
				System.out.println("File has to contain at least 6 lines!");
				return;
			}
			String xDesc = lines.get(0);
			String yDesc = lines.get(1);
			String[] bars = lines.get(2).split("\\s+");
			List<XYValue> barValues = new LinkedList<>();
			for (String bar: bars) {
				String[] xyValues = bar.split(",");
				if (xyValues.length != 2) {
					System.out.println("Bar description has to contain 2 numbers!");
					return;
				}
				barValues.add(new XYValue(
						Integer.parseInt(xyValues[0]), 
						Integer.parseInt(xyValues[1])
						));
			}
			int yMin = Integer.parseInt(lines.get(3));
			int yMax = Integer.parseInt(lines.get(4));;
			int yRes = Integer.parseInt(lines.get(5));
			BarChart chart = new BarChart(barValues, xDesc, yDesc, yMin, yMax, yRes);
			SwingUtilities.invokeLater(() -> new BarChartDemo(
					chart,
					path.toAbsolutePath().normalize().toString())
					.setVisible(true));
		} catch (IOException e) {
			System.out.println("File does not exist or cannot be read!");
			return;
		} catch (NumberFormatException e) {
			System.out.println("Number expected but not provided!");
			return;
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			return;
		}
	}

}
