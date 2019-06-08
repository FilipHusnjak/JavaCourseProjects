package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.OptionalInt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Servlet that creates XLS file showing powers of the numbers from the range
 * given through parameters. It accepts 3 parameters:
 * <ul>
 * <li> a - lower bound of the range (should be between -100 and 100 both inclusive)
 * <li> b - upper bound of the range (should be between -100 and 100 both inclusive)
 * <li> n - number of pages (should be between 1 and 5 both inclusive)
 * </ul>
 * 
 * @author Filip Husnjak
 */
@WebServlet("/powers")
public class PowersServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8017336416836058853L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		OptionalInt a = getParam("a", req, resp, -100, 100);
		if (a.isEmpty()) return;
		OptionalInt b = getParam("b", req, resp, -100, 100);
		if (b.isEmpty()) return;
		OptionalInt n = getParam("n", req, resp, 1, 5);
		if (n.isEmpty()) return;
		// Create workbook and write it to the proper output stream
		try (Workbook wb = createWorkBook(a.getAsInt(), b.getAsInt(), n.getAsInt())) {
			resp.setContentType("application/octet-stream");
			// Set default file name when downloading it
			resp.setHeader("Content-Disposition", "attachment; filename=\"powersTable.xls\"");
			wb.write(resp.getOutputStream());
		}
	}
	
	/**
	 * Creates and returns workbook based on given parameters.
	 * 
	 * @param a
	 *        lower bound of the range
	 * @param b
	 *        upper bound of the range
	 * @param n
	 *        number of pages
	 * @return workbook based on given parameters
	 */
	private Workbook createWorkBook(int a, int b, int n) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		for (int j = 1; j <= n; ++j) {
			HSSFSheet sheet =  hwb.createSheet(String.valueOf(j));
			for (int i = a, k = 0; i <= b; ++i, ++k) {
				HSSFRow rowhead = sheet.createRow((short) (k));
				rowhead.createCell((short) 0).setCellValue(i);
				rowhead.createCell((short) 1).setCellValue(Math.pow(i, j));
			}
		}
		return hwb;
	}
	
	/**
	 * Checks whether the parameter is between lower and upper bound, and if it
	 * can be interpreted as a number. If both conditions are satisfied proper
	 * {@link OptionalInt} is returned. Otherwise {@code OptionalInt.empty()) is
	 * returned.
	 * 
	 * @param key
	 *        key of the parameter
	 * @param req
	 *        {@link HttpServletRequest} used to send error
	 * @param resp
	 *        {@link HttpServletResponse} used to send error
	 * @param lowerBound
	 *        lower bound of the range
	 * @param upperBound
	 *        upper bound of the range
	 * @return {@link OptionalInt} which is empty if conditions are not satisfied
	 * @throws ServletException if an error could not be sent
	 * @throws IOException if an I/O error occurs
	 */
	private OptionalInt getParam(String key, HttpServletRequest req, HttpServletResponse resp,
			int lowerBound, int upperBound) throws ServletException, IOException {
		try {
			int value = Integer.parseInt(req.getParameter(key));
			if (value < lowerBound || value > upperBound) {
				sendError(
						String.format(
								"Parameter %s is out of bounds: [%d, %d]!", 
								key, 
								lowerBound, 
								upperBound),
						req, 
						resp);
				return OptionalInt.empty();
			}
			return OptionalInt.of(value);
		} catch (NumberFormatException e) {
			sendError("Parameter " + key + " not sent or cannot be intepreted as integer!", req, resp);
			return OptionalInt.empty();
		}
	}

	/**
	 * Dispatches an error with the given message using given request and response
	 * objects.
	 * 
	 * @param message
	 *        message of the error
	 * @param req
	 *        request object used to dispatch the request
	 * @param resp
	 *        response object used to dispatch the request
	 * @throws ServletException if an error could not be sent
	 * @throws IOException if an I/O error occurs
	 */
	private void sendError(String message, HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setAttribute("errorMessage", message);
		req.getRequestDispatcher("/WEB-INF/pages/paramError.jsp").forward(req, resp);
	}
	
}
