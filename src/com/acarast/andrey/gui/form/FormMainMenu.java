package com.acarast.andrey.gui.form;

import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * Class for creating the menu of the main window
 * @author Andrey G
 */

public class FormMainMenu
{
	private JMenuBar menu = null;

	//----------------------------------------------------------------

	public FormMainMenu (FormMainHandler controllerButton)
	{
		menu = new JMenuBar();
		menu.setBackground(FormStyle.COLOR_MENU);
		menu.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		createMenu(controllerButton);
	}

	//----------------------------------------------------------------

	public JMenuBar get ()
	{
		return menu;
	}

	private void createMenu (FormMainHandler controllerButton)
	{
		menuFile(controllerButton);
		menuEdit(controllerButton);
		menuView(controllerButton);
		menuRun(controllerButton);
		menuHelp(controllerButton);
	}

	//----------------------------------------------------------------
	//---- Components of the menu

	/**
	 * Menu File: New project, Open file, Add file, Open Folder, Save output, Export
	 * @param controllerButton
	 */
	private void menuFile (FormMainHandler controllerButton)
	{
		JMenu menuFile = new JMenu("File");
		menuFile.setFont(FormStyle.DEFAULT_FONT);
		menuFile.setBorder(new EmptyBorder(5, 5, 5, 5));
		menuFile.setMnemonic(KeyEvent.VK_F);
		menu.add(menuFile);

		//---- Menu for creating a new project, cleaning all data
		JMenuItem menuNewProject = new JMenuItem(FormStyle.MENU_FILE_NEWPROJECT);
		menuNewProject.setFont(FormStyle.DEFAULT_FONT);
		menuNewProject.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuNewProject.setBackground(FormStyle.COLOR_MENU);
		menuNewProject.setActionCommand(FormMainHandlerCommands.AC_NEWPROJECT);
		menuFile.add(menuNewProject);

		//---- Menu for creating a new project and opening a new file
		JMenuItem menuFileOpen = new JMenuItem(FormStyle.MENU_FILE_OPENFILE);
		menuFileOpen.addActionListener(controllerButton);
		menuFileOpen.setFont(FormStyle.DEFAULT_FONT);
		menuFileOpen.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileOpen.setBackground(FormStyle.COLOR_MENU);
		menuFileOpen.setActionCommand(FormMainHandlerCommands.AC_ADDFILE);
		menuFile.add(menuFileOpen);

		//---- Menu for adding a file to the current project
		JMenuItem menuFileAdd = new JMenuItem(FormStyle.MENU_FILE_ADDFILE);
		menuFileAdd.addActionListener(controllerButton);
		menuFileAdd.setFont(FormStyle.DEFAULT_FONT);
		menuFileAdd.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileAdd.setBackground(FormStyle.COLOR_MENU);
		menuFileAdd.setActionCommand(FormMainHandlerCommands.AC_REMOVEFILE);
		menuFile.add(menuFileAdd);

		//---- Menu for adding all files in an existing folder to the current project
		JMenuItem menuFileOpenFolder = new JMenuItem(FormStyle.MENU_FILE_OPENFOLDER);
		menuFileOpenFolder.addActionListener(controllerButton);
		menuFileOpenFolder.setFont(FormStyle.DEFAULT_FONT);
		menuFileOpenFolder.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileOpenFolder.setBackground(FormStyle.COLOR_MENU);
		menuFileOpenFolder.setActionCommand(FormMainHandlerCommands.AC_ADDFOLDER);
		menuFile.add(menuFileOpenFolder);

		//-----------------------------------------------
		menuFile.addSeparator();
		//-----------------------------------------------

		//---- Menu for saving output for the currently selected image
		JMenuItem menuFileSaveFile = new JMenuItem(FormStyle.MENU_FILE_SAVEOUTPUT);
		menuFileSaveFile.addActionListener(controllerButton);
		menuFileSaveFile.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveFile.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveFile.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveFile.setActionCommand(FormMainHandlerCommands.AC_SAVE_OUTPUTDATA);
		menuFile.add(menuFileSaveFile);

		//---- Menu for saving output for all images in the current project
		JMenuItem menuFileSaveAllFile = new JMenuItem(FormStyle.MENU_FILE_SAVEALL);
		menuFileSaveAllFile.addActionListener(controllerButton); 
		menuFileSaveAllFile.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveAllFile.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveAllFile.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveAllFile.setActionCommand(FormMainHandlerCommands.AC_SAVE_OUTPUTDATAALL);
		menuFile.add(menuFileSaveAllFile);

		//-----------------------------------------------
		menuFile.addSeparator();
		//-----------------------------------------------

		JMenu menuExport = new JMenu (FormStyle.MENU_FILE_EXPORT);
		menuExport.setFont(FormStyle.DEFAULT_FONT);
		menuExport.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuExport.setBackground(FormStyle.COLOR_MENU);
		menuExport.setOpaque(true);
		menuFile.add(menuExport);

		menuFileExport(menuExport, controllerButton);

		//-----------------------------------------------
		//---- Time lapse addition
		//-----------------------------------------------
//		menuFile.addSeparator();
//		
//		JMenuItem menuTimelapseImport = new JMenuItem("Timelapse import");
//		menuTimelapseImport.addActionListener(controllerButton); 
//		menuTimelapseImport.setFont(FormStyle.DEFAULT_FONT);
//		menuTimelapseImport.setBorder(FormStyle.DEFAULT_MENU_BORDER);
//		menuTimelapseImport.setBackground(FormStyle.COLOR_MENU);
//		menuTimelapseImport.setOpaque(true);
//		menuTimelapseImport.setActionCommand(FormMainHandlerCommands.AC_TIMELAPSE_IMPORT);
//		menuFile.add(menuTimelapseImport);
//		
//		JMenuItem menuTimelapseExport = new JMenuItem("Timelapse export");
//		menuTimelapseExport.addActionListener(controllerButton); 
//		menuTimelapseExport.setFont(FormStyle.DEFAULT_FONT);
//		menuTimelapseExport.setBorder(FormStyle.DEFAULT_MENU_BORDER);
//		menuTimelapseExport.setBackground(FormStyle.COLOR_MENU);
//		menuTimelapseExport.setOpaque(true);
//		menuTimelapseExport.setActionCommand(FormMainHandlerCommands.AC_TIMELAPSE_EXPORT);
//		menuFile.add(menuTimelapseExport);
		//-----------------------------------------------

	}

