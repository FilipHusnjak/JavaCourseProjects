package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class used to fetch data from the files such as list of bands and
 * list of voting results. Its methods are also synchronized so 2 threads cannot
 * operate on the same file at the same time.
 * 
 * @author Filip Husnjak
 */
public class ServletUtil {

	/**
	 * Instances of this class are prohibited.
	 */
	private ServletUtil() {}
	
	/**
	 * Creates map which maps each band to its ID. Bands are loaded from the proper
	 * file on the disk.
	 * 
	 * @param req
	 *        request object used to determine proper path of the file
	 * @return map which maps each band to its ID
	 * @throws IOException if an I/O error occurs
	 */
	protected synchronized static Map<Integer, Band> loadBands(HttpServletRequest req) throws IOException {
		String fileNameBand = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		Path pathBands = Paths.get(fileNameBand);
		if (!Files.exists(pathBands)) {
			Files.createFile(pathBands);
		}
		List<String> lines = Files.readAllLines(pathBands);
		return lines.stream().collect(Collectors.toMap(
				ServletUtil::getID,
				ServletUtil::getBand));
	}
	
	/**
	 * Creates map which maps each voting result to the proper band. Results
	 * are loaded from the proper file on the disk. Results are also sorted
	 * by the number of votes.
	 * 
	 * @param req
	 *        request object used to determine proper path of the file
	 * @return map which maps each voting result to the proper band
	 * @throws IOException if an I/O error occurs
	 */
	protected synchronized static LinkedHashMap<Integer, Integer> loadResults(HttpServletRequest req)
			throws IOException {
		String fileNameRes = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path pathRes = Paths.get(fileNameRes);
		if (!Files.exists(pathRes)) {
			Files.createFile(pathRes);
		}
		List<String> lines = Files.readAllLines(pathRes);		
		return lines.stream().collect(Collectors.toMap(
				ServletUtil::getID,
				ServletUtil::getVotes)).entrySet().stream()
				   .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .collect(Collectors.toMap(
			    		   Map.Entry::getKey,
			    		   Map.Entry::getValue,
			    		   (e1, e2) -> e1,
			    		   LinkedHashMap::new));
	}
	
	/**
	 * Stores the given results to the proper file on disk.
	 * 
	 * @param req
	 *        request object used to determine proper path of the file
	 * @param results
	 *        results to be stored
	 * @throws IOException if an I/O error occurs
	 */
	protected synchronized static void storeResults(HttpServletRequest req, Map<Integer, Integer> results) 
			throws IOException {
		String fileNameRes = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path pathRes = Paths.get(fileNameRes);
		StringBuilder result = new StringBuilder();
		for (Map.Entry<Integer, Integer> entry: results.entrySet()) {
			result.append(getResultLine(entry));
		}
		Files.writeString(pathRes, result.toString());
	}
	
	/**
	 * Creates and returns the formatted line from the given entry. Key and
	 * value are separated by a tab.
	 * 
	 * @param entry
	 *        entry to be transformed to the string line
	 * @return the formatted line created from the given entry
	 */
	private static String getResultLine(Entry<Integer, Integer> entry) {
		return entry.getKey() + "\t" + entry.getValue() + "\r\n";
	}

	/**
	 * Returns ID determined from the given records line.
	 * 
	 * @param line
	 *        record line which should have defined ID
	 * @return ID determined from the given records line
	 */
	private static int getID(String line) {
		String[] parts = line.split("\\t+");
		return Integer.parseInt(parts[0]);
	}
	
	/**
	 * Returns ID determined from the given records line.
	 * 
	 * @param line
	 *        record line which should have defined ID
	 * @return ID determined from the given records line
	 */
	private static int getVotes(String line) {
		String[] parts = line.split("\\t+");
		return Integer.parseInt(parts[1]);
	}
	
	/**
	 * Returns {@link Band} created from the given line.
	 * 
	 * @param line
	 *        record line which should define all {@link Band} fields
	 * @return {@link Band} created from the given line
	 */
	private static Band getBand(String line) {
		String[] parts = line.split("\\t+");
		return new Band(Integer.parseInt(parts[0]), parts[1], parts[2]);
	}
	
	/**
	 * Helper class that represents band. It has 3 fields:
	 * <ul>
	 * <li> ID - id of the band
	 * <li> name - name of the band
	 * <li> link - link to the song
	 * </ul>
	 * 
	 * @author Filip Husnjak
	 */
	public static class Band {
		
		/**
		 * ID of the band
		 */
		private int ID;
		
		/**
		 * Name of the band		
		 */
		private String name;
		
		/**
		 * Link to the specific song
		 */
		private String link;
		
		/**
		 * Constructs new band with the given parameters.
		 * 
		 * @param ID
		 *        ID of the band
		 * @param name
		 *        name of the band
		 * @param link
		 *        link to the specific song
		 */
		private Band(int ID, String name, String link) {
			this.ID = ID;
			this.name = name;
			this.link = link;
		}

		/**
		 * Returns name of the band.
		 * 
		 * @return name of the band
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns link to the specific song.
		 * 
		 * @return link to the specific song
		 */
		public String getLink() {
			return link;
		}
		
		/**
		 * Returns ID of the band.
		 * 
		 * @return ID of the band
		 */
		public int getID() {
			return ID;
		}

		@Override
		public int hashCode() {
			return Objects.hash(ID, link, name);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Band))
				return false;
			Band other = (Band) obj;
			return ID == other.ID && Objects.equals(link, other.link) && Objects.equals(name, other.name);
		}
		
	}
	
}
