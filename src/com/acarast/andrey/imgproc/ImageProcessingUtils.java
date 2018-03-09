package com.acarast.andrey.imgproc;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class to provide high level image processing methods.
 * @author Andrey G
 */
public class ImageProcessingUtils
{
	//----------------------------------------------------------------
	//---- Grayscale, Convert to rgb, Inverse 
	//----------------------------------------------------------------

	/** Performs grayscaling of the input image. */
	public static Mat filterRgbToGray (Mat inputImage)
	{
		Mat outputImage = new Mat();
		Imgproc.cvtColor(inputImage, outputImage, Imgproc.COLOR_RGB2GRAY);

		return outputImage;
	}

	/** Converts the input image into RGB colorspace from GRAY colorspace. Does not create a colored image. */
	public static Mat filterGrayToRgb (Mat inputImage)
	{
		Mat outputImage = new Mat();
		Imgproc.cvtColor(inputImage, outputImage, Imgproc.COLOR_GRAY2RGB);

		return outputImage;
	}

	/** Inverts intensity in the input image. */
	public static Mat filterInverseIntensity (Mat inputImage)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());

		Mat invertcolormatrix= new Mat(inputImage.rows(),inputImage.cols(), inputImage.type(), new Scalar(255,255,255));

		Core.subtract(invertcolormatrix, inputImage, outputImage);

		return outputImage;
	}

	//----------------------------------------------------------------
	//---- Smoothing
	//----------------------------------------------------------------

	/** Applies gaussian filter X*X, where X is specified by kernel size parameter. */
	public static Mat filterGaussian (Mat inputImage, int kernelSize)
	{
		Mat outputImage = new Mat();

		Imgproc.GaussianBlur(inputImage, outputImage, new Size(0,0), kernelSize);

		return outputImage;
	}

	//----------------------------------------------------------------

	/** Performs dilation of the input image with the specified kernel size. */
	public static Mat filterDilate (Mat inputImage, int kernelSize)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());

		Imgproc.dilate(inputImage, outputImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(kernelSize, kernelSize)));

		return outputImage;
	}
	//----------------------------------------------------------------

	/** Applies 'divide' operation to the two input images. Output = Image1 / Image 2. */
	public static Mat blendDivide (Mat inputImage1, Mat inputImage2)
	{
		if (inputImage1.rows() != inputImage2.rows() || inputImage1.cols() != inputImage2.cols()) { return null; }

		Mat outputImage = new Mat(inputImage1.rows(), inputImage1.cols(), inputImage1.type());

		for (int row = 0; row < inputImage1.rows(); row++)
		{
			for (int col = 0; col < inputImage2.cols(); col++)
			{
				double result = inputImage1.get(row, col)[0] / inputImage2.get(row, col)[0];

				int intensity = (int) Math.round(result * 255);

				if (intensity > 255) { intensity = 255; }

				outputImage.put(row, col, new double[] {intensity});
			}
		}

		return outputImage;
	}

	//----------------------------------------------------------------
	//---- Thresholding
	//----------------------------------------------------------------

	/**
	 * Checks a neighborhood region X*X for each pixel and locates minimum intensity of this patch. Then if this minimum intensity 
	 * is more then threshold, then the pixel's intensity is set to a specified one. Otherwise, not changed.
	 * @param inputImage
	 * @param kernelSize size of the neighborhood region 
	 * @param thresholdCondition
	 * @param thresholdDestValue
	 * @return
	 */
	public static Mat filterLocalMinThreshold (Mat inputImage, int kernelSize, int thresholdCondition, int thresholdDestValue)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());

		for (int row = 0; row < inputImage.rows(); row++)
		{
			for (int col = 0; col < inputImage.cols(); col++)
			{
				//---- Checking neighbor region and finding pixel with minimum intensity
				int minIntensityValue = 255;

				for (int kernelRow = -kernelSize; kernelRow <= kernelSize; kernelRow++)
				{
					for (int kernelCol = -kernelSize; kernelCol <= kernelSize; kernelCol++)
					{
						int globalRow = row + kernelRow;
						int globalCol = col + kernelCol;

						if ((globalRow >= 0 && globalRow < inputImage.rows()) && (globalCol >= 0 && globalCol < inputImage.cols()))
						{
							int currentIntensityValue = (int) inputImage.get(globalRow, globalCol)[0];

							if (currentIntensityValue < minIntensityValue)
							{
								minIntensityValue = currentIntensityValue;
							}
						}
					}
				}

				//---- If min intensity not satisfied condition, change to the thresholded value
				if (minIntensityValue > thresholdCondition)
				{
					outputImage.put(row, col, new double[] {thresholdDestValue});
				}
				else
				{
					outputImage.put(row, col, new double[]{inputImage.get(row, col)[0]});
				}
			}
		}

		return outputImage;
	}

	/** Performs intensity thresholding into two values 255 and 0 depending on the threshold level. */
	public static Mat filterGlobalThreshold (Mat inputImage, int thresholdIntensity)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());

		for (int row = 0; row < inputImage.rows(); row++)
		{
			for (int col = 0; col < inputImage.cols(); col++)
			{
				int currentIntensity = (int) Math.round (inputImage.get(row, col)[0]);

				if (currentIntensity < thresholdIntensity)
				{
					outputImage.put(row, col, new double[] {0});
				}
				else
				{
					outputImage.put(row, col, new double[] {255});
				}
			}
		}

		return outputImage;
	}

	//----------------------------------------------------------------
	//---- Edge detection
	//----------------------------------------------------------------

	/** Performs Canny edge detection on the input image. The input image has to be a grayscale image. */
	public static Mat filterCannyEdgeDetector (Mat inputImage, int thresholdEdge)
	{
		final int DEFAULT_CANNY_MULTIPLIER_THRESHOLD = 3;
		final int DEFAULT_KERNEL_SIZE = 3;

		if (inputImage.channels() != 1) {return null; }

		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());

		Imgproc.Canny(inputImage, outputImage, thresholdEdge, DEFAULT_CANNY_MULTIPLIER_THRESHOLD * thresholdEdge, DEFAULT_KERNEL_SIZE, false);

		return outputImage;
	}

	//----------------------------------------------------------------
	//---- Image copying, subimages 
	//----------------------------------------------------------------

	/** Copies a polygon from the input image to the output image, the memory for the output image is not allocated */
	public static void copyPolygonArea (Mat inputImage, Mat outputImage, Polygon inputPolygon)
	{		
		//---- Find bound box to reduce the cost 
		Rectangle boundBox = inputPolygon.getBounds();

		for (int row = boundBox.y; row < boundBox.y + boundBox.height; row++)
		{
			for (int col = boundBox.x; col < boundBox.x + boundBox.width; col++)
			{
				//---- Check if the point is in polygon
				boolean IsInPolygon = inputPolygon.contains(new Point(col, row));

				//---- Copy area
				if (IsInPolygon)
				{
					outputImage.put(row, col, inputImage.get(row, col));	
				}
			}
		}	
	}

	//----------------------------------------------------------------
	//----- Morphological thinning 
	//----------------------------------------------------------------

	/**
	 * Performs morphological thinning of the input binary image. Equal to matlab bmorph(A, 'thin', Inf) procedure.
	 * The source of the algorithm could be found here: http://www.uel.br/pessoal/josealexandre/stuff/thinning/ftp/lam-lee-survey.pdf
	 * @param inputImage
	 * @return
	 */
	public static Mat filterMorphThin (Mat inputImage)
	{
		Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(outputImage);

		Mat swapImage = new Mat(inputImage.rows(), inputImage.cols(), inputImage.type());
		inputImage.copyTo(swapImage);

		boolean isContinue = true;
		int iterationIndex = 0;

		//---- Mask of the neighbors in format [row, col] x0 = current pixel
		int[][] mask = {{0, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}};

		do
		{
			int pixelChanged = 0;

			//---- Foreach pixel in the image
			for (int row = 0; row < outputImage.rows(); row++)
			{
				for (int col = 0; col < outputImage.cols(); col++)
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

						//Conditions
						int G1 = 0, G2 = 0, G3 = 0, G3m = 0;

						//---- Calculate the G1 condition
						for (int i = 1; i <=4; i++)
						{
							int Bi = 0;

							if (x[2*i-1] == 0 && (x[2*i] == 1 || x[2*i + 1] == 1)) { Bi = 1; } else { Bi = 0; }

							G1 += Bi;
						}

						//---- Calculate the G2 condition
						int n1 = 0, n2 = 0;

						for (int i = 1; i <= 4; i++)
						{
							n1 += Math.max(x[2*i-1], x[2*i]);
							n2 += Math.max(x[2*i], x[2*i+1]);
						}

						G2 = Math.min(n1, n2);

						//---- Calculate the G3 condition
						int nx8 = 0;
						int nx4 = 0;
						if (x[8] == 0) { nx8 = 1; } else { nx8 = 0; }
						if (x[4] == 0) { nx4 = 1; } else { nx4 = 0; }

						G3 = Math.min(Math.max(Math.max(x[2], x[3]), nx8), x[1]);
						G3m = Math.min(Math.max(Math.max(x[6], x[7]), nx4), x[5]);

						if (iterationIndex %2 == 0)
						{
							if (G1 ==1 && G2 >=2 && G2 <=3 && G3 == 0) { outputImage.put(row, col, new double[]{0}); pixelChanged++; }
						}
						else
						{
							if (G1 ==1 && G2 >=2 && G2 <=3 && G3m == 0) { outputImage.put(row, col, new double[]{0}); pixelChanged++; }
						}
					}
				}
			}

			//---- Move to next iteration
			iterationIndex++;

			//---- Swap images
			outputImage.copyTo(swapImage);

			//---- Check the loop stop condition
			if (pixelChanged == 0) { isContinue = false; }
		}
		while(isContinue);


		//---- Refining output image, delete tri-grams
		for (int row = 1; row < outputImage.rows() - 1; row++)
		{
			for (int col = 1; col < outputImage.cols() - 1; col++)
			{

				int[] pixel = new int[9];

				for (int k = 0; k < mask.length; k++)
				{
					pixel[k] = (int)Math.round(outputImage.get(row + mask[k][0], col + mask[k][1])[0]);
				}

				if (pixel[0] == 255)
				{
					boolean case1 = (pixel[7] == 255) && (pixel[1] == 255) && (pixel[5] == 0) && (pixel[3] == 0) && (pixel[4] == 0);
					boolean case2 = (pixel[7] == 255) && (pixel[5] == 255) && (pixel[3] == 0) && (pixel[1] == 0) && (pixel[2] == 0);
					boolean case3 = (pixel[3] == 255) && (pixel[1] == 255) && (pixel[5] == 0) && (pixel[6] == 0) && (pixel[7] == 0);
					boolean case4 = (pixel[3] == 255) && (pixel[5] == 255) && (pixel[7] == 0) && (pixel[1] == 0) && (pixel[8] == 0);

					if (case1 || case2 || case3 || case4)
					{
						outputImage.put(row, col, new double[]{0});
					}
				}
			}
		}

		return outputImage;
	}


	/**
	 * Filters the image with an average filter. Puts in each pixel an average intensity 
	 * of the surrounding pixels (size*size).
	 */
	public static Mat filterAveragePatch (Mat inputImage, int patchSize)
	{
		int imageHeight = inputImage.rows();
		int imageWidth = inputImage.cols();

		int patchAmountHeight = imageHeight / patchSize;
		int patchAmountWidth = imageWidth / patchSize;

		Mat outputAverageImage = new Mat(patchAmountHeight, patchAmountWidth, inputImage.type());

		for (int patchIndexRow = 0; patchIndexRow < patchAmountHeight; patchIndexRow++)
		{
			for (int patchIndexCol = 0; patchIndexCol < patchAmountWidth; patchIndexCol++)
			{
				int patchSumIntensity = 0;

				for (int patchWindowRow = 0; patchWindowRow < patchSize; patchWindowRow++)
				{
					for (int patchWindowCol = 0; patchWindowCol < patchSize; patchWindowCol++)
					{
						int golbalRow = patchIndexRow * patchSize + patchWindowRow;
						int globalCol = patchIndexCol * patchSize + patchWindowCol;

						patchSumIntensity += (int)Math.round(inputImage.get(golbalRow, globalCol)[0]);
					}
				}

				int patchAverageIntensity = (int) Math.round((double) patchSumIntensity / (patchSize * patchSize));

				outputAverageImage.put(patchIndexRow, patchIndexCol, new double[] {patchAverageIntensity});
			}
		}

		return outputAverageImage;
	}

	//----------------------------------------------------------------

	/** Calculates average intensity, summing all image pixels in the parallelogram area. 
	 */ 
	public static double averageIntensityParallelogram (Mat inputImage, int startPoint, int width, double angle)
	{
		double sumIntensity = 0;

		int pixelCount = 0;

		for (int i = 0; i < inputImage.rows(); i++)
		{
			for (int j = 0; j < width; j++)
			{
				int currentCol = (int) Math.round((double)startPoint + i * angle) + j;

				sumIntensity += inputImage.get(i, currentCol)[0];
				pixelCount++;
			}
		}

		return sumIntensity/ pixelCount;
	}
	
	//----------------------------------------------------------------
	/** Get the bounding box of the array of points in the opencv format. */
	public static Rect getBoundingBox (Point[] array)
	{
		return Imgproc.boundingRect(OpencvConverter.convertToMatOfPoint(array));
	}
	
	//----------------------------------------------------------------
	
	/**
	 * Performs thresholding with the Otsu algorithm.
	 * @param inputImage
	 * @return
	 */
	public static Mat filterThresholdOtsu (Mat inputImage)
	{
		int[] histogram = new int[256];
		
		for (int i = 0; i < 256; i++) { histogram[i] = 0; }
		
		for (int row = 0; row < inputImage.rows(); row++)
		{
			for (int col = 0; col < inputImage.cols(); col++)
			{
				int currentIntensity = (int) Math.round (inputImage.get(row, col)[0]);
				
				histogram[currentIntensity]++;
			}
		}
		
		
		int sum = 0; 
		
		for (int i = 0; i < 256; i++) { sum = i * histogram[i]; }
		
		int sumB = 0, wB = 0, wF = 0, mB = 0, mF = 0;
		double max = 0, between = 0, threshold1 = 0, threshold2 =0;
		
		int total = inputImage.rows() * inputImage.cols();
		
		for (int i = 0; i < 256; i++) 
		{
			wB += histogram[i]; 
			
			  wB += histogram[i];
		        if (wB == 0)
		            continue;
		        wF = total - wB;
		        if (wF == 0)
		            break;
		        sumB += i * histogram[i];
		        mB = sumB / wB;
		        mF = (sum - sumB) / wF;
		        between = wB * wF * (mB - mF) * (mB - mF);
		        if ( between >= max ) {
		            threshold1 = i;
		            if ( between > max ) {
		                threshold2 = i;
		            }
		            max = between;            
		        }
		}
		
		int oprimizedThreshold = (int) Math.round(( threshold1 + threshold2 ) / 2);
		
		Mat output = new Mat();
		
		output = filterGlobalThreshold(inputImage, oprimizedThreshold);
		
		return output;
	
	}


}
