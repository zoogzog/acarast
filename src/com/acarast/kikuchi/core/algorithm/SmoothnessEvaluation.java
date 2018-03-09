package com.acarast.kikuchi.core.algorithm;
import java.awt.Point;

import org.opencv.core.Mat;

import com.acarast.kikuchi.core.data.BentCell;
import com.acarast.kikuchi.core.data.CellSegment;

import java.util.Vector;

/**
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class SmoothnessEvaluation
{
	//-----------------------------------------------------------------------------------------

	public static Point[] detectThinningArea(Mat objectMapOrg, CellSegment cell, Point initPix)
	{
		Point[] outputArray = new Point[3];
		Vector<Point> searchedCells = new Vector <Point>();

		outputArray[0] = initPix;

		int[][] mask = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
		Mat objectMap = objectMapOrg.clone();

		int initRow = initPix.x;
		int initCol = initPix.y;

		int label = (int)Math.round(objectMap.get(initRow, initCol)[0]);

		int currentRow = initRow;
		int currentCol = initCol;
		objectMap.put(currentRow, currentCol, new double[] {0});

		searchedCells.add(new Point(currentRow, currentCol));

		do
		{
			boolean isNextCellExist = false;
			for (int k = 0; k < mask.length; k++)
			{
				int neighborPixelRow = currentRow + mask[k][0];
				int neighborPixelCol = currentCol + mask[k][1];

				if ((neighborPixelRow >= 0 && neighborPixelRow < objectMap.rows()) && (neighborPixelCol >= 0 && neighborPixelCol < objectMap.cols()))
				{
					if((int)Math.round(objectMap.get(neighborPixelRow, neighborPixelCol)[0]) == label)
					{
						objectMap.put(currentRow, currentCol, new double[] {0});
						currentRow = neighborPixelRow;
						currentCol = neighborPixelCol;
						searchedCells.add(new Point(currentRow, currentCol));

						isNextCellExist = true;
					}
				}
			} 

			if(isNextCellExist == false) { break; }

		}
		while(true);

		outputArray[1] = searchedCells.get(searchedCells.size()/2);
		outputArray[2] = searchedCells.lastElement();


		return outputArray;
	}

	public static BentCell calcMaximumBent(Point[] cell, int inputSearchLength)
	{
		BentCell bentCell = new BentCell();

		int searchLength = inputSearchLength;
		Vector<Point> pointVec = new Vector<Point>();

		if(cell.length <= searchLength * 2 + 1)
		{
			searchLength =  (int)(cell.length / 2);
		}

		Point[] cellRoute = createRouteArray(cell);

		double returnBent = 180.0;
		Point returnPoint = null;

		for(int i = searchLength; i < cellRoute.length - searchLength; i++)
		{
			Point center = cellRoute[i];

			Point edge1 = cellRoute[i - searchLength];
			Point edge2 = cellRoute[i + searchLength];

			Point vec1 = new Point(edge1.x - center.x, edge1.y - center.y);
			Point vec2 = new Point(edge2.x - center.x, edge2.y - center.y);

			double pixBent = Math.acos(Utils.angleCalc(vec1, vec2))*180/Math.PI;


			double z = (vec1.x * vec2.y) - (vec1.y * vec2.x);

			if(z > 0)
			{
				bentCell.addBent(180 - pixBent);
			}
			else
			{
				bentCell.addBent(pixBent - 180);
			}
			pointVec.add(center);

			if(returnBent > pixBent)
			{
				returnBent = pixBent;
				returnPoint = cellRoute[i];
			}
		}

		Point[] region = new Point[pointVec.size()];
		pointVec.copyInto(region);

		bentCell.setRegion(region);
		bentCell.setMaxBent(returnBent);
		bentCell.setMaxBentPoint(returnPoint);

		return bentCell;
	}

	private static Point[] createRouteArray(Point[] cell)
	{
		Point[] cellRoute = new Point[cell.length];
		int routeIndex = 0;

		int[][] mask = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

		Point[] node = Utils.returnEdgeNode(cell);

		Mat cellMat = Utils.rangedMat(cell);

		int currentRow;
		int currentCol;

		if(node != null)
		{
			currentRow = node[0].x;
			currentCol = node[0].y;
		}
		else
		{
			currentRow = cell[0].x;
			currentCol = cell[0].y;
		}

		boolean isAdded = true;


		do
		{
			isAdded = false;
			for(int k = 0; k < mask.length; k++)
			{
				int currentNeighborRow = currentRow + mask[k][0];
				int currentNeighborCol = currentCol + mask[k][1];

				if((currentNeighborRow >= 0)&&(currentNeighborCol >= 0))
				{
					if((int)cellMat.get(currentNeighborRow, currentNeighborCol)[0] > 0)
					{

						cellMat.put(currentRow, currentCol, 0);
						cellRoute[routeIndex] = new Point(currentRow, currentCol);
						currentRow += mask[k][0];
						currentCol += mask[k][1];
						isAdded = true;
						routeIndex++;
						break;
					}
				}
			}

		}
		while(isAdded);

		cellRoute[routeIndex] = new Point(currentRow, currentCol);

		return cellRoute;
	}

}




