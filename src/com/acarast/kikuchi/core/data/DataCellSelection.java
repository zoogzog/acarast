package com.acarast.kikuchi.core.data;

import java.awt.Point;
import java.util.List;

/**
 * @author Kikuchi K, Andrey G
 */
public class DataCellSelection 
{
	private List<Integer> segments;
	private Point[] region;
	private double eval;
	
	private boolean isSelected = true;

	//-----------------------------------------------------------------------------------------
	
	public DataCellSelection(List<Integer> route, Point[] p, double n)
	{
		segments = route;
		region = p;
		eval = n;
	}

	//-----------------------------------------------------------------------------------------
	
	//---- Returns true if there is a segment with a specified ID
	public boolean isContain(int segmentID)
	{
		for(int i = 0; i < segments.size(); i++)
		{
			if(segments.get(i) == segmentID){return true;}
		}
		return false;
	} 

	//---- Returns true if the current cell was selected
	public boolean isSelected() 
	{
		return isSelected;
	}
	
	//---- Remove selection of this cell
	public void deselect()
	{
		isSelected = false;
	}

	//-----------------------------------------------------------------------------------------
	//---- GET METHODS
	//-----------------------------------------------------------------------------------------
	
	public Point[] getRegion()
	{
		return region;
	}

	public double getEval()
	{
		return eval;
	}
	
	public List<Integer> getRoute()
	{
		return segments;
	}
}
