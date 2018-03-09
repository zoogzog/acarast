package com.acarast.kikuchi.core.algorithm;

import java.util.Vector;

import org.opencv.core.Mat;

import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.imgproc.ImageProcessingUtils;
import com.acarast.kikuchi.core.data.ConnectedRegionCollection;

/**
 * Main entry point for the cell detection sub-algorithm
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class CellDetection 
{
	//-----------------------------------------------------------------------------------------

	/** Entry point for the cell detection algorithm  for connected regions located in a channel. */
	public static void detectCells(DataCellCollection inputConnectedRegionCollection) throws ExceptionMessage
	{
		ConnectedRegionCollection connectedRegionCollection = new ConnectedRegionCollection();

		//---- Create temporary storage for thinned image
		Mat workMat = createWorkImage(inputConnectedRegionCollection);

		//---- Initialize container for storing connected regions
		for(int i = 0; i < inputConnectedRegionCollection.getCellCount(); i++)
		{
			connectedRegionCollection.addNewConnectedRegion(inputConnectedRegionCollection.getCell(i).getPixelArray());

			connectedRegionCollection.getConnectedRegion(i).setBoundingBox(ImageProcessingUtils.getBoundingBox(inputConnectedRegionCollection.getCell(i).getPixelArray()));
		}

		//---- Perform separation/merge for each found cell candidate
		for(int i = 0; i < connectedRegionCollection.getSize(); i++)
		{	
			//---- Detect intersections
			AlgDetectIntersections.run(connectedRegionCollection.getConnectedRegion(i), workMat);
			
			AlgCellSegmentation.run(connectedRegionCollection.getConnectedRegion(i), workMat);
					
			if(connectedRegionCollection.getConnectedRegion(i).getSize() > 1)
			{		
				AlgBentCellSeparation.run(connectedRegionCollection.getConnectedRegion(i), workMat);

				AlgNodeDetection.run(connectedRegionCollection.getConnectedRegion(i), workMat);

				AlgCellMerging.run(connectedRegionCollection, i, workMat);
			}

			//---- Force garbage collector
			if((i % 100) == 0){System.gc();}

		}

		inputConnectedRegionCollection.removeAllCells();

		for(int reg = 0; reg < connectedRegionCollection.getSize(); reg++)
		{

			for(int seg = 0; seg < connectedRegionCollection.getConnectedRegion(reg).getSize(); seg++)
			{

				inputConnectedRegionCollection.addCell();
				inputConnectedRegionCollection.getCellLast().addPixelArray(connectedRegionCollection.getConnectedRegion(reg).getCell(seg).getCellPixelCoordinates());
				inputConnectedRegionCollection.getCellLast().setSizeThinned(inputConnectedRegionCollection.getCellLast().getPixelCount());
			}
		}

		connectedRegionCollection.dispose();	
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * This method creates an image, which displays input connected regions. This is needed for reducing computational
	 * cost further in the algorithm.
	 * @param inputConnectedRegionCollection 
	 * @return image, cut from the original image, [0;maxX+3][0;maxY+3]. 
	 * @throws ExceptionMessage
	 */
	private static Mat createWorkImage(DataCellCollection inputConnectedRegionCollection) throws ExceptionMessage
	{
		//---- Define containers for minimum and maximum positions of X and Y in the image

		int[] minmaxX = {Integer.MAX_VALUE, 0};
		int[] minmaxY = {Integer.MAX_VALUE, 0};

		//---- Find minX, maxX, minY, maxY
		for (int i = 0; i < inputConnectedRegionCollection.getCellCount(); i++)
		{
			//---- Number of pixels in the i-th connected region
			int pixelCount = inputConnectedRegionCollection.getCell(i).getPixelCount();

			//---- Find minX, maxX, minY, maxY for the i-th connected region
			for (int j = 0; j < pixelCount; j++)
			{
				int pixelX = inputConnectedRegionCollection.getCell(i).getPixel(j).x;
				int pixelY = inputConnectedRegionCollection.getCell(i).getPixel(j).y;

				//---- Set minX
				if (pixelX < minmaxX[0]) { minmaxX[0] = pixelX; }

				//---- Set maxX
				if (pixelX > minmaxX[1]) { minmaxX[1] = pixelX; }

				//---- Set minY
				if (pixelY < minmaxY[0]) { minmaxY[0] = pixelY; }

				//---- Set maxY
				if (pixelY > minmaxY[1]) { minmaxY[1] = pixelY; }
			}
		}

		//---- ** Ideally the matrix should be from minX->maxX & minY->maxY
		//---- ** but this results in messing up the indexes of (x,y) in futher processing
		//---- ** so, for now we leave it like this

		final int OFFSET = 3;

		//---- Here just copy all pixels to the output matrix
		Mat returnMat = Mat.zeros((minmaxX[1] + OFFSET), (minmaxY[1] + OFFSET), 0);

	
		for (int i = 0; i < inputConnectedRegionCollection.getCellCount(); i++)
		{
			int pixelCount = inputConnectedRegionCollection.getCell(i).getPixelCount();

			for (int j = 0; j < pixelCount; j++)
			{
				int pixelX = inputConnectedRegionCollection.getCell(i).getPixel(j).x;
				int pixelY = inputConnectedRegionCollection.getCell(i).getPixel(j).y;
				
				returnMat.put(pixelX, pixelY, 255);
			}
		}

		return returnMat;
	}

	//-----------------------------------------------------------------------------------------
	//---- REFINE SEARCH RESULTS
	//-----------------------------------------------------------------------------------------

	//---- In order to increase accuracy of cell detection we made a decision to delete those cells,
	//---- which have less pixels in them than the chosen threshold and stand alone. (does not touch any other detected cells)
	//---- Those cells which have neighbors are connected to those neighbors.
	//---- Also we delete those cells which are one pixel width vertical lines

	/**
	 * Refine cell detection by removing or merging cells, which have less pixels, than the threshold
	 * labelMat is an image with all pixels labeled to cells 
	 * @throws ExceptionMessage 
	 */

	public static void refineRemoveSmallCells (DataCellCollection cellCollection, Mat labelMat, int threshold) throws ExceptionMessage
	{
		//---- Neighbor mask
		int[][] mask = {{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}};

		Vector <Integer> removeList = new Vector<Integer>();

		//---- First, delete all those cell which cell.size < threshold, and they don't have neighbors
		for (int i = 0; i < cellCollection.getCellCount(); i++)
		{
			//---- Check if the number of pixels in the i-th cell is less the threshold
			if (cellCollection.getCell(i).getPixelCount() < threshold)
			{
				//---- Check if there are any neighbor cells]
				for (int p = 0; p < cellCollection.getCell(i).getPixelCount(); p++)
				{
					//--- Check all neighboring pixels of the current pixel
					for (int m = 0; m < mask.length; m++)
					{
						int currentX = cellCollection.getCell(i).getPixel(p).x + mask[m][0];
						int currentY = cellCollection.getCell(i).getPixel(p).y + mask[m][1];

						if (currentX >= 0 && currentX < labelMat.rows() && currentY >= 0 && currentY < labelMat.cols())
						{
							//----- Labels in the label mat start from 1, 0 being background
							int neighbourLabel = (int) labelMat.get(currentX, currentY)[0] - 1;

							if (neighbourLabel != -1 && neighbourLabel != i)
							{
								//---- Merge this cell with the cell with index neighbourLabel
								cellCollection.getCell(neighbourLabel).addPixelArray(cellCollection.getCell(i).getPixelArray());
							}
						}
					}
				}
				removeList.addElement(i);
			}
		}

		for (int i = removeList.size() - 1; i >= 0; i--)
		{
			cellCollection.removeCell(removeList.get(i));
		}

	}

}
