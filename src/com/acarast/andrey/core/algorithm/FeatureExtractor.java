package com.acarast.andrey.core.algorithm;

import java.awt.Polygon;

import com.acarast.andrey.controller.SettingsController;
import com.acarast.andrey.core.device.FeatureVector;
import com.acarast.andrey.data.DataImageChannel;
import com.acarast.andrey.data.DataTableElement;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.imgproc.MathProcessing;

/**
 * Class for extracting various features
 * @author Andrey G
 */
public class FeatureExtractor
{
	private static final int DEFAULT_HISTOGRAM_MULTIPLIER = 4;

	//----------------------------------------------------------------

	/**
	 * Extracts all features from the specified image data.
	 * @param inputImage
	 * @throws ExceptionMessage 
	 */
	public static void extractFromData (DataTableElement inputData) throws ExceptionMessage
	{
		int channelCount = inputData.getChannelCount();
		
		//---- Calculate absolute features for each channel.
		for (int i = 0; i < channelCount; i++)
		{
			/*!*/DebugLogger.logMessage("Extracting absolute feature for the channel: " + i, LOG_MESSAGE_TYPE.INFO);
			
			extractAbsoluteFeatures(inputData.getDataImage().getChannel(i), inputData.getDataDevice().getChannel(i).getFeatureVector());
		}

		//---- Calculate features in relation to the control sample if the control sample was set.
		//---- Before calculation of the relative features, absolute features has to be calculated at first.
		int indexControlSample = inputData.getDataDevice().getControlChannelIndex();

		if (indexControlSample >= 0 && indexControlSample < channelCount)
		{
			for (int i = 0; i < channelCount; i++)
			{
				/*!*/DebugLogger.logMessage("Extracting relative features for the channel: " + i, LOG_MESSAGE_TYPE.INFO);
				
				extractRelativeFeatures(inputData.getDataDevice().getChannel(i).getFeatureVector(), inputData.getDataDevice().getChannel(indexControlSample).getFeatureVector());
				extractHistogram(inputData.getDataImage().getChannel(i), inputData.getDataDevice().getChannel(i).getFeatureVector(), inputData.getDataDevice().getChannel(indexControlSample).getFeatureVector());
			}
		}
	}

	//----------------------------------------------------------------

	/**
	 * Calculates absolute features from the image data and stores them in the feature vector, associated with the device.
	 * @param dataImage
	 * @param dataDevice
	 * @throws ExceptionMessage 
	 */
	private static void extractAbsoluteFeatures(DataImageChannel dataImage, FeatureVector vectorSample) throws ExceptionMessage
	{
		//---- Cell count
		int featureCellCount = extractAbsoluteFeatureCellCount(dataImage);
		vectorSample.setAbsoluteCellCount(featureCellCount);
		
		/*!*/DebugLogger.logMessage("(aFeature) Cell count: " + featureCellCount, LOG_MESSAGE_TYPE.INFO);

		//---- Cell length mean/average
		double featureCellLengthMean = 0;
		if (featureCellCount != 0) { featureCellLengthMean = extractAbsoluteFeatureLengthMean(dataImage); }
		vectorSample.setAbsoluteCellLengthMean(featureCellLengthMean);
		
		/*!*/DebugLogger.logMessage("(aFeature) Length mean: " + featureCellLengthMean, LOG_MESSAGE_TYPE.INFO);

		//---- Cell length min
		int featureCellLengthMin = 0;
		if (featureCellCount != 0) { featureCellLengthMin = extractAbsoluteFeatureLengthMin(dataImage); }
		vectorSample.setAbsoluteCellLengthMin(featureCellLengthMin);
		
		/*!*/DebugLogger.logMessage("(aFeature) Length min: " + featureCellLengthMin, LOG_MESSAGE_TYPE.INFO);

		//---- Cell length max
		int featureCellLengthMax = 0;
		if (featureCellCount != 0) { featureCellLengthMax = extractAbsoluteFeatureLengthMax(dataImage); }
		vectorSample.setAbsoluteCellLengthMax(featureCellLengthMax);

		/*!*/DebugLogger.logMessage("(aFeature) Length max: " + featureCellLengthMax, LOG_MESSAGE_TYPE.INFO);
		
		//---- Cell area mean
		double featureCellAreaMean = 0;
		if (featureCellCount != 0) { featureCellAreaMean = extractAbsoluteFeatureAreaMean(dataImage); }
		vectorSample.setAbsoluteCellAreaMean(featureCellAreaMean);
		
		/*!*/DebugLogger.logMessage("(aFeature) Area mean: " + featureCellAreaMean, LOG_MESSAGE_TYPE.INFO);

		//---- Cell area min
		int featureCellAreaMin = 0;
		if (featureCellCount != 0) { featureCellAreaMin = extractAbsoluteFeatureAreaMin(dataImage); }
		vectorSample.setAbsoluteCellAreaMin(featureCellAreaMin);
		
		/*!*/DebugLogger.logMessage("(aFeature) Area min: " + featureCellAreaMin, LOG_MESSAGE_TYPE.INFO);

		//---- Cell are max
		int featureCellAreaMax = 0;
		if (featureCellCount != 0) { featureCellAreaMax = extractAbsoluteFeatureAreaMax(dataImage); }
		vectorSample.setAbsoluteCellAreaMax(featureCellAreaMax);
		
		/*!*/DebugLogger.logMessage("(aFeature) Area max: " + featureCellAreaMax, LOG_MESSAGE_TYPE.INFO);

		//---- Cell pixel density
		double featurePixelDensity = 0;
		if (featureCellCount != 0) { featurePixelDensity = extractAbsoluteFeaturePixelDensity(dataImage); }
		vectorSample.setAbsolutePixelDensity(featurePixelDensity);
		
		/*!*/DebugLogger.logMessage("(aFeature) Pixel density: " + featurePixelDensity, LOG_MESSAGE_TYPE.INFO);
		
		//---- Length interval
		int[] interval = {0, 0};
		if (featureCellCount != 0) { interval = extractAbsoluteFeatureCellLengthInterval(dataImage); }
		vectorSample.setAbsoluteCellLengthIntervalMin(Math.min(interval[0], interval[1]));
		vectorSample.setAbsoluteCellLengthIntervalMax(Math.max(interval[0], interval[1]));
	}

