package hr.fer.zemris.java.p12.dao.sql;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public Poll getPoll(long ID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		String query = "SELECT * FROM Polls WHERE id=?";
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setLong(1, ID);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs == null || !rs.next()) {
					throw new DAOException("Cannot find specified poll!");
				}
				return new Poll(rs.getLong(1), rs.getString(2), rs.getString(3));
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
	}

	@Override
	public List<Poll> getPollsList() throws DAOException {
		List<Poll> polls = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		String query = "SELECT * FROM Polls ORDER BY id";
		try (PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
			while(rs != null && rs.next()) {
				Poll poll = new Poll(rs.getLong(1), rs.getString(2), rs.getString(3));
				polls.add(poll);
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return polls;
	}

	@Override
	public PollOption getPollOption(long ID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		String query = "SELECT * FROM PollOptions WHERE id=?";
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setLong(1, ID);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs == null || !rs.next()) {
					throw new DAOException("Cannot find specified poll option!");
				}
				return new PollOption(rs.getLong(1), rs.getString(2), rs.getString(3), 
						rs.getLong(4), rs.getLong(5));
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
	}

	@Override
	public List<PollOption> getPollOptionsList(long pollID) throws DAOException {
		List<PollOption> options = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		String query = "SELECT * FROM PollOptions WHERE pollID=? ORDER BY votesCount DESC, id, optionTitle";
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				while(rs != null && rs.next()) {
					PollOption option = new PollOption(rs.getLong(1), 
							rs.getString(2), rs.getString(3), 
							rs.getLong(4), rs.getLong(5));
					options.add(option);
				}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return options;
	}

	@Override
	public void updatePollOption(long optionID, PollOption option) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		String query = "UPDATE PollOptions SET votesCount=? WHERE id=?";
		try (PreparedStatement pst = con.prepareStatement(query)) {
			pst.setLong(1, option.getVotes());
			pst.setLong(2, optionID);
			int rows = pst.executeUpdate();
			if (rows != 1) {
				throw new DAOException("Illegal number of rows affected!");
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
	}

}