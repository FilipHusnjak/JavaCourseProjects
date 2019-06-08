/**
 * 
 */
package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import hr.fer.zemris.java.servlets.ServletUtil.Band;

/**
 * Servlet used to create XLS file showing voting results.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/glasanje-xls")
public final class GlasanjeXlsServlet extends HttpServlet {

	private static final long serialVersionUID = 3318068302341030625L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<Integer, Integer> results = ServletUtil.loadResults(req);
		Map<Integer, Band> bands = ServletUtil.loadBands(req);
		try (Workbook hwb = createWorkBook(results, bands)) {
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=\"glasanjeRezultati.xls\"");
			hwb.write(resp.getOutputStream());
		}
	}
	
	/**
	 * Creates and returns {@link Workbook} from the given results map and bands.
	 * 
	 * @param results
	 *        results used to create {@link Workbook}
	 * @param bands
	 *        bands used to determine names of bands with specified ID
	 * @return {@link Workbook} created from the given results map and bands
	 */
	private Workbook createWorkBook(Map<Integer, Integer> results, Map<Integer, Band> bands) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet =  hwb.createSheet();
		int i = 1;
		for (Map.Entry<Integer, Integer> entry: results.entrySet()) {
			HSSFRow rowhead = sheet.createRow((short) (i++ - 1));
			rowhead.createCell((short) 0).setCellValue(bands.get(entry.getKey()).getName());
			rowhead.createCell((short) 1).setCellValue(entry.getValue());
		}
		return hwb;
	}
	
}