	/**
	 * Calculates features, in relation to the control sample. 
	 * @param vectorSample
	 * @param vectorControl
	 */

	private static void extractRelativeFeatures(FeatureVector vectorSample, FeatureVector vectorControl)
	{
		//---- Cell count
		vectorSample.setRelativeCellCount(extractRelativeFeatureCellCount(vectorSample, vectorControl));

		//---- Cell length
		vectorSample.setRelativeCellLengthMean(extractRelativeFeatureCellLength(vectorSample, vectorControl));

		//---- Cell pixel density
		vectorSample.setRelativePixelDensity(extractRelativeFeaturePixelDensity(vectorSample, vectorControl));
		
		//---- Cell mean area
		vectorSample.setRelativeAreaMean(extractRelativeFeatureAreaMean(vectorSample, vectorControl));
	}

	private static void extractHistogram (DataImageChannel dataImage, FeatureVector vectorSample, FeatureVector vectorControl) throws ExceptionMessage
	{
		int histogramOffset = extractRelativeFrequencyHistogramOffset(vectorSample, vectorControl);
		
		/*!*/DebugLogger.logMessage("(hFeature) Histogram offset: " + histogramOffset, LOG_MESSAGE_TYPE.INFO);
		
		double[] histogram = extractRelativeFrequencyHistogram(dataImage, vectorSample, vectorControl, histogramOffset);
		
		vectorSample.setRelativeFrequencyHistogram(histogram, histogramOffset);
	}

	//----------------------------------------------------------------
	//---- Methods for extracting absolute features
	//----------------------------------------------------------------

	private static int extractAbsoluteFeatureCellCount (DataImageChannel dataImage)
	{
		return dataImage.getCellCollection().getCellCount();
	}

	private static double extractAbsoluteFeatureLengthMean (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		double sumLength = 0;

		for (int i = 0; i < cellCount; i++)
		{
			sumLength += dataImage.getCellCollection().getCell(i).getSizeThinned();
		}

		if (cellCount != 0)
		{
			return sumLength / cellCount;
		}

		return 0;
	}

	private static int extractAbsoluteFeatureLengthMin (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		int lengthMin = Integer.MAX_VALUE;

		for (int i = 0; i < cellCount; i++)
		{
			int lengthCurrent = dataImage.getCellCollection().getCell(i).getSizeThinned();

			if (lengthCurrent < lengthMin)
			{
				lengthMin = lengthCurrent;
			}
		}

		return lengthMin;

	}

	private static int extractAbsoluteFeatureLengthMax (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		int lengthMax = 0;

		for (int i = 0; i < cellCount; i++)
		{
			int lengthCurrent = dataImage.getCellCollection().getCell(i).getSizeThinned();

			if (lengthCurrent > lengthMax)
			{
				lengthMax = lengthCurrent;
			}
		}

		return lengthMax;
	}