	private void menuFileExport (JMenu menuExport, FormMainHandler controllerButton)
	{
		//---- Export an image with bounding boxes around detected cells for the selected input image
		JMenuItem menuFileSaveImage = new JMenuItem(FormStyle.MENU_FILE_EXPORT_CHANNELAREA);
		menuFileSaveImage.addActionListener(controllerButton);
		menuFileSaveImage.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveImage.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveImage.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveImage.setActionCommand(FormMainHandlerCommands.AC_EXPORT_IMAGESAMPLEAREA);
		menuExport.add(menuFileSaveImage);

		//---- Export an image with highlighted detected channel regions for the selected input image 
		JMenuItem menuFileSaveImageFull = new JMenuItem(FormStyle.MENU_FILE_EXPORT_BOUNBOX);
		menuFileSaveImageFull.addActionListener(controllerButton);
		menuFileSaveImageFull.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveImageFull.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveImageFull.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveImageFull.setActionCommand(FormMainHandlerCommands.AC_EXPORT_IMAGEBOUNDINGBOX);
		menuExport.add(menuFileSaveImageFull);

		//---- Export an image where detected cells are colored with different colors for the selected input image
		JMenuItem menuFileSaveColoredCells  = new JMenuItem(FormStyle.MENU_FILE_EXPORT_COLOREDCELLS);
		menuFileSaveColoredCells.addActionListener(controllerButton);
		menuFileSaveColoredCells.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveColoredCells.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveColoredCells.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveColoredCells.setActionCommand(FormMainHandlerCommands.AC_EXPORT_COLOREDCELLS);
		menuExport.add(menuFileSaveColoredCells);
		
		menuExport.addSeparator();

		//---- Export the length frequency chart (the one on the right side), for the selected input image
		JMenuItem menuFileSaveDistributionGraph = new JMenuItem(FormStyle.MENU_FILE_EXPORT_LENGTHFREQCHART);
		menuFileSaveDistributionGraph.addActionListener(controllerButton);
		menuFileSaveDistributionGraph.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveDistributionGraph.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveDistributionGraph.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveDistributionGraph.setActionCommand(FormMainHandlerCommands.AC_EXPORT_CHARTFREQUENCY);
		menuExport.add(menuFileSaveDistributionGraph);

		//---- Export the count & length char for the selected input image
		JMenuItem menuFileSaveDistributionChartCount = new JMenuItem(FormStyle.MENU_FILE_EXPORT_COUNTFREQCHART);
		menuFileSaveDistributionChartCount.addActionListener(controllerButton);
		menuFileSaveDistributionChartCount.setFont(FormStyle.DEFAULT_FONT);
		menuFileSaveDistributionChartCount.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileSaveDistributionChartCount.setBackground(FormStyle.COLOR_MENU);
		menuFileSaveDistributionChartCount.setActionCommand(FormMainHandlerCommands.AC_EXPORT_CHARTCOUNT);
		menuExport.add(menuFileSaveDistributionChartCount);

		menuExport.addSeparator();
		
		//---- Export a feature vector for all input files (used for time lapse series)
		JMenuItem menuFileExportFeatureVector = new JMenuItem(FormStyle.MENU_FILE_EXPORT_FEATUREVECTOR);
		menuFileExportFeatureVector.addActionListener(controllerButton);
		menuFileExportFeatureVector.setFont(FormStyle.DEFAULT_FONT);
		menuFileExportFeatureVector.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuFileExportFeatureVector.setBackground(FormStyle.COLOR_MENU);
		menuFileExportFeatureVector.setActionCommand(FormMainHandlerCommands.AC_EXPORT_FEATUREVECTOR);
		menuExport.add(menuFileExportFeatureVector);
	}

