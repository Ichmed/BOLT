package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import util.FileUtilities;
import util.JSuggestField;
import util.SpringUtilities;
import entity.EntityBuilder;
import entity.util.EntityIO;

/**
 * Entity Editor GUI
 * 
 * @author Dakror
 * 
 */
public class EntityEditor extends JFrame implements TreeSelectionListener
{
	private static final long serialVersionUID = 1L;

	private class EntityFile
	{
		File f;
		EntityBuilder b;

		EntityFile(File f, EntityBuilder b)
		{
			this.f = f;
			this.b = b;
		}

		@Override
		public boolean equals(Object o)
		{
			if (!(o instanceof EntityFile)) return false;
			return f.equals(((EntityFile) o).f);
		}
	}

	File entListFile;
	ArrayList<EntityFile> entityFiles;

	boolean changes;

	// -- toolbar -- //
	JToolBar toolBar;
	JButton create, open, save, saveAll, remove;

	// -- components -- //
	JPanel uiPanel;
	JScrollPane treePanel;
	JTree tree;
	JSuggestField parent;
	JTextField name, fullName, klass, model, collModel;
	JComboBox<String> physType, collType;
	JCheckBox invis, grav;
	JButton browse, apply;
	JTable customVals, functions, triggers;
	JTabbedPane tabs;

