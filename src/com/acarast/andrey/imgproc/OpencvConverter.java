package com.acarast.andrey.imgproc;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;

/**
 * Contains methods for converting containers from OpenCV type to standard jave ones.
 * @author Andrey G
 */
public class OpencvConverter
{
	/** Converts the java (int) point array to opencv (float) point array */
	public static org.opencv.core.Point[] convertToPointCV (Point[] InputPoints)
	{
		org.opencv.core.Point[] OutputArray = new org.opencv.core.Point[InputPoints.length];

		for (int i = 0; i < OutputArray.length; i++)
		{
			OutputArray[i] = new org.opencv.core.Point(InputPoints[i].x, InputPoints[i].y);
		}

		return OutputArray;
	}

	/** Converts input java array to opencv  MatOfPoint2f */
	public static MatOfPoint2f convertToMatOfPoint2f (Point[] inputPoints)
	{
		MatOfPoint2f outputData = new MatOfPoint2f();
		outputData.fromArray(convertToPointCV(inputPoints));

		return outputData;
	}

	/** Converts input java array to opencv MatOfPoint */
	public static MatOfPoint convertToMatOfPoint (Point[] inputPoints)
	{
		MatOfPoint outputData = new MatOfPoint();
		outputData.fromArray(convertToPointCV(inputPoints));

		return outputData;
	}

	/** Converts BufferedImage to Mat */
	public static Mat convertBImagetToMat (BufferedImage inputBufferedImage)
	{
		byte[] pixelData = ((DataBufferByte) inputBufferedImage.getRaster().getDataBuffer()).getData();

		Mat outputMat = new Mat(inputBufferedImage.getHeight(), inputBufferedImage.getWidth(), CvType.CV_8UC3);

		outputMat.put(0, 0, pixelData);

		return outputMat;
	}

	/** Converts Mat to BufferedImage */
	public static BufferedImage convertMatToBImage (Mat inputMat)
	{
		BufferedImage outputImage; 

		int cols = inputMat.cols();  
		int rows = inputMat.rows();  
		int elemSize = (int)inputMat.elemSize();  

		byte[] data = new byte[cols * rows * elemSize];  
		int type;  

		inputMat.get(0, 0, data);  

		//---- Type of channel 
		switch (inputMat.channels()) 
		{  
		case 1:  
			type = BufferedImage.TYPE_BYTE_GRAY;  

			break;

			//---------------------------------------------- 

		case 3:   
			type = BufferedImage.TYPE_3BYTE_BGR;  
			// bgr to rgb  
			byte b;  

			for(int i = 0; i < data.length; i = i + 3) 
			{  
				b = data[i];  
				data[i] = data[i+2];  
				data[i+2] = b;  
			}

			break;  

			//----------------------------------------------

		default:  
			return null; // Error  
		}  

		outputImage = new BufferedImage(cols, rows, type);  
		outputImage.getRaster().setDataElements(0, 0, cols, rows, data);  

		return outputImage;
	}
}
