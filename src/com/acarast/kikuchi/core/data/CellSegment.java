package com.acarast.kikuchi.core.data;
import java.awt.Point;
import java.util.Vector;

import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import com.acarast.andrey.imgproc.OpencvConverter;

/**
 * (Probably) A class to store parts of a cell.
 * @author Kikuchi K, Andrey G
 */
public class CellSegment
{
	private Vector <Point> cellPixelCoordinates;

	private int cellLength;

	private boolean isSelected;
	private Point[] edgeNode;
	private double eval; 

	//-----------------------------------------------------------------------------------------

	public CellSegment ()
	{
		cellPixelCoordinates = new Vector <Point>();
		cellLength = 0;
	}

	//-----------------------------------------------------------------------------------------
	//---- SET METHODS
	//-----------------------------------------------------------------------------------------

	public void setEval(double e)
	{
		eval = e;
	}

	public void setEdgeNode(Point[] point)
	{
		edgeNode = point;
	}


	/** Set coordinates of all the cell pixels. */
	public void setCellPixelCoordinates (Point[] inputCellCoordinates)
	{
		for (int i = 0; i < inputCellCoordinates.length; i++)
		{
			cellPixelCoordinates.addElement(inputCellCoordinates[i]);
		}
	}

	public void setCellLength (int cellLength)
	{
		this.cellLength = cellLength;
	}

	//-----------------------------------------------------------------------------------------
	//---- GET METHODS
	//-----------------------------------------------------------------------------------------


	public double getEval()
	{
		return eval;
	}

	public Point[] getEdgeNode()
	{
		return edgeNode;
	}

	/** Get coordinates of all the cell pixels. */
	public Point[] getCellPixelCoordinates ()
	{
		Point[] outputArray = new Point[cellPixelCoordinates.size()];

		for (int i = 0; i < cellPixelCoordinates.size(); i++)
		{

			outputArray[i] = new Point(cellPixelCoordinates.get(i).x, cellPixelCoordinates.get(i).y);
		}

		return outputArray;
	}

	/** Get angle of the circumscribed rectangle. */
	public double getInclinationAngle ()
	{
		RotatedRect cellCircumscribedRectangle = Imgproc.minAreaRect(OpencvConverter.convertToMatOfPoint2f(getCellPixelCoordinates()));

		return cellCircumscribedRectangle.angle;
	}

	/** Get circumscribed rectangle of the current cell. */
	public RotatedRect getCircumscibedRectangle ()
	{
		RotatedRect cellCircumscribedRectangle = Imgproc.minAreaRect(OpencvConverter.convertToMatOfPoint2f(getCellPixelCoordinates()));

		return cellCircumscribedRectangle;
	}

	/** Get bounding rectangle of the current cell. */
	public Rect getBoundingBox ()
	{
		Rect boundingBox = Imgproc.boundingRect(OpencvConverter.convertToMatOfPoint(getCellPixelCoordinates()));

		return boundingBox;
	}

	/** Get amount of pixels in the cell. */
	public int getPixelCount ()
	{
		return cellPixelCoordinates.size();
	}

	/** Get i-th pixel of the cell, return null if out of range */
	public Point getPixel (int index)
	{
		if (index < 0 || index >= cellPixelCoordinates.size()) { return null; }

		return cellPixelCoordinates.get(index);
	}

	public int getCellLength ()
	{
		return cellLength;
	}

	//-----------------------------------------------------------------------------------------

	/** Adds one more pixel to the current cell. */
	public void addCellPixel (Point inputPixel)
	{

		cellPixelCoordinates.addElement(inputPixel);
	}

	public void addCellPixel (Point[] inputPixel)
	{

		for (int i = 0; i < inputPixel.length; i++)
		{
			cellPixelCoordinates.addElement(inputPixel[i]);
		}
	}

	//-----------------------------------------------------------------------------------------

	public void select()
	{
		isSelected = true;
	}

	public boolean getSelect()
	{
		return isSelected;
	}

	/** Garbage collector. */
	public void dispose ()
	{
		cellPixelCoordinates.removeAllElements();
	}

	public boolean isCellExist(int x, int y)
	{
		return cellPixelCoordinates.contains(new Point(x, y));
	}
}

