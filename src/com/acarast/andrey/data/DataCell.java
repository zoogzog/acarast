package com.acarast.andrey.data;

import java.awt.Point;
import java.util.Vector;

import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

/**
 * Class to describe a cell, detected in the image.
 * @author Andrey G
 */
public class DataCell
{
	private Vector <Point> pixelList;

	//---- This is the number of pixels, which the cell contains after thinning
	private int sizeThinned;

	//----------------------------------------------------------------

	public DataCell ()
	{
		pixelList = new Vector<Point>();

		sizeThinned = 0;
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------

	/** 
	 * Get number of pixels which this cell is combined from.
	 * @return
	 */
	public int getPixelCount ()
	{
		return pixelList.size();
	}

	/**
	 * Get a pixel in the cell, specified by an index. Throws exception if the
	 * index is out of range.
	 * @param index
	 * @return
	 */
	public Point getPixel (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < pixelList.size())
		{
			return pixelList.get(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/** 
	 * Get all pixels which this cell is combined from in a pixel array.
	 * @return
	 */
	public Point[] getPixelArray ()
	{
		Point[] outputArray = new Point[pixelList.size()];

		for (int i = 0; i < pixelList.size(); i++)
		{

			outputArray[i] = new Point(pixelList.get(i).x, pixelList.get(i).y);
		}

		return outputArray;
	}

	public int getSizeThinned ()
	{
		return sizeThinned;
	}

	//----------------------------------------------------------------
	//---- ADD METHODS
	//----------------------------------------------------------------

	/** Set coordinates of all the cell pixels. */
	public void addPixelArray (Point[] pixelArray)
	{
		for (int i = 0; i < pixelArray.length; i++)
		{
			pixelList.addElement(pixelArray[i]);
		}
	}

	/** Adds one more pixel to the current cell. */
	public void addPixel (Point inputPixel)
	{
		pixelList.addElement(inputPixel);
	}

	public void setSizeThinned (int size)
	{
		sizeThinned = size;
	}

	//----------------------------------------------------------------
	//---- REMOVE METHODS
	//----------------------------------------------------------------

	/**
	 * Remove a pixel from the pixel list, which represent this cell. Throws
	 * an exception if the index is out of range.
	 * @param index
	 * @throws ExceptionMessage
	 */
	public void removePixel (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < pixelList.size())
		{
			pixelList.remove(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Removes all pixels from the pixel list, which represent this cell.
	 */
	public void removeAllPixels ()
	{
		pixelList.removeAllElements();
	}
}
