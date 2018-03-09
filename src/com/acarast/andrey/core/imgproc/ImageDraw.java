package com.acarast.andrey.core.imgproc;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import com.acarast.andrey.data.DataCell;
import com.acarast.andrey.data.DataTableElement;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;
import com.acarast.andrey.imgproc.ImageProcessingUtils;
import com.acarast.andrey.imgproc.OpencvConverter;

/**
 * Class which provides methods for drawing cells, bounding boxes in input images.
 * @author Andrey G
 */
public class ImageDraw
{
	/**
	 * Draws bounding boxes, surrounding each cell in every sample channel.
	 * @param element
	 * @param inputImage
	 * @throws ExceptionMessage
	 */
	public static Mat drawImageBoundingBox (DataTableElement element) throws ExceptionMessage
	{
		String refFile = element.getDataFile().getFilePath();

		if (refFile == "") { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILENOTFOUND); }

		Mat refImage = Imgcodecs.imread(refFile);

		Mat outputImage = Mat.zeros(refImage.size(), refImage.type());	
		refImage.copyTo(outputImage);

		//---- Color of a bounding box
		double[] boxColor = {0, 0, 255};

		//---- Draw bounding boxes, surrounding 
		for (int i = 0; i < element.getChannelCount(); i++)
		{
			for (int j = 0; j < element.getDataImage().getChannel(i).getCellCollection().getCellCount(); j++)
			{
				Rect boundingBox = ImageProcessingUtils.getBoundingBox(element.getDataImage().getChannel(i).getCellCollection().getCell(j).getPixelArray());

				for (int boxX = 0; boxX <= boundingBox.width; boxX++)
				{
					outputImage.put(boundingBox.x + boxX, boundingBox.y, boxColor);
					outputImage.put(boundingBox.x + boxX, boundingBox.y + boundingBox.height, boxColor);
				}

				for (int boxY = 0; boxY <= boundingBox.height; boxY++)
				{
					outputImage.put(boundingBox.x, boundingBox.y + boxY, boxColor);
					outputImage.put(boundingBox.x + boundingBox.width, boundingBox.y + boxY, boxColor);
				}
			}
		}

		return outputImage;
	}

	/**
	 * Draws areas, occupied by all sample channels.
	 * @param element
	 * @param inputImage
	 * @throws ExceptionMessage
	 */
	public static Mat drawImageSampleArea (DataTableElement element) throws ExceptionMessage
	{
		String refFile = element.getDataFile().getFilePath();

		if (refFile == "") { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILENOTFOUND); }

		Mat refImage = Imgcodecs.imread(refFile);

		BufferedImage iImage = OpencvConverter.convertMatToBImage(refImage);

		Graphics2D GraphDriver = iImage.createGraphics();
		GraphDriver.setColor(java.awt.Color.black);

		for (int i = 0; i < element.getChannelCount(); i++)
		{
			Polygon samplePolygon = element.getDataImage().getChannel(i).getChannelLocation();

			GraphDriver.draw(samplePolygon);
			GraphDriver.setColor(new java.awt.Color (255, 255, 0, 100));
			GraphDriver.fill(samplePolygon);
		}

		return OpencvConverter.convertBImagetToMat(iImage);
	}

	/**
	 * Draws all cells in all samples in color codes.
	 * @param element
	 * @param inputImage
	 * @return
	 * @throws ExceptionMessage
	 */
	public static Mat drawImageColoredCells (DataTableElement element) throws ExceptionMessage
	{
		String refFile = element.getDataFile().getFilePath();

		if (refFile == "") { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILENOTFOUND); }

		Mat refImage = Imgcodecs.imread(refFile);

		Mat outputImage = Mat.zeros(refImage.size(), refImage.type());

		for (int i = 0; i < element.getChannelCount(); i++)
		{
			for (int j = 0; j < element.getDataImage().getChannel(i).getCellCollection().getCellCount(); j++)
			{
				int R = (int)(Math.random() * 255);
				int G = (int)(Math.random() * 255);
				int B = (int)(Math.random() * 255);

				DataCell cell = element.getDataImage().getChannel(i).getCellCollection().getCell(j);

				for (int p = 0; p < cell.getPixelCount(); p++)
				{
					int row= cell.getPixel(p).x;
					int col = cell.getPixel(p).y;

					if (row >= 0&& col >= 0 && row < outputImage.rows() && col < outputImage.cols())
					{
						outputImage.put(row, col, new double[]{R, G, B});
					}
				}
			}
		}

		return outputImage;
	}
}
