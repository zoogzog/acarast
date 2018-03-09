package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import com.acarast.kikuchi.core.data.BentCell;
import com.acarast.kikuchi.core.data.ConnectedRegion;

/**
 * Performs cell separation
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class AlgBentCellSeparation 
{
	private final static int DETECTOR_THRES_DIST = 60;
	private final static int DETECTOR_SEARCH_LENGTH = 4;

	public static void  run (ConnectedRegion currentRegion, Mat workMat)
	{
		Vector<Point> separetePoints = new Vector<Point>();
		int SPC = 0;

		do
		{	
			SPC = separetePoints.size();
			bentCellSepararion(currentRegion, workMat, separetePoints);
		}
		while(SPC != separetePoints.size());
	}

	private static void bentCellSepararion(ConnectedRegion cells, Mat inputThinImage, Vector<Point> separatePoints)
	{	
		for(int cellNumber = 0; cellNumber < cells.getSize(); cellNumber++)
		{
			int searchLength = DETECTOR_SEARCH_LENGTH;
			int minLength = 4;

			if(cells.getCell(cellNumber).getPixelCount() > minLength)
			{
				try
				{
					BentCell bentCell = SmoothnessEvaluation.calcMaximumBent(cells.getCell(cellNumber).getCellPixelCoordinates(), searchLength);

					double distMax = bentCell.getMaxBent();
					Point maxBentPixel = bentCell.getMaxBentPoint();

					if(distMax < 180 - DETECTOR_THRES_DIST)
					{
						Mat tempMat = new Mat(inputThinImage.size(), 0);
						tempMat.setTo(Scalar.all(0));

						for(int c = 0; c < cells.getCell(cellNumber).getCellPixelCoordinates().length; c++)
						{
							tempMat.put(cells.getCell(cellNumber).getCellPixelCoordinates()[c].x, cells.getCell(cellNumber).getCellPixelCoordinates()[c].y, 255);
						}

						ConnectedRegion tempCells = new ConnectedRegion();
						tempMat.put(maxBentPixel.x, maxBentPixel.y, 0);

						Rect rect = cells.getBoundingBox();
						Rectangle boundBox = new Rectangle(rect.y, rect.x, rect.height, rect.width);
						Utils.detectConnectedRegions(tempMat, boundBox, tempCells);

						if(tempCells.getSize() > 1)
						{
							separatePoints.add(maxBentPixel);
							cells.removeCell(cellNumber);

							cells.addNewCell(tempCells.getCell(0).getCellPixelCoordinates());
							cells.addNewCell(tempCells.getCell(1).getCellPixelCoordinates());
						}
						tempCells.dispose();
					}
				}
				catch(Exception e){}
			}
		}
	}
}
