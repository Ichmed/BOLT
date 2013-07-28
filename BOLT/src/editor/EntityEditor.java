package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import util.SpringUtilities;

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

	}
}
