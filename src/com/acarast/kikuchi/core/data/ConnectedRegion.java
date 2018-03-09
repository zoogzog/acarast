package com.acarast.kikuchi.core.data;
import java.awt.Point;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * This class represents one connected region. It can contain more than one cell. Each such
 * cell in this class represented as a segment (CellSegment).
 * @author Kikuchi K, Andrey G
 */
public class ConnectedRegion
{
	//---- Collection of cell segments, which could possibly be real cells.
	private Vector <CellSegment> cellCollection;

	private Mat neighborMat; 

	//---- All intersections in this connected region.
	private Point[] intersectionXY;

	//---- All intersections and 'leaf' end points
	private Point[] vertexList;
	private Point[] vertexListSelection; 

	private Rect boundingBox;

	//-----------------------------------------------------------------------------------------

	public ConnectedRegion ()
	{
		cellCollection = new Vector <CellSegment>();
	}

	//-----------------------------------------------------------------------------------------
	//---- SET METHODS
	//-----------------------------------------------------------------------------------------

	/** Set bounding box of this connected region */
	public void setBoundingBox(Rect inputRect)
	{
		boundingBox = inputRect;
	}

	/** Set the  */
	public void setNode(Point[] p)
	{
		vertexList = p;
	}

	public void setNode(Point[] p, Point[] ps)
	{
		vertexList = p;
		vertexListSelection = ps;
	}

	public void setNode(Mat mat)
	{
		Vector<Point> nodeVec = new Vector<Point>();
		Rect boundBox = new Rect(boundingBox.y, boundingBox.x, boundingBox.height, boundingBox.width);

		for(int row = boundBox.y; row < boundBox.y + boundBox.height; row++)
		{
			for(int col = boundBox.x; col < boundBox.x + boundBox.width; col++)
			{
				if((int)mat.get(row, col)[0] > 0)
				{
					nodeVec.add(new Point(row, col));
				}
			}
		}

		Point[] outputArray = new Point[nodeVec.size()];
		for(int i = 0; i < nodeVec.size(); i++)
		{
			outputArray[i] = nodeVec.get(i);
		}


		vertexList = outputArray;
	}

	public void setIntersection(Point[] points)
	{
		intersectionXY = points;
	}

	public void setIntersection(Mat mat)
	{
		Vector<Point> nodeVec = new Vector<Point>();
		Rect boundBox = new Rect(boundingBox.y, boundingBox.x, boundingBox.height, boundingBox.width);

		for(int row = boundBox.y; row < boundBox.y + boundBox.height; row++)
		{
			for(int col = boundBox.x; col < boundBox.x + boundBox.width; col++)
			{
				if((int)mat.get(row, col)[0] > 0)
				{
					nodeVec.add(new Point(row, col));
				}
			}
		}

		Point[] outputArray = new Point[nodeVec.size()];
		for(int i = 0; i < nodeVec.size(); i++)
		{
			outputArray[i] = nodeVec.get(i);
		}

		intersectionXY = outputArray;
	}

	public void setNeighborMat(Mat mat)
	{
		neighborMat = mat;
	}

	//-----------------------------------------------------------------------------------------
	//---- GET METHODS
	//-----------------------------------------------------------------------------------------

	/**
	 * Returns all vertices located in the bounding box. 
	 * @return
	 */
	public Point[] getSelectionNode()
	{
		Vector<Point> output = new Vector<Point>();
		
		for (int i = 0; i < vertexListSelection.length; i++)
		{
			int row = vertexListSelection[i].x;
			int col = vertexListSelection[i].y;
			
			if ((row >= boundingBox.x && row < boundingBox.x + boundingBox.width) && 
			   (col >= boundingBox.y && col < boundingBox.y + boundingBox.height))
			{
				output.addElement(new Point(row, col));
			}
		}
		
		return output.toArray(new Point[output.size()]);
		
	}

	/**
	 * Returns all vertices, including those which separate small segments
	 * @return
	 */
	public Point[] getFullNodeBoundingBox(Mat a)
	{
		Vector<Point> output = new Vector<Point>();
		
		for (int i = 0; i < vertexList.length; i++)
		{
			int row = vertexList[i].x;
			int col = vertexList[i].y;
			
			if ((row >= boundingBox.x && row < boundingBox.x + boundingBox.width) && 
			   (col >= boundingBox.y && col < boundingBox.y + boundingBox.height))
			{
				output.addElement(new Point(row, col));
			}
		}
		
		return output.toArray(new Point[output.size()]);
	}

	public Point[] getFullNode()
	{
		return vertexList;
	}

	public Point[] getIntersection()
	{
		return intersectionXY;
	}

	public Mat getNeighborMat()
	{
		return neighborMat;
	}

	/** Get the size of the collection. */
	public int getSize ()
	{
		return cellCollection.size();
	}

	public Vector <CellSegment> getCellCollection()
	{
		return cellCollection;
	}

	/** Get last added cell. */
	public CellSegment getLastCell ()
	{
		return cellCollection.lastElement();
	}
	
	/** Get cell specified by index, if index is out of range return null. */
	public CellSegment getCell (int index)
	{
		if (index < 0 || index >= cellCollection.size()) { return null; }

		return cellCollection.get(index);
	}

	public Rect getBoundingBox()
	{
		return boundingBox;
	}
	
	//-----------------------------------------------------------------------------------------
	//---- ADD METHODS
	//-----------------------------------------------------------------------------------------

	/** Adds new cell to the collection, specified by its coordinates. */
	public void addNewCell (Point[] cellCoordinates)
	{
		cellCollection.add(new CellSegment());

		cellCollection.get(getSize() - 1).setCellPixelCoordinates(cellCoordinates);
	}

	public void addNewCell (CellSegment inputCell)
	{
		cellCollection.add(inputCell);
	}

	/** Adds all pixels from the input array to the specified cell. */
	public void addPixelsToCell (int cellIndex, Point[] pixels)
	{
		if (cellIndex < 0 || cellIndex >= cellCollection.size()) { return; }

		cellCollection.get(cellIndex).addCellPixel(pixels);

	}

	/** Adds only one pixel to this cell.*/
	public void addPixelsToCell (int cellIndex, Point pixel)
	{
		if (cellIndex < 0 || cellIndex >= cellCollection.size()) { return; }

		cellCollection.get(cellIndex).addCellPixel(pixel);
	}

	//-----------------------------------------------------------------------------------------=

	public void removeCell (int cellIndex)
	{
		if (cellIndex < 0 || cellIndex >= cellCollection.size()) { return; }

		cellCollection.removeElementAt(cellIndex);
	}

	/** Garbage collector. Yes, let's be carefull to reduce memory usage. */
	public void dispose ()
	{
		for (int k = 0; k < cellCollection.size(); k++)
		{
			cellCollection.get(k).dispose();
		}

		cellCollection.removeAllElements();
	}

	public void resetCells(ConnectedRegion inputCells)
	{
		this.dispose();

		for(int i = 0; i < inputCells.getSize(); i++){

			cellCollection.add(inputCells.getCell(i));
		}
	}

}