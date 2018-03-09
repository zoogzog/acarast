package com.acarast.andrey.gui.form;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

/**
 * Class helper for the FormMain class. Sets up the components for displaying 
 * toolbar of the main window of the application
 * @author Andrey G
 */

public class FormMainToolbar
{
    private JToolBar toolbar;
    
    private JButton buttonNewProject;
    private JButton buttonAddFile;
    private JButton buttonAddFolder;
    private JButton buttonProcess;
    private JButton buttonProcessAll;
    private JButton buttonSave;
    private JButton buttonAppend;
    private JButton buttonViewImage;
    private JButton buttonSelect;
    private JButton buttonMove;
    private JButton buttonViewSamples;
    
    //----------------------------------------------------------------
    
    public FormMainToolbar (FormMainHandler controllerButton, int width)
    {
	toolbar = new JToolBar();
	toolbar.setLocation(0,0);
	toolbar.setSize(width, 35);
	toolbar.setFloatable(false);
	toolbar.setBorderPainted(true);
	toolbar.setRollover(true);
	toolbar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	toolbar.setBackground(FormStyle.COLOR_TOOLBAR);
	
	createToolbar(controllerButton);
    }
    
    public JToolBar get()
    {
	return toolbar;
    }
    
    //----------------------------------------------------------------
    //---- GET COMPONENT BUTTON
    //----------------------------------------------------------------
    
    public JButton getComponentButtonNewProject()
    {
	return buttonNewProject;
    }
    
    public JButton getComponentButtonAddFile()
    {
	return buttonAddFile;
    }
    
    public JButton getComponentButtonAddFolder()
    {
	return buttonAddFolder;
    }
    
    public JButton getComponentButtonProcess()
    {
	return buttonProcess;
    }
    
    public JButton getComponentButtonProcessAll()
    {
	return buttonProcessAll;
    }
    
    public JButton getComponentButtonSave()
    {
	return buttonSave;
    }
    
    public JButton getComponentButtonAppend()
    {
	return buttonAppend;
    }
    
    public JButton getComponentButtonViewImage()
    {
	return buttonViewImage;
    }
    
    public JButton getComponentButtonSelect()
    {
	return buttonSelect;
    }
    
    public JButton getComponentButtonMove()
    {
	return buttonMove;
    }
    
    public JButton getComponentButtonViewSamples()
    {
	return buttonViewSamples;
    }
    
    //----------------------------------------------------------------
    
    private void createToolbar(FormMainHandler controllerButton)
    {
	componentButtonNewProject(controllerButton);
	componentButtonAddFile(controllerButton);
	componentButtonAddFolder(controllerButton);

	toolbar.addSeparator();

	componentButtonZoomIn(controllerButton);
	componentButtonZoomOut(controllerButton);

	toolbar.addSeparator();

	componentButtonSelect(controllerButton);
	componentButtonMove(controllerButton);

	toolbar.addSeparator();

	componentButtonProcess(controllerButton);
	componentButtonProcessAll(controllerButton);

	toolbar.addSeparator();

	componentButtonResetView(controllerButton);
	componentButtonViewImage(controllerButton);
	componentButtonViewSamples(controllerButton);

	toolbar.addSeparator();

	componentButtonSave(controllerButton);
	componentButtonSaveAll(controllerButton);
    }
    
    //----------------------------------------------------------------
    /**
     * Button: New file. Create a new project, remove all files.
     * @param controllerButton
     */
    private void componentButtonNewProject (FormMainHandler controllerButton)
    {

	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT_FILE);

	buttonNewProject = new JButton(iconButton);
	buttonNewProject.addActionListener (null);
	buttonNewProject.setSize(30, 30);
	buttonNewProject.setToolTipText("New project");
	buttonNewProject.setBackground(FormStyle.COLOR_TOOLBAR);
	buttonNewProject.setForeground(Color.black);
	buttonNewProject.setActionCommand(FormMainHandlerCommands.AC_NEWPROJECT);
	buttonNewProject.addActionListener(controllerButton);

