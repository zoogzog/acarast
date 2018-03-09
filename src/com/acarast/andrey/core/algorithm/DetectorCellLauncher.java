package com.acarast.andrey.core.algorithm;

import org.opencv.core.Mat;

import com.acarast.andrey.core.task.TaskSettings;
import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;

/**
 * Class for performing detection of cells in the input image.
 * @author Andrey G
 */

public class DetectorCellLauncher
{
	/**
	 * Performs detection of the cells in the image. Stores in the data table.
	 * @param table
	 * @param indexImage
	 * @param cellMask
	 * @param settings
	 * @throws ExceptionMessage
	 */
	public static void detectCells(DataTable table, int indexImage, Mat cellMask, TaskSettings settings) throws ExceptionMessage
	{
		switch(settings.getAlgorithmType())
		{
		case SPLIT_AND_MERGE: detectCellsAlgorithmAndrey(table, indexImage, cellMask, settings); break;
		case GRAPH_MERGE: detectCellsAlgorithmKikuchi(table, indexImage, cellMask, settings); break;
		}
	}

	//----------------------------------------------------------------

	/**
	 * Perform cell detection with the algorithm developed by Grushnikov Andrey.
	 * @param table
	 * @param indexImage
	 * @param cellMask
	 * @param settings
	 * @throws ExceptionMessage
	 */
	private static void detectCellsAlgorithmAndrey (DataTable table, int indexImage, Mat cellMask, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Starting cell detection. Algorithm: andrey", LOG_MESSAGE_TYPE.INFO);
		
		com.acarast.andrey.core.algorithm.DetectorCell.run(table.getElement(indexImage), cellMask);
	}

	/**
	 * Perform cell detection with the algorithm developed by Kazuma Kikuchi.
	 * @param table
	 * @param indexImage
	 * @param cellMask
	 * @param settings
	 * @throws ExceptionMessage 
	 */
	private static void detectCellsAlgorithmKikuchi (DataTable table, int indexImage, Mat cellMask, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Starting cell detection. Algorithm: kikuchi", LOG_MESSAGE_TYPE.INFO);
		
		com.acarast.kikuchi.core.algorithm.DetectorCell.run(table.getElement(indexImage), cellMask);
	}

	
}
