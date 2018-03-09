package com.acarast.kikuchi.core.algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.data.DataImage;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionHandler;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

/**
 * Launches parallel execution of the cell detection algorithm
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class CellDetectionThread extends Thread
{
	private DataCellCollection connectedRegionsData;

	private DataImage imageData;
	private int indexChannel;

	private Mat inputThinnedCellMask;
	private Mat inputThickCellMask;


	//----------------------------------------------------------------
	
	public void setSettings (DataImage image, int index, Mat cellMaskThin, Mat cellMaskThick, DataCellCollection collection)
	{
		imageData = image;
		indexChannel = index;
		
		inputThinnedCellMask = cellMaskThin;
		inputThickCellMask = cellMaskThick;
		
		connectedRegionsData = collection;
	}
	
	//----------------------------------------------------------------
	
	private static Mat getObjectMap (DataCellCollection cellCandidates, Mat inputImage) throws ExceptionMessage
	{
		Mat outputMap = Mat.zeros (inputImage.rows(), inputImage.cols(), CvType.CV_16UC1);

		for (int i = 0; i < cellCandidates.getCellCount(); i++)
		{
			for (int k = 0; k < cellCandidates.getCell(i).getPixelCount(); k++)
			{
				int row = cellCandidates.getCell(i).getPixel(k).x;
				int col = cellCandidates.getCell(i).getPixel(k).y;

				outputMap.put(row, col, new double[] {i + 1});
			}
		}

		return outputMap;
	}
	
	//----------------------------------------------------------------

	@Override
	public void run()
	{
		try
		{
			/*!*/DebugLogger.logMessage("THREAD " + indexChannel + ":Running cell detection", LOG_MESSAGE_TYPE.INFO);
			
			CellDetection.detectCells(connectedRegionsData);
		
			/*!*/DebugLogger.logMessage("THREAD " + indexChannel + ": Restoring data deleted during thinning", LOG_MESSAGE_TYPE.INFO);
			
			//---- Restore pixels which were deleted at the thinning step
			Mat sampleImage = Mat.zeros(inputThinnedCellMask.size(), 0);
			sampleImage.setTo(Scalar.all(0));
			Mat objectMap = getObjectMap(connectedRegionsData, sampleImage);
			
			Utils.getRealCells(connectedRegionsData, inputThickCellMask, objectMap);

			/*!*/DebugLogger.logMessage("THREAD " + indexChannel + ": loading cell collection to a container", LOG_MESSAGE_TYPE.INFO);
		
			//---- Here the results could be refined

			int THRESHOLD = 4;
			CellDetection.refineRemoveSmallCells(connectedRegionsData, getObjectMap(connectedRegionsData, sampleImage), THRESHOLD);
			
			//---- Store the calculated data
			imageData.getChannel(indexChannel).setCellCollection(connectedRegionsData);
			
		
		}
		catch(Exception e)
		{
			//---Throw a fatal exception
			if (!DebugLogger.IS_SUPRESS_EXCEPTION) { ExceptionHandler.processException(new ExceptionMessage(EXCEPTION_CODE.EXCODE_CELLDETECTION)); }
		}
	}

}
