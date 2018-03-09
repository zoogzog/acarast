package com.acarast.andrey.core.device;

/**
 * Class to describe microfluidic channels of the RaST-TAS (DSTM) device.
 * @author Andrey G
 */

public class MicrofluidicChannel
{
	public static enum Sensitivity {SENSITIVE, RESISTANT, UNKNOWN}; 

	private FeatureVector featureVector;

	private Sensitivity sensitivity;

	//----------------------------------------------------------------

	public MicrofluidicChannel ()
	{
		featureVector = new FeatureVector();

		sensitivity = Sensitivity.UNKNOWN;
	}

	//----------------------------------------------------------------

	public Sensitivity getSensitivity ()
	{
		return sensitivity;
	}

	public void setSensitivity (Sensitivity s)
	{
		sensitivity = s;
	}

	public FeatureVector getFeatureVector ()
	{
		return featureVector;
	}

	public void setFeatureVector (FeatureVector vector)
	{
		featureVector = vector;
	}

	//----------------------------------------------------------------
}