	toolbar.add(buttonNewProject);
    }

    //----------------------------------------------------------------
    /**
     * Button: Add File. Display a dialog box to select an input file. Add this file to the current
     * project. Also works if it is a first file in a project.
     * @param controllerButton
     */
    private void componentButtonAddFile (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT_FILE_ADD);

	buttonAddFile = new JButton(iconButton);
	buttonAddFile.addActionListener (null);
	buttonAddFile.setSize(30, 30);
	buttonAddFile.setToolTipText("Add file");
	buttonAddFile.setBackground(FormStyle.COLOR_TOOLBAR);
	buttonAddFile.setActionCommand(FormMainHandlerCommands.AC_ADDFILE);
	buttonAddFile.addActionListener(controllerButton);

	toolbar.add(buttonAddFile);
    }

    //----------------------------------------------------------------
    /**
     * Button: Add Folder. Display a dialog box to select a folder. All files (jpg/bmp) from 
     * the selected folder will be added to the current project.
     * @param controllerButton
     */
    private void componentButtonAddFolder (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT_FOLDER);

	buttonAddFolder = new JButton(iconButton);
	buttonAddFolder.addActionListener (null);
	buttonAddFolder.setSize(30, 30);
	buttonAddFolder.setToolTipText("Open folder");
	buttonAddFolder.setBackground(FormStyle.COLOR_TOOLBAR);
	buttonAddFolder.setActionCommand(FormMainHandlerCommands.AC_ADDFOLDER);
	buttonAddFolder.addActionListener(controllerButton);

	toolbar.add(buttonAddFolder);
    }

    //----------------------------------------------------------------
    /**
     * Button: Zoom In. Zooms in the currently displayed image. 
     * @param controllerButton
     */
    private void componentButtonZoomIn (FormMainHandler controllerButton)
    {		
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_ZOOMIN);

	JButton buttonZoomIn = new JButton(iconButton);
	buttonZoomIn.addActionListener (null);
	buttonZoomIn.setSize(30, 30);
	buttonZoomIn.setToolTipText("Zoom in");
	buttonZoomIn.setBackground(FormStyle.COLOR_TOOLBAR);
	buttonZoomIn.setActionCommand(FormMainHandlerCommands.AC_ZOOMIN);
	buttonZoomIn.addActionListener(controllerButton);

	toolbar.add(buttonZoomIn);
    }

    //----------------------------------------------------------------
    /**
     * Button: Zoom out. Zooms out the currently displayed image.
     * @param controllerButton
     */
    private void componentButtonZoomOut (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_ZOOMOUT);

	JButton buttonZoomOut = new JButton(iconButton);
	buttonZoomOut.addActionListener (null);
	buttonZoomOut.setSize(30, 30);
	buttonZoomOut.setToolTipText("Zoom out");
	buttonZoomOut.setBackground(FormStyle.COLOR_TOOLBAR);
	buttonZoomOut.setActionCommand(FormMainHandlerCommands.AC_ZOOMOUT);
	buttonZoomOut.addActionListener(controllerButton);

	toolbar.add(buttonZoomOut);
    }

    //----------------------------------------------------------------
    /**
     * Button: Select. Allows user to select an area in the displayed image.The selected area can 
     * be further added as a sample in case if the processing is planned to be performed in the manual mode.
     * @param controllerButton
     */
    private void componentButtonSelect (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT);

	buttonSelect = new JButton(iconButton);
	buttonSelect.addActionListener (null);
	buttonSelect.setSize(30, 30);
	buttonSelect.setToolTipText("Select sample");
	buttonSelect.addActionListener(controllerButton);
	buttonSelect.setActionCommand(FormMainHandlerCommands.AC_SAMPLE_SELECT);
	buttonSelect.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonSelect);
    }

    //----------------------------------------------------------------
    /**
     * Button: Move. Allows to move the area, selected with the Select tool. This area can be added as 
     * a sample for further processing in the manual mode.
     * @param controllerButton
     */
    private void componentButtonMove (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_MOVE);

	buttonMove = new JButton(iconButton);
	buttonMove.addActionListener (null);
	buttonMove.setSize(30, 30);
	buttonMove.setToolTipText("Move sample");
	buttonMove.addActionListener(controllerButton);
	buttonMove.setActionCommand(FormMainHandlerCommands.AC_SAMPLE_MOVE);
	buttonMove.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonMove);
    }

    //----------------------------------------------------------------
    /**
     * Button: Process. Starts processing of the currently selected (displayed) image. Only one image
     * will be processed.
     * @param controllerButton
     */
    private void componentButtonProcess (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_PROCESS);

	buttonProcess = new JButton(iconButton);
	buttonProcess.addActionListener (null);
	buttonProcess.setSize(30, 30);
	buttonProcess.setToolTipText("Process");
	buttonProcess.addActionListener(controllerButton);
	buttonProcess.setActionCommand(FormMainHandlerCommands.AC_PROCESS);
	buttonProcess.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonProcess);
    }

    //----------------------------------------------------------------
    /**
     * Button: Process All. Start processing of all images in the current project. 
     * @param controllerButton
     */
    private void componentButtonProcessAll (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_PROCESS_ALL);

	buttonProcessAll = new JButton(iconButton);
	buttonProcessAll.setSize(30, 30);
	buttonProcessAll.setToolTipText("Process all");
	buttonProcessAll.addActionListener(controllerButton);
	buttonProcessAll.setActionCommand(FormMainHandlerCommands.AC_PROCESSALL);
	buttonProcessAll.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonProcessAll);
    }

    //----------------------------------------------------------------
    /**
     * Button: Reset/Refresh. Resets the view point of the images, forces to display the 
     * original input image.
     * @param controllerButton
     */
    private void componentButtonResetView (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_REFRESH);

	JButton buttonRefresh = new JButton(iconButton);
	buttonRefresh.addActionListener (null);
	buttonRefresh.setSize(30, 30);
	buttonRefresh.setToolTipText("Refresh image");
	buttonRefresh.addActionListener(controllerButton);
	buttonRefresh.setActionCommand(FormMainHandlerCommands.AC_RESETVIEW);
	buttonRefresh.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonRefresh);
    }

    //----------------------------------------------------------------
    /**
     * Button: Save. Displays a dialog box for specifying desired location and name of the output
     * file. Only output of the currently selected image will be saved.
     * @param controllerButton
     */
    private void componentButtonSave (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SAVE);

	buttonSave = new JButton(iconButton);
	buttonSave.addActionListener (null);
	buttonSave.setSize(30, 30);
	buttonSave.setToolTipText("Save current output");
	buttonSave.addActionListener(controllerButton);
	buttonSave.setActionCommand(FormMainHandlerCommands.AC_SAVE_OUTPUTDATA);
	buttonSave.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonSave);
    }

    //----------------------------------------------------------------
    /**
     * Button: Save all. Displays a dialog box for specifying desired location and name of the output
     * file. Output of all processed files will be saved.
     * @param controllerButton
     */
    private void componentButtonSaveAll (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SAVEALL);

	buttonAppend = new JButton(iconButton);
	buttonAppend.addActionListener (null);
	buttonAppend.setSize(30, 30);
	buttonAppend.setToolTipText("Save all output");
	buttonAppend.addActionListener(controllerButton);
	buttonAppend.setActionCommand(FormMainHandlerCommands.AC_SAVE_OUTPUTDATAALL);
	buttonAppend.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonAppend);
    }

    //----------------------------------------------------------------
    /**
     * Button: View Image. Switches the mode of the displayed image to the bounding box or original 
     * input image.
     * @param controllerButton
     */
    private void componentButtonViewImage (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_ORIGINAL_IMAGE);

	buttonViewImage = new JButton(iconButton);
	buttonViewImage.addActionListener (null);
	buttonViewImage.setSize(30, 30);
	buttonViewImage.setToolTipText("View cells");
	buttonViewImage.addActionListener(controllerButton);
	buttonViewImage.setActionCommand(FormMainHandlerCommands.AC_VIEW_CELL);
	buttonViewImage.setBackground(FormStyle.COLOR_TOOLBAR);

	toolbar.add(buttonViewImage);
    }

    //----------------------------------------------------------------
    
    /**
     * Button: View Samples. Switches displaying detected samples on or off.
     * @param controllerButton
     */
    private void componentButtonViewSamples (FormMainHandler controllerButton)
    {
	ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_VIEW_SAMPLES);
	
	buttonViewSamples = new JButton(iconButton);
	buttonViewSamples.addActionListener (controllerButton);
	buttonViewSamples.setSize(30, 30);
	buttonViewSamples.setToolTipText("View detected samples");
	buttonViewSamples.addActionListener(controllerButton);
	buttonViewSamples.setActionCommand(FormMainHandlerCommands.AC_VIEW_SAMPLES_ON);
	buttonViewSamples.setBackground(FormStyle.COLOR_TOOLBAR);
	
	toolbar.add(buttonViewSamples);
    }
    
}
