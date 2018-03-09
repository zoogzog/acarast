package com.acarast.kikuchi.core.data;

import java.awt.Point;
import java.util.Vector;

/**
 * @author Kikuchi K, Andrey G
 */
public class ConnectedRegionCollection
{
	private Vector<ConnectedRegion> cells;

	//----------------------------------------------------------------
	
	public ConnectedRegionCollection()
	{
		cells = new Vector<ConnectedRegion>();
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------
	
	public int getSize()
	{
		return cells.size();
	}

	public ConnectedRegion getConnectedRegion(int i)
	{
		return cells.get(i);
	}

	//----------------------------------------------------------------
	//----  ADD METHODS
	//----------------------------------------------------------------
	
	public void addNewConnectedRegion(Point[] cellPixels)
	{
		ConnectedRegion newCellCollection = new ConnectedRegion();
		newCellCollection.addNewCell(cellPixels);
		
		cells.addElement(newCellCollection);
	}

	//----------------------------------------------------------------
	
	public void dispose ()
	{
		for (int k = 0; k < cells.size(); k++)
		{
			cells.get(k).dispose();
		}

		cells.removeAllElements();
	}
}