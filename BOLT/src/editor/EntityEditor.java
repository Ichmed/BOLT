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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
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

	File entListFile;

	HashMap<File, EntityBuilder> entityFiles;

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
		entityFiles = new HashMap<>();

		JMenuBar menu = new JMenuBar();
		JMenu entlist = new JMenu("File");
		JMenuItem newFile = new JMenuItem(new AbstractAction("New")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				newEntityList();
			}
		});
		newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		entlist.add(newFile);
		JMenuItem openFile = new JMenuItem(new AbstractAction("Open...")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				openEntityList();
			}
		});
		openFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		entlist.add(openFile);

		menu.add(entlist);

		setJMenuBar(menu);

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

		setContentPane(panel);
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
	}

	private void checkChanged()
	{
		try
		{
			if (tree.getSelectionRows().length == 0) return;

			int selRow = tree.getSelectionRows()[0];
			File f = new ArrayList<>(entityFiles.keySet()).get(selRow - 1);
			boolean equals = entityFiles.get(new ArrayList<>(entityFiles.keySet()).get(selRow - 1)).equals(EntityIO.loadEntityFile(f));
			System.out.println("me: " + entityFiles.get(new ArrayList<>(entityFiles.keySet()).get(selRow - 1)));
			System.out.println("fl: " + EntityIO.loadEntityFile(f));
			((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).setUserObject(((equals) ? "" : "*") + f.getName().replace(".entity", ""));
			((DefaultTreeModel) tree.getModel()).reload((TreeNode) tree.getSelectionPath().getLastPathComponent());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void newEntityList()
	{
		reset();
	}

	private void openEntityList()
	{

		File f = Editor.getDefaultJFileChooser(true, this, new FileNameExtensionFilter("BOLT EntityList-Files (*.entlist)", "entlist"));
		if (f == null) return;

		entListFile = f;

		try
		{
			reset();

			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				File file = new File(FileUtilities.getJarFile().getParentFile(), line);
				entityFiles.put(file, EntityIO.loadEntityFile(file));
				((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(file.getName().replace(".entity", "")), (DefaultMutableTreeNode) tree.getModel().getRoot(), ((DefaultMutableTreeNode) tree.getModel().getRoot()).getChildCount());
				tree.expandRow(0);
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(EntityEditor.this, "Could not open file: \"" + entListFile.getPath() + "\"!", "Error!", JOptionPane.ERROR_MESSAGE);

			reset();
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		uiPanel.setLayout(new FlowLayout());
		uiPanel.removeAll();
		refresh();

		// final DefaultMutableTreeNode s = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		if (tree.getRowForPath(e.getPath()) == 0) // entityList
		showEntitiesUI();
		else if (tree.getRowForPath(e.getPath()) > 0) // entity
		showEntityUI();

		refresh();
	}

	public void showEntitiesUI()
	{
		final JButton create = new JButton();

		JPanel panel = new JPanel(new SpringLayout());
		panel.add(new JLabel("Filepath:"));
		JPanel panel2 = new JPanel(new FlowLayout());
		final JTextField path = new JTextField(15);
		path.getDocument().addDocumentListener(new DocumentListener()
		{

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				create.setEnabled(path.getText().length() > 0);
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				create.setEnabled(path.getText().length() > 0);
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				create.setEnabled(path.getText().length() > 0);
			}
		});
		panel2.add(path);
		panel2.add(new JButton(new AbstractAction("Browse...")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser jfc = new JFileChooser(FileUtilities.getJarFile().getParentFile());
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				if (path.getText().length() > 0) jfc.setSelectedFile(new File(path.getText()));

				jfc.setFileFilter(new FileNameExtensionFilter("BOLT Entity-Files (*.entity)", "entity"));
				if (jfc.showSaveDialog(EntityEditor.this) == JFileChooser.APPROVE_OPTION)
				{
					if (!FileUtilities.getHardDrive(jfc.getSelectedFile()).equals(FileUtilities.getHardDrive(FileUtilities.getJarFile())))
					{
						JOptionPane.showMessageDialog(EntityEditor.this, "Please choose a file stored on the harddrive \"" + FileUtilities.getHardDrive(FileUtilities.getJarFile()).toString() + "\"!", "Error!", JOptionPane.ERROR_MESSAGE);
						return;
					}

					path.setText(jfc.getSelectedFile().getPath().replace(".entity", "") + ".entity");
				}
			}
		}));
		panel.add(panel2);
		panel.add(new JLabel());

		create.setAction(new AbstractAction("New Entity")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				File f = new File(path.getText());
				if (entityFiles.containsKey(f))
				{
					JOptionPane.showMessageDialog(EntityEditor.this, "This file already exists in this EntityList!", "Error!", JOptionPane.ERROR_MESSAGE);
					path.setText("");
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
						else f.delete();
					}
					catch (IOException e1)
					{
						JOptionPane.showMessageDialog(EntityEditor.this, "The entered filepath is invalid!", "Error!", JOptionPane.ERROR_MESSAGE);
					}
				}
				entityFiles.put(f, new EntityBuilder());
				((DefaultTreeModel) tree.getModel()).insertNodeInto(new DefaultMutableTreeNode(f.getName().replace(".entity", "")), (DefaultMutableTreeNode) tree.getModel().getRoot(), ((DefaultMutableTreeNode) tree.getModel().getRoot()).getChildCount());
				tree.expandRow(0);
				refresh();
			}
		});
		create.setEnabled(false);
		panel.add(create);

		SpringUtilities.makeCompactGrid(panel, 2, 2, 6, 6, 6, 6);

		uiPanel.add(panel);
	}

	public void showEntityUI()
	{
		uiPanel.setLayout(null);

		tabs = new JTabbedPane();
		tabs.setBounds(0, -1, uiPanel.getWidth() + 3, uiPanel.getHeight() - 28);
		tabs.setPreferredSize(uiPanel.getPreferredSize());

		if (tree.getSelectionRows()[0] - 1 < 0) return;

		final EntityBuilder b = entityFiles.get(new ArrayList<File>(entityFiles.keySet()).get(tree.getSelectionRows()[0] - 1));
		EntityBuilder p = getParent(b);

		// -- first tab -- //

		JPanel panel = new JPanel(new SpringLayout());
		panel.setPreferredSize(new Dimension(uiPanel.getWidth(), 525));
		JLabel label = new JLabel("Parent:");
		label.setPreferredSize(new Dimension(uiPanel.getWidth() / 2 - 20, 22));
		panel.add(label);

		Vector<String> suggest = new Vector<String>();
		ArrayList<File> keys = new ArrayList<File>(entityFiles.keySet());
		for (int i = 0; i < entityFiles.size(); i++)
		{
			if (i == tree.getSelectionRows()[0] - 1) continue;
			String name = keys.get(i).getName().replace(".entity", "");
			suggest.add(name);
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
				File f = Editor.getDefaultJFileChooser(true, EntityEditor.this, new FileNameExtensionFilter("Wavefront geometry file (*.obj)", "obj"));
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
				File f = Editor.getDefaultJFileChooser(true, EntityEditor.this, new FileNameExtensionFilter("Wavefront Geometry File (*.obj)", "obj"));
				if (f == null) return;

				model.setText(FileUtilities.getRelativePath(FileUtilities.getJarFile().getParentFile(), f).replace("\\", "/"));
			}
		}));
		panel.add(panel4);

		panel.add(new JLabel("Custom Values:"));
		JPanel panel5 = new JPanel(new BorderLayout());
		String[][] data = new String[b.customValues.size()][];
		ArrayList<String> keySet = new ArrayList<>(b.customValues.keySet());
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
			eventData[i] = new String[] { "true", "parent:" + b.triggers.get(i) };
		}
		triggers = new JTable(new DefaultTableModel(eventData, new String[] { "nonInherit", "Name" }))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				if (!triggers.getValueAt(row, 1).toString().contains("parent:") && column == 0) return false; // disable nonInherit of not from parent

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
			functionData[i] = new String[] { "true", "parent:" + b.functions.get(i).substring(0, b.functions.get(i).indexOf("(")).trim(), b.functions.get(i).substring(b.functions.get(i).indexOf("(") + 1, b.functions.get(i).indexOf(")")).trim() };
		}
		functions = new JTable(new DefaultTableModel(functionData, new String[] { "nonInherit", "Name", "Parameters" }))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)
			{
				if (!functions.getValueAt(row, 1).toString().contains("parent:") && column == 0) return false; // disable nonInherit of not from parent

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

		JButton save = new JButton(new AbstractAction("Save")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				apply.doClick();

				File key = new ArrayList<File>(entityFiles.keySet()).get(tree.getSelectionRows()[0] - 1);

				EntityIO.saveEntityFile(entityFiles.get(key), key);

				checkChanged();
				refresh();
			}
		});
		save.setBounds(0, uiPanel.getHeight() - 27, uiPanel.getWidth() / 2, 25);
		uiPanel.add(save);

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
					Boolean inherit = Boolean.valueOf(triggers.getValueAt(i, 0).toString());
					String name = triggers.getValueAt(i, 1).toString();
					if (name.length() == 0)
					{
						valid = false;
						message = "Please enter a name for trigger #" + i + "!";
						break;
					}

					if (name.startsWith("parent:") && !inherit) builder.nonInheritedTriggers.add(name.replace("parent:", ""));
					else builder.triggers.add(name.replace("parent:", ""));
				}

				for (int i = 0; i < functions.getRowCount(); i++)
				{
					Boolean inherit = Boolean.valueOf(functions.getValueAt(i, 0).toString());
					String name = functions.getValueAt(i, 1).toString();
					String params = functions.getValueAt(i, 2).toString();
					if (name.length() == 0)
					{
						valid = false;
						message = "Please enter a name for function #" + i + "!";
						break;
					}

					if (name.startsWith("parent:") && !inherit) builder.nonInheritedFunctions.add(name.replace("parent:", "") + "(" + params + ")");
					else builder.functions.add(name.replace("parent:", "") + "(" + params + ")");
				}

				if (!valid)
				{
					JOptionPane.showMessageDialog(EntityEditor.this, message, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				entityFiles.put(new ArrayList<>(entityFiles.keySet()).get(tree.getSelectionRows()[0] - 1), builder);

				checkChanged();
				refresh();
			}
		});
		apply.setBounds(uiPanel.getWidth() / 2, uiPanel.getHeight() - 27, uiPanel.getWidth() / 2, 25);
		uiPanel.add(apply);

		refresh();
	}

	private EntityBuilder getParent(EntityBuilder b)
	{
		if (b.parent == null) return null;

		for (File key : entityFiles.keySet())
		{
			EntityBuilder v = entityFiles.get(key);
			if (v.name.equals(b.parent)) return v;
		}

		return null;
	}
}
