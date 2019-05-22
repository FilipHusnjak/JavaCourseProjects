package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

/**
 * Represents bar chart component. It places all properties defined
 * in {@link BarChart} class with proper alignments and coordinate system.
 * 
 * @author Filip Husnjak
 */
public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	/**
	 * Space between y axis and description.
	 */
	private static int SPACE_Y = 30;

	/**
	 * Space between x axis and description.
	 */
	private static int SPACE_X = 30;
	
	/**
	 * Length of axis marks.
	 */
	private static int EXTRA = 10;

	/**
	 * Bar chart whose properties are drawn.
	 */
	private BarChart chart;
	
	/**
	 * Color of the net.
	 */
	private static Color netColor = new Color(235, 223, 202);
	
	/**
	 * Color of the shadows.
	 */
	private static Color shadowColor = new Color(138 / 255.0f, 121 / 255.0f, 
			93 / 255.0f, 0.5f);
	
	/**
	 * Color of the bars.
	 */
	private static Color barColor = new Color(244, 119, 71);
	
	/**
	 * Width of a shadow.
	 */
	private static int SHADOW_RIGHT = 5;
	
	/**
	 * Height of a shadow.
	 */
	private static int SHADOW_TOP = 5;

	/**
	 * Constructs new {@link BarChartComponent} with specified chart.
	 * 
	 * @param chart
	 *        chart whose properties are to be drawn
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setFont(new Font("Arial", Font.PLAIN, 15));
		FontMetrics metrics = g2.getFontMetrics();

		// Calculating important values
		int yNumberWidth = metrics.stringWidth(Integer.toString(chart.getyMax()));
		int zeroY = getHeight() - 2 * SPACE_Y - 2 * metrics.getHeight();
		int coordinateYSize = zeroY - SPACE_Y;
		int zeroX = 2 * SPACE_X + metrics.getHeight() + yNumberWidth;
		int coordinateXSize = getWidth() - zeroX - SPACE_X;

		// Rotating y axis description
		AffineTransform savedTransform = g2.getTransform();
		AffineTransform yDesc = (AffineTransform) savedTransform.clone();
		yDesc.rotate(-Math.PI / 2);
		g2.setTransform(yDesc);
		g2.drawString(chart.getyDesc(), -(coordinateYSize + metrics.stringWidth(chart.getyDesc())) / 2 - SPACE_Y, SPACE_X);
		g2.setTransform(savedTransform);
		
		// Setting x axis description
		g2.drawString(chart.getxDesc(), zeroX + (coordinateXSize - metrics.stringWidth(chart.getxDesc())) / 2, getHeight() - SPACE_Y);
		
		// Setting x axis numbers
		g2.setFont(new Font("Arial", Font.BOLD, 15));
		int barWidth = coordinateXSize / chart.getElems().size();
		for (int i = 0; i < chart.getElems().size(); ++i) {
			g2.drawString(Integer.toString(chart.getElems().get(i).x), 
					zeroX + i * barWidth + (barWidth - metrics.stringWidth(Integer.toString(chart.getElems().get(i).x))) / 2, 
					zeroY + metrics.getHeight() + EXTRA);
		}
		
		// Setting y axis numbers
		int n = (chart.getyMax() - chart.getyMin()) / chart.getyRes();
		int heightWidth = coordinateYSize / n;
		for (int i = 0; i <= n; ++i) {
			g2.drawString(Integer.toString(i * chart.getyRes() + chart.getyMin()), zeroX - metrics.stringWidth(Integer.toString(i * chart.getyRes() + chart.getyMin())) - 2 * EXTRA, 
					zeroY - i * heightWidth + metrics.getHeight() / 4);
		}
		
		// Drawing net
		g2.setStroke(new BasicStroke(3));
		g2.setColor(netColor);
		for (int i = 0; i < chart.getElems().size(); ++i) {
			g2.drawLine(zeroX + (i + 1) * barWidth, zeroY, zeroX + (i + 1) * barWidth, SPACE_Y);
		}
		for (int i = 0; i < n; ++i) {
			g2.drawLine(zeroX, zeroY - (i + 1) * heightWidth, zeroX + coordinateXSize, zeroY - (i + 1) * heightWidth);
		}
		
		// Drawing shadows
		g2.setColor(shadowColor);
		for (int i = 0; i < chart.getElems().size(); ++i) {
			g2.fillRect(zeroX + i * barWidth + SHADOW_RIGHT, zeroY - chart.getElems().get(i).y * heightWidth / chart.getyRes() + SHADOW_TOP, barWidth, chart.getElems().get(i).y * heightWidth / chart.getyRes() - SHADOW_TOP);
		}
		
		// Drawing bars
		g2.setStroke(new BasicStroke(1));
		for (int i = 0; i < chart.getElems().size(); ++i) {
			g2.setColor(barColor);
			g2.fillRect(zeroX + i * barWidth, zeroY - chart.getElems().get(i).y * heightWidth / chart.getyRes(), barWidth, chart.getElems().get(i).y * heightWidth / chart.getyRes());
			g2.setColor(Color.WHITE);
			g2.drawRect(zeroX + i * barWidth, zeroY - chart.getElems().get(i).y * heightWidth / chart.getyRes(), barWidth, chart.getElems().get(i).y * heightWidth / chart.getyRes());
		}
		
		// Drawing x axis
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.GRAY);
		Line2D.Double xAxis = new Line2D.Double(zeroX - EXTRA, zeroY, zeroX + coordinateXSize + EXTRA, zeroY);
		g2.draw(xAxis);
		drawArrowHead(g2, xAxis);
		
		// Drawing y axis
		Line2D.Double yAxis = new Line2D.Double(zeroX, zeroY + EXTRA, zeroX, zeroY - coordinateYSize - EXTRA);
		g2.draw(yAxis);
		drawArrowHead(g2, yAxis);
		
		// Drawing axis marks
		for (int i = 0; i < chart.getElems().size(); ++i) {
			g2.drawLine(zeroX + (i + 1) * barWidth, zeroY + EXTRA, zeroX + (i + 1) * barWidth, zeroY);
		}
		for (int i = 0; i < n; ++i) {
			g2.drawLine(zeroX - EXTRA, zeroY - (i + 1) * heightWidth, zeroX, zeroY - (i + 1) * heightWidth);
		}
	}
	
	/**
	 * Draws arrow head on the specified line.
	 * 
	 * @param g2
	 *        Graphics contex used for drawing
	 * @param line
	 *        lines whose arrow head is to be drawn
	 */
	private void drawArrowHead(Graphics2D g2, Line2D.Double line) { 
		Polygon arrowHead = new Polygon();  
		arrowHead.addPoint(0, 6);
		arrowHead.addPoint(-6, -6);
		arrowHead.addPoint(6, -6);
		double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
		AffineTransform tx = (AffineTransform) g2.getTransform().clone();
	    tx.translate(line.x2, line.y2);
	    tx.rotate((angle - Math.PI / 2d));
	    Graphics2D g = (Graphics2D) g2.create();
	    g.setTransform(tx); 
	    g.fill(arrowHead);
	    g.dispose();
	}

}