	/**
	 * Menu Edit: select and move samples
	 * @param controllerButton
	 */
	private void menuEdit (FormMainHandler controllerButton)
	{
		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setFont(FormStyle.DEFAULT_FONT);
		menuEdit.setBorder(new EmptyBorder(5, 5, 5, 5));
		menuEdit.setMnemonic(KeyEvent.VK_E);
		menu.add(menuEdit);

		JMenuItem menuEditSelectSample = new JMenuItem(FormStyle.MENU_EDIT_SELECTSAMPLE);
		menuEditSelectSample.addActionListener(controllerButton);
		menuEditSelectSample.setFont(FormStyle.DEFAULT_FONT);
		menuEditSelectSample.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuEditSelectSample.setBackground(FormStyle.COLOR_MENU);
		menuEditSelectSample.setActionCommand(FormMainHandlerCommands.AC_SAMPLE_SELECT);
		menuEdit.add(menuEditSelectSample);

		JMenuItem menuEditMoveSample = new JMenuItem(FormStyle.MENU_EDIT_MOVESAMPLE);
		menuEditMoveSample.addActionListener(controllerButton);
		menuEditMoveSample.setFont(FormStyle.DEFAULT_FONT);
		menuEditMoveSample.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuEditMoveSample.setBackground(FormStyle.COLOR_MENU);
		menuEditMoveSample.setActionCommand(FormMainHandlerCommands.AC_SAMPLE_MOVE);
		menuEdit.add(menuEditMoveSample);
	}

