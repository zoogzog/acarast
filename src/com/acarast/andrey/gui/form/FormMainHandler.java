package com.acarast.andrey.gui.form;

import java.awt.Polygon;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.acarast.andrey.background.TaskBackground;
import com.acarast.andrey.controller.DataController;
import com.acarast.andrey.controller.OutputController;
import com.acarast.andrey.controller.TaskProgressController;
import com.acarast.andrey.controller.OutputController.OUTPUT_FORMAT;
import com.acarast.andrey.core.algorithm.EnumTypes;
import com.acarast.andrey.core.device.MicrofluidicChannel.Sensitivity;
import com.acarast.andrey.core.imgproc.ImageDraw;
import com.acarast.andrey.core.task.EstimatorSettings.ESTIMATOR_ALG;
import com.acarast.andrey.core.task.TaskSettings;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionHandler;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;
import com.acarast.andrey.imgproc.OpencvConverter;

/**
 * Handler class for the main window of the application. Handles low level (graphical)
 * responses to the clicked buttons and pressed keys. More complex scenarios which
 * involve data manipulation are processed by the ActionController.
 * @author Andrey G
 */

public class FormMainHandler  implements ActionListener
{
	//---- Form to control 
	private FormMain mainFormLink = null;

	boolean isDisplaySamples = false;

	//----------------------------------------------------------------

	public FormMainHandler ()
	{

	}

	public void init (FormMain mainForm)
	{
		mainFormLink = mainForm;

		//---- Bind other handlers and controllers

		//---- Bind progress controller to the GUI progress bar
		TaskProgressController.establishLink(mainFormLink.getComponentPanelDown().panelProgressBar);

		//---- Bind GUI frame to the exception handler
		ExceptionHandler.setJFrameLinkt(mainFormLink.getMainFrame());
	}

	//----------------------------------------------------------------