	private static double extractAbsoluteFeatureAreaMean (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		double sumArea = 0;

		for (int i = 0; i < cellCount; i++)
		{
			//---- Grab all the pixels. Remember Length != amount of pixels, amount of pixels is area
			//---- if thicknning was performed

			sumArea += dataImage.getCellCollection().getCell(i).getPixelCount();
		}

		if (cellCount != 0) { return sumArea / cellCount; }
		else { return 0; }
	}

	private static int extractAbsoluteFeatureAreaMin (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		int areaMin = Integer.MAX_VALUE;

		for (int i = 0; i < cellCount; i++)
		{
			int areaCurrent = dataImage.getCellCollection().getCell(i).getPixelCount();

			if (areaCurrent < areaMin)
			{
				areaMin = areaCurrent;
			}
		}

		return areaMin;
	}

	private static int extractAbsoluteFeatureAreaMax (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();
		int areaMax = 0;

		for (int i = 0; i < cellCount; i++)
		{
			int areaCurrent = dataImage.getCellCollection().getCell(i).getPixelCount();

			if (areaCurrent > areaMax)
			{
				areaMax = areaCurrent;
			}
		}

		return areaMax;
	}

	private static double extractAbsoluteFeaturePixelDensity (DataImageChannel dataImage) throws ExceptionMessage
	{
		int cellCount = dataImage.getCellCollection().getCellCount();

		//---- First, calculate the are of the sample
		Polygon channelPolygon = dataImage.getChannelLocation();

		double channelArea = MathProcessing.calculatePolygonArea(channelPolygon);

		double cellArea = 0;

		for (int i = 0; i < cellCount; i++)
		{
			cellArea += dataImage.getCellCollection().getCell(i).getPixelCount();
		}

		if (channelArea != 0)
		{
			return cellArea / channelArea;
		}

		return 0;
	}

	private static int[] extractAbsoluteFeatureCellLengthInterval (DataImageChannel dataImage) throws ExceptionMessage
	{
		double targetObservationsMin = SettingsController.CURRENT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL;

		//---- Get histogram
		int lengthMin = extractAbsoluteFeatureLengthMin(dataImage);
		int lengthMax = extractAbsoluteFeatureLengthMax(dataImage);
		int cellCount = extractAbsoluteFeatureCellCount(dataImage);

		double[] histogramFrequency = new double[lengthMax - lengthMin + 1];

		for (int i = 0; i < histogramFrequency.length; i++) { histogramFrequency[i] = 0; }

		for (int i = 0; i < cellCount ; i++)
		{
			int currentLength = dataImage.getCellCollection().getCell(i).getSizeThinned();

			histogramFrequency[currentLength - lengthMin] += 1.0 / cellCount;
		}

		//---- Analyzing histogram 
		int intervalMin = 0;
		int intervalMax = 0;

		for (int i = 0; i < histogramFrequency.length; i++)
		{
			double frequencyRight = 1.0 - histogramFrequency[i];

			//---- Check if exceed the limit of interval and stop if yes
			if (frequencyRight < 0.5 + targetObservationsMin / 2) 
			{ 
				intervalMin = lengthMin + i; 

				//---- Force stop loop
				i = histogramFrequency.length + 1;
			}
		}

		for (int i = histogramFrequency.length - 1; i >= 0; i--)
		{
			double frequencyLeft = 1.0 - histogramFrequency[i];

			//---- Check if exceed the limit of interval and stop if yes
			if (frequencyLeft < 0.5 + targetObservationsMin / 2) 
			{ 
				intervalMax = lengthMin + i; 

				//---- Force stop loop
				i = -1;
			}
		}

		return new int[]{intervalMin, intervalMax};
	}

	//----------------------------------------------------------------
	//---- Methods for extracting relative features
	//----------------------------------------------------------------

	/**
	 * Calculates number of bacteria in the current sample in relation to the control sample. count_R = count_S / count_C;
	 * If the number of cells in the control sample equals zero, the method returns the absolute value count_S.
	 * @param vectorSample
	 * @param vectorControl
	 * @return
	 */
	private static double extractRelativeFeatureCellCount (FeatureVector vectorSample, FeatureVector vectorControl)
	{
		double cellCountSample = vectorSample.getAbsoluteCellCount();
		double cellCountControl = vectorControl.getAbsoluteCellCount();

		/*!*/DebugLogger.logMessage("(rFeature) Cell count: " + cellCountSample + "/" + cellCountControl, LOG_MESSAGE_TYPE.INFO);
		
		if (cellCountControl != 0)
		{
			return cellCountSample / cellCountControl;
		}
		else
		{
			return cellCountSample;
		}
	}

