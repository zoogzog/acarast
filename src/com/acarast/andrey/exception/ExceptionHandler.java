package com.acarast.andrey.exception;

import java.awt.Point;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;

/**
 * Class for handling exceptions
 * @author Andrey G
 */
public class ExceptionHandler
{
	private static boolean isModeOutputConsole = false;
	private static boolean isModeOutputGui = false;
	private static boolean isModeOutputFile = false;

	//---- This variable is needed for more detailed analysis of the errors
	//---- If it is not specified no additional information will be saved.
	private static DataTable tableLink = null;

	private static JFrame frameLink = null;

	//----------------------------------------------------------------

	/**
	 * Specifies the way where the exception will be shown.
	 * Console: the exception is displayed in console
	 * Gui: the exception is displayed in a separate window.
	 * File: the exception log is saved in a file error_log_$data.txt
	 */
	public static void setModeOutput (boolean isConsole, boolean isGui, boolean isFile)
	{
		isModeOutputConsole = isConsole;
		isModeOutputGui = isGui;
		isModeOutputFile = isFile;
	}

	public static void setDataTableLink (DataTable table)
	{
		tableLink = table;
	}

	public static void setJFrameLinkt (JFrame frame)
	{
		frameLink = frame;
	}

	//----------------------------------------------------------------

	public static void processException (ExceptionMessage exception)
	{
		/*!*/DebugLogger.logMessage("Exception has been caught. Code: " + exception.getCode(), LOG_MESSAGE_TYPE.WARNING);

		switch (exception.getCode())
		{
		//---- Fatal errors caused by a lazy programmer
		case EXCODE_INDEXOFRANGE: 			fatalError(exception);	break;
		case EXCODE_DATACONTROLLER_INIT: 	fatalError(exception);	break;
		case EXCODE_HANDLER_INIT: 			fatalError(exception);	break;
		case EXCODE_TABLEUNCOSISTENCY: 		fatalError(exception);	break;
		case EXCODE_FILENOTFOUND:			fatalError(exception); 	break;
		case EXCODE_FILEWRITEFAIL:			fatalError(exception); 	break;
		case EXCODE_MATRIXISNULL: 			fatalError(exception); 	break;
		case EXCODE_CELLDETECTION:			fatalError(exception); 	break;

		case EXCODE_SAMPLENOTDETECTED:		warningError(exception); break;
		case EXCODE_SETTINGSERROR:			warningError(exception); break;
		case EXCODE_SETTINGSINDEXCONTROL:	warningError(exception); break;
		case EXCODE_SETTINGSMANUALMODE:		warningError(exception); break;
		case EXCODE_SETTINGSSAMPLESCOUNT:	warningError(exception); break;

		}


	}

	//----------------------------------------------------------------

	/**
	 * Process fatal exception.
	 * @param exception
	 */
	private static void fatalError (ExceptionMessage exception)
	{
		/*!*/DebugLogger.logMessage("Exception " + exception.getCode(), exception);

		if (isModeOutputConsole) { displayExceptionConsole(exception, true); }
		if (isModeOutputGui) { displayExceptionGui(exception, true); }
		if (isModeOutputFile) { displayExceptionFile(exception, true); }
		
		//---- TERMINATE EXECUTION OF THIS APPLICATION
		System.exit(-1);
	}

	private static void warningError (ExceptionMessage exception)
	{
		/*!*/DebugLogger.logMessage("Exception " + exception.getCode(), exception);

		if (isModeOutputConsole) { displayExceptionConsole(exception, false); }
		if (isModeOutputGui) { displayExceptionGui(exception, false); }
	}

	//----------------------------------------------------------------

