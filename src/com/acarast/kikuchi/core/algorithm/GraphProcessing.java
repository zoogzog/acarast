package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Mat;

/**
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class GraphProcessing 
{

	public static List<Integer> route2node(Mat grouphMat, int startPoint, int endPoint)
	{
		int[] visit = new int[grouphMat.width()];

		for(int i = 0; i < visit.length; i++)
		{
			visit[i] = 0;
		}

		LinkedList<List<Integer>> list = new LinkedList<List<Integer>>();

		List<Integer> startP = new ArrayList<Integer>();
		startP.add(startPoint);
		list.offer(startP);
		visit[startPoint] = 1;

		return openNode(grouphMat, list, visit, endPoint);
	}

	public static List<Integer> openNode(Mat grouphMat, LinkedList<List<Integer>> list, int[] visit, int endPoint)
	{
		List<Integer>  returnList = null;

		//---- WTF KIKUCHI! WHY HAVE YOU USED GOTO jumps... :(
		LOOP:
			do
			{
				List<Integer> nodeList = list.poll();
				int node = nodeList.get(nodeList.size() - 1);

				for(int i = 0; i < grouphMat.width(); i++)
				{
					if((visit[i] == 0)&&((int)grouphMat.get(node, i)[0] > 0))
					{
						List<Integer> newList = new ArrayList<Integer>();
						newList.addAll(nodeList);
						newList.add(i);

						list.offer(newList);
						visit[i] = 1;
						if(i == endPoint)
						{
							returnList = newList;
							break LOOP;
						}
					}
				}
			}
			while(list.size() != 0);

		return returnList;
	}
	
	public static List<Integer> getCellSegments(Mat grouphMat, List<Integer> route, int VALUE)
	{
		List<Integer> returnList = new ArrayList<Integer>();

		for(int i = 0; i < route.size() - 1; i++)
		{
			try{		
				if((int)grouphMat.get(route.get(i), route.get(i + 1))[0] < VALUE)
				{
					returnList.add((int)grouphMat.get(route.get(i), route.get(i + 1))[0] - 1);
				}
			}
			catch(Exception e){} 
		}
		return returnList;
	}


	public static void graphDivision(Mat cellGraph, Point[] node, List<List<Integer>> indexList)
	{
		final int DIVISION_VALUE = 120;
		final int THRES = 4;

		int[] visit = new int[cellGraph.width()];

		for(int k = 0; k < visit.length; k++)
		{
			visit[k] = 0;
		}

		List<Integer> graphIndex = new ArrayList<Integer>();

		for(int k = 0; k < visit.length; k++)
		{
			if(visit[k] == 0)
			{
				graphIndex.add(k);
			}
		}
		indexList.add(graphIndex);

		if(cellGraph.width() > THRES)
		{
			for(int i = 0; i < node.length; i++)
			{
				int pathNode = graphCheckPath(cellGraph, i, node, DIVISION_VALUE);

				if(pathNode!= -1)
				{
					int deleteRegion = -1;
					for(int indexListID = 0; indexListID < indexList.size(); indexListID++)
					{
						for(int elem = 0; elem < indexList.get(indexListID).size(); elem++)
						{
							if(indexList.get(indexListID).get(elem) == i)
							{
								deleteRegion = indexListID;
							}
						}
					}

					List<Integer> dividedGraphIndex = new ArrayList<Integer>();
					visit[i] = 1;
					dividedGraphIndex.add(i);
					BreadFirstSearch.run(cellGraph, indexList.get(deleteRegion), pathNode, visit, dividedGraphIndex);
					visit[i] = 0;
					indexList.add(dividedGraphIndex);

					List<Integer> dividedGraphIndexRest = new ArrayList<Integer>();
					for(int k = 0; k < indexList.get(deleteRegion).size(); k++)
					{
						if(visit[indexList.get(deleteRegion).get(k)] == 0)
						{
							dividedGraphIndexRest.add(indexList.get(deleteRegion).get(k));
						}
					}
					indexList.add(dividedGraphIndexRest);

					indexList.remove(deleteRegion);

					for(int k = 0; k < visit.length; k++)
					{
						visit[k] = 0;
					}
				}
			}
		}
	}

	public static int graphCheckPath(Mat graph, int point, Point[] node, int value)
	{
		int MIN_VALUE = 10;

		Vector<Integer> neighborNode = new Vector<Integer>();
		for(int i = 0; i < graph.width(); i++)
		{
			int seg = (int)graph.get(point, i)[0];
			if((seg > 0)&&(seg < value))
			{
				neighborNode.add(i);
			}
		}

		if(neighborNode.size() == 3)
		{
			int n1 = neighborNode.get(0);
			int n2 = neighborNode.get(1);
			int n3 = neighborNode.get(2);

			Point center = node[point];
			Point edge1 = node[n1];
			Point edge2 = node[n2];
			Point vec1 = new Point(edge1.x - center.x, edge1.y - center.y);
			Point vec2 = new Point(edge2.x - center.x, edge2.y - center.y);
			double pixBent1 = Math.acos(Utils.angleCalc(vec1, vec2))*180/Math.PI;

			center = node[point];
			edge1 = node[n2];
			edge2 = node[n3];
			vec1 = new Point(edge1.x - center.x, edge1.y - center.y);
			vec2 = new Point(edge2.x - center.x, edge2.y - center.y);
			double pixBent2 = Math.acos(Utils.angleCalc(vec1, vec2))*180/Math.PI;

			center = node[point];
			edge1 = node[n3];
			edge2 = node[n1];
			vec1 = new Point(edge1.x - center.x, edge1.y - center.y);
			vec2 = new Point(edge2.x - center.x, edge2.y - center.y);
			double pixBent3 = Math.acos(Utils.angleCalc(vec1, vec2))*180/Math.PI;

			if((pixBent1 < value)&&(pixBent3 < value)&&(Math.abs(node[point].x - node[n1].x) + Math.abs(node[point].x - node[n1].x) > MIN_VALUE))
			{
				return n1;
			}

			if((pixBent1 < value)&&(pixBent2 < value))
			{
				return n2;
			}

			if((pixBent2 < value)&&(pixBent3 < value))
			{
				return n3;
			}
		}

		return -1;
	}

}
