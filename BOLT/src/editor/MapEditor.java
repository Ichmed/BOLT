package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import util.SpringUtilities;

public class MapEditor extends JFrame implements TreeSelectionListener
{
  private static final long serialVersionUID = 1L;
  
  // -- tree -- //
  JTree                     tree;
  
  // -- components -- //
  JPanel                    uiPanel;
  JTextField                entityName;
  JSpinner[]                entityPos;
  JSpinner[]                entityRot;
  JTable                    entityCustomValues;
  
  // -- menubar -- //
  JMenuItem                 saveFile;
  JMenuItem                 saveUFile;
  
  public MapEditor()
  {
    super("BOLT MapEditor");
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    initComponents();
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  public void initComponents()
  {
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenuItem newFile = new JMenuItem(new AbstractAction("New")
    {
      private static final long serialVersionUID = 1L;
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        newMap();
      }
    });
    newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
    file.add(newFile);
    JMenuItem openFile = new JMenuItem(new AbstractAction("Open...")
    {
      private static final long serialVersionUID = 1L;
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        openMap();
      }
    });
    openFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
    file.add(openFile);
    saveFile = new JMenuItem(new AbstractAction("Save")
    {
      private static final long serialVersionUID = 1L;
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        saveMap();
      }
    });
    saveFile.setEnabled(false);
    saveFile.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
    file.add(saveFile);
    saveUFile = new JMenuItem(new AbstractAction("Save as...")
    {
      private static final long serialVersionUID = 1L;
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        saveUMap();
      }
    });
    saveUFile.setEnabled(false);
    saveUFile.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
    file.add(saveUFile);
    
    menu.add(file);
    setJMenuBar(menu);
    
    JPanel panel = new JPanel(new BorderLayout());
    
    tree = new JTree(new DefaultMutableTreeNode("World"));
    tree.setEnabled(false);
    tree.setShowsRootHandles(true);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setPreferredSize(new Dimension(200, 600));
    tree.addTreeSelectionListener(this);
    tree.setExpandsSelectedPaths(true);
    panel.add(tree, BorderLayout.LINE_START);
    
    uiPanel = new JPanel(new FlowLayout());
    uiPanel.setEnabled(false);
    uiPanel.setPreferredSize(new Dimension(600, 600));
    panel.add(uiPanel, BorderLayout.LINE_END);
    
    setContentPane(panel);
    
    pack();
  }
  
  public void setTitle(String s)
  {
    super.setTitle("BOLT MapEditor" + ((s != null) ? " - " + s : ""));
  }
  
  public void newMap()
  {
    saveFile.setEnabled(true);
    saveUFile.setEnabled(true);
    tree.setEnabled(true);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("World");
    DefaultMutableTreeNode entities = new DefaultMutableTreeNode("Entities");
    root.add(entities);
    tree.setModel(new DefaultTreeModel(root));
    
    uiPanel.setEnabled(true);
  }
  
  public void openMap()
  {}
  
  public void saveMap()
  {}
  
  public void saveUMap()
  {}
  
  private void refresh()
  {
    revalidate();
    repaint();
  }
  
  @Override
  public void valueChanged(TreeSelectionEvent e)
  {
    uiPanel.setLayout(new FlowLayout());
    uiPanel.removeAll();
    refresh();
    
    final DefaultMutableTreeNode s = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
    
    if (tree.getRowForPath(e.getPath()) == 1) // Entities
    {
      JButton newEntity = new JButton(new AbstractAction("New Entity")
      {
        private static final long serialVersionUID = 1L;
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
          DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
          dtm.insertNodeInto(new DefaultMutableTreeNode("Entity" + (s.getChildCount())), s, s.getChildCount());
          tree.expandRow(1); // Entities
        }
      });
      newEntity.setPreferredSize(new Dimension(uiPanel.getWidth() - 30, 35));
      uiPanel.add(newEntity);
      refresh();
    }
    
    if (s.toString().startsWith("Entity")) // Entity1, Entity2, ...
    {
      JPanel uiP = new JPanel(new SpringLayout());
      uiP.add(new JLabel("Name:"));
      entityName = new JTextField(s.toString());
      uiP.add(entityName);
      
      uiP.add(new JLabel("Position:"));
      JPanel panel = new JPanel();
      entityPos = new JSpinner[3];
      entityPos[0] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityPos[0]);
      entityPos[1] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityPos[1]);
      entityPos[2] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityPos[2]);
      uiP.add(panel);
      
      uiP.add(new JLabel("Rotation:"));
      panel = new JPanel();
      entityRot = new JSpinner[3];
      entityRot[0] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityRot[0]);
      entityRot[1] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityRot[1]);
      entityRot[2] = new JSpinner(new SpinnerNumberModel(0f, -1000000f, 1000000f, 1));
      panel.add(entityRot[2]);
      uiP.add(panel);
      
      uiP.add(new JLabel("Custom Values:"));
      entityCustomValues = new JTable(new DefaultTableModel(new String[] { "Type", "Value" }, 0));
      JScrollPane jsp = new JScrollPane(entityCustomValues, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      entityCustomValues.setFillsViewportHeight(true);
      jsp.setPreferredSize(new Dimension(entityCustomValues.getWidth(), 150));
      uiP.add(jsp);
      
      uiP.add(new JLabel());
      uiP.add(new JButton(new AbstractAction("Add custom Value")
      {
        private static final long serialVersionUID = 1L;
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
          DefaultTableModel dtm = (DefaultTableModel) entityCustomValues.getModel();
          dtm.addRow(new Object[] {});
        }
      }));
      
      SpringUtilities.makeCompactGrid(uiP, 5, 2, 6, 6, 6, 6);
      
      uiPanel.add(uiP);
      refresh();
    }
  }
}