	@Override
	public void actionPerformed(ActionEvent source)
	{
		if (mainFormLink != null)
		{
			String commandCodeName = source.getActionCommand();

			/*!*/DebugLogger.logMessage("Action command: " + commandCodeName, LOG_MESSAGE_TYPE.INFO);

			switch (commandCodeName)
			{
			case FormMainHandlerCommands.AC_NEWPROJECT:		actionNewProject(); 	break;
			case FormMainHandlerCommands.AC_ADDFILE: 		actionAddFile(); 	break;
			case FormMainHandlerCommands.AC_ADDFOLDER:		actionAddFolder();	break;
			case FormMainHandlerCommands.AC_REMOVEFILE:		actionRemoveFile(); 	break;

			case FormMainHandlerCommands.AC_TIMELAPSE_IMPORT: actionTimelapseImport(); break;
			case FormMainHandlerCommands.AC_TIMELAPSE_EXPORT: actionTimelapseExport(); break;

			case FormMainHandlerCommands.AC_ZOOMIN:			actionZoomIn();		break;
			case FormMainHandlerCommands.AC_ZOOMOUT: 		actionZoomOut(); 	break;
			case FormMainHandlerCommands.AC_RESETVIEW:		actionResetView(); 	break;

			case FormMainHandlerCommands.AC_SAMPLE_MOVE:		actionSwitchMove();    	break;
			case FormMainHandlerCommands.AC_SAMPLE_SELECT:		actionSwitchSelect(); 	break;

			case FormMainHandlerCommands.AC_PROCESS:		actionProcess(); 	break;
			case FormMainHandlerCommands.AC_PROCESSALL:		actionProcessAll();	break;

			case FormMainHandlerCommands.AC_MANUAL_ADDCONTROL:	actionAddSample(true); 	break;
			case FormMainHandlerCommands.AC_MANUAL_ADDSAMPLE:	actionAddSample(false); break;
			case FormMainHandlerCommands.AC_MANUAL_DELETESAMPLE:	actionDeleteSample(); 	break;

			case FormMainHandlerCommands.AC_FORM_ABOUT: 		actionFormAbout(); 	break;
			case FormMainHandlerCommands.AC_FORM_HELP: 		actionFormHelp(); 	break;
			case FormMainHandlerCommands.AC_FORM_SETTINGS: 		actionFormSettings(); 	break;

			case FormMainHandlerCommands.AC_VIEW_CELL:		actionViewBoundingBox(); 	break;
			case FormMainHandlerCommands.AC_VIEW_IMAGE:		actionViewOriginalImage(); 	break;
			case FormMainHandlerCommands.AC_VIEW_COLOREDCELL:	actionViewColoredCells(); 	break;
			case FormMainHandlerCommands.AC_VIEW_CHARTFREQUENCY:	helperDisplayChart(true);	break;
			case FormMainHandlerCommands.AC_VIEW_CHARTCOUNT:	helperDisplayChart(false); 	break;
			case FormMainHandlerCommands.AC_VIEW_SAMPLES_ON:		actionViewSamplesON(); 		break;
			case FormMainHandlerCommands.AC_VIEW_SAMPLES_OFF:	actionViewSamplesOFF(); break;

			case FormMainHandlerCommands.AC_SAVE_OUTPUTDATA:	actionSaveOutput(); 	break;
			case FormMainHandlerCommands.AC_SAVE_OUTPUTDATAALL:	actionSaveOutputAll(); 	break;

			case FormMainHandlerCommands.AC_EXPORT_IMAGESAMPLEAREA:		actionExportImageSampleArea(); 		break;
			case FormMainHandlerCommands.AC_EXPORT_IMAGEBOUNDINGBOX:  	actionExportImageBoundingBox ();		break;
			case FormMainHandlerCommands.AC_EXPORT_COLOREDCELLS:		actionExportImageColoredCells(); 	break;
			case FormMainHandlerCommands.AC_EXPORT_FEATUREVECTOR:		actionExportFeatureVector(); 		break;
			case FormMainHandlerCommands.AC_EXPORT_CHARTCOUNT:			actionExportImageChart(false); 		break;
			case FormMainHandlerCommands.AC_EXPORT_CHARTFREQUENCY:  	actionExportImageChart(true);  		break;

			//----- Combobox changed and other interactions
			case FormMainHandlerCommands.AC_COMBOBOX_IMAGECHANGED:  actionComboboxChangedImage(); break;
			case FormMainHandlerCommands.AC_COMBOBOX_SAMPLECHANGED: actionComboboxChangedSample(); break;

			case FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_ON: actionChangedEstimatorON(); break;
			case FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_OFF: actionChangedEstimatorOFF(); break;
			case FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_FILE: actionChangedEstimatorSettingsAuto(); break;
			case FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_MANUAL: actionChangedEstimatorSettingsManual(); break;

			case FormMainHandlerCommands.AC_CHANGED_SAMPLEDETECTIONMODE: actionSampleDetectionModeChanged(); break;
			}
		}
		else
		{
			//---- Handler was not initialized. Somewhere the code is wrong.
			// ExceptionHandler.processException(new ExceptionMessage(EXCEPTION_CODE.EXCODE_HANDLER_INIT));
		}
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Action which is invoked when the user clicks the 'New project' button or chooses 'New project'
	 * option form the menu. Removes all previously added files, cleans everything.
	 */
	private void actionNewProject ()
	{
		try
		{
			DataController.scenarioNewProject();

			helperDisplayProjectFiles();

			//---- Change button icon
			ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_VIEW_SAMPLES);

			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setIcon(iconButton);
			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setActionCommand(FormMainHandlerCommands.AC_VIEW_SAMPLES_ON);
			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setToolTipText("View detected samples");

			mainFormLink.reset();
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	//-----------------------------------------------------------------------------------------
	/**
	 * Action which is invoked when the user clicks the 'Add file' button or chooses 'Add file'
	 * option from the menu. Shows user a dialog box for choosing an input file. When the input
	 * file is selected, adds it to the project.
	 */
	private void actionAddFile ()
	{
		try
		{
			JFileChooser fileChooserDriver = new JFileChooser();

			int isFileSelected =  fileChooserDriver.showOpenDialog(fileChooserDriver);

			if (isFileSelected == JFileChooser.APPROVE_OPTION)
			{
				String filePath = fileChooserDriver.getSelectedFile().getPath();
				DataController.scenarioAddFile(filePath);

				helperDisplayProjectFiles();
				helperDisplayInputImage();
			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}

	}

	/**
	 * Action which is invoked when the user clicks the 'Open folder' button or chooses 'Open folder'
	 * option from the menu. Shows user a dialog box to chose an existing folder. When the folder
	 * is selected, all bmp and jpg images are added to the project
	 */
	private void actionAddFolder ()
	{
		try
		{

			JFileChooser folderChooser = new JFileChooser();
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int isFolderSelected  = folderChooser.showOpenDialog(folderChooser);

			if (isFolderSelected == JFileChooser.APPROVE_OPTION)
			{
				String folderPath = folderChooser.getSelectedFile().getPath();

				File folderFileDescriptor = new File(folderPath);
				File[] listFolderFiles = folderFileDescriptor.listFiles();

				String[] fileList = new String[listFolderFiles.length];

				for (int i = 0; i < listFolderFiles.length; i++)
				{
					fileList[i] = listFolderFiles[i].getPath();
				}

				DataController.scenarioOpenFolder(fileList);

				helperDisplayProjectFiles();
				helperDisplayInputImage();
			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	/**
	 * Removes a currently selected file.
	 */
	private void actionRemoveFile ()
	{
		try
		{
			//---- Get currently selected file index in the combo box
			int index = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

			//---- Remove the file from the project by its index
			DataController.scenarioRemoveFile(index);

			helperDisplayProjectFiles();
			helperDisplaySamplesCombobox();
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}


	/**
	 * Import time lapse images from the following folder structure: rootDir->test$ID->processed->time-$ID
	 */
	private void actionTimelapseImport ()
	{
		try
		{

			JFileChooser folderChooser = new JFileChooser();
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int isFolderSelected  = folderChooser.showOpenDialog(folderChooser);

			if (isFolderSelected == JFileChooser.APPROVE_OPTION)
			{
				String folderPath = folderChooser.getSelectedFile().getPath();

				DataController.scenarioTimelapseImport(folderPath);

				helperDisplayProjectFiles();
				helperDisplayInputImage();

			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	/**
	 * Export time lapse images
	 */
	private void actionTimelapseExport ()
	{
		try
		{

			JFileChooser saveFileChooser = new JFileChooser();

			FileNameExtensionFilter typeCSV = new FileNameExtensionFilter("CSV", "csv", "CSV");
			saveFileChooser.addChoosableFileFilter(typeCSV);

			FileNameExtensionFilter typeTXT = new FileNameExtensionFilter("TXT", "txt", "TXT");
			saveFileChooser.addChoosableFileFilter(typeTXT);

			saveFileChooser.setAcceptAllFileFilterUsed(false);
			saveFileChooser.setFileFilter(typeCSV);

			int isOpen = saveFileChooser.showOpenDialog(saveFileChooser);

			if (isOpen == JFileChooser.APPROVE_OPTION) 
			{
				String folderPath = saveFileChooser.getSelectedFile().getPath();
				
				OutputController.exportTimelapseImage(folderPath, DataController.getTable());
			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Zooms in the currently displayed image.
	 */
	private void actionZoomIn ()
	{
		int tableSize = DataController.getTable().getTableSize();

		//---- Check if the project is empty or not
		if (tableSize != 0)
		{
			//---- Calculate zoom scale factor
			double zoom = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformZoom(+FormMainMouse.DEFAULT_ZOOM_DELTA);    
			mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();

			//---- Set the current zoom, display the zoom scale factor in the GUI
			FormMainMouse.imageViewZoomScale = zoom;
			mainFormLink.getComponentPanelDown().getComponentLabelZoom().setText(String.valueOf(zoom));
		}
	}

	/**
	 * Zooms out the currently displayed image.
	 */
	private void actionZoomOut ()
	{
		int tableSize = DataController.getTable().getTableSize();

		//---- Check if the project is empty or not
		if (tableSize != 0)
		{
			//---- Calculate zoom scale factor
			double zoom = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformZoom(-FormMainMouse.DEFAULT_ZOOM_DELTA);    
			mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();

			//---- Set the current zoom, display the zoom scale factor in the GUI
			FormMainMouse.imageViewZoomScale = zoom;
			mainFormLink.getComponentPanelDown().getComponentLabelZoom().setText(String.valueOf(zoom));
		}
	}

	/**
	 * Resets the viewpoint of the currently displayed image.
	 */
	private void actionResetView ()
	{
		helperSwitchSelectOff();
		helperSwitchMoveOff();

		int tableSize = DataController.getTable().getTableSize();

		//---- Check if the project is empty or not
		if (tableSize != 0)
		{
			mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygonStop();
			mainFormLink.getComponentPanelCenter().getComponentPanelImageView().resetImagePosition();
		}

	}

	//-----------------------------------------------------------------------------------------

	private void actionProcess ()
	{
		try
		{	
			int tableSize = DataController.getTable().getTableSize();

			/*!*/DebugLogger.logMessage("Handler has initiated processing a single image. Table size: " + tableSize, LOG_MESSAGE_TYPE.INFO);

			if (tableSize != 0)
			{
				TaskSettings settings = helperCaptureSettings();

				int index = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				TaskBackground taskDriver = new TaskBackground();
				taskDriver.setSettings(DataController.getTable(), settings, index, this);
				taskDriver.execute();
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	private void actionProcessAll ()
	{
		try
		{	
			int tableSize = DataController.getTable().getTableSize();

			/*!*/DebugLogger.logMessage("Handler has initiated processing all images. Table size: " + tableSize, LOG_MESSAGE_TYPE.INFO);

			if (tableSize != 0)
			{

				TaskSettings settings = helperCaptureSettings();

				TaskBackground taskDriver = new TaskBackground();
				taskDriver.setSettings(DataController.getTable(), settings, -1, this);
				taskDriver.execute();
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------
	//---- SAMPLE MANAGEMNET
	//-----------------------------------------------------------------------------------------

	private void actionAddSample (boolean isControl)
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			//---- Check if the project is empty or not
			if (tableSize != 0)
			{
				//---- Get index of the currently selected file
				int index = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				Polygon samplePolygon = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getDisplayedPolygon();

				if (samplePolygon != null)
				{
					if (samplePolygon.npoints > 0)
					{	
						DataController.getTable().addSample(index, samplePolygon, isControl);

						/*!*/DebugLogger.logMessage("Sample added to the file " + index + " control: " + isControl, LOG_MESSAGE_TYPE.INFO);

						int samplesCount = DataController.getTable().getElement(index).getChannelCount();

						//---- Update the combobox
						helperDisplaySamplesCombobox();

						mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().setSelectedIndex(samplesCount - 1);
					}
				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	private void actionDeleteSample ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			//---- Check if the project is empty or not
			if (tableSize != 0)
			{
				//---- Get index of the currently selected file
				int index = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				//---- Get index of the currently selected sample
				int indexSample = mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().getSelectedIndex();

				DataController.getTable().removeSample(index, indexSample);

				/*!*/DebugLogger.logMessage("Sample " + indexSample + " deleted from the file " + index, LOG_MESSAGE_TYPE.INFO);

				helperDisplaySamplesCombobox();
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------
	//---- VIEW IMAGE, CHART
	//-----------------------------------------------------------------------------------------

	/**
	 * Display original input image.
	 */
	private void actionViewOriginalImage ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int imagIndex = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				//---- Get path to the original image
				String filePath = DataController.getTable().getElement(imagIndex).getDataFile().getFilePath();

				//---- Load the image into the image viewer
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygonStop();
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(filePath);

				//---- Change button icon
				ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_ORIGINAL_IMAGE);

				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setIcon(iconButton);
				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setActionCommand(FormMainHandlerCommands.AC_VIEW_CELL);
				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setToolTipText("View cells");

			}

		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display image with cells, surrounded with boudning boxes.
	 */
	private void actionViewBoundingBox ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int imagIndex = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				//---- Get the image with colored cells, convert it to buffered image instead of opencv Mat.
				BufferedImage imageCC = OpencvConverter.convertMatToBImage(ImageDraw.drawImageBoundingBox(DataController.getTable().getElement(imagIndex)));

				//---- Display image at the image viewer panel
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(imageCC);

				//---- Change button icon
				ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_CELL_IMAGE);

				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setIcon(iconButton);
				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setActionCommand(FormMainHandlerCommands.AC_VIEW_IMAGE);
				mainFormLink.getComponentToolbar().getComponentButtonViewImage().setToolTipText("View cells");
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display an image with cells, encoded in color codes.
	 */
	private void actionViewColoredCells ()
	{

		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int imagIndex = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				//---- Get the image with colored cells, convert it to buffered image instead of opencv Mat.
				BufferedImage imageCC = OpencvConverter.convertMatToBImage(ImageDraw.drawImageColoredCells(DataController.getTable().getElement(imagIndex)));

				//---- Display image at the image viewer panel
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(imageCC);
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display all detected samples.
	 */
	private void actionViewSamplesON ()
	{
		int tableSize = DataController.getTable().getTableSize();

		if (tableSize != 0)
		{
			isDisplaySamples = true;

			helperDisplayDetectedSamples();

			//---- Change button icon
			ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_VIEW_SAMPLES_ON );

			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setIcon(iconButton);
			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setActionCommand(FormMainHandlerCommands.AC_VIEW_SAMPLES_OFF);
			mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setToolTipText("View original image");
		}
	}

	/**
	 * Stop displaying samples
	 */
	private void actionViewSamplesOFF ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				isDisplaySamples = false;

				int imagIndex = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				//---- Get path to the original image
				String filePath = DataController.getTable().getElement(imagIndex).getDataFile().getFilePath();

				//---- Load the image into the image viewer
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygonStop();
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(filePath);

				//---- Change button icon
				ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_VIEW_SAMPLES);

				mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setIcon(iconButton);
				mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setActionCommand(FormMainHandlerCommands.AC_VIEW_SAMPLES_ON);
				mainFormLink.getComponentToolbar().getComponentButtonViewSamples().setToolTipText("View detected samples");


			}

		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------
	//---- ADDITIONAL FORMS
	//-----------------------------------------------------------------------------------------

	private void actionFormAbout ()
	{
		FormAbout formAbout = new FormAbout();
		formAbout.displayFrame(mainFormLink.getMainFrame());
	}

	private void actionFormHelp ()
	{
		FormHelp formHelp = new FormHelp();
		formHelp.displayFrame(mainFormLink.getMainFrame());
	}

	private void actionFormSettings ()
	{
		FormSettings formSettings = new FormSettings();
		formSettings.displayFrame(mainFormLink.getMainFrame());
	}

	//-----------------------------------------------------------------------------------------
	//---- INTERACTIONS WITH THE GUI
	//-----------------------------------------------------------------------------------------

	/**
	 * This action is invoked when user clicks and selects another file in the project manager panel.
	 */
	private void actionComboboxChangedImage ()
	{

		int tableSize = DataController.getTable().getTableSize();

		//---- Check if the project is empty or not
		if (tableSize != 0)
		{
			if (isDisplaySamples) { helperDisplayDetectedSamples(); }
			else { helperDisplayInputImage(); }

			helperDisplaySamplesCombobox();
			helperDisplayResults();
			helperDisplayChart(true);
		}
	}

	/**
	 * This action is invoked when user clicks and selects another sample in the project manager panel.
	 */
	private void actionComboboxChangedSample ()
	{
		int tableSize = DataController.getTable().getTableSize();

		//---- Check if the project is empty or not
		if (tableSize != 0)
		{
			helperDisplaySampleLocation();
			helperDisplayResults();
		}
	}

	/**
	 * This action is invoked when user changes the mode of sample detection from manual to automatic and back.
	 */
	private void actionSampleDetectionModeChanged ()
	{
		if (mainFormLink.getComponentPanelLeft().getComponentRadioModeAuto().isSelected())
		{
			mainFormLink.getComponentPanelLeft().getComponentSampleAdd().setEnabled(false);
			mainFormLink.getComponentPanelLeft().getComponentSampleAddControl().setEnabled(false);
			mainFormLink.getComponentPanelLeft().getComponentSampleDelete().setEnabled(false);
		}

		if (mainFormLink.getComponentPanelLeft().getComponentRadioModeManual().isSelected())
		{
			mainFormLink.getComponentPanelLeft().getComponentSampleAdd().setEnabled(true);
			mainFormLink.getComponentPanelLeft().getComponentSampleAddControl().setEnabled(true);
			mainFormLink.getComponentPanelLeft().getComponentSampleDelete().setEnabled(true);
		}
	}

	//-----------------------------------------------------------------------------------------
	//---- EXPORT, SAVE DATA
	//-----------------------------------------------------------------------------------------

	/**
	 * Save all calculated output data for the selected image into a file.
	 */
	private void actionSaveOutput ()
	{
		try
		{			
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				JFileChooser saveFileChooser = new JFileChooser();

				FileNameExtensionFilter typeCSV = new FileNameExtensionFilter("CSV", "csv", "CSV");
				saveFileChooser.addChoosableFileFilter(typeCSV);

				FileNameExtensionFilter typeTXT = new FileNameExtensionFilter("TXT", "txt", "TXT");
				saveFileChooser.addChoosableFileFilter(typeTXT);

				saveFileChooser.setAcceptAllFileFilterUsed(false);
				saveFileChooser.setFileFilter(typeCSV);

				int isOpen = saveFileChooser.showOpenDialog(saveFileChooser);

				if (isOpen == JFileChooser.APPROVE_OPTION) 
				{
					String filePath = saveFileChooser.getSelectedFile().getPath();

					int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

					/*!*/DebugLogger.logMessage("Saving output of a single image " + indexImage + " to file " + filePath, LOG_MESSAGE_TYPE.INFO);

					if (indexImage >= 0 && indexImage < tableSize)
					{
						if (saveFileChooser.getFileFilter().equals(typeCSV))
						{
							if (!filePath.substring(filePath.length() - 4).equals(".csv")) { filePath += ".csv"; }

							//---- Save file in a CSV format
							OutputController.saveFile(filePath, DataController.getTable(), indexImage, OutputController.OUTPUT_FORMAT.CSV);
						}
						else
						{
							if (!filePath.substring(filePath.length() - 4).equals(".txt")) { filePath += ".txt"; }

							//---- Save file in a txt format
							OutputController.saveFile(filePath, DataController.getTable(), indexImage, OutputController.OUTPUT_FORMAT.TXT);
						}

					}
				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Save all calculated output data for every image in the project into a file
	 */
	private void actionSaveOutputAll ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				JFileChooser saveFileChooser = new JFileChooser();

				FileNameExtensionFilter typeCSV = new FileNameExtensionFilter("CSV", "csv", "CSV");
				saveFileChooser.addChoosableFileFilter(typeCSV);

				FileNameExtensionFilter typeTXT = new FileNameExtensionFilter("TXT", "txt", "TXT");
				saveFileChooser.addChoosableFileFilter(typeTXT);

				saveFileChooser.setAcceptAllFileFilterUsed(false);
				saveFileChooser.setFileFilter(typeCSV);

				int isOpen = saveFileChooser.showOpenDialog(saveFileChooser);

				if (isOpen == JFileChooser.APPROVE_OPTION) 
				{
					String filePath = saveFileChooser.getSelectedFile().getPath();

					/*!*/DebugLogger.logMessage("Saving output of all images to file " + filePath, LOG_MESSAGE_TYPE.INFO);

					if (saveFileChooser.getFileFilter().equals(typeCSV))
					{
						if (!filePath.substring(filePath.length() - 4).equals(".csv")) { filePath += ".csv"; }

						OutputController.saveFileAll(filePath, DataController.getTable(), OUTPUT_FORMAT.CSV);
					}
					else
					{
						if (!filePath.substring(filePath.length() - 4).equals(".txt")) { filePath += ".txt"; }

						OutputController.saveFileAll(filePath, DataController.getTable(), OUTPUT_FORMAT.TXT);
					}
				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	private void actionExportFeatureVector ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			//---- Check if the project is empty or not
			if (tableSize != 0)
			{
				JFileChooser saveFileChooser = new JFileChooser();

				FileNameExtensionFilter typeCSV = new FileNameExtensionFilter("CSV", "csv", "CSV");
				saveFileChooser.addChoosableFileFilter(typeCSV);

				saveFileChooser.setAcceptAllFileFilterUsed(false);
				saveFileChooser.setFileFilter(typeCSV);

				int isOpen = saveFileChooser.showOpenDialog(saveFileChooser);

				if (isOpen == JFileChooser.APPROVE_OPTION) 
				{
					String filePath = saveFileChooser.getSelectedFile().getPath();

					OutputController.exportFeatureVector(filePath, DataController.getTable());
				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}

	}

	/**
	 * Export the displayed image with location of samples.
	 */
	private void actionExportImageSampleArea ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					JFileChooser saveFileChooser = new JFileChooser();

					FileNameExtensionFilter fileTypeBMP = new FileNameExtensionFilter("BMP", "bmp", "BMP");
					saveFileChooser.addChoosableFileFilter(fileTypeBMP);	 
					FileNameExtensionFilter fileTypeJPG = new FileNameExtensionFilter("JPEG", "jpg", "jpeg", "JPG", "JPEG");
					saveFileChooser.addChoosableFileFilter(fileTypeJPG);

					saveFileChooser.setAcceptAllFileFilterUsed(false);
					saveFileChooser.setFileFilter(fileTypeBMP);

					int isSelectedFile = saveFileChooser.showOpenDialog(saveFileChooser);

					if (isSelectedFile == JFileChooser.APPROVE_OPTION) 
					{
						String outputFilePath = saveFileChooser.getSelectedFile().getPath();

						if (saveFileChooser.getFileFilter().equals(fileTypeBMP)) { outputFilePath += ".bmp"; }
						else if (saveFileChooser.getFileFilter().equals(fileTypeJPG)) { outputFilePath += ".jpg"; }

						OutputController.exportImageSampleArea(outputFilePath, DataController.getTable().getElement(indexImage));

					}

				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Export image with colored cells into a file.
	 */
	private void actionExportImageColoredCells ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					JFileChooser saveFileChooser = new JFileChooser();

					FileNameExtensionFilter fileTypeBMP = new FileNameExtensionFilter("BMP", "bmp", "BMP");
					saveFileChooser.addChoosableFileFilter(fileTypeBMP);	 
					FileNameExtensionFilter fileTypeJPG = new FileNameExtensionFilter("JPEG", "jpg", "jpeg", "JPG", "JPEG");
					saveFileChooser.addChoosableFileFilter(fileTypeJPG);

					saveFileChooser.setAcceptAllFileFilterUsed(false);
					saveFileChooser.setFileFilter(fileTypeBMP);

					int isSelectedFile = saveFileChooser.showOpenDialog(saveFileChooser);

					if (isSelectedFile == JFileChooser.APPROVE_OPTION) 
					{
						String outputFilePath = saveFileChooser.getSelectedFile().getPath();

						if (saveFileChooser.getFileFilter().equals(fileTypeBMP)) { outputFilePath += ".bmp"; }
						else if (saveFileChooser.getFileFilter().equals(fileTypeJPG)) { outputFilePath += ".jpg"; }

						OutputController.exportImageColoredCells(outputFilePath, DataController.getTable().getElement(indexImage));
					}

				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Export an image with cells marked with bounding boxes.
	 */
	private void actionExportImageBoundingBox ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					JFileChooser saveFileChooser = new JFileChooser();

					FileNameExtensionFilter fileTypeBMP = new FileNameExtensionFilter("BMP", "bmp", "BMP");
					saveFileChooser.addChoosableFileFilter(fileTypeBMP);	 
					FileNameExtensionFilter fileTypeJPG = new FileNameExtensionFilter("JPEG", "jpg", "jpeg", "JPG", "JPEG");
					saveFileChooser.addChoosableFileFilter(fileTypeJPG);

					saveFileChooser.setAcceptAllFileFilterUsed(false);
					saveFileChooser.setFileFilter(fileTypeBMP);

					int isSelectedFile = saveFileChooser.showOpenDialog(saveFileChooser);

					if (isSelectedFile == JFileChooser.APPROVE_OPTION) 
					{
						String outputFilePath = saveFileChooser.getSelectedFile().getPath();

						if (saveFileChooser.getFileFilter().equals(fileTypeBMP)) { outputFilePath += ".bmp"; }
						else if (saveFileChooser.getFileFilter().equals(fileTypeJPG)) { outputFilePath += ".jpg"; }

						OutputController.exportImageBoundingBox(outputFilePath, DataController.getTable().getElement(indexImage));
					}

				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Export the displayed chart of the length frequencies.
	 * @param isFrequency
	 */
	private void actionExportImageChart (boolean isFrequency)
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					JFileChooser saveFileChooser = new JFileChooser();

					FileNameExtensionFilter fileTypeBMP = new FileNameExtensionFilter("BMP", "bmp", "BMP");
					saveFileChooser.addChoosableFileFilter(fileTypeBMP);	 
					FileNameExtensionFilter fileTypeJPG = new FileNameExtensionFilter("JPEG", "jpg", "jpeg", "JPG", "JPEG");
					saveFileChooser.addChoosableFileFilter(fileTypeJPG);

					saveFileChooser.setAcceptAllFileFilterUsed(false);
					saveFileChooser.setFileFilter(fileTypeBMP);

					int isSelectedFile = saveFileChooser.showOpenDialog(saveFileChooser);

					if (isSelectedFile == JFileChooser.APPROVE_OPTION) 
					{
						String outputFilePath = saveFileChooser.getSelectedFile().getPath();

						if (saveFileChooser.getFileFilter().equals(fileTypeBMP)) { outputFilePath += ".bmp"; }
						else if (saveFileChooser.getFileFilter().equals(fileTypeJPG)) { outputFilePath += ".jpg"; }

						helperDisplayChart(isFrequency);

						//Save displayed cjart directly from the panel
						mainFormLink.getComponentPanelRight().getComponentPanelChart().saveChart(outputFilePath);
					}

				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------
	//---- HELPERS
	//-----------------------------------------------------------------------------------------

	/**
	 * Display project files in the combo box of the gui.
	 */
	private void helperDisplayProjectFiles ()
	{
		try
		{
			String[] fileNameList = DataController.scenarioGetFileNames();

			mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().removeAllItems();

			for (int i = 0; i < fileNameList.length; i++)
			{
				mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().addItem(fileNameList[i]);
			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	/**
	 * Display the selected file in the image viewer panel
	 */
	private void helperDisplayInputImage()
	{
		try
		{
			//---- Get currently selected file index in the combo box
			int index = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

			//---- Check the index if the action is invoked by image deletion then causes error
			int imageCount = DataController.getTable().getTableSize();

			if (index >= 0 && index < imageCount)
			{
				//---- Get file path of the table

				String filePath = DataController.getTable().getElement(index).getDataFile().getFilePath();

				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygonStop();
				mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(filePath);
			}
		}
		catch (ExceptionMessage e)
		{
			ExceptionHandler.processException(e);
		}
	}

	/**
	 * Display detected samples.
	 * @throws ExceptionMessage 
	 */
	private void helperDisplayDetectedSamples ()
	{
		try
		{
			int imagIndex = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();
			//---- Get the image with colored cells, convert it to buffered image instead of opencv Mat.
			BufferedImage imageCC = OpencvConverter.convertMatToBImage(ImageDraw.drawImageSampleArea(DataController.getTable().getElement(imagIndex)));

			//---- Display image at the image viewer panel
			mainFormLink.getComponentPanelCenter().getComponentPanelImageView().loadImage(imageCC);
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Switches between sample selection modes.
	 */
	private void actionSwitchSelect()
	{
		if (FormMainMouse.isSampleSelectOn)
		{
			helperSwitchSelectOff();
		}
		else
		{
			helperSwitchSelectOn();

			//---- If we can select sample, we can not move it
			helperSwitchMoveOff();
		}
	}

	/**
	 * Turns on the sample area selection.
	 */
	private void helperSwitchSelectOn()
	{
		ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT_PRESSED);

		mainFormLink.getComponentToolbar().getComponentButtonSelect().setIcon(iconButton);

		FormMainMouse.isSampleSelectOn = true;
	}

	/**
	 * Turns off the sample area selection.
	 */
	private void helperSwitchSelectOff()
	{
		ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SELECT);

		mainFormLink.getComponentToolbar().getComponentButtonSelect().setIcon(iconButton);

		FormMainMouse.isSampleSelectOn = false;
	}

	/**
	 * Switches between sample move modes.
	 */
	private void actionSwitchMove()
	{
		if (FormMainMouse.isSampleMovable)
		{
			helperSwitchMoveOff();
		}
		else
		{
			helperSwitchMoveOn();

			//---- Turn off sample selection mode
			helperSwitchSelectOff();
		}
	}

	/**
	 * Turns on sample moving.
	 */
	private void helperSwitchMoveOn()
	{
		FormMainMouse.isSampleMovable = true;

		ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_MOVE_ACTIVE);

		mainFormLink.getComponentToolbar().getComponentButtonMove().setIcon(iconButton);
	}

	/**
	 * Turns off sample moving
	 */
	private void helperSwitchMoveOff()
	{
		FormMainMouse.isSampleMovable = false;

		ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_MOVE);

		mainFormLink.getComponentToolbar().getComponentButtonMove().setIcon(iconButton);
	}

	//-----------------------------------------------------------------------------------------
	//----- Estimatro settings changed

	/**
	 * Response to switching estimator ON.
	 */
	private void actionChangedEstimatorON ()
	{
		mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetFile().setEnabled(true);
		mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetManual().setEnabled(true);

		mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().setEnabled(false);

		//---- Switch on if manual mode on
		if (mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetManual().isSelected())
		{
			mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().setEnabled(true);
			mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().setEnabled(true);
		}

		mainFormLink.getComponentPanelLeft().getComboboxEstimatorAlgorithmType().setEnabled(true);
	}

	/**
	 * Response to switching estimator OFF
	 */
	private void actionChangedEstimatorOFF ()
	{
		mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetFile().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetManual().setEnabled(false);

		mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComboboxEstimatorAlgorithmType().setEnabled(false);
	}

	/**
	 * Response to switching estimator settings to automatic scanning of parameters from the file name.
	 */
	private void actionChangedEstimatorSettingsAuto ()
	{
		mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().setEnabled(false);
	}

	/**
	 * Response to switching estimator settings to manual set up.
	 */
	private void actionChangedEstimatorSettingsManual ()
	{
		mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().setEnabled(true);
		mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().setEnabled(true);
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Display all samples corresponding to the selected file in the combobox.
	 */
	private void helperDisplaySamplesCombobox ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					//---- Get number of samples and index of the control sample
					int samplesCount = DataController.getTable().getElement(indexImage).getChannelCount();
					int indexControlSample = DataController.getTable().getElement(indexImage).getDataDevice().getControlChannelIndex();

					mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().removeAllItems();
					mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().setEnabled(true);

					for (int i = 0; i < samplesCount; i++)
					{
						String displayedSampleName = "";

						if (i == indexControlSample) { displayedSampleName = "control " + (i+1); }
						else { displayedSampleName = "sample " + (i+1);}

						mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().addItem(displayedSampleName);
					}
				}
			}

		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display all calculated results in the result table.
	 */
	private void helperDisplayResults ()
	{

		try
		{
			int tableSize = DataController.getTable().getTableSize();

			//---- Check if the project is empty or not
			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{

					int samplesCount = DataController.getTable().getElement(indexImage).getChannelCount();

					for (int i = 0; i < Math.min(samplesCount, FormMainPanelRight.TABLE_SIZE); i++)
					{
						//---- ABSOLUTE  --------------------------------------------

						//---- Count
						int featureCount = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellCount();
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featureCount, 0, i + 1);

						//---- Length
						double featureLength = (double) Math.round(DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellLengthMean() * 1000) / 1000;
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featureLength, 1, i + 1);

						//---- Pixel density
						double featurePixelDensity = (double) Math.round (DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsolutePixelDensity() * 1000) / 1000;
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featurePixelDensity, 2, i + 1);

						//---- RELATIVE --------------------------------------------

						//---- Count
						double featureCountR = (double) Math.round(DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellCount() * 1000) / 1000;
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featureCountR, 3, i + 1);

						//---- Length
						double featureLengthR = (double) Math.round(DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellLengthMean() * 1000) / 1000;
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featureLengthR, 4, i + 1);

						//---- Pixel density
						double featurePixelDensityR = (double) Math.round (DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellPixelDensity() * 1000) / 1000;
						mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt(featurePixelDensityR, 5, i + 1);

						//---- SENSETIVITY
						Sensitivity sampleSensitivity = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getSensitivity();

						switch (sampleSensitivity)
						{
						case RESISTANT: mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("R", 6, i + 1); break;
						case SENSITIVE: mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("S", 6, i + 1); break;
						case UNKNOWN: mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("U", 6, i + 1); break;
						}
					}

				}
			}
			else
			{
				for (int i = 0; i < FormMainPanelRight.TABLE_SIZE; i++)
				{
					//---- ABSOLUTE
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 0, i + 1);
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 1, i + 1);
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 2, i + 1);

					//---- RELATIVE
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 3, i + 1);
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 4, i + 1);
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 5, i + 1);

					//---- SENSETIVITY
					mainFormLink.getComponentPanelRight().getComponentTableFeatures().setValueAt("", 6, i + 1);
				}



			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display a chart of the calculated feature in the chart box.
	 */
	private void helperDisplayChart (boolean isFrequency)
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			if (tableSize != 0)
			{
				/*!*/DebugLogger.logMessage("Displaying the frequency chart", LOG_MESSAGE_TYPE.INFO);

				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

				if (indexImage >= 0 && indexImage < tableSize)
				{
					mainFormLink.getComponentPanelRight().getComponentPanelChart().resetChartData();

					int sampleCount = DataController.getTable().getElement(indexImage).getChannelCount();

					for (int i = 0; i < sampleCount; i++)
					{

						/*!*/DebugLogger.logMessage("Converting data to display in the chart", LOG_MESSAGE_TYPE.INFO);

						//---- Prepare data for displaying in the chart box
						int chartStartX = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellLengthIntervalMin();
						int chartOffsetX = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellLengthFrequencyHistogramOffset();

						double[] chartDataY = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellLengthFrequencyHistogram();

						//---- if features has not been extracted return
						if (chartDataY == null) 
						{
							/*!*/DebugLogger.logMessage("Chart data is NULL", LOG_MESSAGE_TYPE.INFO);
							return; 
						}

						double[] chartDataX = new double[chartDataY.length];

						int cellCount = DataController.getTable().getElement(indexImage).getDataDevice().getChannel(i).getFeatureVector().getAbsoluteCellCount();

						/*!*/DebugLogger.logMessage("Chart data size is: " + chartDataY.length, LOG_MESSAGE_TYPE.INFO);

						for (int k = 0; k < chartDataY.length; k++)
						{
							chartDataX[k] = chartStartX + k * chartOffsetX;
						}

						if (isFrequency)
						{
							for (int k = 0; k < chartDataY.length; k++)
							{
								chartDataY[k] = chartDataY[k] / cellCount;
							}
						}

						String chartDataName = "";

						if (i == DataController.getTable().getElement(indexImage).getDataDevice().getControlChannelIndex())
						{ chartDataName = "control " + (i+1); }
						else
						{ chartDataName = "sample" + (i+1); }

						/*!*/DebugLogger.logMessage("Loading data to the chart panel", LOG_MESSAGE_TYPE.INFO);

						mainFormLink.getComponentPanelRight().getComponentPanelChart().addChartData(chartDataX, chartDataY, chartDataName, isFrequency);
					}

					mainFormLink.getComponentPanelRight().getComponentPanelChart().displayChartData();
				}

			}

		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	/**
	 * Display an area, occupied by the selected sample.
	 */
	private void helperDisplaySampleLocation ()
	{
		try
		{
			int tableSize = DataController.getTable().getTableSize();

			//---- Check if the project is empty or not
			if (tableSize != 0)
			{
				int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();
				int indexSample = mainFormLink.getComponentPanelLeft().getComponentComboboxSampleName().getSelectedIndex();

				int samplesCount = DataController.getTable().getElement(indexImage).getChannelCount();

				if (samplesCount != 0 && indexSample >= 0 && indexSample < samplesCount)
				{
					//---- Get points of the sample polygon
					Point[] samplePolygon = DataController.getTable().getElement(indexImage).getDataImage().getChannel(indexSample).getChannelLocationPoints();

					if (samplePolygon != null)
					{
						mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygon(samplePolygon);
						mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();
					}
				}
			}
		}
		catch (ExceptionMessage exception)
		{
			ExceptionHandler.processException(exception);
		}
	}

	//-----------------------------------------------------------------------------------------

	private TaskSettings helperCaptureSettings () throws ExceptionMessage
	{
		int indexImage = mainFormLink.getComponentPanelLeft().getComponentComboboxFileName().getSelectedIndex();

		TaskSettings settings = new TaskSettings();

		//---- Set the sample detection mode. If manual mode was chosen, check if at least one control
		//---- and one test sample have been added.
		if (mainFormLink.getComponentPanelLeft().getComponentRadioModeManual().isSelected())
		{
			int samplesCount = DataController.getTable().getElement(indexImage).getChannelCount();
			int contolSampleIndex = DataController.getTable().getElement(indexImage).getDataDevice().getControlChannelIndex();

			if (samplesCount < 2 || contolSampleIndex == -1) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSMANUALMODE); }

			settings.setIsManualMode(true);
		}
		else
		{
			settings.setIsManualMode(false);
		}

		//--- Get the parameters of the total count of sample to detect (in the automati processing mode) and
		//--- the index of the control sample
		int samplesCount = 0;
		try { samplesCount = Integer.parseInt(mainFormLink.getComponentPanelLeft().getComponentTextfieldSamplesCount().getText()); }
		catch (Exception e) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSSAMPLESCOUNT); }

		int indexControlSample = -1;
		try { indexControlSample = Integer.parseInt(mainFormLink.getComponentPanelLeft().getComponentTextfieldControlSample().getText()); }
		catch (Exception e) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSINDEXCONTROL); }

		if (indexControlSample <= 0 || indexControlSample >= samplesCount) 
		{ throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSINDEXCONTROL); }

		settings.setSamplesCount(samplesCount);

		//---- Displayed index and interanal index are different
		//---- Internal starts from 0
		//---- Displayed starts from 1
		//---- Don't forget to perform conversion here
		settings.setIndexControlSample(indexControlSample - 1);

		//---- set cell detection algorithm
		int indexAlgorithm = mainFormLink.getComponentPanelLeft().getComponentComboboxAlgorithmType().getSelectedIndex();

		//---- If something went wrong throw exception
		if (indexAlgorithm < 0 || indexAlgorithm >= EnumTypes.AlgorithmTypeValue.length) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE); }

		settings.setAlgorithmType(EnumTypes.AlgorithmTypeValue[indexAlgorithm]);

		//---- Estimator settings
		if (mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorOFF().isSelected()) { settings.setIsEstimateSensitivity(false); }
		if (mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorON().isSelected())
		{
			settings.setIsEstimateSensitivity(true);

			if (mainFormLink.getComponentPanelLeft().getComponentRadioEstimatorSetFile().isSelected()) 
			{ 
				settings.setIsEstimatorUseFileName(true);
			}
			else 
			{ 
				settings.setIsEstimatorUseFileName(false);

				int selectedIndexBacteriaType = mainFormLink.getComponentPanelLeft().getCombobobxEstimatorBacteriaType().getSelectedIndex();
				int selectedIndexDrugType = mainFormLink.getComponentPanelLeft().getComboboxEstimatorDrugType().getSelectedIndex();

				//----FIXME
				/*!!!*///Be carefull here!
				settings.setEstimatorBacteriaType(EnumTypes.BacteriaTypeValue[selectedIndexBacteriaType]);
				settings.setEstimatorDrugType(EnumTypes.DrugTypeValue[selectedIndexDrugType]);
			}

			int selectedIndexAlgorithmType = mainFormLink.getComponentPanelLeft().getComboboxEstimatorAlgorithmType().getSelectedIndex();
			if (selectedIndexAlgorithmType == 0) {settings.setEstimatorAlgorithm(ESTIMATOR_ALG.EMPIRIC); }
			else { settings.setEstimatorAlgorithm(ESTIMATOR_ALG.SVM); }
		}

		/*!*/DebugLogger.logMessage("Captured GUI settings", LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Samples count:" + samplesCount, LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Control sample: " + indexControlSample, LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Cell detection algorithm: " + settings.getAlgorithmType().toString(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Manual mode: " + settings.getIsManualMode(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Is estimate sensitivity: " + settings.getIsEstimateSensitivity(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator auto settings: " + settings.getIsEstimatorUseFileName(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator bacteria type: " + settings.getEstimatorBacteriaType().toString(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator drug type: " + settings.getEstimatorDrugType().toString(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator algorithm: " + settings.getEstimatorAlgorithm().toString(), LOG_MESSAGE_TYPE.INFO);

		return settings;
	}

	//-----------------------------------------------------------------------------------------
	//---- CALLBACK (responses on callback, which specify if the processing starts, stops, terminates)
	//-----------------------------------------------------------------------------------------

	public void callbackMessageProcessingStart()
	{
		//---- Block the buttons, so user cannot click on them
		mainFormLink.getComponentToolbar().getComponentButtonAddFile().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonAddFolder().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonNewProject().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonProcess().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonProcessAll().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonAppend().setEnabled(false);
		mainFormLink.getComponentToolbar().getComponentButtonSave().setEnabled(false);

		//---- Turn on the animation of the progress bar
		mainFormLink.getComponentPanelDown().getComponentProgressBar().setIsDisplayAnimation(true);

		//---- Block the buttons responsible for manual sample selection
		mainFormLink.getComponentPanelLeft().getComponentRadioModeManual().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComponentSampleAdd().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComponentSampleAddControl().setEnabled(false);
		mainFormLink.getComponentPanelLeft().getComponentSampleDelete().setEnabled(false);
	}

	public void callbackMessageProcessingStop()
	{
		//---- Unblock the buttons
		mainFormLink.getComponentToolbar().getComponentButtonAddFile().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonAddFolder().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonNewProject().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonProcess().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonProcessAll().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonAppend().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonSave().setEnabled(true);

		mainFormLink.getComponentPanelDown().getComponentProgressBar().setIsDisplayAnimation(false);

		//---- Update the combobox to display samples
		helperDisplaySamplesCombobox();

		actionViewSamplesON();

		mainFormLink.getComponentPanelLeft().getComponentRadioModeManual().setEnabled(true);
		actionSampleDetectionModeChanged();

		helperDisplayChart(true);

		mainFormLink.getComponentPanelRight().getComponentTableFeatures().repaint();

		helperDisplayResults();
	}

	public void callbackMessageProcessingTerminated(ExceptionMessage exception)
	{
		/*!*/DebugLogger.logMessage("The background process has been terminated", exception);

		//---- Unblock the buttons
		mainFormLink.getComponentToolbar().getComponentButtonAddFile().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonAddFolder().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonNewProject().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonProcess().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonProcessAll().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonAppend().setEnabled(true);
		mainFormLink.getComponentToolbar().getComponentButtonSave().setEnabled(true);

		mainFormLink.getComponentPanelDown().getComponentProgressBar().setIsDisplayAnimation(false);

		//---- Process the received exception
		ExceptionHandler.processException(exception);
	}
}


