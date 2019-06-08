package hr.fer.zemris.java.p12.dao;

import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

import java.util.List;

/**
 * Interface to the database layer. This interface provides all needed methods
 * for servlets to load proper data.
 * 
 * @author Filip Husnjak
 */
public interface DAO {

	/**
	 * Returns Poll record with specified ID.
	 * 
	 * @param ID
	 *        ID of the Poll record to be returned
	 * @return Poll record with specified ID
	 * @throws DAOException if there was a problem when reaching database
	 */
	public Poll getPoll(long ID) throws DAOException;
	
	/**
	 * Returns all Poll records stored in database as list.
	 * 
	 * @return all Poll records stored in database as list
	 * @throws DAOException if there was a problem when reaching database
	 */
	public List<Poll> getPollsList() throws DAOException;
	
	/**
	 * Returns pool option with specified ID.
	 * 
	 * @param ID
	 *        ID of the pool option to be returned
	 * @return pool option with specified ID
	 * @throws DAOException if there was a problem when reaching database
	 */
	public PollOption getPollOption(long ID) throws DAOException;
	
	/**
	 * Returns list of PollOptions that are options for the poll specified by
	 * the given ID.
	 * 
	 * @param pollID
	 *        ID of the poll whose options are to be returned
	 * @return list of PollOptions that are options for the poll specified by
	 *         the given ID
	 * @throws DAOException if there was a problem when reaching database
	 */
	public List<PollOption> getPollOptionsList(long pollID) throws DAOException;
	
	/**
	 * Updates poll record with given ID using given {@link PollOption} object.
	 * 
	 * @param optionID
	 *        ID of the option to be updated
	 * @param option
	 *        option object whose values are used for update
	 * @throws DAOException if there was a problem when reaching database
	 */
	public void updatePollOption(long optionID, PollOption option) throws DAOException;
	
}