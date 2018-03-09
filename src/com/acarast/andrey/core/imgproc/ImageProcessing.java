package com.acarast.andrey.core.imgproc;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import com.acarast.andrey.controller.SettingsController;
import com.acarast.andrey.data.DataCellCollection;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.imgproc.ImageProcessingUtils;

/**
 * Class which provides image processing methods for the task of channel, cell detection
 * @author Andrey G
 */
public class ImageProcessing
{
	/** Calculates Mask which contains cells and sample borders */
	public static Mat getCellMask (Mat InputImage)
	{
		Mat imageGray = ImageProcessingUtils.filterRgbToGray(InputImage);

		Mat imageGauss = ImageProcessingUtils.filterGaussian(imageGray, SettingsController.CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE);
		Mat imageDivided = ImageProcessingUtils.blendDivide(imageGray, imageGauss);

		int localThresholdKernelSize = SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE;
		int localThresholdCondition = SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION;
		int localThresholdDestvalue = SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE;
		
		Mat imageThreshold = ImageProcessingUtils.filterLocalMinThreshold(imageDivided, localThresholdKernelSize, localThresholdCondition, localThresholdDestvalue);
		Mat imageInverse = ImageProcessingUtils.filterInverseIntensity(imageThreshold);
		DebugLogger.debugSaveMat("testinput.jpg", imageThreshold);
		//FIXME use addaptive threshold with certain level or specified threshold level
		int globalThresholdValue = SettingsController.CURRENT_IMAGE_PROCESSING_GLOBAL_THRESHOLD;
		
		/*!*/DebugLogger.logMessage("Obtaining cell mask. Threshold kernel size: " + localThresholdKernelSize, LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("Obtaining cell mask. Threshold condition: " + localThresholdCondition, LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("Obtaining cell mask. Threshold dest value: " + localThresholdDestvalue, LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("Obtaining cell mask. Threshold dest value global: " + globalThresholdValue , LOG_MESSAGE_TYPE.INFO);
		
		Mat outputMask = ImageProcessingUtils.filterGlobalThreshold(imageInverse, globalThresholdValue);

		return outputMask;
	}

	/** Processes image and creates a binary image (Mask) which represents cells and borders. However  this mask
	 * should be used only for detecting sample borders with the Hough transform. 
	 */
	public static Mat getEdgeMask (Mat inputImage)
	{
		Mat imageGray = ImageProcessingUtils.filterRgbToGray(inputImage);

		Mat imageGauss = ImageProcessingUtils.filterGaussian(imageGray, SettingsController.CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE);
		Mat imageDivided = ImageProcessingUtils.blendDivide(imageGray, imageGauss);

		Mat imageEdges = ImageProcessingUtils.filterCannyEdgeDetector(imageDivided, SettingsController.CURRENT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD);

		Mat imageDilated = ImageProcessingUtils.filterDilate(imageEdges, SettingsController.CURRENT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE);
		imageDilated = ImageProcessingUtils.filterDilate(imageDilated, 2);

		return imageDilated;
	}

	//----------------------------------------------------------------
	//---- Localization of  the connected regions for cell collections
	//----------------------------------------------------------------

	/**
	 * Locates all connected regions in the input image. Stores them as cells in the cell collection.
	 * @param inputImage
	 * @param outputData
	 * @throws ExceptionMessage 
	 */
	public static void detectConnectedRegions (Mat inputImage, DataCellCollection outputData) throws ExceptionMessage
	{
		Rectangle fullArea = new Rectangle(new Point (0,0), new Dimension(inputImage.width(), inputImage.height()));

		detectConnectedRegions(inputImage, fullArea, outputData);
	}

	/**
	 * Locates all connected region in the specified area of the input image. Stores found regions as cells
	 * in the cell collection.
	 * @param inputImage
	 * @param searchArea
	 * @param outputData
	 * @throws ExceptionMessage 
	 */
	public static void detectConnectedRegions (Mat inputImage, Rectangle searchArea, DataCellCollection outputData) throws ExceptionMessage
	{
		Mat labelMat = new Mat();

		//---- Detect connected components. OpenCV >3.0 functions 
		int componentCount = Imgproc.connectedComponents(inputImage, labelMat, 8, CvType.CV_32S);
		
		//---- Allocate memory for cell candidates
		for (int i = 0; i < componentCount; i++)
		{
			outputData.addCell();
		}
		
		for (int row = 0; row < labelMat.rows(); row++)
		{
			for (int col = 0; col < labelMat.cols(); col++)
			{
				int label = (int) labelMat.get(row, col)[0];
				
				//---- Store pixel to data if it is not background
				if (label != 0)
				{
					//---- 0 is the background, 1 is the first cell. in the array 0-th is the first cell
					outputData.getCell(label - 1).addPixel(new Point(row, col));
				}
			}
		}
	}

	

	//----------------------------------------------------------------
	//---- THINNING
	//----------------------------------------------------------------

	/**
	 * Perform thinning. Algorithm developed and implemented by Kazuma Kikuchi.
	 * @param inputImage
	 * @return
	 */
	public static Mat thinningImage(Mat inputImage)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(outputImage);

		Mat swapImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(swapImage);

		int[][] mask = {{0, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};


		Vector <Point> deletePixels = new Vector <Point> ();
		boolean isDeleted;

		//---------------------------------------------
		//>>>> LOOP START
		do
		{
			isDeleted = false;

			//---------------------------------------------
			//---- STEP ONE

			for (int row = 0; row < outputImage.rows(); row++)
			{
				for (int col = 0; col < outputImage.cols(); col++)
				{
					thinningStepOne(swapImage, row, col, mask, deletePixels);
				}
			}

			//---- DELETE
			if(deletePixels.size() != 0)
			{
				isDeleted = true;
				for(int i = 0; i < deletePixels.size(); i++){swapImage.put(deletePixels.get(i).x, deletePixels.get(i).y, 0);}

				deletePixels.removeAllElements();
			}
			if(deletePixels.size() != 0){}
			if(!isDeleted){break;}

			//---------------------------------------------
			//---- STEP TWO
			for (int row = 0; row < outputImage.rows(); row++)
			{
				for (int col = 0; col < outputImage.cols(); col++)
				{
					thinningStepTwo(swapImage, row, col, mask, deletePixels);
				}

			}

			//---- DELETE
			if(deletePixels.size() != 0)
			{
				isDeleted = true;
				for(int i = 0; i < deletePixels.size(); i++){swapImage.put(deletePixels.get(i).x, deletePixels.get(i).y, 0);}

				deletePixels.removeAllElements();
			}
			if(deletePixels.size() != 0){}
			if(!isDeleted){break;}

		}
		while(true);
		//<<<< LOOP END
		//---------------------------------------------

		for (int row = 0; row < swapImage.rows(); row++)
		{
			for (int col = 0; col < swapImage.cols(); col++)
			{
				thinningStepThree(swapImage, row, col, mask, deletePixels);
			}
		}

		return swapImage;
	}

	public static Mat thinningImage(Mat inputImage, Point[] pixelArray)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(outputImage);

		Mat swapImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(swapImage);

		int[][] mask = {{0, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};


		Vector <Point> deletePixels = new Vector <Point> ();
		boolean isDeleted;

		//---------------------------------------------
		//>>>> LOOP START
		do
		{
			isDeleted = false;

			//---------------------------------------------
			//---- STEP ONE

			for (int i = 0; i < pixelArray.length; i++)
			{
				int row = pixelArray[i].x;
				int col = pixelArray[i].y;

				thinningStepOne(swapImage, row, col, mask, deletePixels);
			}

			//---- DELETE
			if(deletePixels.size() != 0)
			{
				isDeleted = true;
				for(int i = 0; i < deletePixels.size(); i++){swapImage.put(deletePixels.get(i).x, deletePixels.get(i).y, 0);}

				deletePixels.removeAllElements();
			}
			if(deletePixels.size() != 0){}
			if(!isDeleted){break;}

			//---------------------------------------------
			//---- STEP TWO
			for (int i = 0; i < pixelArray.length; i++)
			{
				int row = pixelArray[i].x;
				int col = pixelArray[i].y;
						
				thinningStepTwo(swapImage, row, col, mask, deletePixels);
			}

			//---- DELETE
			if(deletePixels.size() != 0)
			{
				isDeleted = true;
				for(int i = 0; i < deletePixels.size(); i++){swapImage.put(deletePixels.get(i).x, deletePixels.get(i).y, 0);}

				deletePixels.removeAllElements();
			}
			if(deletePixels.size() != 0){}
			if(!isDeleted){break;}

		}
		while(true);
		//<<<< LOOP END
		//---------------------------------------------

		for (int i = 0; i < pixelArray.length; i++)
		{
			int row = pixelArray[i].x;
			int col = pixelArray[i].y;
			
			thinningStepThree(swapImage, row, col, mask, deletePixels);
		}

		return swapImage;
	}

	private static void thinningStepOne (Mat swapImage, int row, int col, int[][] mask, Vector <Point> pixelList)
	{
		if (swapImage.get(row, col)[0] == 255)
		{


			int[] x = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

			try { x[1] = (int) Math.round( swapImage.get(row + mask[1][0], col + mask[1][1])[0]>100?1:0); } catch (Exception e) { };
			try { x[2] = (int) Math.round( swapImage.get(row + mask[2][0], col + mask[2][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[3] = (int) Math.round( swapImage.get(row + mask[3][0], col + mask[3][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[4] = (int) Math.round( swapImage.get(row + mask[4][0], col + mask[4][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[5] = (int) Math.round( swapImage.get(row + mask[5][0], col + mask[5][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[6] = (int) Math.round( swapImage.get(row + mask[6][0], col + mask[6][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[7] = (int) Math.round( swapImage.get(row + mask[7][0], col + mask[7][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[8] = (int) Math.round( swapImage.get(row + mask[8][0], col + mask[8][1])[0]>100?1:0); } catch (Exception e) { }; 

			//1st step
			boolean isDeletion = false;
			if(x[1] == 0 || x[3] == 0)
			{
				isDeletion = true;
				if(x[1] == 1 && x[3] == 0 && x[7] == 1 && x[8] == 0){isDeletion = false;}
				if(x[1] == 0 && x[3] == 1 && x[4] == 0 && x[5] == 1){isDeletion = false;}

				if(x[1] == 0 && x[5] == 0 && x[7] == 1){isDeletion = false;}
				if(x[3] == 0 && x[5] == 1 && x[7] == 0){isDeletion = false;}
				if(x[1] == 0 && x[3] == 1 && x[5] == 0){isDeletion = false;}
				if(x[1] == 1 && x[3] == 0 && x[7] == 0){isDeletion = false;}
				if(x[5] == 0 && x[6] == 1 && x[7] == 0){isDeletion = false;}
				if(x[3] == 0 && x[4] == 1 && x[5] == 0){isDeletion = false;}
				if(x[1] == 0 && x[2] == 1 && x[3] == 0){isDeletion = false;}
				if(x[7] == 0 && x[8] == 1 && x[1] == 0){isDeletion = false;}

				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[3] == 1&& x[5] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[3] == 1&& x[7] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[5] == 1&& x[7] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[3] == 1 && x[5] == 1&& x[7] == 1){isDeletion = false;}
			}
			if(isDeletion)
			{
				pixelList.add(new Point(row, col));
			}	
		}
	}

	private static void thinningStepTwo (Mat swapImage, int row, int col, int[][] mask, Vector <Point> pixelList)
	{
		if (swapImage.get(row, col)[0] == 255)
		{
			int[] x = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

			try { x[1] = (int) Math.round( swapImage.get(row + mask[1][0], col + mask[1][1])[0]>100?1:0); } catch (Exception e) { };
			try { x[2] = (int) Math.round( swapImage.get(row + mask[2][0], col + mask[2][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[3] = (int) Math.round( swapImage.get(row + mask[3][0], col + mask[3][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[4] = (int) Math.round( swapImage.get(row + mask[4][0], col + mask[4][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[5] = (int) Math.round( swapImage.get(row + mask[5][0], col + mask[5][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[6] = (int) Math.round( swapImage.get(row + mask[6][0], col + mask[6][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[7] = (int) Math.round( swapImage.get(row + mask[7][0], col + mask[7][1])[0]>100?1:0); } catch (Exception e) { }; 
			try { x[8] = (int) Math.round( swapImage.get(row + mask[8][0], col + mask[8][1])[0]>100?1:0); } catch (Exception e) { }; 

			//1st step
			boolean isDeletion = false;
			if(x[5] == 0 || x[7] == 0)
			{
				isDeletion = true;
				if(x[3] == 1 && x[4] == 0 && x[5] == 1 && x[7] == 0){isDeletion = false;}
				if(x[1] == 1 && x[5] == 0 && x[7] == 1 && x[8] == 0){isDeletion = false;}

				if(x[1] == 0 && x[5] == 0 && x[7] == 1){isDeletion = false;}
				if(x[3] == 0 && x[5] == 1 && x[7] == 0){isDeletion = false;}
				if(x[1] == 0 && x[3] == 1 && x[5] == 0){isDeletion = false;}
				if(x[1] == 1 && x[3] == 0 && x[7] == 0){isDeletion = false;}
				if(x[5] == 0 && x[6] == 1 && x[7] == 0){isDeletion = false;}
				if(x[3] == 0 && x[4] == 1 && x[5] == 0){isDeletion = false;}
				if(x[1] == 0 && x[2] == 1 && x[3] == 0){isDeletion = false;}
				if(x[7] == 0 && x[8] == 1 && x[1] == 0){isDeletion = false;}

				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[3] == 1&& x[5] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[3] == 1&& x[7] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[1] == 1 && x[5] == 1&& x[7] == 1){isDeletion = false;}
				if(x[2] == 0 && x[4] == 0 && x[6] == 0 && x[8] == 0 && x[3] == 1 && x[5] == 1&& x[7] == 1){isDeletion = false;}
			}
			if(isDeletion)
			{
				pixelList.add(new Point(row, col));
			}	
		}
	}

	private static void thinningStepThree (Mat swapImage, int row, int col, int[][] mask, Vector <Point> pixelList)
	{
		int[] pixel = new int[9];

		for (int k = 0; k < mask.length; k++)
		{
			if((row + mask[k][0] >= 0)&&(col + mask[k][1] >= 0))
			{
				try
				{
					pixel[k] = (int)Math.round(swapImage.get(row + mask[k][0], col + mask[k][1])[0]);
				}
				catch(Exception e){pixel[k] = 0;}
			}
			else {pixel[k] = 0;}

		}

		if (pixel[0] == 255)
		{
			boolean case1 = (pixel[7] == 255) && (pixel[1] == 255) && (pixel[5] == 0) && (pixel[3] == 0) && (pixel[4] == 0);
			boolean case2 = (pixel[7] == 255) && (pixel[5] == 255) && (pixel[3] == 0) && (pixel[1] == 0) && (pixel[2] == 0);
			boolean case3 = (pixel[3] == 255) && (pixel[1] == 255) && (pixel[5] == 0) && (pixel[6] == 0) && (pixel[7] == 0);
			boolean case4 = (pixel[3] == 255) && (pixel[5] == 255) && (pixel[7] == 0) && (pixel[1] == 0) && (pixel[8] == 0);

			//ADD
			boolean case5 = (pixel[1] == 255)&&(pixel[2] == 0)&&(pixel[3] == 255)&&(pixel[4] == 0)&&(pixel[5] == 0)&&(pixel[6] == 0)&&(pixel[7] == 255)&&(pixel[8] == 0);
			boolean case6 = (pixel[1] == 255)&&(pixel[2] == 0)&&(pixel[3] == 255)&&(pixel[4] == 0)&&(pixel[5] == 255)&&(pixel[6] == 0)&&(pixel[7] == 0)&&(pixel[8] == 0);
			boolean case7 = (pixel[1] == 0)&&(pixel[2] == 0)&&(pixel[3] == 255)&&(pixel[4] == 0)&&(pixel[5] == 255)&&(pixel[6] == 0)&&(pixel[7] == 255)&&(pixel[8] == 0);
			boolean case8 = (pixel[1] == 255)&&(pixel[2] == 0)&&(pixel[3] == 0)&&(pixel[4] == 0)&&(pixel[5] == 255)&&(pixel[6] == 0)&&(pixel[7] == 255)&&(pixel[8] == 0);

			if (case1 || case2 || case3 || case4 || case5 || case6 || case7 || case8)
			{
				swapImage.put(row, col, new double[]{0});
			}
		}
	}
}
