package hr.fer.zemris.java.hw17.jvdraw.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.model.Tool;

/**
 * Represents main frame of the application. It initializes all proper components
 * and places then at proper positions.
 * 
 * @author Filip Husnjak
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 915748500838568583L;
	
	/** Drawing model of this frame */
	private DrawingModel model = new DrawingModelImpl();
	
	/** Foreground color provider */
	private JColorArea fgColor = new JColorArea(Color.RED);
	
	/** Background color provider */
	private JColorArea bgColor = new JColorArea(Color.WHITE);
	
	/** Drawing canvas used by this frame */
	private JDrawingCanvas canvas = new JDrawingCanvas(model, fgColor, bgColor);

	/** Line tool used by this frame */
	private Tool lineTool = canvas.getLineTool();
	
	/** Circle tool used by this frame */
	private Tool circleTool = canvas.getCircleTool();
	
	/** Filles circle tool used by this frame */
	private Tool filledCircleTool = canvas.getFilledCircleTool();
	
	/** Represents currently selected object in JList */
	private GeometricalObject currentObject;
	
	/**
	 * Constructs new {@link MainFrame} and initializes it with proper components.
	 */
	public MainFrame() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		initGui();
		pack();
	}
	
	/**
	 * Initializes GUI of this frame.
	 */
	private void initGui() {
		initActions();
		createMenubar();
		addWindowListener(windowClosing);
		setLayout(new BorderLayout());
		add(createToolbar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(new JScrollPane(createJList()), BorderLayout.EAST);
		add(createStatusBar(), BorderLayout.SOUTH);
	}
	
	/**
	 * Initializes actions.
	 */
	private void initActions() {
		saveAction.putValue(Action.NAME, "Save");
		saveAsAction.putValue(Action.NAME, "Save As");
		openAction.putValue(Action.NAME, "Open");
		exportAction.putValue(Action.NAME, "Export");
		exitAction.putValue(Action.NAME, "Exit");
	}

	/**
	 * Creates and returns JList.
	 * 
	 * @return new JList with proper listeners initialized.
	 */
	private JList<GeometricalObject> createJList() {
		JList<GeometricalObject> list = new JList<>(new DrawingObjectListModel(model));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) return;
				GeometricalObjectEditor editor = list.getSelectedValue().createGeometricalObjectEditor();
                if (JOptionPane.showConfirmDialog(MainFrame.this, editor, "Edit", JOptionPane.YES_NO_CANCEL_OPTION)
                        == JOptionPane.OK_OPTION) {
                    try {
                        editor.checkEditing();
                        editor.acceptEditing();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage());
                    }
                }
			}
		});
		list.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				currentObject = list.getSelectedValue();
			}
			@Override
			public void focusLost(FocusEvent e) {
				currentObject = null;
			}
		});
		list.addListSelectionListener(e -> {
			currentObject = list.getSelectedValue();
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (currentObject == null) return;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					model.remove(currentObject);
					break;
				case KeyEvent.VK_ADD:
					model.changeOrder(currentObject, 1);
					break;
				case KeyEvent.VK_SUBTRACT:
					model.changeOrder(currentObject, -1);
					break;
				}
			}
		});
		return list;
	}
	
	/**
	 * Creates menu bar on this frame.
	 */
	private void createMenubar() {
		JMenuBar mb = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem openFile = new JMenuItem(openAction);
		JMenuItem saveFile = new JMenuItem(saveAction);
		JMenuItem saveAsFile = new JMenuItem(saveAsAction);
		JMenuItem exportFile = new JMenuItem(exportAction);
		JMenuItem exit = new JMenuItem(exitAction);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveAsFile);
		file.addSeparator();
		file.add(exportFile);
		file.addSeparator();
		file.add(exit);
		
		mb.add(file);

		setJMenuBar(mb);
	}
	
	/**
	 * Creates and returns status bar.
	 * 
	 * @return new status bar
	 */
	private JToolBar createStatusBar() {
		JToolBar statusbar = new JToolBar();
		statusbar.setMargin(new Insets(0, 10, 0, 10));
		statusbar.add(new JBottomLabel(fgColor, bgColor));
		return statusbar;
	}
	
	/**
	 * Creates and returns toolbar
	 * 
	 * @return new toolbar
	 */
	private JToolBar createToolbar() {
		JToolBar toolbar = new JToolBar();
		JToggleButton line = new JToggleButton("Line", true);
		JToggleButton circle = new JToggleButton("Circle");
		JToggleButton filledCircle = new JToggleButton("Filled Circle");
		ButtonGroup group = new ButtonGroup();
		group.add(line);
		group.add(circle);
		group.add(filledCircle);
		line.addActionListener(e -> {
			if (line.isSelected()) {
				canvas.setCurrentState(lineTool);
			}
		});
		circle.addActionListener(e -> {
			if (circle.isSelected()) {
				canvas.setCurrentState(circleTool);
			}
		});
		filledCircle.addActionListener(e -> {
			if (filledCircle.isSelected()) {
				canvas.setCurrentState(filledCircleTool);
			}
		});
		toolbar.addSeparator();
		toolbar.add(fgColor);
		toolbar.addSeparator();
		toolbar.add(bgColor);
		toolbar.addSeparator();
		toolbar.addSeparator();
		toolbar.add(line);
		toolbar.add(circle);
		toolbar.add(filledCircle);
		return toolbar;
	}
	
	/**
	 * Observes the window closing event and before disposing checks whether
	 * there are modified documents and prompts user with appropriate message.
	 */
	private final WindowListener windowClosing = new WindowAdapter() {
    	@Override
    	public void windowClosing(WindowEvent e) {
			if (model.isModified()) {
				int res = JOptionPane.showOptionDialog(
						MainFrame.this,
						"Do you want to save unsaved work?",
						"Exit",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null, null, null);
				if (res == JOptionPane.CANCEL_OPTION) {
					return;
				} else if (res == JOptionPane.YES_OPTION) {
					if (!saveModel(model.getCurrentPath() == null)) {
						return;
					}
				}
			}
    		dispose();
    	}
	};

	/** Action used to save file. */
	private final Action saveAction = new AbstractAction() {
		private static final long serialVersionUID = -7410073389847201594L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveModel(model.getCurrentPath() == null);
		}
	};
	
	/** Action used to save file as. */
	private final Action saveAsAction = new AbstractAction() {
		private static final long serialVersionUID = 2016841804055034190L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveModel(true);
		}
	};
	
	/** Action used to open file. */
	private final Action openAction = new AbstractAction() {
		private static final long serialVersionUID = -4094173716671916958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			loadModel();
		}
	};
	
	/** Action used to export an image. */
	private final Action exportAction = new AbstractAction() {
		private static final long serialVersionUID = -4094173716671916958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportModel();
		}
	};
	
	/** Action used to export an image. */
	private final Action exitAction = new AbstractAction() {
		private static final long serialVersionUID = -4094173716671916958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
		}
	};
	
	/**
	 * Exports the model of this frame as an image.
	 */
	private void exportModel() {
		Path path = selectPath(true, "jpg, png, gif", "jpg", "png", "gif");
		if (path == null) return;
		try {
			model.export(path);
			JOptionPane.showMessageDialog(this, "Image exported successfully!");
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Loads the model with the given path provided by the user.
	 */
	private void loadModel() {
		Path path = selectPath(false, "jvd", "jvd");
		if (path == null) return;
		try {
			model.load(path);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	/**
	 * Saves model of this frame with the given path.
	 * 
	 * @param askForPath
	 *        flag which determines whether new path is to be selected
	 */
	private boolean saveModel(boolean askForPath) {
		Path path = model.getCurrentPath();
		if (askForPath) {
			path = selectPath(true, "jvd", "jvd");
		}
		try {
			if (path == null) return false;
			model.save(path);
			return true;
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Asks user to select the path and returns it.
	 * 
	 * @param save
	 *        flag which determines whether path is used for saving or opening docs
	 * @return selected path
	 */
	private Path selectPath(boolean save, String desc, String ... extensions) {
		if (extensions == null) return null;
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter(desc, extensions));
		if ((save ? chooser.showSaveDialog(this) : 
			chooser.showOpenDialog(this)) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return chooser.getSelectedFile().toPath();
	}
		
}
