package com.acarast.andrey.core.algorithm;

import java.awt.Point;
import java.util.Arrays;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.acarast.andrey.controller.SettingsController;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.imgproc.ImageProcessingUtils;

/**
 * Class for detecting locations of the channels in the input microscopy image.
 * @author Andrey G
 */

public class DetectorChannelBorder
{
	//----------------------------------------------------------------

	/** Finds intersection point between two lines.  
	 * @param Line1
	 * @param Line2
	 * @return
	 */
	static Point getIntersection(org.opencv.core.Point[] Line1, org.opencv.core.Point[] Line2)
	{

		org.opencv.core.Point x = new org.opencv.core.Point(Line2[0].x - Line1[0].x, Line2[0].y - Line1[0].y);
		org.opencv.core.Point d1 = new org.opencv.core.Point(Line1[1].x - Line1[0].x, Line1[1].y - Line1[0].y);
		org.opencv.core.Point d2 = new org.opencv.core.Point(Line2[1].x - Line2[0].x, Line2[1].y - Line2[0].y);

		double CrossProduct = d1.x * d2.y - d1.y * d2.x;

		if (Math.abs(CrossProduct) < /*EPS*/1e-8) { return null; }

		double t1 = (x.x * d2.y - x.y * d2.x) / CrossProduct;

		double OutputX = Line1[0].x + d1.x * t1;
		double OutputY = Line1[0].y + d1.y * t1;

		return new Point((int)Math.round(OutputX), (int)Math.round(OutputY));
	}

	//----------------------------------------------------------------

	/** Returns coordinates of the intersection between the segment, specified by 2 points with two line y = 0 and y = height
	 * The output point contains point.x =  [x coordinate of intersection with y = 0] = f(0), point.y = f(height) */
	private static Point getIntersection (org.opencv.core.Point Vertex1, org.opencv.core.Point Vertex2, int MaxWidth, int MaxHeight)
	{
		//---- P.x = f(0), P.y = f(height)
		Point OutputPoint = new Point(0, 0);

		if (Vertex1.y == Vertex2.y) { return new Point(-1, -1); }

		//---- Y = 0 line segment
		org.opencv.core.Point SouthLine1 = new org.opencv.core.Point(0, 0);
		org.opencv.core.Point SouthLine2 = new org.opencv.core.Point(MaxWidth, 0);

		Point SouthPoint = getIntersection(new org.opencv.core.Point[] {Vertex1, Vertex2}, new org.opencv.core.Point[] {SouthLine1, SouthLine2});

		org.opencv.core.Point NorthLine1 = new org.opencv.core.Point(0, MaxHeight);
		org.opencv.core.Point NorthLine2 = new org.opencv.core.Point(MaxWidth, MaxHeight);

		Point NorthPoint = getIntersection(new org.opencv.core.Point[] {Vertex1, Vertex2}, new org.opencv.core.Point[] {NorthLine1, NorthLine2});

		if (SouthPoint != null) { OutputPoint.x = SouthPoint.x; }
		if (NorthPoint != null) { OutputPoint.y = NorthPoint.x; }

		if (OutputPoint.x < 0 || OutputPoint.y < 0 || OutputPoint.x > MaxWidth || OutputPoint.y > MaxWidth)
		{
			//---- This type of point indicates that the calculated intersection is out of range
			return new Point(-1, -1);
		}
		else
		{
			return OutputPoint;
		}
	}

	//----------------------------------------------------------------

