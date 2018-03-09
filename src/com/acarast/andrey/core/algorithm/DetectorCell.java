package com.acarast.andrey.core.algorithm;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import com.acarast.andrey.controller.SettingsController;
import com.acarast.andrey.core.imgproc.ImageProcessing;
import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.data.DataTableElement;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.imgproc.ImageProcessingUtils;
import com.acarast.andrey.imgproc.MathProcessing;
import com.acarast.andrey.imgproc.OpencvConverter;

/**
 * Cell detection algorithm (ver. 2)
 * @author Andrey G
 */
public class DetectorCell 
{
    public static void run (DataTableElement element, Mat cellMask) throws ExceptionMessage
    {
	/*!*/DebugLogger.logMessage("Perform thinning of the cell mask", LOG_MESSAGE_TYPE.INFO);

	//---- Perform thinning of the input binary image.
	Mat thinImageCells = ImageProcessing.thinningImage(cellMask);

	/*!*/DebugLogger.debugSaveMat("debug-thinnout.jpg", thinImageCells);

	int channelCount = element.getChannelCount();

	for (int i = 0; i < channelCount; i++)
	{
	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Performing cell detection of the channel " + i, LOG_MESSAGE_TYPE.INFO);

	    //---- Acquire channel current (i-th) channel location in the image
	    Polygon sampleChannelLocation =  element.getDataImage().getChannel(i).getChannelLocation();

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Copying channel area into an image", LOG_MESSAGE_TYPE.INFO);

	    //---- Prepare an image for analysis. This image contains only cell image data
	    //---- located in the current (i-th) sample channel.
	    Mat sampleChannelImage = Mat.zeros(thinImageCells.size(), thinImageCells.type());
	    ImageProcessingUtils.copyPolygonArea(thinImageCells, sampleChannelImage, sampleChannelLocation);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Searching for intersections", LOG_MESSAGE_TYPE.INFO);			

	    //---- Detects and deletes intersections, returns list of coordinates of those intersections
	    //---- All intersections are deleted, input image is changed
	    Point[] intersectionXY = detectIntersections (sampleChannelImage);

	    //---- Define a temporary collection for storing cell candidates
	    DataCellCollection cellCandidates = new DataCellCollection();

	    //---- Reduce computational cost by selecting area where cells will be detected
	    Rectangle searchArea = sampleChannelLocation.getBounds();

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Detecting connected regions", LOG_MESSAGE_TYPE.INFO);

	    //---- Perform detection of all connected regions
	    ImageProcessing.detectConnectedRegions(sampleChannelImage, searchArea, cellCandidates);

	    Mat objectMap = getObjectMap(cellCandidates, sampleChannelImage);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Merging intersections with the detected cell candidates", LOG_MESSAGE_TYPE.INFO);

	    //---- Merge detected intersections with closest neighbor
	    DetectorCell.mergeIntersection (cellCandidates, intersectionXY, objectMap);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Generating object map", LOG_MESSAGE_TYPE.INFO);

	    //---- Update object map, then update it inside the previous function
	    objectMap = getObjectMap(cellCandidates, sampleChannelImage);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Merging small cells", LOG_MESSAGE_TYPE.INFO);

	    //---- Merge cells located near each other, object map has to be updated after
	    DetectorCell.mergeCellsSmall (cellCandidates, objectMap, SettingsController.CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE, SettingsController.CURRENT_CELL_DETECTOR_MIN_BACTERIA_SIZE);	

	    objectMap = getObjectMap(cellCandidates, sampleChannelImage);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Labeling pixels deleted during the thinning process", LOG_MESSAGE_TYPE.INFO);

	    DetectorCell.getRealCells(cellCandidates, cellMask, objectMap);

	    DebugLogger.switchDebugMatOutputON();
	    DebugLogger.debugSaveMat("test-x.bmp", cellMask);
	    DebugLogger.switchDebugMatOutpuOFF();

	    //---- Update object map, if optimization is required, then update it inside the previous function
	    objectMap = getObjectMap(cellCandidates, sampleChannelImage);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Cell count before smoothness merge is applied: " + cellCandidates.getCellCount(), LOG_MESSAGE_TYPE.INFO);
	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Check smoothness constraints, merge if not satisfied", LOG_MESSAGE_TYPE.INFO);

	    //---- Finding related objects
	    mergeCellsSmooth(cellCandidates, objectMap, SettingsController.CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE, SettingsController.CURRENT_CELL_DETECTOR_ANGLE_DIFFERENCE);

	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Cell count after smoothness merge is applied: ", LOG_MESSAGE_TYPE.INFO);
	    /*!*/DebugLogger.logMessage("[" + i + "/" + channelCount + "] " + "Store the detected cells in the data table", LOG_MESSAGE_TYPE.INFO);

	    //---- Store the extracted cell as cell collection in the data table
	    element.getDataImage().getChannel(i).setCellCollection(cellCandidates);			
	}
    }

