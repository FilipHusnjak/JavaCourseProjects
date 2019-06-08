package hr.fer.zemris.java.p12.model;

/**
 * Class which models poll options. It consists of 5 fields:
 * <ul>
 * <li> ID - id of the option
 * <li> title - title of the option
 * <li> link - link of the option
 * <li> pollID - ID of the poll which this option belongs to
 * <li> votes - number of votes of this option
 * </ul>
 * 
 * @author Filip Husnjak
 */
public class PollOption {

	/**
	 * ID of this option
	 */
	private long ID;
	
	/**
	 * Title of this option
	 */
	private String title;
	
	/**
	 * Link of this option
	 */
	private String link;
	
	/**
	 * ID of the poll which this option belongs to
	 */
	private long pollID;
	
	/**
	 * Number of votes of this option
	 */
	private long votes;

	/**
	 * Constructs new {@link PollOption} with given parameters.
	 * 
	 * @param ID
	 *        ID of the option
	 * @param title
	 *        title of the option
	 * @param link
	 *        link of the option
	 * @param pollID
	 *        ID of the poll which this option belongs to
	 * @param votes
	 *        number of votes of this option
	 */
	public PollOption(long ID, String title, String link, long pollID, long votes) {
		this.ID = ID;
		this.title = title;
		this.link = link;
		this.pollID = pollID;
		this.votes = votes;
	}

	/**
	 * Returns ID of this option.
	 * 
	 * @return ID of this option
	 */
	public long getID() {
		return ID;
	}

	/**
	 * Returns title of this option.
	 * 
	 * @return title of this option
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns link of this option.
	 * 
	 * @return link of this option
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Returns ID of the poll this option belongs to.
	 * 
	 * @return ID of the poll this option belongs to
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Returns number of votes this option has.
	 * 
	 * @return number of votes this option has
	 */
	public long getVotes() {
		return votes;
	}
	
	/**
	 * Increases number of votes of this option by one. Also returns this option.
	 * 
	 * @return this {@link PollOption}
	 */
	public PollOption vote() {
		votes++;
		return this;
	}

}
