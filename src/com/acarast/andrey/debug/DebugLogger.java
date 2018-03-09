package com.acarast.andrey.debug;

import java.awt.Color;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.exception.ExceptionMessage;

/**
 * Class for debugging purposes. Contains methods to output data/images.
 * @author Andrey G
 */
public class DebugLogger
{
	public static enum LOG_MESSAGE_TYPE {INFO, CONFIG, WARNING, SEVERE};

	//---- Switcher of the debug mode. In debug mode the app will display messages and log
	//---- all activity.
	private static boolean IS_DEBUG_MODE_ON = false;
	private static boolean IS_OUTPUT_MAT = false;
	
	public static boolean IS_SUPRESS_EXCEPTION = false;

	//---- Logger logs everything what happens.
	private static final Logger loggerController = Logger.getLogger(DebugLogger.class.getName());

	private static Handler loggerHandlerConsole = null;
	private static Handler loggerHandlerFile = null;

	//-----------------------------------------------------------------------------------------

	public static void switchDebugModeON ()
	{
		try
		{
			//DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd-HHmmss");

			//String fileName = dateFormat.format(new Date()) + ".log";
			String fileName = "debug.log";

			//---- Create logger handlers
			loggerHandlerConsole = new ConsoleHandler();
			loggerHandlerFile = new FileHandler(fileName);

			//---- Set logging levels
			loggerHandlerConsole.setLevel(Level.ALL);
			loggerHandlerConsole.setFormatter(new LogFormatter());

			loggerHandlerFile.setLevel(Level.ALL);


			IS_DEBUG_MODE_ON = true;

			loggerController.setUseParentHandlers(false);

			//loggerController.addHandler(loggerHandlerConsole);
			loggerController.addHandler(loggerHandlerFile);
			loggerController.addHandler(loggerHandlerConsole);
		}
		catch (Exception e) {}
	}

	public static void switchDebugModeOFF ()
	{
		IS_DEBUG_MODE_ON = false;

		loggerHandlerConsole = null;
		loggerHandlerFile = null;
	}

	public static void switchDebugMatOutputON ()
	{
		IS_OUTPUT_MAT = true;
	}

	public static void switchDebugMatOutpuOFF ()
	{
		IS_OUTPUT_MAT = false;
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Print out the contents of the data table, if in debug mode
	 * @param table
	 */
	public static void debugPrintDataTable (DataTable table)
	{
		if (!IS_DEBUG_MODE_ON) { return; }

		try
		{
			int tableSize = table.getTableSize();

			System.out.println("Elements in table: " + tableSize);

			for (int i = 0; i < tableSize; i++)
			{
				System.out.println("----------[" + i + "]----------");

				System.out.println("File path: " + table.getElement(i).getDataFile().getFilePath());
				System.out.println("File name: " + table.getElement(i).getDataFile().getFileName());

				System.out.println("Samples count (image container): " +  table.getElement(i).getDataImage().getChannelCount());
				System.out.println("Index of the control sample: " + table.getElement(i).getDataDevice().getControlChannelIndex());
			}

		}
		catch(ExceptionMessage exception)
		{
			System.out.println("Error in debugger! Something is really wrong.");
		}
	}

	/**
	 * Displays a message, if in debug mode
	 * @param message
	 */
	public static void debugDisplayMessage (String message)
	{
		if (!IS_DEBUG_MODE_ON) { return; }

		System.out.println(message);
	}

	/**
	 * Saves an opencv matrix into file, if in debug mode
	 * @param outputFile
	 * @param matrix
	 */
	public static void debugSaveMat (String outputFile, Mat matrix)
	{
		if (!IS_DEBUG_MODE_ON) { return; }
		if (!IS_OUTPUT_MAT) { return; }

		Imgcodecs.imwrite(outputFile, matrix);
	}

	/**
	 * Save labelled matrix with false colours
	 * @param message
	 * @param type
	 */
	public static void debugSaveLabelled (String outputFile, Mat matrix)
	{
		if (!IS_DEBUG_MODE_ON) { return; }
		if (!IS_OUTPUT_MAT) { return; }

		Mat outputMat = Mat.zeros(matrix.size(), CvType.CV_8UC3);

		for (int row = 0; row < matrix.rows(); row++)
		{
			for (int col = 0; col < matrix.cols(); col++)
			{
				int value = (int) matrix.get(row, col)[0];
				
				if (value != 0)
				{
					Color color = Color.getHSBColor((float) value / (float) 50, 0.85f, 1.0f);
					
					outputMat.put(row, col, new double[] {color.getRed(), color.getGreen(), color.getBlue()});
				}
			
			}
		}


		Imgcodecs.imwrite(outputFile, outputMat);
	}

	//-----------------------------------------------------------------------------------------

	public static void logMessage (String message, LOG_MESSAGE_TYPE type)
	{
		if (!IS_DEBUG_MODE_ON) { return; }

		switch (type)
		{
		case CONFIG: 	loggerController.config(message); return;
		case INFO:	 	loggerController.info(message); return;
		case SEVERE: 	loggerController.info(message); return;
		case WARNING:	loggerController.warning(message); return;
		default: return;
		}
	}

	public static void logMessage (String message, ExceptionMessage exception)
	{
		if (!IS_DEBUG_MODE_ON) { return; }

		loggerController.log(Level.SEVERE, message, exception);
	}
}