    //----------------------------------------------------------------

    /** Detects intersections, performs separation of the overlapped cells. */
    private static Point[] detectIntersections (Mat inputImage)
    {
	int[][] neighborMask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

	Vector <Point> intersectionXY = new Vector <Point>();

	for (int row = 0; row < inputImage.rows(); row++)
	{
	    for (int col = 0; col < inputImage.cols(); col++)
	    {
		//---- if this is a white pixel
		if ((int)Math.round(inputImage.get(row, col)[0]) == 255)
		{
		    //---- check all 8 neighbors
		    int neighborCount = 0;

		    for (int indexMask = 0; indexMask < neighborMask.length; indexMask++)
		    {
			try 
			{ 
			    if ((int) Math.round(inputImage.get(row + neighborMask[indexMask][0], col + neighborMask[indexMask][1])[0]) == 255) { neighborCount++; } 
			} 
			catch (Exception e) {}; 
		    }

		    //---- Check if it is an intersection, and add if it is
		    if (neighborCount >= 3) 
		    {
			intersectionXY.addElement(new Point(row, col));
		    }
		}
	    }
	}

	Point[] outputArray = new Point[intersectionXY.size()];

	for (int k = 0; k < intersectionXY.size(); k++)
	{
	    outputArray[k] = new Point(intersectionXY.get(k).x, intersectionXY.get(k).y);

	    inputImage.put(intersectionXY.get(k).x, intersectionXY.get(k).y, new double[]{0});
	}

	return outputArray;
    }

    /** Merge the detected intersections with cells. 
     * @throws ExceptionMessage */
    private static void mergeIntersection (DataCellCollection cellCandidates, Point[] cellIntersections, Mat objectMap) throws ExceptionMessage
    {
	int MASK_SIZE = 2;

	for (int k = 0; k < cellIntersections.length; k++)
	{
	    int PX = cellIntersections[k].x;
	    int PY = cellIntersections[k].y;

	    double minDistance = Integer.MAX_VALUE;
	    int minDistanceLabel = -1;

	    for (int mx = -MASK_SIZE; mx < MASK_SIZE; mx++)
	    {
		for (int my = -MASK_SIZE; my < MASK_SIZE; my++)
		{
		    if (mx != 0 && my != 0)
		    {
			int NX = PX + mx;
			int NY = PY + my;

			if (NX >= 0 && NX < objectMap.rows() && NY >= 0 && NY < objectMap.cols())
			{
			    int label = (int)Math.round(objectMap.get(NX, NY)[0]);

			    //---- If a labelled cell found in vicinity of the current point, check the distance
			    if (label != 0)
			    {
				double distance = MathProcessing.getDistance(new Point(PX, PY), new Point(NX, NY));

				if (distance < minDistance) { minDistance = distance; minDistanceLabel = label; }
			    }
			}
		    }
		}
	    }

	    if (minDistanceLabel >= 1)
	    {
		cellCandidates.getCell(minDistanceLabel - 1).addPixel(new Point(PX, PY));
		objectMap.put(PX, PY, new double[]{minDistanceLabel});
	    }
	}
    }

