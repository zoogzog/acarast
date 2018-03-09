package com.acarast.andrey.core.task;

import com.acarast.andrey.core.algorithm.EnumTypes;
import com.acarast.andrey.core.algorithm.EnumTypes.AlgorithmType;

/**
 * Settings for launching the main algorithm 
 * @author Andrey G
 */
public class TaskSettings extends EstimatorSettings
{	
	//---- Is perform channel detection in automatic or manual mode
	private boolean isManualMode;

	//---- Is perform estimation of the channel sensitivity
	private boolean isEstimateSensitivity;

	//---- Total number of samples in the image/device
	private int samplesCount;

	//---- Index of the control sample
	private int indexControlSample;
	
	
	private EnumTypes.AlgorithmType algorithm;
	
	//----------------------------------------------------------------

	public TaskSettings ()
	{
		super();
		
		isManualMode = false;
		isEstimateSensitivity = false;

		indexControlSample = -1;
		
		algorithm = AlgorithmType.SPLIT_AND_MERGE;
	}

	//----------------------------------------------------------------

	/**
	 * Check if the specifies settings are correct. Only preliminary check. 
	 * @return
	 */
	public boolean isOK ()
	{
		if (indexControlSample < 0) { return false; }

		return true;
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------

	public boolean getIsManualMode ()
	{
		return isManualMode;
	}

	public boolean getIsEstimateSensitivity ()
	{
		return isEstimateSensitivity;
	}

	public int getSamplesCount ()
	{
		return samplesCount;
	}

	public int getIndexControlSample ()
	{
		return indexControlSample;
	}
	
	public AlgorithmType getAlgorithmType ()
	{
		return algorithm;
	}
	
	//----------------------------------------------------------------
	//---- SET METHODS
	//----------------------------------------------------------------

	public void setIsManualMode (boolean value)
	{
		isManualMode = value;
	}

	public void setIsEstimateSensitivity (boolean value)
	{
		isEstimateSensitivity = value;
	}

	public void setSamplesCount (int value)
	{
		samplesCount = value;
	}

	public void setIndexControlSample (int value)
	{
		indexControlSample = value;
	}

	public void setAlgorithmType (AlgorithmType value)
	{
		algorithm = value;
	}
}

