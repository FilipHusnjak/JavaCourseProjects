package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Represents bar chart in the graph. It stores defined bars in a list
 * and also provides information about max and min y values. Other 3 properties
 * are description of a x axis, y axis and resolution of the y axis. Each bar is 
 * represented with x and y values defined by {@link XYValue} class.
 * 
 * @author Filip Husnjak
 */
public class BarChart {

	/**
	 * Bars in this bar charts.
	 */
	private List<XYValue> elems;
	
	/**
	 * Description of the x axis.
	 */
	private String xDesc;
	
	/**
	 * Description of the y axis.
	 */
	private String yDesc;
	
	/**
	 * Min y value of defined bars in {@link #elems} list.
	 */
	private int yMin;
	
	/**
	 * Max y value of defined bars in {@link #elems} list.
	 */
	private int yMax;
	
	/**
	 * Resolution of y axis.
	 */
	private int yRes;

	/**
	 * Constructs new {@link BarChart} with specified parameters.
	 * 
	 * @param elems
	 *        bars contained in this chart
	 * @param xDesc
	 *        description of x axis
	 * @param yDesc
	 *        description of y axis
	 * @param yMin
	 *        min y value in this chart
	 * @param yMax
	 *        max y value in this chart
	 * @param yRes
	 *        resolution of y axis
	 * @throws IllegalArgumentException if the given arguments are not valid,
	 *         more appropriate explanation is given through the exception message
	 */
	public BarChart(List<XYValue> elems, String xDesc, String yDesc, int yMin, int yMax, int yRes) {
		this.elems = elems;
		this.xDesc = xDesc;
		this.yDesc = yDesc;
		this.yMin = yMin;
		if (yMin < 0) {
			throw new IllegalArgumentException("Given y min cannot be negative!");
		}
		this.yMax = yMax;
		if (yMax <= yMin) {
			throw new IllegalArgumentException("YMax has to be greater than minimum y!");
		}
		this.yRes = yRes;
		if (yRes <= 0) {
			throw new IllegalArgumentException("Given y resolution has to be positive!");
		}
		this.yMax = (int) (Math.ceil((double)(yMax - yMin) / yRes) * yRes + yMin);
		for (XYValue value: elems) {
			if (value.y < yMin) {
				throw new IllegalArgumentException("Given list contains illegal y values!");
			}
		}
	}

	/**
	 * Returns list of bars in this chart.
	 * 
	 * @return list of bars in this chart
	 */
	public List<XYValue> getElems() {
		return elems;
	}

	/**
	 * Returns description of a x axis.
	 * 
	 * @return description of a x axis
	 */
	public String getxDesc() {
		return xDesc;
	}

	/**
	 * Returns description of a y axis.
	 * 
	 * @return description of a y axis
	 */
	public String getyDesc() {
		return yDesc;
	}

	/**
	 * Returns min y value in this chart.
	 * 
	 * @return min y value in this chart
	 */
	public int getyMin() {
		return yMin;
	}

	/**
	 * Returns max y value in this chart.
	 * 
	 * @return max y value in this chart
	 */
	public int getyMax() {
		return yMax;
	}

	/**
	 * Returns resolution of a y axis.
	 * 
	 * @return resolution of a y axis
	 */
	public int getyRes() {
		return yRes;
	}
	
}
