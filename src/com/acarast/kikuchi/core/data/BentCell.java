package com.acarast.kikuchi.core.data;

import java.awt.Point;
import java.util.Vector;

/**
 * (Probably) A descriptor for storing points where cell is bended.
 * @author Kikuchi K, Andrey G
 */
public class BentCell 
{
	
	private Vector <Double> bent; 
	
	//---- Point where bend value is maximum
	private double maxBent;
	private Point maxBentPoint;
	
	private Point[] region;

    //----------------------------------------------------------------
	
	public BentCell()
	{
		bent = new Vector<Double>();
	}

    //----------------------------------------------------------------
	//---- SET METHODS
    //----------------------------------------------------------------
	
	public void setMaxBentPoint(Point p)
	{
		maxBentPoint = p;
	}

	public void setRegion(Point[] reg)
	{
		region = reg;
	}

	public void setMaxBent(double d)
	{
		maxBent = d;
	}

    //----------------------------------------------------------------
	//---- GET METHODS
    //----------------------------------------------------------------
	
	public Point getMaxBentPoint()
	{
		return maxBentPoint;
	}

	public double getMaxBent()
	{
		return maxBent;
	}

	public double[] getBent()
	{
		double[] retBent = new double[bent.size()];
		for(int i = 0; i < bent.size(); i++)
		{
			retBent[i] = bent.get(i).doubleValue();
		}
		return retBent;
	}

	public Point[] getRegion()
	{
		return region;
	}
	
    //----------------------------------------------------------------
	
	public void addBent(double d)
	{
		bent.add(d);
	}


}

