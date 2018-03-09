package com.acarast.andrey.data;

import java.awt.Polygon;

import com.acarast.andrey.core.device.MicrofluidicDevice;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

/**
 * Class to describe single element of the project. The element contains 
 * all the data: input file location, input image, location of the 
 * detected channels, information about the device, etc.
 * @author Andrey G
 */
public class DataTableElement
{
    //---- This class contains information about location of the
    //---- input data in the file system. It's name, path, etc.
    private DataFile dataFile;

    //---- This class stores information related to image processing.
    //---- Such ass position of the detected channels, detected 
    //---- cells etc.
    private DataImage dataImage;

    //---- This structure stores feature vectors, sensitivity
    //---- all information related to biological data.
    private MicrofluidicDevice dataDevice; 

    //----------------------------------------------------------------

    public DataTableElement ()
    {
	dataFile = new DataFile();
	dataImage = new DataImage();
	dataDevice = new MicrofluidicDevice();
    }

    //----------------------------------------------------------------

    public DataFile getDataFile ()
    {
	return dataFile;
    }

    public DataImage getDataImage ()
    {
	return dataImage;
    }

    public MicrofluidicDevice getDataDevice ()
    {
	return dataDevice;
    }

    /**
     * Returns number of sample channels in this element. Also checks the consistency of lists.
     * @return
     */
    public int getChannelCount () throws ExceptionMessage
    {
	int countImage = getDataImage().getChannelCount();
	int countDevice = getDataDevice().getChannelCount();

	if (countDevice != countImage)
	{
	    throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_TABLEUNCOSISTENCY);
	}

	return countDevice;
    }

    //----------------------------------------------------------------
    //---- RESET DATA
    //----------------------------------------------------------------

    public void resetData ()
    {

    }

    //----------------------------------------------------------------
    //---- MANIPULATION WITH THE SAMPLES
    //----------------------------------------------------------------

    public void addSample (Polygon polygon, boolean isControl) throws ExceptionMessage
    {
	//---- Check number of currently added sample channels
	int countImage = getDataImage().getChannelCount();
	int countDevice = getDataDevice().getChannelCount();

	if (countDevice == countImage)
	{
	    //---- If everything is fine add new sample channel. 
	    //---- Be careful and preserve lists consistency. Similar number of channels 
	    //---- Should be in every part of the device.

	    if (polygon == null) { getDataImage().addChannel(); }
	    else { getDataImage().addChannel(new DataImageChannel(polygon)); }

	    getDataDevice().addChannel();

	    //---- Set index of the control sample if the flag is raised. Since it is the last element in
	    //---- the channels list, we can set the index as the size of the list before addition of the element.
	    if (isControl)
	    {
		getDataDevice().setControlChannelIndex(countDevice);
	    }
	}
	else
	{
	    //---- Inconsitency of the lists is detected, throw error
	    throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_TABLEUNCOSISTENCY);
	}
    }

    public void removeSample (int index) throws ExceptionMessage
    {
	//---- Check number of currently added sample channels
	int countImage = getDataImage().getChannelCount();
	int countDevice = getDataDevice().getChannelCount();

	if (countDevice == countImage)
	{
	    if (index >= 0 && index < countDevice)
	    {
		//---- Everything has been checked, safe to remove
		getDataImage().removeChannel(index);
		getDataDevice().removeChannel(index);

		//---- Reset index of the control sample if before this one was control
		if (index == getDataDevice().getControlChannelIndex())
		{
		    getDataDevice().setControlChannelIndex(-1);
		}
	    }
	    else
	    {
		throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
	    }
	}
	else
	{
	    //---- Inconsitency of the lists is detected, throw error
	    throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_TABLEUNCOSISTENCY);
	}
    }

    public void removeAllSamples () 
    {
	dataImage.removeAllChannels();
	dataDevice.removeAllChannels();
    }
}
