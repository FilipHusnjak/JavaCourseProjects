package hr.fer.zemris.java.hw11.jnotepadpp.model;

/**
 * Listener that notifies about changes upon a specific single document
 * model. Two changes are possible: document modification or document path
 * update.
 * 
 * @author Filip Husnjak
 */
public interface SingleDocumentListener {
	
	/**
	 * Notifies observer that subject document was modified.
	 * 
	 * @param model
	 *        document that was modified
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Notifies observer that subject documents path was updated.
	 * 
	 * @param model
	 *        document whose path was updated
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
	
}
