package com.acarast.andrey.data;

import java.awt.Point;
import java.awt.Polygon;

import com.acarast.andrey.exception.ExceptionMessage;

/** This class contains information about the location of a channel
 * in the image, as well as data list, storing pixels, belonging to cells.
 * @author Andrey G
 */
public class DataImageChannel
{
    //---- Location of this channel in the image.
    private Polygon channelLocation;

    //---- All detected cells in this channel.
    private DataCellCollection cellCollection;

    //----------------------------------------------------------------

    public DataImageChannel ()
    {
	channelLocation = new Polygon();

	cellCollection = new DataCellCollection();
    }
    
    public DataImageChannel (Polygon polygon)
    {
	channelLocation = polygon;
	
	cellCollection = new DataCellCollection();
    }

    //----------------------------------------------------------------
    //---- GET METHODS
    //----------------------------------------------------------------

    /**
     * Returns the collection of detected cells.
     * @return
     */
    public DataCellCollection getCellCollection ()
    {
	return cellCollection;
    }
    
    /**
     * Returns the location of the channel in the image in a polygon.
     * @return
     */
    public Polygon getChannelLocation ()
    {
	return channelLocation;
    }

    /**
     * Returns the location of the channel in the image in an array of point.
     * @return
     */
    public Point[] getChannelLocationPoints ()
    {
	int[] pointX = channelLocation.xpoints;
	int[] pointY = channelLocation.ypoints;

	Point[] outputArray = new Point[pointX.length];

	for (int i = 0; i < outputArray.length; i++)
	{
	    outputArray[i] = new Point(pointX[i], pointY[i]);
	}

	return outputArray;
    }

    
    
    //----------------------------------------------------------------
    //---- SET METHODS
    //----------------------------------------------------------------

    /**
     * Sets the location of the microfluidic channel in the image by
     * specifying points in the polygon.
     * @param inputSampleArea
     */
    public void setChannelLocation (Polygon inputSampleArea)
    {
	int[] pointsX = inputSampleArea.xpoints;
	int[] pointsY = inputSampleArea.ypoints;

	for (int k = 0; k < pointsX.length; k++)
	{
	    channelLocation.addPoint(pointsX[k], pointsY[k]);
	}
    }
    
    public void setCellCollection (DataCellCollection collection) throws ExceptionMessage
    {
    	cellCollection.copyFrom(collection);
    }
}
