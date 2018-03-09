package com.acarast.andrey.core.device;

import java.util.Vector;

import com.acarast.andrey.exception.ExceptionMessage;

/**
 * Class to describe the RaST-TAS (DSTM) device. Each device contains several 
 * microfluidic channels.  One of those channels is a control one.
 * @author Andrey G
 */
public class MicrofluidicDevice
{
	private Vector <MicrofluidicChannel> channelSet; 

	private int controlChannelIndex;

	//----------------------------------------------------------------

	public MicrofluidicDevice ()
	{	
		channelSet = new Vector<MicrofluidicChannel>();

		controlChannelIndex = -1;
	}

	//----------------------------------------------------------------

	public int getChannelCount ()
	{
		return channelSet.size();
	}

	//----------------------------------------------------------------

	/**
	 * Add new channel to the channel set of this device.
	 */
	public void addChannel ()
	{
		channelSet.addElement(new MicrofluidicChannel());
	}

	/**
	 * Add a specified channel to the channel set of this device.
	 */
	public void addChannel (MicrofluidicChannel channel)
	{
		channelSet.addElement(channel);
	}

	/**
	 * Remove a channel of this device, specified by an index. If an index less
	 * than zero, or more than number of channels is specified the method throws an exception
	 * @param index
	 */
	public void removeChannel (int index) throws ExceptionMessage
	{
		if (index >= 0 && index <channelSet.size())
		{
			channelSet.remove(index);
		}
		else
		{
			throw new ExceptionMessage (ExceptionMessage.EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	/**
	 * Remove all channels of this device. Clean all garbage.
	 */
	public void removeAllChannels ()
	{
		channelSet.removeAllElements();

		controlChannelIndex = -1;
	}

	//----------------------------------------------------------------

	/** 
	 * Returns a device's channel by its index. Throws exception if the index is out of range.
	 * @param index
	 * @return
	 * @throws ExceptionMessage
	 */
	public MicrofluidicChannel getChannel (int index) throws ExceptionMessage
	{
		if (index >= 0 && index < channelSet.size())
		{
			return channelSet.get(index);
		}
		else
		{
			throw new ExceptionMessage (ExceptionMessage.EXCEPTION_CODE.EXCODE_INDEXOFRANGE);
		}
	}

	//----------------------------------------------------------------

	public int getControlChannelIndex ()
	{
		return controlChannelIndex;
	}

	/**
	 * Sets the control index of the channel. The index should be in range.
	 * @param index
	 */
	public void setControlChannelIndex (int index)
	{
		if (index >= 0 && index < channelSet.size())
		{
			controlChannelIndex = index;
		}
	}

}
