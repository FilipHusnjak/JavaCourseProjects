package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * Creates a pie chart representing OS usage. Writes the created
 * image to the proper output stream of the response.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/reportImage")
public class ReportImageServlet extends HttpServlet {

	private static final long serialVersionUID = -7781041289179202399L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("image/png");
		// This will create the dataset
        PieDataset dataset = createDataset();
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, "OS Usage");
		int width = 500;
		int height = 350;

		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, width, height);
	}

	/**
	 * Creates and returns dataset with OS usage.
	 * 
	 * @return dataset with OS usage
	 */
	private  PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 29);
        result.setValue("Mac", 20);
        result.setValue("Windows", 51);
        return result;
    }
	
	/**
	 * Creates a pie chart from the given dataset with given title.
	 * 
	 * @param dataset
	 *        dateset used for creation
	 * @param title
	 *        title of the pie chart
	 * @return a pie chart from the given dataset with given title
	 */
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }

}
