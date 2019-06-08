package hr.fer.zemris.java.p12.model;

/**
 * Class which models Poll records. It has 3 properties:
 * <ul>
 * <li> ID - ID of the poll
 * <li> title - title of the poll
 * <li> message - message of the poll
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class Poll {

	/**
	 * ID of the poll
	 */
	private long ID;
	
	/**
	 * Title of the poll
	 */
	private String title;
	
	/**
	 * Message of the poll
	 */
	private String message;

	/**
	 * Constructs new Poll with given parameters.
	 * 
	 * @param ID
	 *        ID of the poll
	 * @param title
	 *        title of the poll
	 * @param message
	 *        message of the poll
	 */
	public Poll(long ID, String title, String message) {
		super();
		this.ID = ID;
		this.title = title;
		this.message = message;
	}

	/**
	 * Returns ID of this poll.
	 * 
	 * @return ID of this poll
	 */
	public long getID() {
		return ID;
	}

	/**
	 * Returns title of this poll.
	 * 
	 * @return title of this poll
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns message of this poll.
	 * 
	 * @return message of this poll
	 */
	public String getMessage() {
		return message;
	}
	
}
