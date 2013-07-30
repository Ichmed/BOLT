package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import util.SpringUtilities;

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
	ArrayList<File> entityFiles;

	JPanel uiPanel;
	JScrollPane treePanel;
	JTree tree;
	JTextField parent, name, fullName, klass, model, collModel;
	JComboBox<String> physType, collType;
	JCheckBox invis, grav;
	JTable customVals;

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
		menu.add(entlist);

		setJMenuBar(menu);

		JPanel p = new JPanel(new BorderLayout());
		p.setPreferredSize(new Dimension(800, 600));

		treePanel = new JScrollPane(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treePanel.setPreferredSize(new Dimension(200, 0));
		p.add(treePanel, BorderLayout.LINE_START);
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
		p.add(uiPanel, BorderLayout.LINE_END);

		setContentPane(p);
		pack();
	}

	private void applyEntity()
	{

	}

	private void reset()
	{
		tree.setEnabled(true);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("EntityList");
		tree.setModel(new DefaultTreeModel(root));
	}

	private void refresh()
	{
		revalidate();
		repaint();
		treePanel.revalidate();
	}

	private void newEntityList()
	{
		reset();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		uiPanel.setLayout(new FlowLayout());
		uiPanel.removeAll();
		refresh();

		// final DefaultMutableTreeNode s = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		if (tree.getRowForPath(e.getPath()) == 0) // entityList
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
					JFileChooser jfc = new JFileChooser(new File("C:/"));
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setMultiSelectionEnabled(false);
					if (path.getText().length() > 0) jfc.setSelectedFile(new File(path.getText()));

					jfc.setFileFilter(new FileNameExtensionFilter("BOLT Entity-Files", "entity"));
					if (jfc.showSaveDialog(EntityEditor.this) == JFileChooser.APPROVE_OPTION) path.setText(jfc.getSelectedFile().getPath().replace(".entity", "") + ".entity");
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
					if (entityFiles.contains(f))
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
					entityFiles.add(f);
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
		else if (tree.getRowForPath(e.getPath()) > 0) // entity
		{
			JPanel panel = new JPanel(new SpringLayout());
			panel.add(new JLabel("Parent:"));
			JPanel panel2 = new JPanel();
			parent = new JTextField(15);
			panel2.add(parent);
			panel2.add(new JButton(new AbstractAction("Validate...")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO: check if parent is valid & loadable. If not valid, show filebrowser and add to entlist
				}
			}));
			panel.add(panel2);

			panel.add(new JLabel("Name:"));
			name = new JTextField(15);
			panel.add(name);

			panel.add(new JLabel("Full Name:"));
			fullName = new JTextField(15);
			panel.add(fullName);

			panel.add(new JLabel("Physical Type:"));
			physType = new JComboBox<>(new String[] { "physical", "static" });
			panel.add(physType);

			panel.add(new JLabel("Collision Type:"));
			collType = new JComboBox<>(new String[] { "solid", "gameSolid", "not solid" });
			panel.add(collType);

			panel.add(new JLabel("Invisible:"));
			invis = new JCheckBox();
			panel.add(invis);

			panel.add(new JLabel("Gravity:"));
			grav = new JCheckBox();
			panel.add(grav);

			panel.add(new JLabel("Class:"));
			klass = new JTextField(15);
			panel.add(klass);

			panel.add(new JLabel("Model:"));
			JPanel panel3 = new JPanel();
			model = new JTextField(15);
			panel3.add(model);
			panel3.add(new JButton(new AbstractAction("Browse...")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO: file browser
				}
			}));
			panel.add(panel3);

			panel.add(new JLabel("Collision Model:"));
			JPanel panel4 = new JPanel();
			model = new JTextField(15);
			panel4.add(model);
			panel4.add(new JButton(new AbstractAction("Browse...")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO: file browser
				}
			}));
			panel.add(panel4);

			panel.add(new JLabel("Custom Values:"));
			JPanel panel5 = new JPanel(new BorderLayout());
			customVals = new JTable(new DefaultTableModel(new String[] { "Type", "Name", "Default Value" }, 0));
			customVals.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			JScrollPane jsp = new JScrollPane(customVals, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			customVals.setFillsViewportHeight(true);
			customVals.setRowHeight(23);
			JComboBox<String> type = new JComboBox<String>(new String[] { "Byte", "Integer", "Float", "Boolean", "String" });
			type.setSelectedIndex(4);
			customVals.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(type));
			jsp.setPreferredSize(new Dimension(customVals.getWidth(), 150));
			panel5.add(jsp, BorderLayout.NORTH);
			panel5.add(new JButton(new AbstractAction("Append Row")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e)
				{
					DefaultTableModel model = (DefaultTableModel) customVals.getModel();

					model.addRow(new Object[] { "String", " ", " " });
					model.fireTableDataChanged();
					customVals.repaint();
				}
			}), BorderLayout.SOUTH);
			panel.add(panel5);

			panel.add(new JLabel());
			panel.add(new JButton(new AbstractAction("Apply")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e)
				{
					applyEntity();
				}
			}));

			SpringUtilities.makeCompactGrid(panel, 12, 2, 6, 6, 6, 6);
			uiPanel.add(panel);
		}

		refresh();
	}
}
