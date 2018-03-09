package com.acarast.kikuchi.core.algorithm;

import java.awt.Polygon;
import java.awt.Rectangle;

import org.opencv.core.Mat;

import com.acarast.andrey.core.imgproc.ImageProcessing;
import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.data.DataTableElement;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.imgproc.ImageProcessingUtils;


/**
 * Entry point for the parallel cell detection algorithm
 * @author Kikuchi K, Andrey G
 */
public class DetectorCell 
{
	public static void run (DataTableElement element, Mat cellMask) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("(CD K) Perform thinning of the cell mask", LOG_MESSAGE_TYPE.INFO);
		
		//---- Perform thinning of the input binary image.
		Mat thinImageCells = ImageProcessing.thinningImage(cellMask);
		
		//---- Initialize thread pool
		CellDetectionThread[] threadPool = new CellDetectionThread[element.getChannelCount()];
		
		/*!*/DebugLogger.logMessage("(CD K) Preprocessing and thread initialization", LOG_MESSAGE_TYPE.INFO);
		
		//---- Perform processing and set the data to the thread pool
		for (int i = 0; i < element.getChannelCount(); i++)
		{
			//---- Acquire channel current (i-th) channel location in the image
			Polygon sampleChannelLocation =  element.getDataImage().getChannel(i).getChannelLocation();
			
			//---- Prepare an image for analysis. This image contains only cell image data
			//---- located in the current (i-th) sample channel.
			Mat sampleChannelImage = Mat.zeros(thinImageCells.size(), thinImageCells.type());
			ImageProcessingUtils.copyPolygonArea(thinImageCells, sampleChannelImage, sampleChannelLocation);
			
			//---- Define a temporary collection for storing cell candidates
			DataCellCollection cellCandidates = new DataCellCollection();
			
			Rectangle searchArea = sampleChannelLocation.getBounds();
			
			//---- Perform detection of all connected regions
			ImageProcessing.detectConnectedRegions(sampleChannelImage, searchArea, cellCandidates);
			
			threadPool[i] = new CellDetectionThread();
			threadPool[i].setSettings(element.getDataImage(), i, sampleChannelImage, cellMask, cellCandidates);
		}
		
		/*!*/DebugLogger.logMessage("(CD K) Launching threads", LOG_MESSAGE_TYPE.INFO);
		
		//---- Launch all threads simultaneously
		for (int i = 0; i < element.getChannelCount(); i++)
		{
			threadPool[i].start();
		}
		
		
		//---- Join all threads 
		for (int i = 0; i < element.getChannelCount(); i++)
		{
			try{ threadPool[i].join(); } catch (Exception e) { e.printStackTrace(); }
		}
		
	}
}
