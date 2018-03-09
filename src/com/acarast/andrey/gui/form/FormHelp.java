package com.acarast.andrey.gui.form;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Form to display help window.
 * @author Andrey G
 *
 */
public class FormHelp implements TreeSelectionListener
{
    public static final String DEFAULT_HTML_FOLDER = "data/html/";

    public static String FORM_HELP_HTML_NAME_SYSTEM_OVERVIEW = "System overview";
    public static String FORM_HELP_HTML_NAME_GETTING_STARTED = "Getting started";
    public static String FORM_HELP_HTML_NAME_INPUT_FILES = "Adding files";
    public static String FORM_HELP_HTML_NAME_VIEWING_IMAGES = "Viewing images";
    public static String FORM_HELP_HTML_NAME_SELECTING_SAMPLES = "Selecting samples";
    public static String FORM_HELP_HTML_NAME_PROCESSING_IMAGES = "Processing images";
    public static String FORM_HELP_HTML_NAME_VIEWING_RESULTS = "Viewing results";
    public static String FORM_HELP_HTML_NAME_SAVING_RESULTS = "Saving results";
    public static String FORM_HELP_HTML_NAME_ADVANCED_SETTINGS = "Advanced settings";

    public static String FORM_HELP_HTML_PATH_SYSTEM_OVERVIEW = DEFAULT_HTML_FOLDER + "help_sysoverview.html";
    public static String FORM_HELP_HTML_PATH_GETTING_STARTED = DEFAULT_HTML_FOLDER + "help_getstarted.html";
    public static String FORM_HELP_HTML_PATH_INPUT_FILES = DEFAULT_HTML_FOLDER + "help_inputfiles.html";
    public static String FORM_HELP_HTML_PATH_VIEWING_IMAGES = DEFAULT_HTML_FOLDER + "help_viewimage.html";
    public static String FORM_HELP_HTML_PATH_SELECTING_SAMPLES = DEFAULT_HTML_FOLDER + "help_selectsample.html";
    public static String FORM_HELP_HTML_PATH_PROCESSING_IMAGES = DEFAULT_HTML_FOLDER + "help_procimage.html";
    public static String FORM_HELP_HTML_PATH_VIEWING_RESULTS = DEFAULT_HTML_FOLDER + "help_viewresults.html";
    public static String FORM_HELP_HTML_PATH_SAVING_RESULTS = DEFAULT_HTML_FOLDER + "help_saveresults.html";
    public static String FORM_HELP_HTML_PATH_ADVANCED_SETTINGS = DEFAULT_HTML_FOLDER + "help_advanced.html";

    //----------------------------------------------------------------

    private class HelpInfo 
    {
	public String helpTitle;
	public URL helpURL;

	public HelpInfo(String title, String filepath) 
	{
	    helpTitle = title;

	    File urlFile = new File(filepath);

	    try
	    {

		helpURL = urlFile.toURI().toURL();

	    }
	    catch (Exception e) { helpURL = null; }
	}

	public String toString() 
	{
	    return helpTitle;
	}
    }

    //----------------------------------------------------------------

    private JEditorPane helpPageView;
    private JTree helpContentsTree;

    //----------------------------------------------------------------

    public FormHelp()
    {
	loadHelp();
    }

    public void displayFrame(JFrame parentFrame) 
    {
	JPanel panelMain = new JPanel();
	panelMain.setLayout(new BorderLayout());
	componentMainPanel(panelMain);

	JDialog dialog = new JDialog(parentFrame, "Help");
	dialog.setModal(true);
	dialog.setContentPane(panelMain);
	dialog.pack();
	dialog.setLocationRelativeTo(parentFrame);
	dialog.setResizable(false);
	dialog.setVisible(true);
    }

    //----------------------------------------------------------------

    private void componentMainPanel (JPanel displayPanel)
    {
	displayPanel.setBackground(FormStyle.COLOR_MENU);
	displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	displayPanel.setLayout(new GridBagLayout());

	JScrollPane treeView = new JScrollPane(helpContentsTree);
	JScrollPane htmlView = new JScrollPane(helpPageView);

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	splitPane.setLeftComponent(treeView);
	splitPane.setRightComponent(htmlView);

	Dimension minimumSize = new Dimension(100, 50);
	htmlView.setMinimumSize(minimumSize);
	treeView.setMinimumSize(minimumSize);
	splitPane.setDividerLocation(180); 
	splitPane.setPreferredSize(new Dimension(800, 300));

	displayPanel.add(splitPane);	
    }

    //----------------------------------------------------------------

    private void loadHelp ()
    {
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Contents");

	DefaultMutableTreeNode node = null;

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_SYSTEM_OVERVIEW, FORM_HELP_HTML_PATH_SYSTEM_OVERVIEW));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_GETTING_STARTED, FORM_HELP_HTML_PATH_GETTING_STARTED));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_INPUT_FILES, FORM_HELP_HTML_PATH_INPUT_FILES));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_VIEWING_IMAGES, FORM_HELP_HTML_PATH_VIEWING_IMAGES));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_SELECTING_SAMPLES, FORM_HELP_HTML_PATH_SELECTING_SAMPLES));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_PROCESSING_IMAGES, FORM_HELP_HTML_PATH_PROCESSING_IMAGES));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_VIEWING_RESULTS, FORM_HELP_HTML_PATH_VIEWING_RESULTS));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_SAVING_RESULTS, FORM_HELP_HTML_PATH_SAVING_RESULTS));
	top.add(node);

	node = new DefaultMutableTreeNode(new HelpInfo(FORM_HELP_HTML_NAME_ADVANCED_SETTINGS, FORM_HELP_HTML_PATH_ADVANCED_SETTINGS));
	top.add(node);


	helpContentsTree = new JTree(top);
	helpContentsTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
	helpContentsTree.setFont(FormStyle.DEFAULT_FONT);
	helpContentsTree.addTreeSelectionListener(this);

	helpPageView = new JEditorPane();
	helpPageView.setEditable(false);

	try
	{
	    File urlFile = new File(FORM_HELP_HTML_PATH_SYSTEM_OVERVIEW);
	    URL startUrl;

	    startUrl = urlFile.toURI().toURL();


	    helpPageView.setPage(startUrl);
	}
	catch (Exception e) {}


    }

    //----------------------------------------------------------------
    //---- HANDLERS
    //----------------------------------------------------------------

    public void valueChanged(TreeSelectionEvent e) 
    {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) helpContentsTree.getLastSelectedPathComponent();

	if (node == null) { return; }

	Object nodeInfo = node.getUserObject();

	if (node.isLeaf()) 
	{
	    //---- Display the html content
	    HelpInfo currentHelpContent = (HelpInfo)nodeInfo;

	    if (currentHelpContent.helpURL != null)
	    {
		try
		{
		    helpPageView.setPage(currentHelpContent.helpURL);
		}
		catch (Exception ex) {  }
	    }
	}

    }

}