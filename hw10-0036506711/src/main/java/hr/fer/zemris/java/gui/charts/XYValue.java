package hr.fer.zemris.java.gui.charts;

/**
 * Represents x and y values of each bar in the chart.
 * 
 * @author Filip Husnjak
 */
public class XYValue {

	/**
	 * X value of this bar
	 */
	int x;
	
	/**
	 * Y value of this bar
	 */
	int y;

	/**
	 * Constructs new {@link XYValue} with specified parameters.
	 * 
	 * @param x
	 *        x value of this bar
	 * @param y
	 *        y value of this bar
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
}