    /** Function to merge pair of cell candidates in one cell candidate.
     *  Here we assume that cell candidates which are small nad quite close to each other are more likely to be one cells. 
     * @throws ExceptionMessage */
    private static void mergeCellsSmall (DataCellCollection cellCandidates, Mat objectMap, int maxDistance, int minCellSize) throws ExceptionMessage
    {
	//---- Create neighbor mask
	int[][] mask = new int [(2 * maxDistance + 1) * (2 * maxDistance + 1) - 1][2];

	int index = 0;

	for (int i = -maxDistance; i <= maxDistance; i++)
	{
	    for (int j = -maxDistance; j <= maxDistance; j++)
	    {
		if (i != 0 && j != 0) 
		{ 
		    mask[index][0] = i; 
		    mask[index][1] = j;

		    index++;
		}
	    }
	}

	//---- Try to combine all cells less then min cell size with closest cell
	Vector <Integer> cellLabelToRemove = new Vector <Integer>();

	//---- Hash map, what cell to merge with which cell, hash map structure is used because
	//---- when we will merge cells, we want to perform merge to the cell which size is more
	//---- the min cell size.
	Vector <Integer> mergeWhat = new Vector <Integer>();
	Vector <Integer> mergeWith = new Vector <Integer>();


	for (int i = 0; i < cellCandidates.getCellCount(); i++)
	{
	    if (cellCandidates.getCell(i).getPixelCount() < minCellSize)
	    {
		//---- Finding all neighbors, choosing one neighbor
		Vector <Integer> neighborLabel = new Vector <Integer>();

		//---- For each pixel find neighbors
		for (int p = 0; p < cellCandidates.getCell(i).getPixelCount(); p++)
		{
		    for (int m = 0; m < mask.length; m++)
		    {
			int currentRow = cellCandidates.getCell(i).getPixel(p).x + mask[m][0];
			int currentCol = cellCandidates.getCell(i).getPixel(p).y + mask[m][1];

			if (currentRow >= 0 && currentRow < objectMap.rows() && currentCol >= 0 && currentCol < objectMap.cols())
			{
			    int label = (int)Math.round(objectMap.get(currentRow, currentCol)[0]);

			    //---- Check if label is not the background pixel and not of the same label as the current cell
			    //---- Remember, labels in object map start from 1, 0 is the background.
			    if (label != 0 && (label - 1) != i)
			    {
				if (!neighborLabel.contains(label - 1)) { neighborLabel.addElement(label - 1); }
			    }
			}
		    }
		}

		//---- Randomly choose neighbor if neighobrs were found
		if (neighborLabel.size() != 0)
		{
		    //---- Choose random neighbor to merge with
		    int neighborIndex =  0;
		    if (neighborLabel.size() >= 2) { neighborIndex = (int) Math.floor(Math.random() * neighborLabel.size()); }
		    else { neighborIndex = 0; }

		    //---- Mark to merge with that cell
		    mergeWhat.addElement(i);
		    mergeWith.addElement(neighborLabel.get(neighborIndex));
		}

		//---- Mark this cell to be removed
		cellLabelToRemove.addElement(i);
	    }
	}


	//---- Perform looking for the longest cell to merge with, delete loops	
	for (int i = 0; i < mergeWhat.size(); i++)
	{
	    int mergeSourceBase = mergeWhat.get(i);
	    int mergeDestinationBase = mergeWith.get(i);

	    for (int j = 0; j < mergeWith.size(); j++)
	    {
		int mergeSource = mergeWhat.get(j);
		int mergeDestination = mergeWith.get(j);

		if (i != j)
		{
		    if (mergeDestination == mergeSourceBase)
		    {
			if (mergeSource != mergeDestinationBase)
			{
			    mergeWith.set(j, mergeDestinationBase);
			}
			else
			{
			    mergeWhat.set(j, 0);
			    mergeWith.set(j, 0);
			}
		    }
		}
	    }
	}

	//---- Delete located loops
	for (int i = mergeWhat.size() - 1; i >= 0; i--)
	{
	    if (mergeWhat.get(i) == 0 && mergeWith.get(i) == 0)
	    {
		mergeWhat.remove(i);
		mergeWith.remove(i);
	    }
	}

	//--- Finally merge cells
	for (int i = 0; i < mergeWhat.size(); i++)
	{
	    int mergeSource = mergeWhat.get(i);
	    int mergeDestination = mergeWith.get(i);

	    cellCandidates.getCell(mergeDestination).addPixelArray(cellCandidates.getCell(mergeSource).getPixelArray());
	}

	//---- Remove cells, marked to be removed. In the vector indexes of the cells which has to be removed are
	//---- stored in the ascending order, thus for correct removal from the container the removing has to be perform
	//---- From the last element, storing the highest index, to the first element, storing the lowest index. 
	for (int k = cellLabelToRemove.size() - 1; k >= 0; k--)
	{
	    cellCandidates.removeCell(cellLabelToRemove.get(k));
	}
    }

