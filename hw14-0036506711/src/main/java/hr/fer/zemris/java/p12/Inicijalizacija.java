package hr.fer.zemris.java.p12;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Listener used for initializing connection pool and tables in database.
 * Tables Polls and PollOptions are created if they do not exist. Also
 * if table Polls is empty then both tables are filled with initial data
 * same as in hw13.
 * 
 * @author Filip Husnjak
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {
	
	/**
	 * Relative path to the properties file
	 */
	private static final String PROPERTIES = "/WEB-INF/dbsettings.properties";
	
	/**
	 * SQL state which is triggered when creating existing table
	 */
	private static final String TABLE_EXISTS_STATE = "X0Y32";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Create connection pool
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		Connection con = null;
		try {
			// Load and set proper driver
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
			// Read properties, initialize cpds and get proper connection
			con = readProperties(cpds, sce);
			// Create tables if they does not exist
			createProperTables(con);
			// Insert initial element is Polls table is empty
			insertProperElements(con, sce);
		} catch (Exception e) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e);
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Inserts elements into Polls and PollOptions tables. If Polls table is not
	 * empty this method does nothing.
	 * 
	 * @param con
	 *        {@link Connection} object used to insert data
	 * @throws SQLException if data cannot be inserted
	 * @throws IOException if I/O error occurs
	 */
	private void insertProperElements(Connection con, ServletContextEvent sce) 
			throws SQLException, IOException {
		// If Polls table is not empty return
		if (!checkIfEmpty(con)) return;
		// Insert voting poll into Polls table
		long pollID1 = insertIntoPolls(con, "Glasanje za omiljeni bend", 
				"Od sljedećih bendova, koji Vam je bend najdraži? "
				+ "Kliknite na link kako biste glasali!");
		// Insert proper options for voting poll
		loadFileIntoDatabase(con, sce, pollID1, "glasanje-definicija.txt");
		long pollID2 = insertIntoPolls(con, "Glasanje za omiljeni metal bend", 
				"Od sljedećih metal bendova, koji Vam je najdraži? "
				+ "Kliknite na link kako biste glasali!");
		loadFileIntoDatabase(con, sce, pollID2, "glasanje-definicija2.txt");
	}
	
	/**
	 * Inserts poll options read from the given file into the proper database.
	 * 
	 * @param con
	 *        {@link Connection} object used to insert data
	 * @param sce
	 *        servlet context used to determine file path
	 * @param pollID
	 *        id of the poll which this options belong to
	 * @param filename
	 *        name of the file used to load options
	 * @throws IOException if an I/O error occurs
	 * @throws SQLException if there was an error when reaching database
	 */
	private void loadFileIntoDatabase(Connection con, ServletContextEvent sce, long pollID,
			String filename) 
			throws IOException, SQLException {
		String fileNameBand = sce.getServletContext().getRealPath("/WEB-INF/" + filename);
		Path pathBands = Paths.get(fileNameBand);
		if (!Files.exists(pathBands)) {
			return;
		}
		List<String> lines = Files.readAllLines(pathBands);
		if (lines == null) return;
		for (String line: lines) {
			String[] parts = line.split("\\t+");
			insertIntoPollOptions(con, parts[0], parts[1], pollID, Integer.parseInt(parts[2]));
		}
	}

	/**
	 * Checks whether Polls table is empty and returns {@code true} if thats
	 * the case.
	 * 
	 * @param con
	 *        {@link Connection} object used to check whether Polls table is empty
	 * @return {@code true} if Polls table is empty
	 * @throws SQLException if there was an error when reaching database
	 */
	private boolean checkIfEmpty(Connection con) throws SQLException {
		String query = "SELECT * FROM Polls";
		PreparedStatement pst = con.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return rs == null || !rs.next();
	}

	/**
	 * Creates tables Polls and PollOptions if they does not exist. If tables
	 * already exist nothing happens.
	 * 
	 * @param con
	 *        {@link Connection} object used to create tables
	 * @throws SQLException if there was an error when reaching database
	 */
	private void createProperTables(Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		String query1 = "CREATE TABLE Polls( "
		         + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
		         + "title VARCHAR(150) NOT NULL, "
		         + "message CLOB(2048) NOT NULL)";
		createTableIfNeeded(stmt, query1);
		String query2 = "CREATE TABLE PollOptions( "
		         + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
		         + "optionTitle VARCHAR(100) NOT NULL, "
		         + "optionLink VARCHAR(150) NOT NULL, "
		         + "pollID BIGINT, "
		         + "votesCount BIGINT, "
		         + "FOREIGN KEY (pollID) REFERENCES Polls(id))";
		createTableIfNeeded(stmt, query2);
	}
	
	/**
	 * Inserts new Poll with specified title and message. Its ID is auto generated.
	 * 
	 * @param con
	 *        {@link Connection} object used to insert record
	 * @param title
	 *        title of the poll
	 * @param message
	 *        message of the poll
	 * @return generated ID of the created poll
	 * @throws SQLException if there was an error when reaching database
	 */
	private long insertIntoPolls(Connection con, String title, String message) 
			throws SQLException {
		String query = "INSERT INTO Polls (title, message) VALUES (?,?)";
		PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.setString(1, title);
		pst.setString(2, message);
		pst.executeUpdate();
		try (ResultSet rset = pst.getGeneratedKeys()) {
			if(rset != null && rset.next()) {
				long noviID = rset.getLong(1);
				return noviID;
			}
			throw new SQLException("Key was not generated!");
		}
	}
	
	/**
	 * Inserts new poll option with given title, link, pollID and number of votes.
	 * 
	 * @param con
	 *        {@link Connection} object used to insert record
	 * @param title
	 *        title of the PollOption
	 * @param link
	 *        link of the PollOption
	 * @param pollID
	 *        ID of the poll that uses this option
	 * @param votesCount
	 *        initial number of votes
	 * @throws SQLException if there was an error when reaching database
	 */
	private void insertIntoPollOptions(Connection con, String title, String link,
			long pollID, long votesCount) 
			throws SQLException {
		String query = "INSERT INTO PollOptions (optionTitle, optionLink, pollID, "
				+ "votesCount) VALUES (?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, title);
		pst.setString(2, link);
		pst.setLong(3, pollID);
		pst.setLong(4, votesCount);
		pst.executeUpdate();
	}

	/**
	 * Executes given query and ignores exception if its result of creating
	 * existing table. This method is used to create tables that does not exist.
	 * 
	 * @param stmt
	 *        {@link Statement} object used to execute query
	 * @param query
	 *        query to be executed
	 * @throws SQLException if there was an error when reaching database
	 */
	private void createTableIfNeeded(Statement stmt, String query) throws SQLException {
		try {
			stmt.execute(query);
		} catch(SQLException e) {
		    if(tableAlreadyExists(e)) return;
		    throw e;
		}
	}

	/**
	 * Checks whether given {@link SQLException} is the result of creating
	 * table that already exists. If thats the case this method returns
	 * {@code true}.
	 * 
	 * @param e
	 *        exception to be checked
	 * @return {@code true} if the given exception is the result of creating a
	 *         table that already exists
	 */
	private boolean tableAlreadyExists(SQLException e) {
		return e.getSQLState().equals(TABLE_EXISTS_STATE);
	}

	/**
	 * Reads properties from the properties file. Initializes given connection
	 * pool with proper values read from the file. Returns proper connection after
	 * initialization.
	 * 
	 * @param cpds
	 *        connection pool to be initialized
	 * @param sce
	 *        servlet context used to determine file path
	 * @return proper connection after initialization
	 * @throws URISyntaxException if there was an error creating URI
	 * @throws IOException if I/O error occurs
	 * @throws SQLException if there was an error when reaching database
	 */
	private Connection readProperties(ComboPooledDataSource cpds, ServletContextEvent sce) 
			throws URISyntaxException, IOException, SQLException {
		Path properties = Paths.get(sce.getServletContext().getRealPath(PROPERTIES));
		Properties config = new Properties();
		config.load(Files.newInputStream(properties));
		String host = config.getProperty("host");
		int port = Integer.parseInt(config.getProperty("port"));
		String name = config.getProperty("name");
		URI uri = new URI("jdbc:derby", null, host, port, "/" + name, null, null);
		cpds.setJdbcUrl(uri.toString());
		cpds.setUser(config.getProperty("user"));
		cpds.setPassword(config.getProperty("password"));
		return cpds.getConnection();
	}

}