package editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpringLayout;

import util.SpringUtilities;

//TODO: tree for other entities in entlist

public class EntityEditor extends JFrame
{
	private static final long serialVersionUID = 1L;

	File entityFile;

	JTree tree;
	JTextField parent, name, fullName, klass, model, collModel;
	JComboBox<String> physType, collType;
	JCheckBox invis, grav;

	public EntityEditor()
	{
		super("BOLT EntityEditor");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);

		initComponents();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void initComponents()
	{
		JMenuBar menu = new JMenuBar();
		JMenu entlist = new JMenu("EntityList");
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

		SpringUtilities.makeCompactGrid(panel, 10, 2, 6, 6, 6, 6);

		JPanel p = new JPanel(new FlowLayout());
		p.setPreferredSize(new Dimension(600, 600));
		p.add(panel);

		setContentPane(p);
		pack();
	}

	public void newEntityList()
	{

	}
}
