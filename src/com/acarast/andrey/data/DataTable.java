package com.acarast.andrey.data;

import java.awt.Polygon;
import java.util.Vector;

import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

/**
 * This class represents a table which stores all the data for the project.
 * Each element represents data which describes a single input image with
 * all information, including biological and image processing data.
 * @author Andrey G
 */
public class DataTable
{
	private Vector <DataTableElement> table;

	//----------------------------------------------------------------

	public DataTable ()
	{
		table = new Vector<DataTableElement>();
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------

	/**
	 * Returns the number of elements in the table.
	 * @return
	 */
	public int getTableSize ()
	{
		return table.size();
	}

	/**
	 * Returns an element in the table by its index. If the index is 
	 * out of range, throws an exception.
	 * @param index
	 * @return
	 */
	public DataTableElement getElement (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < table.size())
		{
			return table.get(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Returns the last element in the table.
	 * @return
	 */
	public DataTableElement getElementLast () 
	{
		return table.lastElement();
	}

	//----------------------------------------------------------------
	//---- ADD METHODS
	//----------------------------------------------------------------

	/**
	 * Adds a dummy (empty) element to the table. 
	 */
	public void addElement ()
	{
		table.addElement(new DataTableElement());
	}

	/**
	 * Adds an element to the table.
	 * @param element
	 */
	public void addElement (DataTableElement element)
	{
		table.addElement(element);
	}

	/**
	 * Adds a new sample (channel) to the specified image/device. If control flag is 
	 * true, then the index of the control sample is also updated.
	 * @param index
	 */
	public void addSample (int index, Polygon polygon, boolean isControl) throws ExceptionMessage
	{
		if (index >= 0 && index < table.size())
		{
			table.get(index).addSample(polygon, isControl);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	//----------------------------------------------------------------
	//---- REMOVE METHODS
	//----------------------------------------------------------------

	/**
	 * Removes an element from the table by its index. If the index is
	 * out of range, throws an excpetion.
	 * @param index
	 */
	public void removeElement (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < table.size())
		{
			table.remove(index);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Removes all elements from the table
	 */
	public void removeAllElements ()
	{
		table.removeAllElements();

		System.gc();
	}

	/**
	 * Removes a sample channel from the list of channels of the image/device. 
	 * @param indexDevice
	 * @param indexSample
	 * @throws ExceptionMessage
	 */
	public void removeSample (int indexDevice, int indexSample) throws ExceptionMessage
	{
		if (indexDevice >= 0 && indexDevice < table.size())
		{
			table.get(indexDevice).removeSample(indexSample);
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	public void removeAllSamples (int indexDevice) throws ExceptionMessage
	{
		if (indexDevice >= 0 && indexDevice < table.size())
		{
			table.get(indexDevice).removeAllSamples();
		}
		else
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}
}