	/**
	 * Menu View: zoom in, zoom out, reset view, image with bound box, colored cells, original image, switch charts
	 * @param controllerButton
	 */
	private void menuView (FormMainHandler controllerButton)
	{
		JMenu menuView = new JMenu("View");
		menuView.setFont(FormStyle.DEFAULT_FONT);
		menuView.setBorder(new EmptyBorder(5, 5, 5, 5));
		menuView.setMnemonic(KeyEvent.VK_V);
		menu.add(menuView);

		//---- Zoom in the displayed image 
		JMenuItem menuViewZoomIn = new JMenuItem(FormStyle.MENU_VIEW_ZOOMIN);
		menuViewZoomIn.addActionListener(controllerButton);
		menuViewZoomIn.setFont(FormStyle.DEFAULT_FONT);
		menuViewZoomIn.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewZoomIn.setBackground(FormStyle.COLOR_MENU);
		menuViewZoomIn.setActionCommand(FormMainHandlerCommands.AC_ZOOMIN);
		menuView.add(menuViewZoomIn);

		//---- Zoom out the displayed image
		JMenuItem menuViewZoomOut = new JMenuItem(FormStyle.MENU_VIEW_ZOOMOUT);
		menuViewZoomOut.addActionListener(controllerButton);
		menuViewZoomOut.setFont(FormStyle.DEFAULT_FONT);
		menuViewZoomOut.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewZoomOut.setBackground(FormStyle.COLOR_MENU);
		menuViewZoomOut.setActionCommand(FormMainHandlerCommands.AC_ZOOMOUT);
		menuView.add(menuViewZoomOut);

		//---- Reset the viewpoint of the displayed image
		JMenuItem menuViewRefresh = new JMenuItem(FormStyle.MENU_VIEW_RESETVIEWPOINT);
		menuViewRefresh.addActionListener(controllerButton);
		menuViewRefresh.setFont(FormStyle.DEFAULT_FONT);
		menuViewRefresh.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewRefresh.setBackground(FormStyle.COLOR_MENU);
		menuViewRefresh.setActionCommand(FormMainHandlerCommands.AC_RESETVIEW);
		menuView.add(menuViewRefresh);

		//-----------------------------------------------
		menuView.addSeparator();
		//-----------------------------------------------

		//---- Display bounding boxes surrounding the detected cells
		JMenuItem menuViewDetectedCells = new JMenuItem(FormStyle.MENU_VIEW_BOUNDINGBOX);
		menuViewDetectedCells.addActionListener(controllerButton);
		menuViewDetectedCells.setFont(FormStyle.DEFAULT_FONT);
		menuViewDetectedCells.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewDetectedCells.setBackground(FormStyle.COLOR_MENU);
		menuViewDetectedCells.setActionCommand(FormMainHandlerCommands.AC_VIEW_CELL);
		menuView.add(menuViewDetectedCells);

		//---- Display image with all detected cells colored with different colors
		JMenuItem menuViewColoredCells = new JMenuItem(FormStyle.MENU_VIEW_COLOREDCELL);
		menuViewColoredCells.addActionListener(controllerButton);
		menuViewColoredCells.setFont(FormStyle.DEFAULT_FONT);
		menuViewColoredCells.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewColoredCells.setBackground(FormStyle.COLOR_MENU);
		menuViewColoredCells.setActionCommand(FormMainHandlerCommands.AC_VIEW_COLOREDCELL);
		menuView.add(menuViewColoredCells);

		//---- Display the original input image
		JMenuItem menuViewOriginalImage = new JMenuItem(FormStyle.MENU_VIEW_ORIGINALINPUT);
		menuViewOriginalImage.addActionListener(controllerButton);
		menuViewOriginalImage.setFont(FormStyle.DEFAULT_FONT);
		menuViewOriginalImage.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewOriginalImage.setBackground(FormStyle.COLOR_MENU);
		menuViewOriginalImage.setActionCommand(FormMainHandlerCommands.AC_VIEW_IMAGE);
		menuView.add(menuViewOriginalImage);

		//-----------------------------------------------
		menuView.addSeparator();
		//-----------------------------------------------

		//---- Display the length frequency chart in the chart window
		JMenuItem menuViewChartFrequency = new JMenuItem(FormStyle.MENU_VIEW_FREQUENCYCHART);
		menuViewChartFrequency.addActionListener(controllerButton);
		menuViewChartFrequency.setFont(FormStyle.DEFAULT_FONT);
		menuViewChartFrequency.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewChartFrequency.setBackground(FormStyle.COLOR_MENU);
		menuViewChartFrequency.setActionCommand(FormMainHandlerCommands.AC_VIEW_CHARTFREQUENCY);
		menuView.add(menuViewChartFrequency);

		//---- Display the count + length chart in the chart window
		JMenuItem menuViewChartCount = new JMenuItem(FormStyle.MENU_VIEW_COUNTFREQCHART);
		menuViewChartCount.addActionListener(controllerButton);
		menuViewChartCount.setFont(FormStyle.DEFAULT_FONT);
		menuViewChartCount.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuViewChartCount.setBackground(FormStyle.COLOR_MENU);
		menuViewChartCount.setActionCommand(FormMainHandlerCommands.AC_VIEW_CHARTCOUNT);
		menuView.add(menuViewChartCount);
	}

