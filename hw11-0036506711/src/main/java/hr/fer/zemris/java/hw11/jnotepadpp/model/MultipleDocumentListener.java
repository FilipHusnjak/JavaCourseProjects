package hr.fer.zemris.java.hw11.jnotepadpp.model;

/**
 * Listener that notifies main program about current document changes, addition of a document,
 * removal of a document and if user wants to close specific document.
 * 
 * @author Filip Husnjak
 */
public interface MultipleDocumentListener {
	
	/**
	 * Notifies that current document was changed and provides previous model
	 * and current model. If previous model is {@code null} that means there was
	 * no document opened before this method was called. If current model is
	 * {@code null} that means last document is closed. Both previous and current
	 * models cannot be null at the same time.
	 * 
	 * @param previousModel
	 *        previous model that was activated before current document changed
	 * @param currentModel
	 *        current document that is activated
	 * @throws IllegalArgumentException if both previous and current models are {@code null}
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Notifies that the document was added. Provides added document as an
	 * argument.
	 * 
	 * @param model
	 *        document that was just added
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Notifies that the document was removed. Provides removed document as an
	 * argument.
	 * 
	 * @param model
	 *        document that was just removed
	 */
	void documentRemoved(SingleDocumentModel model);
	
	/**
	 * Notifies that the user wants to close specific document. Provides that
	 * document as an argument.
	 * 
	 * @param model
	 *        document that the user wants to close
	 */
	void wantCLoseDocument(SingleDocumentModel model);
	
}