    /** Function to merge pair of cell candidates in one cell candidate.
     * Here we assume that cell candidates which are close together and their inclination is quite similar is the same cell.	 
     * @throws ExceptionMessage 
     */
    private static void mergeCellsSmooth (DataCellCollection cellCandidates, Mat objectMap, int maxDistance, int thresholdAngle) throws ExceptionMessage
    {
	Vector <Integer> mergeWhat = new Vector<Integer>();
	Vector <Integer> mergeWith = new Vector<Integer>();

	//---- Calculate ideal cell representation
	Mat idealCell = new Mat(cellCandidates.getCellCount(), 4, CvType.CV_32FC1);

	for (int i = 0; i < cellCandidates.getCellCount(); i++)
	{
	    //---- Get cell orientation angle
	    Mat cellPoints = OpencvConverter.convertToMatOfPoint2f(cellCandidates.getCell(i).getPixelArray());
	    Rect boundingBox = Imgproc.boundingRect(OpencvConverter.convertToMatOfPoint(cellCandidates.getCell(i).getPixelArray()));

	    int minX = boundingBox.x - 1;
	    int maxX = boundingBox.x + boundingBox.width + 1;

	    int minY = boundingBox.y - 1;
	    int maxY = boundingBox.y + boundingBox.height + 1;

	    //---- Fit the line into the cell
	    Mat cellLine = new Mat();
	    Imgproc.fitLine(cellPoints, cellLine, Imgproc.CV_DIST_L2, 0, 0.01, 0.01);

	    int multminX = Math.abs((int) Math.round((cellLine.get(2, 0)[0] - minX) / cellLine.get(0, 0)[0]));
	    int multmaxX = Math.abs((int) Math.round((maxX - cellLine.get(2, 0)[0]) / cellLine.get(0, 0)[0]));

	    int multminY = Math.abs((int)  Math.round((cellLine.get(3, 0)[0] - minY) / cellLine.get(1, 0)[0]));
	    int multmaxY = Math.abs((int) Math.round((maxY - cellLine.get(3, 0)[0]) / cellLine.get(1, 0)[0]));

	    int multConst = Math.min(Math.min(multminX, multmaxX), Math.min(multminY, multmaxY));

	    double[] idealCellX = { cellLine.get(2, 0)[0] - multConst * cellLine.get(0, 0)[0],  cellLine.get(2, 0)[0] + multConst * cellLine.get(0, 0)[0]};
	    double[] idealCellY = { cellLine.get(3, 0)[0] - multConst * cellLine.get(1, 0)[0], cellLine.get(3, 0)[0] + multConst * cellLine.get(1, 0)[0]};

	    idealCell.put(i, 0, new double[]{idealCellX[0]});
	    idealCell.put(i, 1, new double[]{idealCellY[0]});
	    idealCell.put(i, 2, new double[]{idealCellX[1]});
	    idealCell.put(i, 3, new double[]{idealCellY[1]});

	}


	//---- Find candidates to merge
	for (int i = 0; i < cellCandidates.getCellCount(); i++)
	{
	    Point[] cellCurrent = {new Point((int)idealCell.get(i, 0)[0], (int)idealCell.get(i, 1)[0]), new Point((int)idealCell.get(i, 2)[0], (int)idealCell.get(i, 3)[0])};

	    for (int k = 0; k < cellCandidates.getCellCount(); k++)
	    {
		//---- Check if this cell is a neighbor cell with satisfying conditions
		if (k != i)
		{
		    Point[] cellTest = {new Point((int)idealCell.get(k, 0)[0], (int)idealCell.get(k, 1)[0]), new Point((int)idealCell.get(k, 2)[0], (int)idealCell.get(k, 3)[0])};

		    //---- 1. Check if it lies in the distance and store the points which will be connected.
		    int[][] index = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
		    double distance = Double.MAX_VALUE;
		    int indexMinDistance = 0;

		    for (int d = 0; d < index.length; d++)
		    {
			double distanceCurrent = MathProcessing.getDistance(cellCurrent[index[d][0]], cellTest[index[d][1]]);

			if (distanceCurrent < distance)
			{
			    distance =  distanceCurrent;
			    indexMinDistance = d;
			}
		    }

		    //---- 2. Check if the angle between them is less then the condition (remember in the settings this is not in radians but in grads)
		    //---- 3. Take into account that we want angle with different direction

		    //---- cell vector coordinate is end - start point, where start point is one with min distance
		    if (distance <= maxDistance)
		    {
			int currentCellVectorX = cellCurrent[1 - index[indexMinDistance][0]].x - cellCurrent[index[indexMinDistance][0]].x;
			int currentCellVectorY = cellCurrent[1 - index[indexMinDistance][0]].y - cellCurrent[index[indexMinDistance][0]].y;

			int testCellVectorX = cellTest[1 - index[indexMinDistance][1]].x - cellTest[index[indexMinDistance][1]].x;
			int testCellVectorY = cellTest[1 - index[indexMinDistance][1]].y - cellTest[index[indexMinDistance][1]].y;

			double angleCos = (double) (currentCellVectorX * testCellVectorX + currentCellVectorY * testCellVectorY) / 
				(Math.sqrt(currentCellVectorX * currentCellVectorX + currentCellVectorY * currentCellVectorY) * 
					Math.sqrt(testCellVectorX * testCellVectorX + testCellVectorY * testCellVectorY)); 

			double angle = Math.acos(angleCos);

			if (angle * 180 / Math.PI > 180 - thresholdAngle)
			{
			    mergeWhat.addElement(i);
			    mergeWith.addElement(k);
			}
		    }

		}
	    }


	}


	//---- Perform looking for the longest cell to merge with, delete loops	
	for (int i = 0; i < mergeWhat.size(); i++)
	{
	    int mergeSourceBase = mergeWhat.get(i);
	    int mergeDestinationBase = mergeWith.get(i);

	    for (int j = 0; j < mergeWith.size(); j++)
	    {
		int mergeSource = mergeWhat.get(j);
		int mergeDestination = mergeWith.get(j);

		if (i != j)
		{
		    if (mergeDestination == mergeSourceBase)
		    {
			if (mergeSource != mergeDestinationBase)
			{
			    mergeWith.set(j, mergeDestinationBase);
			}
			else
			{
			    mergeWhat.set(j, 0);
			    mergeWith.set(j, 0);
			}
		    }
		}
	    }
	}

	//---- Delete located loops
	for (int i = mergeWhat.size() - 1; i >= 0; i--)
	{
	    if (mergeWhat.get(i) == 0 && mergeWith.get(i) == 0)
	    {
		mergeWhat.remove(i);
		mergeWith.remove(i);
	    }
	}

	for (int i = 0; i < mergeWhat.size(); i++)
	{
	    int mergeSource = mergeWhat.get(i);
	    int mergeDestination = mergeWith.get(i);

	    cellCandidates.getCell(mergeDestination).addPixelArray(cellCandidates.getCell(mergeSource).getPixelArray());
	}


	for (int i = mergeWhat.size() - 1; i >= 0; i--)
	{
	    cellCandidates.removeCell(mergeWhat.get(i));
	}


    }

    /** Returns map of the detected cells. Background = 0, cell # starts from 1. 
     * @throws ExceptionMessage */
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

    /** Labels pixels, which were deleted during the thinning step 
     * @throws ExceptionMessage */
    private static void getRealCells (DataCellCollection cellCandidates, Mat originalImage, Mat objectMap) throws ExceptionMessage
    {
	//---- First save the length, represented as thinned cell
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
		int pixelCount =cellCandidates.getCell(i).getPixelCount();

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
				    objectMap.put(neighborPixelRow, neighborPixelCol, new double[i+1]);

				    isContinueLoop = true;

				}
			    }
			}

			//---- Mark this pixel as processed
			objectMap.put(currentRow, currentCol, new double[] {-1});

		    }
		}

	    }
	}
	while (isContinueLoop); 
    }

}
