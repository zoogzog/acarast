package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import com.acarast.kikuchi.core.data.ConnectedRegion;

/**
 * Performs node detection.
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class AlgNodeDetection 
{
	public static void  run (ConnectedRegion currentRegion, Mat workMat)
	{
		Mat intersectionMat = new Mat(workMat.size(), 0);
		intersectionMat.setTo(Scalar.all(0));

		Mat nodeMat = new Mat(workMat.size(), 0);
		nodeMat.setTo(Scalar.all(0));

		final int MINIMUM_CELL_LENGTH = 3;

		intersectionMat.setTo(Scalar.all(0));
		nodeMat.setTo(Scalar.all(0));

		for(int p = 0; p <  currentRegion.getIntersection().length; p++)
		{
			int row = currentRegion.getIntersection()[p].x;
			int col = currentRegion.getIntersection()[p].y;
			intersectionMat.put(row, col, 255);
		}

		for(int cell = 0; cell < currentRegion.getSize(); cell++)
		{
			Point[] node = detectEdgeNode(currentRegion.getCell(cell).getCellPixelCoordinates(),intersectionMat);

			if(node != null)
			{
				if(currentRegion.getCell(cell).getPixelCount() >= MINIMUM_CELL_LENGTH)
				{
					currentRegion.getCell(cell).setEdgeNode(node);
					nodeMat.put(node[0].x, node[0].y, 255);
					nodeMat.put(node[1].x, node[1].y, 255);

				}
				else
				{
					currentRegion.getCell(cell).setEdgeNode(node);
					nodeMat.put(node[0].x, node[0].y, 155);
					nodeMat.put(node[1].x, node[1].y, 155);
				}
			}
			else{}

		}

		for(int p = 0; p <  currentRegion.getIntersection().length; p++)
		{
			int row = currentRegion.getIntersection()[p].x;
			int col = currentRegion.getIntersection()[p].y;
			nodeMat.put(row, col, 255);
		}

		//-------------------------
		Vector<Point> edgeNodeV = new Vector<Point>();

		Rect rect = currentRegion.getBoundingBox();

		Rectangle boundBox = new Rectangle(rect.y, rect.x, rect.height, rect.width);

		for(int row = boundBox.y; row < boundBox.y + boundBox.height; row++)
		{
			for(int col = boundBox.x; col < boundBox.x + boundBox.width; col++)
			{
				if((int)nodeMat.get(row, col)[0] > 200)
				{		
					edgeNodeV.add(new Point(row, col));
				}
			}
		}
		Point[] nodeArray = new Point[edgeNodeV.size()];
		edgeNodeV.copyInto(nodeArray);

		//-------------------------
		Vector<Point> edgeNodeFullV = new Vector<Point>();

		for(int row = boundBox.y; row < boundBox.y + boundBox.height; row++)
		{
			for(int col = boundBox.x; col < boundBox.x + boundBox.width; col++)
			{
				if((int)nodeMat.get(row, col)[0] > 0)
				{		
					edgeNodeFullV.add(new Point(row, col));
				}
			}
		}

		Point[] nodeArrayFull = new Point[edgeNodeFullV.size()];
		edgeNodeFullV.copyInto(nodeArrayFull);
		currentRegion.setNode(nodeArrayFull, nodeArray);

		intersectionMat.release();
	}
	

	private static Point[] detectEdgeNode(Point[] cells, Mat mat)
	{

		Mat cellMat = new Mat(mat.size(), 0);
		cellMat.setTo(Scalar.all(0));

		for(int i = 0; i < cells.length; i++)
		{
			cellMat.put(cells[i].x, cells[i].y, 255);
		}

		Point[] node = new Point[2];
		int nInd = 0;
		int[][] neighborMask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

		for(int i = 0; i < cells.length; i++)
		{
			int neighborCount = 0;

			for (int indexMask = 0; indexMask < neighborMask.length; indexMask++)
			{
				int currentNeighborRow = cells[i].x + neighborMask[indexMask][0];
				int currentNeighborCol = cells[i].y + neighborMask[indexMask][1];

				int cellPix = 0;
				try
				{
					cellPix = (int)cellMat.get(currentNeighborRow, currentNeighborCol)[0];
				}
				catch(Exception e){}

				if(cellPix > 0)
				{
					neighborCount++;
				} 
			}

			if((neighborCount < 2)&&(nInd < 2))
			{
				node[nInd] = cells[i];
				nInd++;
			}
		}

		if(nInd < 2)
		{
			node[1] = node[0];
		}

		if(nInd == 0){return null;}	

		if(node[0] != node[1])
		{
			for (int indexMask = 0; indexMask < neighborMask.length; indexMask++)
			{
				int row0 = node[0].x + neighborMask[indexMask][0];
				int col0 = node[0].y + neighborMask[indexMask][1];
				int nodePix0 = 0;

				try
				{
					nodePix0 = (int)mat.get(row0, col0)[0];
				}
				catch(Exception e){}

				if(nodePix0 > 0)
				{
					node[0] = new Point(node[0].x + neighborMask[indexMask][0], node[0].y + neighborMask[indexMask][1]);
				} 

				if(nInd > 1)
				{
					int row1 = node[1].x + neighborMask[indexMask][0];
					int col1 = node[1].y + neighborMask[indexMask][1];

					int nodePix1 = 0;

					try
					{
						nodePix1 = (int)mat.get(row1, col1)[0];
					}
					catch(Exception e){}

					if(nodePix1 > 0)
					{
						node[1] = new Point(node[1].x + neighborMask[indexMask][0], node[1].y + neighborMask[indexMask][1]);
					} 
				}
			}	
		}
		else
		{
			int slide = 0;

			for (int indexMask = 0; indexMask < neighborMask.length; indexMask++)
			{
				int row0 = node[0].x + neighborMask[indexMask][0];
				int col0 = node[0].y + neighborMask[indexMask][1];
				int nodePix0 = 0;

				try
				{
					nodePix0 = (int)mat.get(row0, col0)[0];
				}
				catch(Exception e){}

				if(nodePix0 > 0)
				{
					node[0] = new Point(node[0].x + neighborMask[indexMask][0], node[0].y + neighborMask[indexMask][1]);
					slide++;
				} 

				if(slide > 0)
				{
					int row1 = node[1].x + neighborMask[indexMask][0];
					int col1 = node[1].y + neighborMask[indexMask][1];
					int nodePix1 = 0;

					try
					{
						nodePix1 = (int)mat.get(row1, col1)[0];
					}
					catch(Exception e){}

					if(nodePix1 > 0)
					{
						node[1] = new Point(node[1].x + neighborMask[indexMask][0], node[1].y + neighborMask[indexMask][1]);
					} 
				}
			}	
		}
		cellMat.release();
		return node;
	}
}