	/**
	 * Displays information about the occurred exception in the console box.
	 * @param exception
	 */
	private static void displayExceptionConsole (ExceptionMessage exception, boolean isFatal)
	{
		try
		{
			if (isFatal)
			{
				System.out.println("Exception code: " + exception.getCode().toString());

				//---- Saving table info
				if (tableLink != null)
				{
					System.out.println("Data table dump");
					System.out.println("--------------------------------------\n");
					System.out.println("Table size: " + tableLink.getTableSize());

					for (int i = 0; i < tableLink.getTableSize(); i++)
					{
						//---- Input file information 
						System.out.println("File path: " + tableLink.getElement(i).getDataFile().getFilePath());
						System.out.println("File name: " + tableLink.getElement(i).getDataFile().getFileName());

						//---- Number of channel samples in the sample set
						System.out.println("Sample set size: " + tableLink.getElement(i).getChannelCount());

						System.out.println("Control sample index: " + tableLink.getElement(i).getDataDevice().getControlChannelIndex());

						for (int j = 0; j < tableLink.getElement(i).getChannelCount(); j++)
						{
							//---- Sample location (polygon)
							Point[] chPol = tableLink.getElement(i).getDataImage().getChannel(j).getChannelLocationPoints();

							for (int k = 0; k < chPol.length; k++) { System.out.print("[" + chPol[k].x + "," + chPol[k].y + "] "); }
							System.out.println("");

							//---- Number of cells in this sample
							System.out.println("Channel ["+j +"] size " + tableLink.getElement(i).getDataImage().getChannel(j).getCellCollection().getCellCount());

							//---- Features (except histogram)
							System.out.println("Absolute cell count: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsoluteCellCount());
							System.out.println("Absolute cell length: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsoluteCellLengthMean());
							System.out.println("Absolute pixel density: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsolutePixelDensity());

							System.out.println("Relative cell count: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellCount());
							System.out.println("Relative cell length: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellLengthMean());
							System.out.println("Relative pixel density: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellPixelDensity());

							//---- Sensitivity
							System.out.println("Sensitivity: " + tableLink.getElement(i).getDataDevice().getChannel(j).getSensitivity().toString());
						}

						System.out.println("++++++++++++++++++++++++++++++++++++++\n");
					}
				}

				exception.printStackTrace();
			}
			else
			{
				/*!*/DebugLogger.debugDisplayMessage("Exception (not fatal) caught. Code: " + exception.getCode());
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			/*!*/DebugLogger.debugDisplayMessage("Problem with handling an exception. " + exception.getCode());
		}
	}

	/**
	 * Displays information about the occurred exception as a dialog box.
	 * @param exception
	 */
	private static void displayExceptionGui (ExceptionMessage exception, boolean isFatal)
	{
		if (frameLink != null)
		{
			if (isFatal)
			{
				String exceptionDialogMessage = "";

				exceptionDialogMessage += "Fatal exception occured. Terminated.\n\n";
				exceptionDialogMessage += "Exception code: " + exception.getCode().toString() + "\n";

				//---- Make a message to display
				exceptionDialogMessage += "Description: " + exception.getInfo();

				if (isModeOutputFile)
				{
					exceptionDialogMessage += "\n" + "Stack is dumped into an error log file.\n";
				}

				exceptionDialogMessage += "Please contact the developer for more information.";

				JOptionPane.showMessageDialog(frameLink,exceptionDialogMessage, "Fatal error", JOptionPane.ERROR_MESSAGE);
				
			}
			else
			{
				String exceptionDialogMessage = "";
				
				exceptionDialogMessage += exception.getInfo();
				
				JOptionPane.showMessageDialog(frameLink,exceptionDialogMessage, "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * Stores the information about the occurred exception in a file.
	 * @param exception
	 */
	private static void displayExceptionFile (ExceptionMessage exception, boolean isFatal)
	{  
		if (isFatal)
		{
			try
			{
				DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd-HHmmss");

				String fileName = "err-" + dateFormat.format(new Date()) + ".log";

				FileWriter outputFile = new FileWriter(fileName, false);
				PrintWriter outputStream = new PrintWriter(outputFile);

				outputStream.println("Exception code: " + exception.getCode().toString());

				//---- Saving table info
				if (tableLink != null)
				{
					outputStream.println("Data table dump");
					outputStream.println("--------------------------------------\n");
					outputStream.println("Table size: " + tableLink.getTableSize());

					for (int i = 0; i < tableLink.getTableSize(); i++)
					{
						//---- Input file information 
						outputStream.println("File path: " + tableLink.getElement(i).getDataFile().getFilePath());
						outputStream.println("File name: " + tableLink.getElement(i).getDataFile().getFileName());

						//---- Number of channel samples in the sample set
						outputStream.println("Sample set size: " + tableLink.getElement(i).getChannelCount());

						outputStream.println("Control sample index: " + tableLink.getElement(i).getDataDevice().getControlChannelIndex());

						for (int j = 0; j < tableLink.getElement(i).getChannelCount(); j++)
						{
							//---- Sample location (polygon)
							Point[] chPol = tableLink.getElement(i).getDataImage().getChannel(j).getChannelLocationPoints();

							for (int k = 0; k < chPol.length; k++) { outputStream.print("[" + chPol[k].x + "," + chPol[k].y + "] "); }
							outputStream.println("");

							//---- Number of cells in this sample
							outputStream.println("Channel ["+j +"] size " + tableLink.getElement(i).getDataImage().getChannel(j).getCellCollection().getCellCount());

							//---- Features (except histogram)
							outputStream.println("Absolute cell count: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsoluteCellCount());
							outputStream.println("Absolute cell length: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsoluteCellLengthMean());
							outputStream.println("Absolute pixel density: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getAbsolutePixelDensity());

							outputStream.println("Relative cell count: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellCount());
							outputStream.println("Relative cell length: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellLengthMean());
							outputStream.println("Relative pixel density: " + tableLink.getElement(i).getDataDevice().getChannel(j).getFeatureVector().getRelativeCellPixelDensity());

							//---- Sensitivity
							outputStream.println("Sensitivity: " + tableLink.getElement(i).getDataDevice().getChannel(j).getSensitivity().toString());
						}

						outputStream.println("++++++++++++++++++++++++++++++++++++++\n");
					}
				}

				outputStream.println("--------------------------------------\n");


				outputStream.println("Exception stack trace dump.");

				//----- Convert stack trace to a string and output to the file
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exception.printStackTrace(pw);

				String exceptionStackTrace = sw.toString();

				outputStream.println(exceptionStackTrace);

				outputStream.close();
				outputFile.close();
			}
			catch (Exception e)
			{
				/*!*/DebugLogger.logMessage("Fatal exception: saving exception file", LOG_MESSAGE_TYPE.SEVERE);
			}
		}
	}

}
