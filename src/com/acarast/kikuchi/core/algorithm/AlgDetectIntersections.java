package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.acarast.kikuchi.core.data.CellSegment;
import com.acarast.kikuchi.core.data.ConnectedRegion;

/**
 * Performs detection of intersection points
 * @author Kikuchi K, Hanada S, Andrey G
 */

public class AlgDetectIntersections 
{
	//---- Minimimum connectivity number which shows that current pixels is an intersection
	private static final int INTERSECTION_MINCONNECTIVITY = 3;

	/**
	 * Detect all intersection in the specified connected region.
	 * @param currentRegion connected region, which can contain more than one cell
	 * @param workMat image which contains all connected regions
	 */
	public static void  run (ConnectedRegion currentRegion, Mat workMat)
	{
		//---- From here we seach for all intersections

		Vector <Point> intersectionXY = new Vector <Point>();

		int[][] mask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};


		//---- Get a default segment, which represents the whole connected region
		CellSegment segment = currentRegion.getCell(0);	

		for(int i = 0; i < segment.getPixelCount(); i++)
		{
			int X = segment.getPixel(i).x;
			int Y = segment.getPixel(i).y;

			if ((int)Math.round(workMat.get(X, Y)[0]) == 255)
			{
				//---- Submatrix of the work matrix to temporary store all pixels
				int[] workSubMat = new int[9];

				for (int indexMask = 0; indexMask < mask.length; indexMask++)
				{
					int nX = X + mask[indexMask][0];
					int nY = Y + mask[indexMask][1];

					if (nX >= 0 && nX < workMat.rows() && nY >= 0 && nY < workMat.cols())
					{
						workSubMat[indexMask] = (int)workMat.get(X + mask[indexMask][0], Y + mask[indexMask][1])[0];
					}
					else
					{
						workSubMat[indexMask] = 0;
					}
				}

				int cp = connectivityNumber(workSubMat);

				if (cp >= INTERSECTION_MINCONNECTIVITY) 
				{		
					intersectionXY.addElement(new Point(X, Y));
				}
			}
		}

		//---- Save the detected intersection in the input/output container
		currentRegion.setIntersection(intersectionXY.toArray(new Point[intersectionXY.size()]));
	}

	private static int connectivityNumber(int[] pix)
	{
		Mat mat = new Mat(5,5,0);
		mat.setTo(Scalar.all(0));
		int[][] mask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
		int row = 2;
		int col = 2;
		
		for(int i = 0; i < 8; i++)
		{
			mat.put(row + mask[i][0], col + mask[i][1], pix[i]); 
		}


		ConnectedRegion cells = new ConnectedRegion();

		detectConnectedRegions(mat, cells);
	
		int size = cells.getSize();
		return size;
	}

	//-----------------------------------------------------------------------------------------

	private static void detectConnectedRegions (Mat inputImage, ConnectedRegion outputData)
	{
		Mat tempImage = new Mat(inputImage.size(), inputImage.type());
		inputImage.copyTo(tempImage);

		for (int row = 0; row < inputImage.height(); row++)
		{
			for (int col = 0; col < inputImage.width(); col++)
			{
				if ((int)tempImage.get(row, col)[0] > 0)
				{
					outputData.addNewCell(new CellSegment());

					recursionNeighbor(tempImage, row, col, outputData.getLastCell());
				}	
			}
		}

	}

	private static void recursionNeighbor (Mat inputImage, int row, int col, CellSegment cellXY)
	{

		cellXY.addCellPixel(new Point(row, col));
		inputImage.put(row, col, new double[] {0});

		int[][] maskNeighbor = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

		for (int k = 0; k < maskNeighbor.length; k++)
		{
			try
			{
				int currentPixel = (int)inputImage.get(row + maskNeighbor[k][0], col + maskNeighbor[k][1])[0];

				if (currentPixel  > 0)
				{
					recursionNeighbor(inputImage, row + maskNeighbor[k][0], col + maskNeighbor[k][1], cellXY);
				}
			}
			catch (Exception e) {}
		}
	}

	//-----------------------------------------------------------------------------------------

}
