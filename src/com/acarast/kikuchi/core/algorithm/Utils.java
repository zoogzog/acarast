package com.acarast.kikuchi.core.algorithm;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;
import com.acarast.andrey.imgproc.MathProcessing;
import com.acarast.kikuchi.core.data.CellSegment;
import com.acarast.kikuchi.core.data.ConnectedRegion;


/**
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class Utils
{
	/**
	 * Creates a labeled map, with location of all cells in segment collection, stored in a matrix.
	 * It is necessary to allocate the Mat in memory with specified sizes before using this function.
	 * @param cellCollection
	 * @param objectMap
	 */
	public static void convertCellCollectiontoObjectMap (ConnectedRegion cellCollection, Mat objectMap) throws ExceptionMessage
	{
		if (objectMap == null) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_MATRIXISNULL); }

		objectMap.setTo(Scalar.all(0));

		for(int i = 0; i < cellCollection.getSize(); i++)
		{
			for(int j = 0; j < cellCollection.getCell(i).getPixelCount(); j++)
			{
				int row = cellCollection.getCell(i).getPixel(j).x;
				int col = cellCollection.getCell(i).getPixel(j).y;

				//--- Check if the size of matrix is correct.
				if (row >=0 && row < objectMap.rows() &&  col >= 0 && col < objectMap.cols())
				{
					objectMap.put(row, col, i + 1);
				}
				else
				{
					//---- Location of the cell is beyond the borders of the matrix

					//---- Hard exit: throw exception message
					throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);

					//---- Soft exit: do nothing
				}
			}
		}
	}

	/**
	 * Creates a labeled map, with location of all cells in segment collection, stored in a matrix.
	 * It is necessary to allocate the Mat in memory with specified sizes before using this function.
	 * @param cellCollection
	 * @param objectMap
	 */
	public static void convertCellCollectiontoObjectMap (DataCellCollection cellCollection, Mat objectMap) throws ExceptionMessage
	{
		if (objectMap == null) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_MATRIXISNULL); }

		objectMap.setTo(Scalar.all(0));

		for(int i = 0; i < cellCollection.getCellCount(); i++)
		{
			for(int j = 0; j < cellCollection.getCell(i).getPixelCount(); j++)
			{
				int row = cellCollection.getCell(i).getPixel(j).x;
				int col = cellCollection.getCell(i).getPixel(j).y;

				//--- Check if the size of matrix is correct.
				if (row >=0 && row < objectMap.rows() &&  col >= 0 && col < objectMap.cols())
				{
					objectMap.put(row, col, i + 1);
				}
				else
				{
					//---- Location of the cell is beyond the borders of the matrix

					//---- Hard exit: throw exception message
					throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);

					//---- Soft exit: do nothing
				}
			}
		}


	}

	/**
	 * Converts the labeled map of cells into a collection of cells.
	 * @param objectMap
	 * @param cells
	 */
	public static void convertObjectMaptoCellCollection(Mat objectMap, ConnectedRegion cells)
	{
		int label = 1;
		int count = 0;

		Vector <Point> cellPixel = new Vector <Point>();

		do
		{
			count = 0;

			for(int i = 0; i < objectMap.width(); i++)
			{
				for(int j = 0; j < objectMap.height(); j++)
				{
					if((int)objectMap.get(j, i)[0] == label)
					{
						count++;
						cellPixel.addElement(new Point(i, j));
					}
				}
			}
			if(count > 0)
			{
				Point[] outputArray = new Point[cellPixel.size()];

				for (int k = 0; k < cellPixel.size(); k++)
				{
					outputArray[k] = new Point(cellPixel.get(k).x, cellPixel.get(k).y);
				}

				cells.addNewCell(outputArray);
				cellPixel.removeAllElements();
			}
			label++;
		}
		while (count > 0);
	}

	//-----------------------------------------------------------------------------------------

	public static Point[] returnEdgeNode(Point[] cells)
	{
		Mat cellMat = rangedMat(cells);

		Point[] node = new Point[2];
		int nInd = 0;
		int[][] neighborMask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

		for(int i = 0; i < cells.length; i++)
		{
			int neighborCount = 0;

			for (int indexMask = 0; indexMask < neighborMask.length; indexMask++)
			{
				int currentNeighborRow = cells[i].x + neighborMask[indexMask][0];
				int currentNeighborCol = cells[i].y + neighborMask[indexMask][1];
				int pix = 0;

				if((currentNeighborRow >= 0)&&(currentNeighborCol >= 0))
				{
					pix = (int)cellMat.get(currentNeighborRow, currentNeighborCol)[0];
				}

				if(pix > 0)
				{
					neighborCount++;
				} 
			}

			if((neighborCount <= 1)&&(nInd < 2))
			{
				node[nInd] = cells[i];
				nInd++;
			}
		}

		if(nInd == 1)
		{
			node[1] = node[0];
		}

		if(nInd == 0){return null;}

		return node;
	}

	public static Mat rangedMat(Point[] points)
	{
		final int PRELIM = 2;

		int min;
		int max;

		if(points == null){return null;}

		min = points[0].x;
		max = points[0].x;
		for(int i = 0; i < points.length; i++)
		{
			if(min > points[i].x){min = points[i].x;}
			if(max < points[i].x){max = points[i].x;}
		}
		//int minX = min;
		int maxX = max;

		min = points[0].y;
		max = points[0].y;
		for(int i = 0; i < points.length; i++)
		{
			if(min > points[i].y){min = points[i].y;}
			if(max < points[i].y){max = points[i].y;}
		}

		//int minY = min;
		int maxY = max;

		Mat returnMat = new Mat((maxX + 1 + PRELIM), (maxY + 1 + PRELIM), 0);
		returnMat.setTo(Scalar.all(0));
		for(int i = 0; i < points.length; i++)
		{
			returnMat.put(points[i].x, points[i].y, 255);
		}

		return returnMat;
	}

	public static double angleCalc(Point v1, Point v2)
	{
		return (v1.x*v2.x + v1.y*v2.y)/(Math.sqrt((v1.x * v1.x)+(v1.y * v1.y)) * Math.sqrt((v2.x * v2.x)+(v2.y * v2.y)));
	}

	public static double calcCurvature(Point[] cells, Point node0, Point node1)
	{
		double m;
		double n;
		double distMax = 0;

		if((node1.x - node0.x) != 0)
		{

			m = (double)(node1.y - node0.y)/(double)(node1.x - node0.x);
			n = (double)(node1.x * node0.y - node0.x * node1.y)/(double)(node1.x - node0.x);

			double dist;
			for(int i = 0; i < cells.length; i++)
			{
				dist = Math.abs(cells[i].y - m*cells[i].x - n)/Math.sqrt(1 + m*m);
				if(distMax < dist){distMax = dist; }
			}
		}

		else
		{

			double dist;
			for(int i = 0; i < cells.length; i++)
			{
				dist = Math.abs(node0.x - cells[i].x);
				if(distMax < dist){distMax = dist; }
			}
		}

		return (double)((double)distMax/(double)MathProcessing.getDistance(node1, node0));
	}

	public static boolean calcMaximumBent(List<Integer> route, Point[] node, int thres)
	{
		boolean isStraight = true; 
		for(int i = 1; i < route.size() - 1; i++)
		{
			Point center = node[route.get(i)];
			Point edge1 = node[route.get(i - 1)];
			Point edge2 = node[route.get(i + 1)];
			Point vec1 = new Point(edge1.x - center.x, edge1.y - center.y);
			Point vec2 = new Point(edge2.x - center.x, edge2.y - center.y);
			double pixBent = Math.acos(Utils.angleCalc(vec1, vec2))*180/Math.PI;
			if(pixBent < (180 - thres)){isStraight = false;}
		}

		return isStraight;
	}
	
	//-----------------------------------------------------------------------------------------

	public static void detectConnectedRegions (Mat inputImage, ConnectedRegion outputData)
	{
		Rectangle fullArea = new Rectangle(new Point (0,0), new Dimension(inputImage.width(), inputImage.height()));

		detectConnectedRegions(inputImage, fullArea, outputData);
	}

	public static void detectConnectedRegions (Mat inputImage, Rectangle searchArea, ConnectedRegion outputData)
	{
		//Mat tempImage = new Mat(inputImage.size(), inputImage.type());
		Mat tempImage = new Mat(inputImage.size(), CvType.CV_16UC1);
		inputImage.copyTo(tempImage);

		for (int row = searchArea.y; row < searchArea.y + searchArea.height; row++)
		{
			for (int col = searchArea.x; col < searchArea.x + searchArea.width; col++)
			{
				if ((int)Math.round(tempImage.get(row, col)[0]) == 255)
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

		int[][] maskNeighbor = {{0, 1}, {0, -1}, {1, 0}, {1, 1}, {1, -1}, {-1, 0}, {-1, 1}, {-1, -1}};

		for (int k = 0; k < maskNeighbor.length; k++)
		{
			try
			{
				if(((row + maskNeighbor[k][0]) >= 0)&&((col + maskNeighbor[k][1]) >= 0))
				{
					int currentPixel = (int)Math.round(inputImage.get(row + maskNeighbor[k][0], col + maskNeighbor[k][1])[0]);

					if (currentPixel == 255)
					{
						recursionNeighbor(inputImage, row + maskNeighbor[k][0], col + maskNeighbor[k][1], cellXY);
					}
				}

			}
			catch (Exception e) {}
		}
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Locates pixels which were deleted during the thinning process and adds them to cells
	 * @param cellCandidates
	 * @param originalImage	matrix with the original thick cells
	 * @param objectMap labeled matrix map
	 * @throws ExceptionMessage
	 */
	public static void getRealCells (DataCellCollection cellCandidates, Mat originalImage, Mat objectMap) throws ExceptionMessage
	{
		objectMap.setTo(Scalar.all(0));		
		convertCellCollectiontoObjectMap(cellCandidates, objectMap);

		for (int i = 0; i < cellCandidates.getCellCount(); i++)
		{
			cellCandidates.getCell(i).setSizeThinned(cellCandidates.getCell(i).getPixelCount());
		}

		int[][] mask = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

		boolean isContinueLoop = false;

		do
		{
			isContinueLoop = false;

			//---- For each cell perform grabbing neighbor pixels
			for (int i = 0; i < cellCandidates.getCellCount(); i++)
			{
				//---- For each pixel in the cell
				int pixelCount = cellCandidates.getCell(i).getPixelCount();

				for (int pixIndex = 0; pixIndex < pixelCount;  pixIndex++)
				{
					int currentRow = cellCandidates.getCell(i).getPixel(pixIndex).x;
					int currentCol = cellCandidates.getCell(i).getPixel(pixIndex).y;

					//---- Check only previously added pixels, which were not processed
					if ((int)Math.round(objectMap.get(currentRow, currentCol)[0]) == i + 1)
					{
						for (int k = 0; k < mask.length; k++)
						{

							int neighborPixelRow = currentRow + mask[k][0];
							int neighborPixelCol = currentCol + mask[k][1];

							if ((neighborPixelRow >= 0 && neighborPixelRow < originalImage.rows()) && 
									(neighborPixelCol >= 0 && neighborPixelCol < originalImage.cols()))
							{

								//---- Check if on the original image there is a pixel
								//---- Check if this pixel was not added before

								if ((int)Math.round(originalImage.get(neighborPixelRow, neighborPixelCol)[0]) == 255 &&
										(int)Math.round(objectMap.get(neighborPixelRow, neighborPixelCol)[0]) == 0)
								{

									cellCandidates.getCell(i).addPixel(new Point(neighborPixelRow, neighborPixelCol));

									//---- Mark as added, remember in object map indexes for cells start from 1! 
									//objectMap.put(neighborPixelRow, neighborPixelCol, new double[i+1]);
									objectMap.put(neighborPixelRow, neighborPixelCol, i + 1 );

									isContinueLoop = true;

								}
							}
						}
					}
				}

			}

		}
		while (isContinueLoop);
	}
}


