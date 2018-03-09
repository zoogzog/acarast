package com.acarast.andrey.core.algorithm;

import java.awt.Point;
import java.awt.Polygon;

import org.opencv.core.Mat;

import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;

/**
 * Class for detecting sample channels in the input image.
 * @author Andrey G
 */

public class DetectorChannel
{
	/** Perform sample detection with two methods. Compare the acquired border coordinates and choose the best one. */ 
	public static Polygon[] detectSamples (Mat inputEdgeMask, int sampleCount, int borderOffset)
	{	
		//----- Detect all channel border candidates, return null if can not detect samples
		
		/*!*/DebugLogger.logMessage("Detecting sample borders", LOG_MESSAGE_TYPE.INFO);
		Point[] borderCandidates = DetectorChannelBorder.detectSampleBorders(inputEdgeMask, sampleCount);

		//---- Force exit if we could not find any channels
		if (borderCandidates == null) { return null; }
		
		/*!*/DebugLogger.logMessage("Normalizing sample borders", LOG_MESSAGE_TYPE.INFO);
		Point[] borderCandidatesNormed = normalizeBorders(borderCandidates, borderOffset);	

		/*!*/DebugLogger.logMessage("Locating sample area", LOG_MESSAGE_TYPE.INFO);
		Polygon[] outputPolygons = convertToPolygonArray(borderCandidatesNormed, inputEdgeMask.height()); 

		return outputPolygons;
	}

	//----------------------------------------------------------------

	private static Point[] normalizeBorders (Point[] sampleBorders, int normOffset)
	{
		int maxSampleSize = 0;
		int minSampleSize = Integer.MAX_VALUE;
		
		for (int i = 0; i < sampleBorders.length / 2; i++)
		{			
			int currentSize = sampleBorders[2 * i + 1].x - sampleBorders[2 * i].x;

			minSampleSize = Math.min(minSampleSize, currentSize);
			maxSampleSize = Math.max(maxSampleSize, currentSize);
		}
		
		int normedSize = (maxSampleSize + minSampleSize) / 2;

		Point[] outputNormedArray = new Point[sampleBorders.length];
		
		for (int i = 0; i < sampleBorders.length / 2; i++)
		{

			int centerX = (sampleBorders[2 * i].x  + sampleBorders[2 *i + 1].x) / 2;
			int centerY = (sampleBorders[2 * i].y  + sampleBorders[2 *i + 1].y) / 2;

			outputNormedArray[2 * i] = new Point(centerX - normedSize / 2 + normOffset, centerY - normedSize / 2 + normOffset);
			outputNormedArray[2 * i + 1] = new Point(centerX + normedSize / 2 - normOffset, centerY + normedSize / 2 - normOffset);
		}
		return outputNormedArray;
	}

	//----------------------------------------------------------------

	/**
	 * Convert the sample border points into a polygon format.
	 * @param sampleBorders
	 * @param imageHeight
	 * @return
	 */
	private static Polygon[] convertToPolygonArray (Point[] sampleBorders, int imageHeight)
	{
		Polygon[] outputSampleData = new Polygon[sampleBorders.length / 2];

		//---- Prepare output as polygons, narrow down their area to reduce overlapping with the borders 
		for (int i = 0; i < outputSampleData.length; i++)
		{
			outputSampleData[i] = new Polygon();

			outputSampleData[i].addPoint(sampleBorders[2 * i].x, 0);
			outputSampleData[i].addPoint(sampleBorders[2 * i].y, imageHeight);
			outputSampleData[i].addPoint(sampleBorders[2 * i + 1].y, imageHeight);
			outputSampleData[i].addPoint(sampleBorders[2 * i + 1].x, 0);

		}

		return outputSampleData;
	}
}
