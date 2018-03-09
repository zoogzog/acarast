package com.acarast.andrey.data;

import java.util.Vector;

import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

/**
 * A collection of cells, located in one channel.
 * @author Andrey G
 */
public class DataCellCollection
{
	private Vector <DataCell> cellList;

	//----------------------------------------------------------------

	public DataCellCollection ()
	{
		cellList = new Vector<DataCell>();
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------

	/**
	 * Returns number of cells in this collection.
	 * @return
	 */
	public int getCellCount ()
	{
		return cellList.size();
	}

	/**
	 * Returns the cell in the list by its index. Throws an exception if the index
	 * is out of range.
	 * @param index
	 * @return
	 * @throws ExceptionMessage
	 */
	public DataCell getCell (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < cellList.size())
		{
			return cellList.get(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Returns the last cell in the list.
	 * @return
	 */
	public DataCell getCellLast ()
	{
		return cellList.lastElement();
	}

	//----------------------------------------------------------------
	//---- ADD METHODS
	//----------------------------------------------------------------

	/**
	 * Add a dummy cell to the cell list. Cell contains no pixels.
	 */
	public void addCell ()
	{
		cellList.addElement(new DataCell());
	}

	/**
	 * Add cell to the cell list.
	 * @param cell
	 */
	public void addCell (DataCell cell)
	{
		cellList.addElement(cell);
	}

	//----------------------------------------------------------------
	//---- REMOVE METHODS
	//----------------------------------------------------------------

	/**
	 * Removes a cell from the cell list, specified by the index. If the index is 
	 * out of range, throws an exception.
	 */
	public void removeCell (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < cellList.size())
		{
			cellList.remove(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Removes all cells in the cell list.
	 */
	public void removeAllCells ()
	{
		cellList.removeAllElements();
	}

	//----------------------------------------------------------------

	public void copyFrom (DataCellCollection collection) throws ExceptionMessage
	{
		cellList.removeAllElements();
		
		for (int i = 0; i < collection.getCellCount(); i++)
		{
			this.addCell();
			this.getCellLast().addPixelArray(collection.getCell(i).getPixelArray());
			this.getCellLast().setSizeThinned(collection.getCell(i).getSizeThinned());
			
		}
	}
}