	private void menuRun (FormMainHandler controllerButton)
	{
		JMenu menuRun = new JMenu("Run");
		menuRun.setFont(FormStyle.DEFAULT_FONT);
		menuRun.setBorder(new EmptyBorder(5, 5, 5, 5));
		menuRun.setMnemonic(KeyEvent.VK_R);
		menu.add(menuRun);

		JMenuItem menuRunProcess = new JMenuItem(FormStyle.MENU_RUN_RUNTHIS);
		menuRunProcess.addActionListener(controllerButton);
		menuRunProcess.setFont(FormStyle.DEFAULT_FONT);
		menuRunProcess.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuRunProcess.setBackground(FormStyle.COLOR_MENU);
		menuRunProcess.setActionCommand(FormMainHandlerCommands.AC_PROCESS);
		menuRun.add(menuRunProcess);

		JMenuItem menuRunProcessAll = new JMenuItem(FormStyle.MENU_RUN_RUNALL);
		menuRunProcessAll.addActionListener(controllerButton);
		menuRunProcessAll.setFont(FormStyle.DEFAULT_FONT);
		menuRunProcessAll.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuRunProcessAll.setBackground(FormStyle.COLOR_MENU);
		menuRunProcessAll.setActionCommand(FormMainHandlerCommands.AC_PROCESSALL);
		menuRun.add(menuRunProcessAll);
	}

	private void menuHelp (FormMainHandler controllerButton)
	{
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setFont(FormStyle.DEFAULT_FONT);
		menuHelp.setBorder(new EmptyBorder(5, 5, 5, 5));
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menu.add(menuHelp);

		JMenuItem menuHelpSettings = new JMenuItem(FormStyle.MENU_HELP_SETTINGS);
		menuHelpSettings.addActionListener(controllerButton);
		menuHelpSettings.setFont(FormStyle.DEFAULT_FONT);
		menuHelpSettings.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuHelpSettings.setBackground(FormStyle.COLOR_MENU);
		menuHelpSettings.setActionCommand(FormMainHandlerCommands.AC_FORM_SETTINGS);
		menuHelp.add(menuHelpSettings);

		JMenuItem menuHelpContents = new JMenuItem(FormStyle.MENU_HELP_HELPMANUAL);
		menuHelpContents.addActionListener(controllerButton);
		menuHelpContents.setFont(FormStyle.DEFAULT_FONT);
		menuHelpContents.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuHelpContents.setBackground(FormStyle.COLOR_MENU);
		menuHelpContents.setActionCommand(FormMainHandlerCommands.AC_FORM_HELP);
		menuHelp.add(menuHelpContents);

		JMenuItem menuHelpAbout = new JMenuItem(FormStyle.MENU_HELP_ABOUT);
		menuHelpAbout.addActionListener(controllerButton);
		menuHelpAbout.setFont(FormStyle.DEFAULT_FONT);
		menuHelpAbout.setBorder(FormStyle.DEFAULT_MENU_BORDER);
		menuHelpAbout.setBackground(FormStyle.COLOR_MENU);
		menuHelpAbout.setActionCommand(FormMainHandlerCommands.AC_FORM_ABOUT);
		menuHelp.add(menuHelpAbout);
	}
}
