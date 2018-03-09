package com.acarast.andrey.core.device;

/**
 * Data structure for storing extracted cell features.
 * @author Andrey G
 */

public class FeatureVector
{
	//----------------------------------------------------------------
	//---- Absolute features 
	//----------------------------------------------------------------

	//---- Number of detected cells.
	private int cellCount;

	//---- Length of cells.
	private double cellLengthMean;
	private int cellLengthMin;
	private int cellLengthMax;
	private double cellLengthDeviation;

	//---- Area occupied by cells
	private double cellAreaMean;
	private int cellAreaMin;
	private int cellAreaMax;

	//---- Average width of cells, where width is calculated as amount of pixels / length
	private double cellWidthMean;

	//---- Pixel density. Relation of area occupied by cells to 
	//---- the total area of the channel.
	private double cellPixelDensity;

	//---- Histogram, which displays the frequency of cells with certain lengths. The histogram's delta between two steps is the 
	//---- featureCellLengthFrequencyHistogramOffset, it starts from the cell with min length
	private double[] cellLengthFrequencyHistogram;
	private int cellLengthFrequencyHistogramOffset;

	//---- Confidence interval for the length feature. X% amount of bacteria
	//---- belong to the interval. X is specified in DetectorSettings by theFEATURE_EXTRACTOR_BaseLength_INTERVAL
	private int cellLengthIntervalMin;
	private int cellLengthIntervalMax;

	//----------------------------------------------------------------
	//---- Relative features
	//----------------------------------------------------------------

	//---- Number of detected cells, relative to the control sample.
	private double cellCountR;

	//---- Average length of cells, relative to the control sample.
	private double cellLengthMeanR;

	//---- Pixel density, relative to the control sample.
	private double cellPixelDensityR;
	
	//---- Relative mean area
	private double cellAreaMeanR;

	//----------------------------------------------------------------

	public FeatureVector ()
	{
		cellCount = 0;
		cellLengthMean = 0;
		cellPixelDensity = 0;


		cellCountR = 0;
		cellLengthMeanR = 0;
		cellPixelDensityR = 0;
		cellAreaMeanR = 0;
	}

	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------

	public int getAbsoluteCellCount ()
	{
		return cellCount;
	}

	//----------------------------------------------------------------
	//---- Absolute length

	public double getAbsoluteCellLengthMean ()
	{
		return cellLengthMean;
	}

	public int getAbsoluteCellLengthMin ()
	{
		return cellLengthMin;
	}

	public int getAbsoluteCellLengthMax ()
	{
		return cellLengthMax;
	}

	public double getAbsoluteCellLengthDeviation ()
	{
		return cellLengthDeviation;
	}

	//----------------------------------------------------------------

	public double getAbsolutePixelDensity ()
	{
		return cellPixelDensity;
	}

	//----------------------------------------------------------------
	//---- Absolute area

	public double getAbsoluteCellAreaMean ()
	{
		return cellAreaMean;
	}

	public double getAbsoluteCellAreaMin ()
	{
		return cellAreaMin;
	}

	public double getAbsoluteCellAreaMax ()
	{
		return cellAreaMax;
	}

	public double getAbsoluteWidhtMean ()
	{
		return cellWidthMean;
	}

	//----------------------------------------------------------------
	//---- Histogram

	/**
	 * Returns the length frequency histogram if it was calculated or null if not.
	 * @return
	 */
	public double[] getAbsoluteCellLengthFrequencyHistogram ()
	{
		//---- Check if the frequency histogram was calculated
		if (cellLengthFrequencyHistogram == null) { return null; }
		
		//---- return as copy!
		double[] outputArray = new double[cellLengthFrequencyHistogram.length];

		for (int k = 0; k < outputArray.length; k++)
		{
			outputArray[k] = cellLengthFrequencyHistogram[k];
		}

		return outputArray;
	}

	public int getAbsoluteCellLengthFrequencyHistogramOffset()
	{
		return cellLengthFrequencyHistogramOffset;
	}

	public int getAbsoluteCellLengthIntervalMin ()
	{
		return cellLengthIntervalMin;
	}

	public int getAbsoluteCellLengthIntervalMax ()
	{
		return cellLengthIntervalMax;
	}

	//----------------------------------------------------------------
	//---- Relative
	//----------------------------------------------------------------

	public double getRelativeCellCount ()
	{
		return cellCountR;
	}

	public double getRelativeCellLengthMean ()
	{
		return cellLengthMeanR;
	}

	public double getRelativeCellPixelDensity ()
	{
		return cellPixelDensityR;
	}

	public double getRelativeCellAreaMean ()
	{
	    return cellAreaMeanR;
	}
	
	//----------------------------------------------------------------
	//---- SET METHODS
	//----------------------------------------------------------------

	public void setAbsoluteCellCount (int value)
	{
		if (value >= 0)
		{
			cellCount = value;
		}
	}

	//----------------------------------------------------------------
	//---- Absolute length

	public void setAbsoluteCellLengthMean (double value)
	{
		if (value >= 0)
		{
			cellLengthMean = value;
		}
	}

	public void setAbsoluteCellLengthMin (int value)
	{
		cellLengthMin = value;
	}

	public void setAbsoluteCellLengthMax (int value)
	{
		cellLengthMax = value;
	}

	public void setAbsoluteCellLengthDeviation (double value)
	{
		cellLengthDeviation = value;
	}

	//----------------------------------------------------------------

	public void setAbsolutePixelDensity (double value)
	{
		if (value >= 0)
		{
			cellPixelDensity = value;
		}
	}

	//----------------------------------------------------------------
	//---- Absolute area

	public void setAbsoluteCellAreaMean (double value)
	{
		cellAreaMean = value;
	}

	public void setAbsoluteCellAreaMin (int value)
	{
		cellAreaMin = value;
	}

	public void setAbsoluteCellAreaMax (int value)
	{
		cellAreaMax = value;
	}

	public void setAbsoluteCellWidhtMean (int value)
	{
		cellWidthMean = value;
	}

	//----------------------------------------------------------------
	//---- Histogram

	public void setRelativeFrequencyHistogram (double[] value, int histogramOffset)
	{
		cellLengthFrequencyHistogram = new double[value.length];

		for (int i = 0; i < value.length; i++)
		{
			cellLengthFrequencyHistogram[i] = value[i];
		}

		cellLengthFrequencyHistogramOffset = histogramOffset;
	}

	public void setAbsoluteCellLengthIntervalMin (int value)
	{
		cellLengthIntervalMin = value;
	}

	public void setAbsoluteCellLengthIntervalMax (int value)
	{
		cellLengthIntervalMax = value;
	}

	//----------------------------------------------------------------
	//---- Relative
	//----------------------------------------------------------------

	public void setRelativeCellCount (double value)
	{
		cellCountR = value;
	}

	public void setRelativeCellLengthMean (double value)
	{
		cellLengthMeanR = value;
	}

	public void setRelativePixelDensity (double value)
	{
		cellPixelDensityR = value;
	}

	public void setRelativeAreaMean (double value)
	{
	    cellAreaMeanR = value;
	}
	
	//----------------------------------------------------------------
	//---- HIGH LEVEL GET/SET METHODS
	//----------------------------------------------------------------

	/**
	 * Returns an array, which contains [cell count, cell length min]
	 */
	public double[] getFeatureVectorMin ()
	{
		return new double[] {cellCount, cellLengthMean};
	}

}
