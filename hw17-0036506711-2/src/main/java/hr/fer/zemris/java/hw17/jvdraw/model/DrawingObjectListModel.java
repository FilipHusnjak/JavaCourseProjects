package hr.fer.zemris.java.hw17.jvdraw.model;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;

/**
 * Implementation of {@link ListModel} which is adaptor for the Drawing model
 * given through constructor.
 * 
 * @author Filip Husnjak
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> 
									implements DrawingModelListener {

	private static final long serialVersionUID = 7182565663628435094L;

	/** Drawing model used by this ListModel */
	private DrawingModel model;

	/**
	 * Constructs new {@link DrawingObjectListModel} with the given {@link DrawingModel}.
	 * 
	 * @param model
	 *        {@link DrawingModel} used by this ListModel
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(source, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(source, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(source, index0, index1);
	}

}