	public EntityEditor()
	{
		super("BOLT Entity Editor");
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		initComponents();

		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void initComponents()
	{
		entityFiles = new ArrayList<>();

		// -- menu -- //

		// JMenuBar menu = new JMenuBar();
		// JMenu entlist = new JMenu("File");
		// JMenuItem newFile = new JMenuItem(new AbstractAction("New")
		// {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void actionPerformed(ActionEvent e)
		// {
		// newEntityList();
		// }
		// });
		// newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		// entlist.add(newFile);
		// JMenuItem openFile = new JMenuItem(new AbstractAction("Open...")
		// {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void actionPerformed(ActionEvent e)
		// {
		// openEntityList();
		// }
		// });
		// openFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		// entlist.add(openFile);
		//
		// menu.add(entlist);
		//
		// setJMenuBar(menu);

		JPanel contentPanel = new JPanel(new BorderLayout());

		// -- toolbar -- //
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.add(createToolBarButton("New EntityList", "newprj_wiz", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				newEntityList();
			}
		}));
		toolBar.add(createToolBarButton("Open EntityList", "prj_obj", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				openEntityList();
			}
		}));
		toolBar.addSeparator();

		create = createToolBarButton("New Entity", "new_con", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				newEntity();
			}
		});
		create.setEnabled(false);
		toolBar.add(create);
		open = createToolBarButton("Open Entity", "fldr_obj", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				openEntity();
			}
		});
		open.setEnabled(false);
		toolBar.add(open);
		save = createToolBarButton("Save Entity", "save_edit", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveEntity(tree.getSelectionRows()[0] - 1);
				reselect();
			}
		});
		save.setEnabled(false);
		toolBar.add(save);
		saveAll = createToolBarButton("Save All Entities", "saveall_edit", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (int i = 0; i < entityFiles.size(); i++)
				{
					saveEntity(i);
				}
			}
		});
		saveAll.setEnabled(false);
		toolBar.add(saveAll);
		remove = createToolBarButton("Unlink Entity", "remove_from_buildpath", new AbstractAction()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int sel = tree.getSelectionRows()[0] - 1;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();

				if (node.getUserObject().toString().startsWith("*"))
				{
					int r = JOptionPane.showConfirmDialog(EntityEditor.this, "\"" + node.getUserObject().toString().substring(1) + "\" has been modified. Save changes?", "Save Resource", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (r == JOptionPane.YES_OPTION) saveEntity(sel);
					else if (r == JOptionPane.CANCEL_OPTION) return;
				}

				DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
				dtm.removeNodeFromParent(node);
				entityFiles.remove(sel);
				dtm.reload();
				tree.expandRow(0);
				tree.setSelectionRow(0);

				saveEntityList();
				refresh();
			}
		});
		remove.setEnabled(false);
		toolBar.add(remove);

		contentPanel.add(toolBar, BorderLayout.PAGE_START);

		// -- components -- //

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		treePanel = new JScrollPane(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treePanel.setPreferredSize(new Dimension(200, 0));
		panel.add(treePanel);
		tree = new JTree(new DefaultMutableTreeNode("EntityList"));
		tree.setModel(null);
		tree.setEnabled(false);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.setExpandsSelectedPaths(true);

		treePanel.setViewportView(tree);

		uiPanel = new JPanel(new FlowLayout());
		uiPanel.setPreferredSize(new Dimension(600, 600));
		panel.add(uiPanel);

		contentPanel.add(panel, BorderLayout.PAGE_END);

		setContentPane(contentPanel);
		pack();
	}

	private void reset()
	{
		tree.setEnabled(true);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("EntityList");
		tree.setModel(new DefaultTreeModel(root));

		entListFile = null;
		entityFiles.clear();
	}

	private void refresh()
	{
		revalidate();
		repaint();
		treePanel.revalidate();

		// -- toolbar -- //
		create.setEnabled(tree.getRowCount() > 0);
		open.setEnabled(tree.getRowCount() > 0);
		save.setEnabled(tree.getSelectionRows().length > 0 && tree.getSelectionRows()[0] > 0 && ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).getUserObject().toString().startsWith("*"));
		remove.setEnabled(tree.getSelectionRows().length > 0 && tree.getSelectionRows()[0] > 0);
		saveAll.setEnabled(changes);
	}

	private void checkChanged()
	{
		try
		{
			changes = false;

			for (int i = 0; i < entityFiles.size(); i++)
			{
				EntityFile f = entityFiles.get(i);

				boolean equals = f.b.equals(EntityIO.loadEntityFile(f.f));
				if (!equals) changes = true;

				DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) dtm.getChild(dtm.getRoot(), i);

				node.setUserObject(((equals) ? "" : "*") + f.b.name);
				dtm.reload(node);
			}

			refresh();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void newEntityList()
	{
		reset();

		refresh();
	}

	private void openEntityList()
	{
		File f = Editor.getDefaultJFileChooser(true, this, Editor.FILE_FILTER_ENTLIST);
		if (f == null) return;

		try
		{
			reset();
			entListFile = f;

			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				File file = new File(FileUtilities.getJarFile().getParentFile(), line);
				addEntity(file);
			}
			br.close();

			refresh();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(EntityEditor.this, "Could not open file: \"" + entListFile.getPath() + "\"!", "Error!", JOptionPane.ERROR_MESSAGE);

			reset();
		}
	}

	private void addEntity(File file)
	{
		try
		{
			EntityFile f = new EntityFile(file, EntityIO.loadEntityFile(file));

			if (entityFiles.indexOf(f) > -1)
			{
				JOptionPane.showMessageDialog(EntityEditor.this, "This Entity is already in this EntityList!", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			entityFiles.add(f);
			((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(f.f.getName().replace(".entity", "")), (DefaultMutableTreeNode) tree.getModel().getRoot(), ((DefaultMutableTreeNode) tree.getModel().getRoot()).getChildCount());
			tree.expandRow(0);

			saveEntityList();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void newEntity()
	{
		File f = Editor.getDefaultJFileChooser(false, this, Editor.FILE_FILTER_ENTITY);
		if (f == null) return;

		f = new File(f.getPath().replace(".entity", "") + ".entity");

		if (entityFiles.contains(new EntityFile(f, null)))
		{
			JOptionPane.showMessageDialog(EntityEditor.this, "This file already exists in this EntityList!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (f.exists())
		{
			int r = JOptionPane.showConfirmDialog(EntityEditor.this, "This file already exists! By creating a new entity in that file, it's old content will be lost!", "Warning!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (r == JOptionPane.CANCEL_OPTION) return;
		}
		else
		{
			try
			{
				boolean created = f.createNewFile();
				if (!created) JOptionPane.showMessageDialog(EntityEditor.this, "The entered filepath is invalid!", "Error!", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e1)
			{
				JOptionPane.showMessageDialog(EntityEditor.this, "The entered filepath is invalid!", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}

		EntityBuilder b = new EntityBuilder();
		b.name = b.fullName = f.getName().replace(".entity", "");
		entityFiles.add(new EntityFile(f, b));
		((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode("*" + f.getName().replace(".entity", "")), (DefaultMutableTreeNode) tree.getModel().getRoot(), ((DefaultMutableTreeNode) tree.getModel().getRoot()).getChildCount());
		tree.expandRow(0);
		saveEntityList();

		saveEntity(entityFiles.size() - 1);

		refresh();
		checkChanged();
	}

	private void openEntity()
	{
		File f = Editor.getDefaultJFileChooser(true, this, Editor.FILE_FILTER_ENTITY);
		if (f == null) return;

		addEntity(f);
	}

	private void saveEntity(int index)
	{
		refresh();
		checkChanged();

		EntityFile f = entityFiles.get(index);

		if (f.b.name.length() > 0 && !f.b.name.equals(f.f.getName().replace(".entity", "")))
		{
			f.f.renameTo(new File(f.f.getParentFile(), f.b.name + ".entity"));
			f.f = new File(f.f.getParentFile(), f.b.name + ".entity");

			saveEntityList();
		}

		EntityIO.saveEntityFile(f.b, getParent(f.b), f.f);

		refresh();
		checkChanged();
	}

	private void saveEntityList()
	{
		if (entListFile == null)
		{
			File f = Editor.getDefaultJFileChooser(false, this, Editor.FILE_FILTER_ENTLIST);
			if (f == null) return;

			entListFile = f;
		}

		String s = "";
		for (EntityFile f : entityFiles)
		{
			s += FileUtilities.getRelativePath(FileUtilities.getJarFile().getParentFile(), f.f).replace("\\", "/") + "\n";
		}
		FileUtilities.setFileContent(entListFile, s);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		// uiPanel.setLayout(new FlowLayout());
		// uiPanel.removeAll();
		// refresh();

		if (tree.getSelectionRows().length == 0) return;

		if (tree.getSelectionRows()[0] > 0) // entity
		showEntityUI();

		refresh();
	}

	public void showEntityUI()
	{
		refresh();

		uiPanel.setLayout(null);

		if (tabs == null)
		{
			tabs = new JTabbedPane();
			tabs.setBounds(0, -1, uiPanel.getWidth() + 3, uiPanel.getHeight() - 28);
			tabs.setPreferredSize(uiPanel.getPreferredSize());
		}
		tabs.removeAll();

		if (tree.getSelectionRows()[0] - 1 < 0) return;

		final EntityBuilder b = entityFiles.get(tree.getSelectionRows()[0] - 1).b;
		final EntityBuilder p = getParent(b);
		b.loadParent(p);

		// -- first tab -- //

		JPanel panel = new JPanel(new SpringLayout());
		panel.setPreferredSize(new Dimension(uiPanel.getWidth(), 525));
		JLabel label = new JLabel("Parent:");
		label.setPreferredSize(new Dimension(uiPanel.getWidth() / 2 - 20, 22));
		panel.add(label);

		Vector<String> suggest = new Vector<String>();
		for (int i = 0; i < entityFiles.size(); i++)
		{
			if (i == tree.getSelectionRows()[0] - 1) continue;
			suggest.add(entityFiles.get(i).b.name);
		}
		parent = new JSuggestField(this, suggest);
		parent.setText(b.parent);
		panel.add(parent);

		panel.add(new JLabel("Name:"));
		name = new JTextField(15);
		name.setText(b.name);
		panel.add(name);

		panel.add(new JLabel("Full Name:"));
		fullName = new JTextField(15);
		fullName.setText(b.fullName);
		panel.add(fullName);

		panel.add(new JLabel("Physical Type:"));
		physType = new JComboBox<>(new String[] { "none", "physical", "static" });
		physType.setSelectedIndex(b.physicsType);
		panel.add(physType);

		panel.add(new JLabel("Collision Type:"));
		collType = new JComboBox<>(new String[] { "solid", "gameSolid", "nonSolid" });
		collType.setSelectedIndex(b.collisionType);
		panel.add(collType);

		panel.add(new JLabel("Invisible:"));
		invis = new JCheckBox();
		invis.setSelected(b.invisible);
		panel.add(invis);

		panel.add(new JLabel("Gravity:"));
		grav = new JCheckBox();
		grav.setSelected(b.gravity);
		panel.add(grav);

		panel.add(new JLabel("Class:"));
		klass = new JTextField(15);
		klass.setText(b.classPath);
		panel.add(klass);

		panel.add(new JLabel("Model:"));
		JPanel panel3 = new JPanel();
		model = new JTextField(15);
		model.setText(b.model);
		panel3.add(model);
		panel3.add(new JButton(new AbstractAction("Browse...")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				File f = Editor.getDefaultJFileChooser(true, EntityEditor.this, Editor.FILE_FILTER_OBJECT);
				if (f == null) return;

				model.setText(FileUtilities.getRelativePath(FileUtilities.getJarFile().getParentFile(), f).replace("\\", "/"));
			}
		}));
		panel.add(panel3);

		panel.add(new JLabel("Collision Model:"));
		JPanel panel4 = new JPanel();
		collModel = new JTextField(15);
		collModel.setText(b.collisionModel);
		panel4.add(collModel);
		panel4.add(new JButton(new AbstractAction("Browse...")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				File f = Editor.getDefaultJFileChooser(true, EntityEditor.this, Editor.FILE_FILTER_OBJECT);
				if (f == null) return;

				model.setText(FileUtilities.getRelativePath(FileUtilities.getJarFile().getParentFile(), f).replace("\\", "/"));
			}
		}));
		panel.add(panel4);

		panel.add(new JLabel("Custom Values:"));
		JPanel panel5 = new JPanel(new BorderLayout());
		String[][] data = new String[b.customValues.size()][];
		ArrayList<String> keySet = new ArrayList<>(b.customValues.keySet());
		Collections.sort(keySet);
		for (int i = 0; i < b.customValues.size(); i++)
		{
			String key = keySet.get(i);
			data[i] = new String[] { b.customValues.get(key).getClass().getSimpleName(), key, b.customValues.get(key).toString() };
		}
		customVals = new JTable(new DefaultTableModel(data, new String[] { "Type", "Name", "Value" }));
		customVals.putClientProperty("terminateEditOnFocusLost", true);
		JScrollPane jsp = new JScrollPane(customVals, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(customVals.getWidth(), 150));
		customVals.setFillsViewportHeight(true);
		customVals.setRowHeight(22);
		customVals.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customVals.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (customVals.getSelectedRow() > -1) browse.setEnabled(customVals.getValueAt(customVals.getSelectedRow(), 0).toString().equals("File"));
			}
		});

		JComboBox<String> type = new JComboBox<String>(new String[] { "Byte", "Integer", "Double", "Boolean", "String", "File" });
		type.setSelectedIndex(4);
		customVals.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(type));
		jsp.setPreferredSize(new Dimension(customVals.getWidth(), 150));
		panel5.add(jsp, BorderLayout.NORTH);
		browse = new JButton(new AbstractAction("Browse...")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				File f = Editor.getDefaultJFileChooser(true, EntityEditor.this, null);
				if (f == null) return;

				customVals.setValueAt(FileUtilities.getRelativePath(FileUtilities.getJarFile().getParentFile(), f).replace("\\", "/"), customVals.getSelectedRow(), 2);
			}
		});
		browse.setEnabled(false);
		panel5.add(browse, BorderLayout.PAGE_END);
		panel5.add(new JButton(new AbstractAction("Remove")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DefaultTableModel model = (DefaultTableModel) customVals.getModel();
				if (customVals.getSelectedRow() == -1) return;
				model.removeRow(customVals.getSelectedRow());
			}
		}), BorderLayout.WEST);
		panel5.add(new JButton(new AbstractAction("New")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DefaultTableModel model = (DefaultTableModel) customVals.getModel();
				model.addRow(new Object[] { "String", "", "" });
			}
		}), BorderLayout.EAST);
		panel.add(panel5);

		SpringUtilities.makeCompactGrid(panel, 11, 2, 6, 6, 6, 6);

		JPanel wrap = new JPanel();
		wrap.add(panel);

		tabs.addTab("Basic", wrap);

		// -- second tab -- //
		panel = new JPanel(new SpringLayout());
		panel.setPreferredSize(new Dimension(uiPanel.getWidth(), 370));

		label = new JLabel("Triggers:");
		label.setPreferredSize(new Dimension(uiPanel.getWidth() / 2 - 20, 22));
		panel.add(label);

		String[][] eventData = new String[b.triggers.size() + b.nonInheritedTriggers.size()][];

		for (int i = 0; i < b.triggers.size(); i++)
		{
			eventData[i] = new String[] { "false", ((p != null && p.triggers.contains(b.triggers.get(i))) ? "parent:" : "") + b.triggers.get(i) };
		}
		for (int i = b.triggers.size(); i < eventData.length; i++)
		{
			eventData[i] = new String[] { "true", "parent:" + b.nonInheritedTriggers.get(i - b.triggers.size()) };
		}

		triggers = new JTable(new DefaultTableModel(eventData, new String[] { "nonInherit", "Name" }))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				if (!triggers.getValueAt(row, 1).toString().contains("parent:") && column == 0) return false; // disable nonInherit of not from parent
				if (triggers.getValueAt(row, 1).toString().contains("parent:") && column > 0) return false;

				return true;
			}
		};
		triggers.putClientProperty("terminateEditOnFocusLost", true);
		triggers.setRowHeight(22);
		triggers.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		triggers.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jsp = new JScrollPane(triggers, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		triggers.setFillsViewportHeight(true);
		jsp.setPreferredSize(new Dimension(0, 150));
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.add(jsp, BorderLayout.NORTH);
		panel2.add(new JButton(new AbstractAction("Remove")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (triggers.getSelectedRow() > -1) ((DefaultTableModel) triggers.getModel()).removeRow(triggers.getSelectedRow());
			}
		}), BorderLayout.WEST);
		panel2.add(new JButton(new AbstractAction("Add")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DefaultTableModel) triggers.getModel()).addRow(new String[] { "false", "" });
			}
		}), BorderLayout.EAST);
		panel.add(panel2);

		panel.add(new JLabel("Functions:"));

		String[][] functionData = new String[b.functions.size() + b.nonInheritedFunctions.size()][];
		for (int i = 0; i < b.functions.size(); i++)
		{
			String f = b.functions.get(i);
			functionData[i] = new String[] { "false", ((p != null && p.functions.contains(f)) ? "parent:" : "") + f.substring(0, f.indexOf("(")).trim(), f.substring(f.indexOf("(") + 1, f.indexOf(")")).trim() };
		}
		for (int i = b.functions.size(); i < functionData.length; i++)
		{
			functionData[i] = new String[] { "true", "parent:" + b.nonInheritedFunctions.get(i - b.functions.size()).substring(0, b.nonInheritedFunctions.get(i - b.functions.size()).indexOf("(")).trim(), b.nonInheritedFunctions.get(i - b.functions.size()).substring(b.nonInheritedFunctions.get(i - b.functions.size()).indexOf("(") + 1, b.nonInheritedFunctions.get(i - b.functions.size()).indexOf(")")).trim() };
		}
		functions = new JTable(new DefaultTableModel(functionData, new String[] { "nonInherit", "Name", "Parameters" }))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				if (!functions.getValueAt(row, 1).toString().contains("parent:") && column == 0) return false; // disable nonInherit of not from parent
				if (functions.getValueAt(row, 1).toString().contains("parent:") && column > 0) return false;

				return true;
			}
		};
		functions.putClientProperty("terminateEditOnFocusLost", true);
		functions.setRowHeight(22);
		functions.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		functions.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jsp = new JScrollPane(functions, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		functions.setFillsViewportHeight(true);
		jsp.setPreferredSize(new Dimension(0, 150));
		panel2 = new JPanel(new BorderLayout());
		panel2.add(jsp, BorderLayout.NORTH);
		panel2.add(new JButton(new AbstractAction("Remove")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (functions.getSelectedRow() > -1) ((DefaultTableModel) functions.getModel()).removeRow(functions.getSelectedRow());
			}
		}), BorderLayout.WEST);
		panel2.add(new JButton(new AbstractAction("Add")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DefaultTableModel) functions.getModel()).addRow(new String[] { "false", "", "" });
			}
		}), BorderLayout.EAST);
		panel.add(panel2);

		SpringUtilities.makeCompactGrid(panel, 2, 2, 6, 6, 6, 6);

		wrap = new JPanel();
		wrap.add(panel);

		tabs.addTab("Events", wrap);

		uiPanel.add(tabs);

		apply = new JButton(new AbstractAction("Apply")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean valid = true;
				String message = "";

				EntityBuilder builder = new EntityBuilder();

				builder.parent = parent.getText();

				if (valid && name.getText().length() == 0)
				{
					valid = false;
					message = "Please enter a name!";
				}
				else builder.name = name.getText();

				if (fullName.getText().length() == 0) builder.fullName = name.getText();
				else builder.fullName = fullName.getText();

				builder.physicsType = physType.getSelectedIndex();
				builder.collisionType = collType.getSelectedIndex();
				builder.invisible = invis.isSelected();
				builder.gravity = grav.isSelected();
				builder.model = model.getText();

				builder.collisionModel = collModel.getText();

				builder.classPath = klass.getText();

				for (int i = 0; i < customVals.getRowCount(); i++)
				{
					String type = customVals.getValueAt(i, 0).toString();
					String name = customVals.getValueAt(i, 1).toString();
					String value = customVals.getValueAt(i, 2).toString();

					if (type.length() == 0 || name.length() == 0 || value.length() == 0)
					{
						valid = false;
						message = "Please fill all fields for customValue #" + i + "!";
						break;
					}

					if (type.equals("Integer")) builder.customValues.put(name, Integer.parseInt(value));
					else if (type.equals("Double")) builder.customValues.put(name, Double.parseDouble(value));
					else if (type.equals("Byte")) builder.customValues.put(name, Byte.parseByte(value));
					else if (type.equals("Boolean")) builder.customValues.put(name, Boolean.parseBoolean(value));
					else if (type.equals("File")) builder.customValues.put(name, value);
				}

				for (int i = 0; i < triggers.getRowCount(); i++)
				{
					Boolean nonInherit = Boolean.valueOf(triggers.getValueAt(i, 0).toString());
					String name = triggers.getValueAt(i, 1).toString();
					if (name.length() == 0)
					{
						valid = false;
						message = "Please enter a name for trigger #" + i + "!";
						break;
					}

					if (name.startsWith("parent:") && nonInherit) builder.nonInheritedTriggers.add(name.replace("parent:", ""));
					else builder.triggers.add(name.replace("parent:", ""));
				}

				for (int i = 0; i < functions.getRowCount(); i++)
				{
					Boolean nonInherit = Boolean.valueOf(functions.getValueAt(i, 0).toString());
					String name = functions.getValueAt(i, 1).toString();
					String params = functions.getValueAt(i, 2).toString();
					if (name.length() == 0)
					{
						valid = false;
						message = "Please enter a name for function #" + i + "!";
						break;
					}

					if (name.startsWith("parent:") && nonInherit) builder.nonInheritedFunctions.add(name.replace("parent:", "") + "(" + params + ")");
					else builder.functions.add(name.replace("parent:", "") + "(" + params + ")");
				}

				if (!valid)
				{
					JOptionPane.showMessageDialog(EntityEditor.this, message, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				int index = tree.getSelectionRows()[0] - 1;

				entityFiles.set(index, new EntityFile(entityFiles.get(index).f, builder));

				refresh();
				checkChanged();

				reselect();
			}
		});
		apply.setBounds(0, uiPanel.getHeight() - 27, uiPanel.getWidth(), 25);
		uiPanel.add(apply);

		refresh();
	}

	private void reselect()
	{
		int tab = tabs.getSelectedIndex();
		int sel = tree.getSelectionRows()[0];
		tree.setSelectionRow(0);
		tree.setSelectionRow(sel);
		tabs.setSelectedIndex(tab);
	}

	private JButton createToolBarButton(String tooltip, String icon, Action action)
	{
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(24, 24));
		button.setIcon(Editor.getIcon(icon));
		action.putValue(Action.SMALL_ICON, Editor.getIcon(icon));
		action.putValue(Action.SHORT_DESCRIPTION, tooltip);
		button.setAction(action);
		button.setFocusPainted(false);

		return button;
	}

	private EntityBuilder getParent(EntityBuilder b)
	{
		if (b.parent == null) return null;

		for (EntityFile key : entityFiles)
		{
			if (key.b.name.equals(b.parent)) return key.b;
		}

		return null;
	}
}
