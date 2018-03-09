package com.acarast.kikuchi.core.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;

public class BreadFirstSearch 
{
	//-----------------------------------------------------------------------------------------
	//---- Breadth first search is an algorithm for searching in a tree or graph
	//---- Brief introduction could be found in wiki: https://en.wikipedia.org/wiki/Breadth-first_search

	public static List<Integer> run (Mat graphMat, List<Integer> indexList, int startPoint, int[] visit, List<Integer> dividedGraphIndex)
	{
		List<Integer> subRegion = new ArrayList<Integer>();
		LinkedList<List<Integer>> list = new LinkedList<List<Integer>>();

		List<Integer> startP = new ArrayList<Integer>();
		startP.add(startPoint);
		list.offer(startP);
		visit[startPoint] = 1;
		dividedGraphIndex.add(startPoint);
		subRegion.add(startPoint);

		return traceNode(graphMat, indexList, list, visit, dividedGraphIndex);
	}

	private static List<Integer> traceNode(Mat grouphMat, List<Integer> indexList, LinkedList<List<Integer>> list, int[] visit, List<Integer> dividedGraphIndex)
	{
		List<Integer>  returnList = null;

		do
		{
			List<Integer> nodeList = list.poll();
			int node = nodeList.get(nodeList.size() - 1);

			for(int i = 0; i < indexList.size(); i++)
			{
				if((visit[indexList.get(i)] == 0)&&((int)grouphMat.get(node, indexList.get(i))[0] > 0))
				{
					List<Integer> newList = new ArrayList<Integer>();
					newList.addAll(nodeList);
					newList.add(indexList.get(i));

					list.offer(newList);
					visit[indexList.get(i)] = 1;
					dividedGraphIndex.add(indexList.get(i));
				}
			}
		}
		while(list.size() != 0);

		return returnList;
	}

}