	/**
	 * Detects all points for border candidates
	 * @param inputImage
	 * @return
	 */
	private static Point[] detectBorderLineCandidates (Mat inputImage)
	{
		double HoughTransformRho = SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO;
		double HoughTransformThetta = SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA;
		int HoughTransformThreshold = SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD;
		double HoughTransformMinLineLength = SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH;
		double HoughTransformMaxLineGap = SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP;

		//---- Max angle tg for deleting all lines that have bigger inclination angle
		int DEFAILT_MAX_ANGLE = 90 - 5;
		int DEFAULT_MAX_ANGLE_DELTA = (int) Math.round(inputImage.rows() / Math.tan(Math.PI * DEFAILT_MAX_ANGLE / 180));

		//---- Line segments container
		Mat LineCandidates = new Mat();
		
		//---- Perform hough transform
		Imgproc.HoughLinesP(inputImage, LineCandidates, HoughTransformRho, HoughTransformThetta, HoughTransformThreshold, HoughTransformMinLineLength, HoughTransformMaxLineGap);
		
		Vector <Point> borderCandidatePoints = new Vector <Point>();
		
		for (int i = 0; i < LineCandidates.rows(); i++)
		{
			//---- Get points of the line segments
			org.opencv.core.Point pt1 = new org.opencv.core.Point(LineCandidates.get(i, 0)[0], LineCandidates.get(i, 0)[1]);
			org.opencv.core.Point pt2 = new org.opencv.core.Point(LineCandidates.get(i, 0)[2], LineCandidates.get(i, 0)[3]);

			//---- We have to find intersection between the line candidate and y = 0, y = height
			Point AddPoint = getIntersection(pt1, pt2, inputImage.width(), inputImage.height());

			//---- Check if the calculated point is in range
			if (AddPoint.x != -1 && AddPoint.y != -1)
			{
				int currentTanDelta = Math.abs(AddPoint.x - AddPoint.y);

				if (currentTanDelta < DEFAULT_MAX_ANGLE_DELTA)
				{
					borderCandidatePoints.addElement(AddPoint);
				}
			}
		}

		Point[] outputBorderCandidatePoint = new Point[borderCandidatePoints.size()];

		for (int indexPoint = 0; indexPoint < borderCandidatePoints.size(); indexPoint++)
		{
			outputBorderCandidatePoint[indexPoint] = borderCandidatePoints.get(indexPoint);
		}

		return outputBorderCandidatePoint;
	}

	//----------------------------------------------------------------

	/** Checks the left side and the right side of the border. Border should have dark region on the left or right side or both. */
	private static Point[] selectBorderCandidatesIntensity (Mat inputImage, Point[] borderCandidatePoints, int thresholdDark, int thresholdBright)
	{
		int DEFAULT_PATCH_SIZE = SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_PATCH_SIZE;

		Vector <Point> outputBorderCandidates = new Vector <Point>();

		Mat averageIntensity = ImageProcessingUtils.filterAveragePatch(inputImage, DEFAULT_PATCH_SIZE);

		int patchAmountRows =  averageIntensity.rows();
		int patchAmountCols = averageIntensity.cols();

		for (int indexLine = 0; indexLine < borderCandidatePoints.length; indexLine++)
		{
			int patchIndexStart = borderCandidatePoints[indexLine].x / DEFAULT_PATCH_SIZE;
			int patchIndexEnd = borderCandidatePoints[indexLine].y / DEFAULT_PATCH_SIZE;

			double patchOffsetPerStep = (double) (patchIndexEnd - patchIndexStart ) / (patchAmountRows - 1);

			double sumIntensityCenter = 0;
			double sumIntensityLeft = 0;
			double sumIntensityRight = 0;

			int MaxIntensity = 0;
			int MinIntensity = 255;

			for (int patchRowIndex = 0; patchRowIndex < patchAmountRows; patchRowIndex++)
			{
				int patchColIndex = (int) Math.round((double)patchIndexStart + patchRowIndex * patchOffsetPerStep);

				if (patchColIndex >= 0 && patchColIndex < patchAmountCols) 
				{ 
					sumIntensityCenter += averageIntensity.get(patchRowIndex, patchColIndex)[0]; 
					MaxIntensity = Math.max(MaxIntensity, (int)averageIntensity.get(patchRowIndex, patchColIndex)[0]);
					MinIntensity = Math.min(MinIntensity, (int)averageIntensity.get(patchRowIndex, patchColIndex)[0]);
				}
				if (patchColIndex - 1 >= 0) { sumIntensityLeft += averageIntensity.get(patchRowIndex, patchColIndex - 1)[0]; }
				if (patchColIndex - 2 >= 0) { sumIntensityLeft += averageIntensity.get(patchRowIndex, patchColIndex - 2)[0]; }
				if (patchColIndex + 1 < patchAmountCols) { sumIntensityRight += averageIntensity.get(patchRowIndex, patchColIndex + 1)[0]; }
				if (patchColIndex + 2 < patchAmountCols) { sumIntensityRight += averageIntensity.get(patchRowIndex, patchColIndex + 2)[0]; }

			}

			sumIntensityCenter /= patchAmountRows;
			sumIntensityLeft /= patchAmountRows;
			sumIntensityRight /= patchAmountRows;

			if (sumIntensityLeft < thresholdDark || sumIntensityRight < thresholdDark)
			{
				if (sumIntensityCenter > thresholdBright)
				{
					outputBorderCandidates.add(borderCandidatePoints[indexLine]);
				}
			}
		}

		Point[] outputArray = new Point[outputBorderCandidates.size()];

		for (int i = 0; i < outputArray.length; i++)
		{
			outputArray[i] = new Point(outputBorderCandidates.get(i).x, outputBorderCandidates.get(i).y);
		}

		if (outputArray.length == 0) { return borderCandidatePoints; }
		else { return outputArray; }
	}

