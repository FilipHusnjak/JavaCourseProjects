package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Map;

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

import hr.fer.zemris.java.servlets.ServletUtil.Band;

/**
 * Servlet that draws the pie chart with proper values given through request
 * parameters. Pie chart is then written to the proper output stream.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 8027131377706417053L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<Integer, Integer> results = ServletUtil.loadResults(req);
		Map<Integer, Band> bands = ServletUtil.loadBands(req);
		
		resp.setContentType("image/png");
		// This will create the dataset
        PieDataset dataset = createDataset(results, bands);
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, "");
        int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(resp.getOutputStream(), chart, width, height);
	}

	/**
	 * Creates dataset from the given parameters.
	 * 
	 * @param results
	 *        results to be written
	 * @param bands
	 *        map used to fetch band names
	 * @return {@link PieDataset} created from parameters
	 * @throws IOException if an I/O error occurs
	 */
	private  PieDataset createDataset(Map<Integer, Integer> results, Map<Integer, Band> bands) 
			throws IOException {
        DefaultPieDataset result = new DefaultPieDataset();
        for (Map.Entry<Integer, Integer> entry: results.entrySet()) {
        	result.setValue(bands.get(entry.getKey()).getName(), entry.getValue());
        }
        return result;
    }
	
	/**
	 * Creates the chart from the given dataset with given title.
	 * 
	 * @param dataset
	 *        dataset used to create the chart
	 * @param title
	 *        title of the chart
	 * @return created pie chart
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