	/**
	 * Calculates average length of the bacteria cells in relation to the control sample. length_R = length_S / lengthC.
	 * If the length of cells in the control sample equals zero, the method return the absolute value length-S.
	 * @param vectorSample
	 * @param vectorControl
	 * @return
	 */
	private static double extractRelativeFeatureCellLength (FeatureVector vectorSample, FeatureVector vectorControl)
	{
		double cellLengthSample = vectorSample.getAbsoluteCellLengthMean();
		double cellLengthControl = vectorControl.getAbsoluteCellLengthMean();

		/*!*/DebugLogger.logMessage("(rFeature) Cell length: " + cellLengthSample + "/" + cellLengthControl, LOG_MESSAGE_TYPE.INFO);
	
		if (cellLengthControl != 0)
		{
			return cellLengthSample / cellLengthControl;
		}
		else
		{
			return cellLengthSample;
		}
	}

	/**
	 * Calculates pixel density of the current channel in relation to the control sample. pd_R = pd_S / pd_C.
	 * If the pixel density of the control sample is zero, return the absolute value pd_r.
	 * @param vectorSample
	 * @param vectorControl
	 * @return
	 */
	private static double extractRelativeFeaturePixelDensity (FeatureVector vectorSample, FeatureVector vectorControl)
	{
		double pixelDensitySample = vectorSample.getAbsolutePixelDensity();
		double pixelDensityControl = vectorControl.getAbsolutePixelDensity();

		/*!*/DebugLogger.logMessage("(rFeature) Cell length: " + pixelDensitySample + "/" + pixelDensityControl, LOG_MESSAGE_TYPE.INFO);
		
		if (pixelDensityControl != 0)
		{
			return pixelDensitySample / pixelDensityControl;
		}
		else
		{
			return pixelDensitySample;
		}
	}

	/**
	 * Calculates mean area of the current channel in relation to the control sample. A_R = am_S / am_C.
	 * @param vectorSample
	 * @param vectorControl
	 * @return
	 */
	private static double extractRelativeFeatureAreaMean (FeatureVector vectorSample, FeatureVector vectorControl)
	{
	    double areaMeanSample = vectorSample.getAbsoluteCellAreaMean();
	    double areaMeanControl = vectorControl.getAbsoluteCellAreaMean();
	    
	    /*!*/DebugLogger.logMessage("(rFeature) Area mean: " + areaMeanSample + "/" + areaMeanControl, LOG_MESSAGE_TYPE.INFO);
	    
	    if (areaMeanControl != 0)
	    {
		return areaMeanSample /areaMeanControl;
	    }
	    else
	    {
		return areaMeanSample;
	    }
	}
	
	private static int extractRelativeFrequencyHistogramOffset (FeatureVector vectorSample, FeatureVector vectorControl)
	{
		int controlIntervalMin = vectorControl.getAbsoluteCellLengthIntervalMin();
		int controlIntervalMax = vectorControl.getAbsoluteCellLengthIntervalMax();

		/*!*/DebugLogger.logMessage("(hFeature) Interval: [" + controlIntervalMin + "," + controlIntervalMax + "]", LOG_MESSAGE_TYPE.INFO);

		if (controlIntervalMax - controlIntervalMin <= SettingsController.CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS) { return 1; }
		else 
		{
			return (int) Math.round((double)(controlIntervalMax - controlIntervalMin) /  SettingsController.CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS);
		}

	}

	private static double[] extractRelativeFrequencyHistogram (DataImageChannel dataImage, FeatureVector vectorSample, FeatureVector vectorControl, int offset) throws ExceptionMessage
	{	
		int controlIntervalMin = vectorControl.getAbsoluteCellLengthIntervalMin();

		int outputHistogramSize = DEFAULT_HISTOGRAM_MULTIPLIER * SettingsController.CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS;

		double[] outputHistogram = new double[outputHistogramSize];

		for (int i = 0; i < outputHistogramSize; i++)
		{
			outputHistogram[i] = 0;
		}

		int cellCount = vectorSample.getAbsoluteCellCount();

		//---- If no cells in this sample, then return zero histogram
		if (cellCount == 0) { return outputHistogram; }

		for (int i = 0; i < cellCount ; i++)
		{
			int currentCellLength = dataImage.getCellCollection().getCell(i).getSizeThinned();

			int histogramIndex = (int)Math.floor((double)(currentCellLength - controlIntervalMin) / offset);

			if (histogramIndex < 0) { histogramIndex = 0; }
			if (histogramIndex >= outputHistogramSize) { histogramIndex = outputHistogramSize - 1;}

			for (int k = histogramIndex; k < outputHistogram.length; k++)
			{
				outputHistogram[k] += 1.0;
			}
		}
		
		return outputHistogram;
	}


}
