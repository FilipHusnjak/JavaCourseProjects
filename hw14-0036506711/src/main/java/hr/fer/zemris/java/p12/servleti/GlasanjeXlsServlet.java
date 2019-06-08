/**
 * 
 */
package hr.fer.zemris.java.p12.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Servlet used to create XLS file showing voting results.
 * 
 * @author Filip Husnjak
 */
@WebServlet("/servleti/glasanje-xls")
public final class GlasanjeXlsServlet extends HttpServlet {

	private static final long serialVersionUID = 3318068302341030625L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> results = DAOProvider.getDao().getPollOptionsList(pollID);
		try (Workbook hwb = createWorkBook(results)) {
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
	private Workbook createWorkBook(List<PollOption> results) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet =  hwb.createSheet();
		int i = 0;
		for (PollOption option: results) {
			HSSFRow rowhead = sheet.createRow((short) (i++));
			rowhead.createCell((short) 0).setCellValue(option.getTitle());
			rowhead.createCell((short) 1).setCellValue(option.getVotes());
		}
		return hwb;
	}
	
}