	//----------------------------------------------------------------

	/** Rotates all borders candidates to be parallel to each other. */
	private static Point[] rotateBorders (Point[] borderCandidatePoints)
	{
		double averageDelta = 0;
		for (int i = 0; i < borderCandidatePoints.length; i++)
		{
			averageDelta += (borderCandidatePoints[i].x - borderCandidatePoints[i].y);
		}

		averageDelta /= borderCandidatePoints.length;

		Point[] outputArray = new Point[borderCandidatePoints.length];

		for (int i = 0; i < borderCandidatePoints.length; i++)
		{			
			int variation = (borderCandidatePoints[i].x - borderCandidatePoints[i].y) - (int) Math.round(averageDelta);

			int newX = borderCandidatePoints[i].x - variation / 2;
			int newY = borderCandidatePoints[i].y + variation / 2;

			outputArray[i] = new Point(newX, newY);
		}

		return outputArray;
	}

	//----------------------------------------------------------------

	/** Merge all border candidates which are close to each other. Returns minX, maxX and angle for each border*/
	private static int[][] mergeBorderCandidates (Mat inputImage, Point[] borderCandidatePoints, int thresholdMerge)
	{ 
		Point[] borderCandidatesRotated = rotateBorders(borderCandidatePoints);

		int currentAngle = borderCandidatesRotated[0].x - borderCandidatesRotated[0].y;

		//---- Prepare to sort
		int[] borderXCoordinate = new int[borderCandidatesRotated.length];

		for (int i = 0; i < borderXCoordinate.length; i++) { borderXCoordinate[i] = borderCandidatesRotated[i].x; }

		//---- Temporary storage for found minX border coordinate and maxX
		Vector <Integer> borderMin = new Vector <Integer>();
		Vector <Integer> borderMax = new Vector <Integer>();

		//---- Sort the x = (0,f(0)) coordinates of the border line candidates
		Arrays.sort(borderXCoordinate);

		for (int i = 0; i < borderXCoordinate.length; i++)
		{
			int newBorderMin = borderXCoordinate[i];
			int newBorderMax = borderXCoordinate[i];
			int newLastBorder = borderXCoordinate[i];

			//---- Check next
			i = i+1;

			//---- Stop loop flag
			boolean isContinue = true;

			do
			{
				if (i < borderXCoordinate.length)
				{
					if (borderXCoordinate[i] - newLastBorder < thresholdMerge)
					{
						newBorderMax = borderXCoordinate[i];
						newLastBorder = borderXCoordinate[i]; 

						i++;
					}
					else 
					{ 
					    
						isContinue = false; 

						//---- Move index back, the for loop will put index back
						i--; 
					}
				}
				else { isContinue = false; }
			}
			while (isContinue);

			borderMin.addElement(newBorderMin);
			borderMax.addElement(newBorderMax);	
		}



		int[][] outputArray = new int[borderMin.size()][3];

		for (int i = 0; i < outputArray.length; i++)
		{
			outputArray[i][0] = borderMin.get(i);
			outputArray[i][1] = borderMax.get(i);
			outputArray[i][2] = currentAngle;
		}



		return outputArray;
	}

	//----------------------------------------------------------------

