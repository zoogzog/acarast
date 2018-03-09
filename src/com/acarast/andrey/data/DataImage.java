package com.acarast.andrey.data;

import java.util.Vector;

import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

public class DataImage
{
    private Vector <DataImageChannel> channelSet;

    //----------------------------------------------------------------

    public DataImage ()
    {
	channelSet = new Vector<DataImageChannel>();
    }

    //----------------------------------------------------------------
    //---- ADD METHODS
    //----------------------------------------------------------------

    /**
     * Adds a dummy (empty) channel to the channel set.
     */
    public void addChannel ()
    {
	channelSet.addElement(new DataImageChannel());
    }

    /**
     * Adds a new channel to the channel set
     * @param channel
     */
    public void addChannel (DataImageChannel channel)
    {
	channelSet.addElement(channel);
    }

    //----------------------------------------------------------------
    //---- GET METHODS
    //----------------------------------------------------------------

    /**
     * Returns number of channels in the list.
     * @return
     */
    public int getChannelCount ()
    {
	return channelSet.size();
    }
    
    /**
     * Returns a channel by its index. If the index is out of range
     * an exception is thrown.
     * @param index
     * @return
     * @throws ExceptionMessage
     */
    public DataImageChannel getChannel (int index) throws ExceptionMessage
    {
	if (index >= 0 && index < channelSet.size())
	{
	    return channelSet.get(index);
	}
	else
	{
	    throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
	}
    }
    
    public DataImageChannel getChannelLast () 
    {
	return channelSet.lastElement();
    }
    
    //----------------------------------------------------------------
    //---- REMOVE METHODS
    //----------------------------------------------------------------

    /**
     * Removes a channel by its index. If the index is out of range
     * an exception is thrown.
     * @param index
     * @throws ExceptionMessage
     */
    public void removeChannel (int index) throws ExceptionMessage
    {
	if (index >= 0 && index < channelSet.size())
	{
	    channelSet.remove(index);
	}
	else
	{
	    throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
	}
    }

    /**
     * Removes all channels from the list.
     */
    public void removeAllChannels ()
    {
	channelSet.removeAllElements();
    }
}
