package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.acarast.kikuchi.core.data.CellSegment;
import com.acarast.kikuchi.core.data.ConnectedRegion;
import com.acarast.kikuchi.core.data.ConnectedRegionCollection;
import com.acarast.kikuchi.core.data.DataCellSelection;

/**
 * This class is a part of the cell detection algorithm CellDetection
 * The main method of this algorithm reconstructs the graph structure of 
 * the entangled, overlapped cells. Then it performs merging of the separated segments. 
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class AlgCellMerging 
{
	private final static double DETECTOR_THRES_CURVE = 0.15;

	//-----------------------------------------------------------------------------------------

	public static void  run (ConnectedRegionCollection inputRegionCollection, int index, Mat workMat)
	{
		ConnectedRegion mergedCells = new ConnectedRegion();
		Vector<Boolean> isSelected = new Vector<Boolean>();

		for(int s = 0; s < inputRegionCollection.getConnectedRegion(index).getSize(); s++)
		{
			isSelected.add(false);
		}

		if(inputRegionCollection.getConnectedRegion(index).getIntersection().length != 0)
		{	
			mergedCells = mergeCells(inputRegionCollection, DETECTOR_THRES_CURVE, index, isSelected, workMat);
		}
		else
		{
			for(int ind = 0; ind < inputRegionCollection.getConnectedRegion(index).getSize(); ind++)
			{
				mergedCells.addNewCell(inputRegionCollection.getConnectedRegion(index).getCell(ind).getCellPixelCoordinates());
			}
		}

		//---- Here we don't want to delete segments, if we can no merge them
		if (mergedCells.getSize() == 0 && inputRegionCollection.getSize() != 0)
		{

		}
		else
		{
			inputRegionCollection.getConnectedRegion(index).resetCells(mergedCells);
		}
	}

	//-----------------------------------------------------------------------------------------

	private static ConnectedRegion mergeCells(ConnectedRegionCollection connectedRegionCollection, double thres, int regionNumber, Vector<Boolean> isSelected, Mat workMat)
	{
		final int MINIMUN_CELL_LENGTH = 3;
		final int THRES_MAXBENT = 60;

		int VALUE_NODEtoNODE = connectedRegionCollection.getConnectedRegion(regionNumber).getSize() + 1;

		ConnectedRegion currentRegion = connectedRegionCollection.getConnectedRegion(regionNumber); 

		//---- Create the matrix where all pixels are labelled as segment cells or background
		//---- Background is labelled as '0', while i-th cell in the currentRegion 
		Mat objMap = Mat.zeros(workMat.size(), 0);
		convertCellCollectiontoObjectMap(currentRegion, objMap);

		Point[] node = currentRegion.getSelectionNode();

		//---- Obtain the matrix which describes the structure of the graph, representing entangled cells
		Mat rin = reconstructGraphStructure(currentRegion, VALUE_NODEtoNODE, workMat, objMap, node);

		List<List<Integer>> dividedGraphIndexList = new ArrayList<List<Integer>>();
		GraphProcessing.graphDivision(rin, node, dividedGraphIndexList);	

		currentRegion.setNeighborMat(rin);
		ConnectedRegion workCells = new ConnectedRegion();

		Vector<DataCellSelection> cellsForSelection = new Vector<DataCellSelection>(); 

		for(int k = 0; k < dividedGraphIndexList.size(); k++)
		{
			for(int t = 0; t < dividedGraphIndexList.get(k).size(); t++)
			{
				double evalMin = 255;

				for(int j = t; j < dividedGraphIndexList.get(k).size(); j++)
				{	
					if(t != j)
					{
						int x = dividedGraphIndexList.get(k).get(t);
						int y = dividedGraphIndexList.get(k).get(j);

						try
						{
							List<Integer> route = null;

							try	{ route = GraphProcessing.route2node(rin, x, y); }
							catch(Exception e){}

							if(route.size() > 2)
							{
								Point[] LSMRegion = getCellRoute(rin, route, currentRegion, currentRegion.getIntersection(), workMat.height(), workMat.width(), VALUE_NODEtoNODE);
								List<Integer> segments = GraphProcessing.getCellSegments(rin, route, VALUE_NODEtoNODE);

								double eval = 0;
								try
								{
									eval = Utils.calcCurvature(LSMRegion, node[route.get(0)], node[route.get(route.size() - 1)]);
								}
								catch(Exception e){System.out.println("exception");}

								if((eval < thres)&&(Utils.calcMaximumBent(route, node, THRES_MAXBENT)))
								{
									if((eval < thres))
									{
										if(evalMin > eval)
										{
											evalMin = eval;
										}

										for(int i = 0; i < LSMRegion.length; i++)
										{	
											try
											{
												currentRegion.getCell(route.get((int)objMap.get(LSMRegion[i].x, LSMRegion[i].y)[0] - 1)).select();
											}
											catch(Exception e){}
										}

										workCells.addNewCell(LSMRegion);
										cellsForSelection.add(new DataCellSelection(segments, LSMRegion, eval));
									}
								}
							}
						}
						catch(Exception e){}
					}
				}
			}
		}

		ConnectedRegion returnCells = new ConnectedRegion();

		sortCells(cellsForSelection);

		Mat work = new Mat(workMat.size(), 0);
		work.setTo(Scalar.all(0));		

		Vector<Boolean> isEdgeCell = detectEdgeCells(currentRegion, work);
		Vector<DataCellSelection> subsetDeletedcells = new Vector<DataCellSelection>();

		subsetDeletion(cellsForSelection, work, subsetDeletedcells);

		for(int i = 0; i < isEdgeCell.size(); i++)
		{
			if(isEdgeCell.get(i))
			{
				double minEval = 3000;
				int minEvalIndex = -1;
				int count = 0;

				for(int s = 0; s < subsetDeletedcells.size(); s++)
				{
					if(subsetDeletedcells.get(s).isContain(i))
					{
						if(subsetDeletedcells.get(s).getEval() < minEval)
						{
							minEval = subsetDeletedcells.get(s).getEval();
							minEvalIndex = s;

						}
						count++;
					}
				}

				if((minEvalIndex != -1)&&(count > 1))
				{
					for(int s = 0; s < subsetDeletedcells.size(); s++)
					{
						if((subsetDeletedcells.get(s).isContain(i))&&(s != minEvalIndex))
						{
							subsetDeletedcells.remove(s);
							s--;
						}
					}
				}
			}
		}

		//--------------------------------
		for(int i = 0; i < subsetDeletedcells.size(); i++)
		{
			returnCells.addNewCell(subsetDeletedcells.get(i).getRegion());
		}

		int i = 0;

		while(i < returnCells.getSize())
		{
			if(returnCells.getCell(i).getPixelCount() < MINIMUN_CELL_LENGTH)
			{
				returnCells.removeCell(i);
			}
			else{i++;}
		}

		for(int s = 0; s < subsetDeletedcells.size(); s++)
		{
			for(int t = 0; t < subsetDeletedcells.get(s).getRoute().size(); t++)
			{
				isSelected.set(subsetDeletedcells.get(s).getRoute().get(t), true);
			}
		}


		for(int s = 0; s < isSelected.size(); s++)
		{
			if((!isSelected.get(s))&&(currentRegion.getCell(s).getPixelCount() >= MINIMUN_CELL_LENGTH))
			{
				returnCells.addNewCell(currentRegion.getCell(s).getCellPixelCoordinates());
			}
		}

		work.release();
		return returnCells;
	} 
	//-----------------------------------------------------------------------------------------


	/**
	 * This function builds a matrix which represents the structure of the graph, modelling the entangled cells.
	 * The output matrix is a square one and symmetric. It's width = height and equals the number of vertexes in the graph. 
	 * Each cell (i,j) in the matrix is zero if there is no graph edge connecting i-th and j-th vertexes. If there is an edge,
	 * its label, which is stored in the objectMap matrix, is put into the cell (i,j).
	 * @param cells  -- cell
	 * @param VALUE_NODEtoNODE
	 * @param mat
	 * @param objectMap -- a labelled matrix of cell segments
	 * @param vertexList -- the set of vertexes in the graph, each vertex is presented as a point
	 * @return
	 */

	private static Mat reconstructGraphStructure(ConnectedRegion cells, int VALUE_NODEtoNODE, Mat mat, Mat objectMap, Point[] vertexList)
	{
		Mat intersectionMat = new Mat(mat.size(), 0);
		intersectionMat.setTo(Scalar.all(0));

		for(int i = 0; i < cells.getIntersection().length; i++)
		{
			intersectionMat.put(cells.getIntersection()[i].x, cells.getIntersection()[i].y, (i + 1));
		}


		int maskSize = 2;
		int[][] mask= new int[(maskSize * 2 +1) * (maskSize * 2 +1) - 1][2];

		int maskIndex = 0;
		for (int x = -maskSize; x <= maskSize; x++)
		{
			for (int y = -maskSize; y <= maskSize; y++)
			{
				if (!( x == 0 && y == 0))
				{
					mask[maskIndex][0] = y;
					mask[maskIndex][1] = x;
			
					maskIndex++;
				}
			
			}
		}
		

		Mat cellNeighborMat = Mat.zeros(vertexList.length, 8, 0);

		int currentRow = 0;
		int currentCol = 0;

		for(int i = 0; i < vertexList.length; i++)
		{
			currentCol = 0;
			if((int)intersectionMat.get(vertexList[i].x, vertexList[i].y)[0] > 0)
			{
				for(int k = 0; k < mask.length; k++)
				{
					if((vertexList[i].x+mask[k][0])<0||(vertexList[i].y + mask[k][1])<0)
					{

					}
					else
					{
						if((int)objectMap.get(vertexList[i].x + mask[k][0], vertexList[i].y + mask[k][1])[0] > 0)
						{
							cellNeighborMat.put(currentRow, currentCol, (int)objectMap.get(vertexList[i].x + mask[k][0], vertexList[i].y + mask[k][1])[0]);
							currentCol++;
						}

						if((int)intersectionMat.get(vertexList[i].x + mask[k][0], vertexList[i].y + mask[k][1])[0] > 0)
						{
							//---- Set labels for the intersections which have neighboring intersections. We don't want 
							//---- two or more intersections represent one connection

							int z=0;
							for(int row = 0; row < cellNeighborMat.height(); row++)
							{
								for(int col = 0; col < cellNeighborMat.width(); col++)
								{
									if(cellNeighborMat.get(row, col)[0]==255-z)
									{

										if(Math.abs(vertexList[row].x-vertexList[i].x)<3 && Math.abs(vertexList[row].y-vertexList[i].y)<3)
										{
										}
										else
										{
											z++;
											row=0;
											col=0;
										}
									}
								}
							}
							cellNeighborMat.put(currentRow, currentCol,255-z);
							currentCol++;
						}
					}
				}
			}
			else
			{
				cellNeighborMat.put(currentRow, currentCol, (int)objectMap.get(vertexList[i].x, vertexList[i].y)[0]);
				currentCol++;
			}
			currentRow++;
		}


		Mat cellGraphMat = Mat.zeros(vertexList.length, vertexList.length, 0);

		int common = 0;
		for(int pointBegin = 0; pointBegin < cellNeighborMat.height(); pointBegin++)
		{
			for(int col = 0; col < cellNeighborMat.width(); col++)
			{
				common = (int)cellNeighborMat.get(pointBegin, col)[0];

				if(common != 0)
				{


					for(int pointEnd = 0; pointEnd < cellNeighborMat.height(); pointEnd++)
					{
						for(int n = 0; n < cellNeighborMat.width(); n++)
						{
							if((int)cellNeighborMat.get(pointEnd, n)[0] == common)
							{
								if((int)cellGraphMat.get(pointBegin, pointEnd)[0] < VALUE_NODEtoNODE)
								{
									cellGraphMat.put(pointBegin, pointEnd, common);	
								}	
							}
						}
					}
				}
			}
		}
		for(int i = 0; i < cellGraphMat.width(); i++)
		{
			cellGraphMat.put(i, i, 0);	
		}

		for(int row = 0; row < cellGraphMat.height(); row++)
		{
			for(int col = 0; col < cellGraphMat.width(); col++)
			{
				if((int)cellGraphMat.get(row, col)[0] != 0)
				{
					for(int col2 = 0; col2 < cellGraphMat.width(); col2++)
					{
						if(((int)cellGraphMat.get(row, col)[0] == (int)cellGraphMat.get(row, col2)[0])&&(col != col2))
						{
							//TODO actually here be careful of setting the threshold for labels (200)

							//---- Here try to merge two intersections which are too close, and in reality represent one
							if((cellGraphMat.get(row, col)[0]<200) && (cellGraphMat.get(row, col2)[0]<200))
							{
								cellGraphMat.put(row, col2, 0);
								cellGraphMat.put(col2, row, 0);
							}
						}
					}
				}
			}
		}

		for(int row = 0; row < cellGraphMat.height(); row++)
		{
			for(int col = row+1; col < cellGraphMat.width(); col++)
			{
				if(cellGraphMat.get(row, col)[0]>VALUE_NODEtoNODE){
					for(int yoko=0;yoko<cellGraphMat.width();yoko++)
					{
						//TODO
						if(cellGraphMat.get(col, yoko)[0]>0&&cellGraphMat.get(col, yoko)[0]!=cellGraphMat.get(row, col)[0]){

							cellGraphMat.put(row, yoko, cellGraphMat.get(col, yoko)[0]);
							cellGraphMat.put(yoko, row, cellGraphMat.get(col, yoko)[0]);
							cellGraphMat.put(col,yoko,new double []{0});
							cellGraphMat.put(yoko, col, new double []{0});


						}
					}
					cellGraphMat.put(row, col,new double []{0});
					cellGraphMat.put(col, row,new double []{0});
				}

			}
		}

		return cellGraphMat;
	}

	//-----------------------------------------------------------------------------------------

	public static Point[] getCellRoute(Mat grouphMat, List<Integer> route, ConnectedRegion cells, Point[] junc, int width, int height, int VALUE_NODEtoNODE)
	{
		CellSegment cellPixels = new CellSegment();
		for(int i = 0; i < route.size() - 1; i++)
		{
			try{
				if((int)grouphMat.get(route.get(i), route.get(i + 1))[0] < VALUE_NODEtoNODE)
				{
					cellPixels.addCellPixel(cells.getCell((int)grouphMat.get(route.get(i), route.get(i + 1))[0] - 1).getCellPixelCoordinates());
				}
			}
			catch(Exception e){}
		}

		Mat mat = Mat.zeros(width, height, 0);


		for(int i = 0; i < junc.length; i++)
		{
			mat.put(junc[i].x, junc[i].y, 255);
		}
		mat.setTo(Scalar.all(0));

		for(int i = 0; i < cellPixels.getCellPixelCoordinates().length; i++)
		{
			mat.put(cellPixels.getCellPixelCoordinates()[i].x, cellPixels.getCellPixelCoordinates()[i].y, 255);
		}

		int[][] mask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

		for(int i = 0; i < junc.length; i++)
		{
			boolean isAdd = false;
			for(int k = 0; k < mask.length; k++)
			{
				try
				{
					if((int)mat.get(junc[i].x + mask[k][0], junc[i].y + mask[k][1])[0] > 0)
					{
						isAdd = true;
					}
				}
				catch(Exception e){}
			}
			if(isAdd)
			{
				cellPixels.addCellPixel(junc[i]);
			}
		}

		return cellPixels.getCellPixelCoordinates();
	}

	private static void subsetDeletion(Vector<DataCellSelection> cellsForSelection, Mat work, Vector<DataCellSelection> returnCells)
	{

		work.setTo(Scalar.all(0));

		for(int i = 0; i < cellsForSelection.size(); i++)
		{	
			boolean isPropAll = true;
			for(int you = 0; you < cellsForSelection.size(); you++)
			{	
				if(i != you)
				{
					boolean isProp = false;
					work.setTo(Scalar.all(0));
					for(int p = 0; p < cellsForSelection.get(you).getRegion().length; p++)
					{

						work.put(cellsForSelection.get(you).getRegion()[p].x, cellsForSelection.get(you).getRegion()[p].y, 255);
					}

					for(int p = 0; p < cellsForSelection.get(i).getRegion().length; p++)
					{
						if((int)work.get(cellsForSelection.get(i).getRegion()[p].x, cellsForSelection.get(i).getRegion()[p].y)[0] == 0)
						{
							isProp = true;
						}
					}

					if(!isProp)
					{
						isPropAll = false;		
					}
				}
			}

			if(isPropAll)
			{
				Point[] region = cellsForSelection.get(i).getRegion();
				List<Integer> segments = cellsForSelection.get(i).getRoute();
				double eval = cellsForSelection.get(i).getEval();

				returnCells.add(new DataCellSelection(segments, region, eval));
			}
		}
	}

	private static Vector<Boolean> detectEdgeCells(ConnectedRegion cellCandidates, Mat work)
	{
		Vector<Point> edgeNodeVec = new Vector<Point>();

		work.setTo(Scalar.all(0));
		Point[] node = cellCandidates.getFullNodeBoundingBox(work);
		for(int i = 0; i < node.length; i++)
		{
			int row = node[i].x;
			int col = node[i].y;
			if((int)work.get(row, col)[0] == 0)
			{
				edgeNodeVec.add(new Point(row, col));
			}

			work.put(node[i].x, node[i].y, 255);
		}

		Point[] edgeNodeArray = new Point[edgeNodeVec.size()];
		edgeNodeVec.copyInto(edgeNodeArray);

		work.setTo(Scalar.all(0));
		convertCellCollectiontoObjectMap(cellCandidates, work);
		Vector<Boolean> isEdgeCell = new Vector<Boolean>();
		for(int i = 0; i < cellCandidates.getSize(); i++)
		{
			isEdgeCell.add(false);
		}

		for(int i = 0; i < edgeNodeArray.length; i++)
		{
			int pix = (int)work.get(edgeNodeArray[i].x, edgeNodeArray[i].y)[0];
			if(pix > 0)
			{
				isEdgeCell.set(pix - 1, true);
			}
		}

		work.setTo(Scalar.all(0));
		for(int i = 0; i < isEdgeCell.size(); i++)
		{
			for(int j = 0; j < cellCandidates.getCell(i).getPixelCount(); j++)
			{
				if(isEdgeCell.get(i))
				{
					work.put(cellCandidates.getCell(i).getPixel(j).x, cellCandidates.getCell(i).getPixel(j).y, 255);
				}
				else
				{
					work.put(cellCandidates.getCell(i).getPixel(j).x, cellCandidates.getCell(i).getPixel(j).y, 155);
				}
			}
		}
		for(int i = 0; i < cellCandidates.getIntersection().length; i++)
		{
			work.put(cellCandidates.getIntersection()[i].x, cellCandidates.getIntersection()[i].y, 60);
		}

		return isEdgeCell;
	}

	//-----------------------------------------------------------------------------------------

	/**
	 * Creates a labelled matrix objectMap from the collection of connected regions. 
	 * @param cellCollection
	 * @param objectMap
	 */
	private static void convertCellCollectiontoObjectMap(ConnectedRegion cellCollection, Mat objectMap)
	{
		objectMap.setTo(Scalar.all(0));

		for(int i = 0; i < cellCollection.getSize(); i++)
		{
			for(int j = 0; j < cellCollection.getCell(i).getPixelCount(); j++){
				int row = cellCollection.getCell(i).getPixel(j).x;
				int col = cellCollection.getCell(i).getPixel(j).y;

				objectMap.put(row, col, i + 1);
			}
		}
	}

	private static void sortCells(Vector<DataCellSelection> cellsForSelection)
	{
		if(cellsForSelection.size() < 2){return;}

		for(int i = 1; i < cellsForSelection.size(); i++)
		{
			DataCellSelection tempData = cellsForSelection.get(i);

			if(cellsForSelection.get(i - 1).getEval() > tempData.getEval())
			{
				int j = i;

				do 
				{
					cellsForSelection.set(j, cellsForSelection.get(j - 1));
					j--;
				} 
				while (j > 0 && cellsForSelection.get(j - 1).getEval() > tempData.getEval());

				cellsForSelection.set(j, tempData);
			}
		}
	}

}