	/** Check if the size of all samples is quite the same. Sample size is the distance between two borders 2k and 2k+1.
	 * Parameter of threshold shows what could be the maximum difference between sample sizes. Input array has to be */
	private static boolean isSameSize (Point[] inputBorderCoodinates, int thresholdMax)
	{
		if (inputBorderCoodinates.length % 2 != 0) { return false; }

		int maxSampleSize = 0;
		int minSampleSize = Integer.MAX_VALUE;

		for (int i = 0; i < inputBorderCoodinates.length / 2; i++)
		{
			int currentSampleSize = inputBorderCoodinates[2 * i + 1].x - inputBorderCoodinates[2 * i].x;

			maxSampleSize = Math.max(currentSampleSize, maxSampleSize);
			minSampleSize = Math.min(currentSampleSize, minSampleSize);
		}

		if (maxSampleSize - minSampleSize< thresholdMax)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//----------------------------------------------------------------

	/** Delete redundant borders. Here we are assume that additional borders could be found only inside some samples. */
	private static Point[] estimateBordersDelete (int[][] inputBorderCandidates, Mat inputIntensityImage, int samplesAmount)
	{
		//---- WHATS THAT?
		int DEFAULT_STRIP_OFFSET = 10;

		//---- Container for storing intensity level for each strip
		Vector <Double> intensityLevel = new Vector <Double>();

		//---- Container for storing indexes of the borders which used to form a strip
		Vector <Point> stripBorderIndex  = new Vector <Point>();

		for (int i = 0; i < inputBorderCandidates.length - 1; i++)
		{
			stripBorderIndex.addElement(new Point(i, i++));

			//---- Calculate intensity for the current strip
			int stripStart  = inputBorderCandidates[i][1] +  DEFAULT_STRIP_OFFSET;
			int stripWidth = inputBorderCandidates[i+1][0] - inputBorderCandidates[i][1];
			double stripAngle = inputBorderCandidates[i][2] / inputIntensityImage.rows();

			double averageIntensity = ImageProcessingUtils.averageIntensityParallelogram(inputIntensityImage, stripStart, stripWidth, stripAngle);

			intensityLevel.addElement(averageIntensity);
		}

		int[] minIntensityStripIndex = new int[samplesAmount - 1];

		//---- Bold algorithm for locating (samplesAmount - 1) strips with min intensity 
		for (int i = 0; i < samplesAmount - 1; i++)
		{
			double minIntensity = Double.MAX_VALUE;
			int minIntensityIndex = 0;

			for (int j = 0; j < intensityLevel.size(); j++)
			{
				if (intensityLevel.get(j) < minIntensity)
				{
					minIntensityIndex = j;
					minIntensity = intensityLevel.get(j);
				}
			}

			minIntensityStripIndex[i] = minIntensityIndex;
		}

		//---- Sort in order
		Arrays.sort(minIntensityStripIndex);


		//---- Store to the output array
		Point[] outputBorder = new Point[samplesAmount * 2];

		outputBorder[0] = new Point(inputBorderCandidates[0][1], inputBorderCandidates[0][1] - inputBorderCandidates[0][2]);

		int indexOutput = 1;

		for (int k = 0; k < minIntensityStripIndex.length; k++)
		{
			int currentLeftStripIndex = stripBorderIndex.get(minIntensityStripIndex[k]).x;
			outputBorder[indexOutput] = new Point(inputBorderCandidates[currentLeftStripIndex][0], inputBorderCandidates[currentLeftStripIndex][0] - inputBorderCandidates[currentLeftStripIndex][2]);
			indexOutput++;

			int currentRightStripIndex = stripBorderIndex.get(minIntensityStripIndex[k]).y;
			outputBorder[indexOutput] = new Point(inputBorderCandidates[currentRightStripIndex][1], inputBorderCandidates[currentRightStripIndex][1] - inputBorderCandidates[currentRightStripIndex][2]);
			indexOutput++;			
		}

		int lastIndex = inputBorderCandidates.length - 1;
		outputBorder[samplesAmount * 2 - 1] = new Point(inputBorderCandidates[lastIndex][0], inputBorderCandidates[lastIndex][0] - inputBorderCandidates[lastIndex][2]);


		return outputBorder;
	}

	//----------------------------------------------------------------

	/** This method detects coordinates of the border candidates, returns an array of Points, where each point.x is a  x coordinate of the border with y = 0 and 
	 * point.y is an x coordinate of the border with y = height
	 */
	public static Point[] detectSampleBorders (Mat inputImage, int samplesAmount) 
	{
		
		Point[] outputBorders = new Point[samplesAmount * 2];

		/*!*/DebugLogger.logMessage("Detecting border candidates", LOG_MESSAGE_TYPE.INFO);
		
		//---- Locate lines on an image, which could be sample borders
		Point[] borderCandidatePoints = detectBorderLineCandidates(inputImage);

		//--- Perform iterative search of the borders. On each iteration change the threshold for the 
		//--- detection lines with high intensity and dark intensity on the left or right side.


		Mat ix = Mat.zeros(inputImage.size(), inputImage.type());
		for (int i = 0; i < borderCandidatePoints.length; i++)
		{
			Imgproc.line(ix, new org.opencv.core.Point(borderCandidatePoints[i].x, 0), new org.opencv.core.Point(borderCandidatePoints[i].y, inputImage.height()), new Scalar(255));
		}
		DebugLogger.debugSaveMat("testix.jpg", ix);

		int DEFAULT_MAX_SAMPLE_SIZE_DELTA = SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_MINSAMPLEWIDTH;
		int DEFAULT_MERGE_THRESHOLD = SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH;

		int[][] thresholdDB = {{40, 40}, {50, 30}, {60, 20}, 
				{SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX, SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN}, 
				{120, 5}};
		int DEFAULT_MAX_ITERATIONS = thresholdDB.length - 1;
			
		boolean isContinueLoop = true;

		int indexIteration = 0;

		/*!*/DebugLogger.logMessage("Locating " + samplesAmount + " sample borders among " + borderCandidatePoints.length + " found candidates", LOG_MESSAGE_TYPE.INFO);

		do
		{
		    //---- Detect lines which are near the dark areas and are quite bright;
			Point[] borderCandidatePointsRefined = selectBorderCandidatesIntensity(inputImage, borderCandidatePoints, thresholdDB[indexIteration][0], thresholdDB[indexIteration][1]);	
			
			int[][] borderCandidatePointsMerged = mergeBorderCandidates(inputImage, borderCandidatePointsRefined,DEFAULT_MERGE_THRESHOLD);

			
			//---- Found target amount of borders, check if they have equal gaps between
			if (borderCandidatePointsMerged.length == samplesAmount * 2)
			{
				for (int i = 0; i < borderCandidatePointsMerged.length; i++)
				{
					int currentX = 0;
					int currentY = 0;

					//---- Take max for the left borders, take min to the right borders
					if (i % 2 == 0) 
					{
						currentX = borderCandidatePointsMerged[i][1];
						currentY = borderCandidatePointsMerged[i][1] - borderCandidatePointsMerged[i][2];
					}
					else 
					{ 
						currentX = borderCandidatePointsMerged[i][0];
						currentY = borderCandidatePointsMerged[i][0] - borderCandidatePointsMerged[i][2];
					}

					outputBorders[i] = new Point(currentX, currentY);
				}

				//---- Check if the size of all samples is the same, if iteration limit is reached, return, what was found
				if (isSameSize(outputBorders, DEFAULT_MAX_SAMPLE_SIZE_DELTA) || indexIteration >= DEFAULT_MAX_ITERATIONS) { isContinueLoop = false; }
				else { isContinueLoop = true; }

			}

			//---- Not found target amount of borders
			else
			{
				//---- If not the max limit of iterations reached, try to estimate on the next iteration
				if (indexIteration != DEFAULT_MAX_ITERATIONS) { isContinueLoop = true; }

				//---- Last iteration, try to estimate borders from the acquired information 
				else
				{
					//---- Amount of detected border candidates is less then the target amount: try to estimate borders
					if (borderCandidatePointsMerged.length < samplesAmount * 2)
					{
						//TODO FIXME (in current version in case this happens user has to chose different processing parameters)

						//---- Her we force the method to return null to indicate that it was not possible to detect
						outputBorders = null;
						isContinueLoop = false;
					}

					//---- Amount of detected border candidates is more then the target amount: delete some borders
					if (borderCandidatePointsMerged.length > samplesAmount * 2)
					{
						outputBorders = estimateBordersDelete(borderCandidatePointsMerged, inputImage, samplesAmount);

						isContinueLoop = false;
					}
				}
			}

			indexIteration++;
		}
		while (isContinueLoop);


		return outputBorders;
	}

	//----------------------------------------------------------------

}
