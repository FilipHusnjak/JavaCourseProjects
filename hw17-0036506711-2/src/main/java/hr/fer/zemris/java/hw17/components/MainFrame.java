package hr.fer.zemris.java.hw17.components;

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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw17.model.DrawingModel;
import hr.fer.zemris.java.hw17.model.DrawingModelImpl;
import hr.fer.zemris.java.hw17.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.model.GeometricalObject;
import hr.fer.zemris.java.hw17.model.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.model.Tool;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 915748500838568583L;
	
	private DrawingModel model = new DrawingModelImpl();
	
	private JColorArea fgColor = new JColorArea(Color.RED);
	
	private JColorArea bgColor = new JColorArea(Color.WHITE);
	
	private JDrawingCanvas canvas = new JDrawingCanvas(model, fgColor, bgColor);

	private Tool lineTool = canvas.getLineTool();
	
	private Tool circleTool = canvas.getCircleTool();
	
	private Tool filledCircleTool = canvas.getFilledCircleTool();
	
	private GeometricalObject currentObject;
	
	public MainFrame() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGui();
		pack();
	}
	
	
	private void initGui() {
		initActions();
		createMenubar();
		setLayout(new BorderLayout());
		add(createToolbar(), BorderLayout.NORTH);
		add(canvas, BorderLayout.CENTER);
		add(new JScrollPane(createJList()), BorderLayout.EAST);
		add(createStatusBar(), BorderLayout.SOUTH);
	}
	
	private void initActions() {
		saveAction.putValue(Action.NAME, "Save");
		saveAsAction.putValue(Action.NAME, "Save As");
		openAction.putValue(Action.NAME, "Open");
		exportAction.putValue(Action.NAME, "Export");
	}


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
	
	private void createMenubar() {
		JMenuBar mb = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem openFile = new JMenuItem(openAction);
		JMenuItem saveFile = new JMenuItem(saveAction);
		JMenuItem saveAsFile = new JMenuItem(saveAsAction);
		JMenuItem exportFile = new JMenuItem(exportAction);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveAsFile);
		file.addSeparator();
		file.add(exportFile);
		
		mb.add(file);

		setJMenuBar(mb);
	}
	
	private JToolBar createStatusBar() {
		JToolBar statusbar = new JToolBar();
		statusbar.setMargin(new Insets(0, 10, 0, 10));
		statusbar.add(new JBottomLabel(fgColor, bgColor));
		return statusbar;
	}
	
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

	private final Action saveAction = new AbstractAction() {
		private static final long serialVersionUID = -7410073389847201594L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveModel(model.getCurrentPath() == null);
		}
	};
	
	private final Action saveAsAction = new AbstractAction() {
		private static final long serialVersionUID = 2016841804055034190L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveModel(true);
		}
	};
	
	private final Action openAction = new AbstractAction() {
		private static final long serialVersionUID = -4094173716671916958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			loadModel();
		}
	};
	
	private final Action exportAction = new AbstractAction() {
		private static final long serialVersionUID = -4094173716671916958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exportModel();
		}
	};
	
	private void exportModel() {
		Path path = selectPath(true);
		try {
			model.export(path);
			JOptionPane.showMessageDialog(this, "Image exported successfully!");
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void loadModel() {
		Path path = selectPath(false);
		try {
			model.load(path);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void saveModel(boolean askForPath) {
		Path path = model.getCurrentPath();
		if (askForPath) {
			path = selectPath(true);
			if (path == null) return;
		}
		try {
			model.save(path);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	// TODO - filepath needs to have proper extension
	private Path selectPath(boolean save) {
		JFileChooser chooser = new JFileChooser();
		//chooser.setFileFilter(new FileNameExtensionFilter("jvd"));
		if ((save ? chooser.showSaveDialog(this) : 
			chooser.showOpenDialog(this)) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return chooser.getSelectedFile().toPath();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}
		
}
