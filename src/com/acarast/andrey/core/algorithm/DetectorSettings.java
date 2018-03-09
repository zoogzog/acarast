package com.acarast.andrey.core.algorithm;

/**
 * Class to store default settings for the image processing algorithm.
 * @author Andrey G
 */

public class DetectorSettings
{
    //============================================================================================
    //---- IMAGE PROCESSING
    //============================================================================================

    /** Size of the Gaussian kernel, which is used for illumination correction in the process of creating cell mask. 
     * Allowed values: 10-50. */
    public static final int DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE = 20;
    public static final int DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MIN = 5;
    public static final int DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MAX = 50;

    /** Threshold of the canny edge detector, used int the process of creating edge mask for detecting sample borders. 
     * Allowed values: 50-200. */
    public static final int DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD = 100;
    public static final int DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MIN = 50;
    public static final int DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MAX = 200;

    /** Size of the kernel, which is used for dilating the image in the process of creating edge mask for detecting sample borders. 
     * Allowed values: 1-5 */
    public static final int DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE = 3;
    public static final int DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MIN = 1;
    public static final int DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MAX = 5;

    /** Local thresholding, kernel size for selecting neighbors. Used for creating cell mask.*/
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE = 3;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MIN = 1;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MAX = 10;

    /** Local thresholding, condition threshold, all pixels < this value will be 0, >  threshold dest value. */
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION = 100;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MIN = 20;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MAX = 200;

    /** Local thresholding, condition transformation value. */
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE = 255;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MIN = 1;
    public static final int DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MAX = 255;

    public static final int DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD = 80;
    public static final int DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MIN = 10;
    public static final int DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MAX = 200;

    //============================================================================================
    //---- SAMPLE DETECTOR
    //============================================================================================

    /** Hough transform distance resolution of the accumulator in pixels. */
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO = 1;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MIN = 1;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MAX = 5;

    /** Hough transform angle resolution of the accumulator in radians. */
    public static final double DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA = Math.PI / 180;
    public static final double DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA_MIN = Math.PI / 180;
    public static final double DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA_MAX = Math.PI / 180 * 30;

    /** Hough transform thresholding coefficient. */
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD = 200;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MIN = 100;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MAX = 250;

    /** Hough transform Min line length, which is treated as one line. */
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH = 100;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MIN = 10;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MAX = 1000;

    /** Hough transform max gap between lines to be merged into one line. */
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP = 4;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MIN = 1;
    public static final int DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MAX = 1000;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_PATCH_SIZE = 20;	
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_PATCH_SIZE_MIN = 5;
    public static final int DEFULAT_SAMPLE_DETECTOR_BORDERCORRECTION_PATHC_SIZE_MAX = 500;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP = 10;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MIN = 0;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MAX = 100;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH = 30;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MIN = 5;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MAX = 100;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_MINSAMPLEWIDTH = 50;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX = 100;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMIN = 0;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMAX = 255;

    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN = 5;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMIN = 0;
    public static final int DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMAX = 255;

    //============================================================================================
    //---- CELL DETECTOR
    //============================================================================================

    /** Minimal size of the bacteria. All detected objects wich have less pixels then this parameter will be deleted. 
     * Allowed values: 0 - MAXINT */
    public static final int DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE = 1;
    public static final int DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MIN = 0;
    public static final int DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MAX = 500;

    /** Maximal distance between two cells. If two bacteria are closer then the specified distance they will be merged if the
     * condition is satisfied. 
     * Allowed values: 0 - 10*/
    public static final int DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE = 3;
    public static final int DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MIN = 0;
    public static final int DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MAX = 10;

    /** Maximal difference of angles between two bacterias. If the angle between two bacteria is less then this parameter
     * those two bacteria will be merged if other conditions are satisfied. 
     * Allowed values: 0 - 180.*/
    public static final int DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE = 10;
    public static final int DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MIN = 0;
    public static final int DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MAX = 180;

    //============================================================================================
    //---- FEATURE EXTRACTOR
    //============================================================================================

    /** BASELENGTH interval represents interval where X amount of bacteria with ceratin feature are. */
    public static final double DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL = 0.9;
    public static final double DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MIN = 0.5;
    public static final double DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MAX = 0.95;

    public static final int DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS = 5;
    public static final int DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MIN = 1;
    public static final int DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MAX = 10;

    //============================================================================================
    //---- CURRENT SETTINGS: IMAGE PROCESSING
    //============================================================================================

    public static int CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE;
    public static int CURRENT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD = DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD;
    public static int CURRENT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE;
    public static int CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE;
    public static int CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION;
    public static int CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE;
    public static int CURRENT_IMAGE_PROCESSING_GLOBAL_THRESHOLD  = DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD ;

    //============================================================================================
    //---- CURRENT SETTINGS: SAMPLE DETECTOR, HOUGH TRANSFORM
    //============================================================================================

    public static int CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO;
    public static double CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA;
    public static int CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD;
    public static int CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH;
    public static int CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP;

    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_PATCH_SIZE = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_PATCH_SIZE;
    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP;
    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH;
    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_MINSAMPLEWIDTH = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_MINSAMPLEWIDTH;
    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN;
    public static int CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX = DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX;
    //============================================================================================
    //---- CURRENT SETTINGS: CELL DETECTOR
    //============================================================================================

    public static int CURRENT_CELL_DETECTOR_MIN_BACTERIA_SIZE = DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE;
    public static int CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE = DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE;
    public static int CURRENT_CELL_DETECTOR_ANGLE_DIFFERENCE = DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE;

    //============================================================================================
    //---- CURRENT SETTINGS: FEATURE EXTRACTOR
    //============================================================================================

    public static double CURRENT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL = DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL;
    public static int CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS = DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS;

    //============================================================================================

    public static void resetToDefault()
    {
	CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE;
	CURRENT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD = DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD;
	CURRENT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE;
	CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE;
	CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION;
	CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE = DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE;
	CURRENT_IMAGE_PROCESSING_GLOBAL_THRESHOLD  = DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD ;

	CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO;
	CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA;
	CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD;
	CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH;
	CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP = DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP;

	CURRENT_CELL_DETECTOR_MIN_BACTERIA_SIZE = DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE;
	CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE = DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE;
	CURRENT_CELL_DETECTOR_ANGLE_DIFFERENCE = DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE;

	CURRENT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL = DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL;
	CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS = DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS;
    }

    //============================================================================================

    public static void setImageProcessingGaussKernelSize (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MIN && value <= DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE = value;
	}
    }

    public static void setImageProcessingCannyEdgeThreshold (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MIN && value <= DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD = value;
	}
    }

    public static void setImageProcessingDilateKernelSize (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MIN && value <= DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE = value;
	}
    }

    public static void setImageProcessingLocalThresholdKernelSize (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MIN && value <= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE = value;
	}
    }

    public static void setImageProcessingLocalThresholdCondition (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MIN && value <= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION = value;
	}
    }

    public static void setImageProcessingLocalThresholdDestvalue (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MIN && value <= DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE = value;
	}
    }

    public static void setImageProcessingGlobalThreshold (int value)
    {
	if (value >= DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MIN && value <= DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MAX)
	{
	    CURRENT_IMAGE_PROCESSING_GLOBAL_THRESHOLD = value;
	}
    }

    //============================================================================================

    public static void setSampleDetectorHoughTransformRho (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MIN && value <= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO = value;
	}
    }

    public static void setSampleDetectorHoughTransformThetta (double value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA_MIN && value <= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA = value;
	}
    }

    public static void setSampleDetectorHoughTransformThreshold (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MIN && value <= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD = value;
	}
    }

    public static void setSampleDetectorHoughTransformMinLineLength (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MIN && value <= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH = value;
	}
    }

    public static void setSampleDetectorHoughTransformMaxLineGape (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MIN && value <= DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP = value;
	}
    }

    public static void setSampleDetectoBorderCorrectionNarrowSampleStrip (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MIN && value <= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP = value;
	}
    }

    public static void setSampleDetectoBorderCorrectionBorderWidth (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MIN && value <= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MAX)
	{
	    CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH = value;
	}
    }

    public static void setSampleDetectoBorderCorrectionIntensityMin (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMIN && value <= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMAX)
	{
	    CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN = value;
	}
    }

    public static void setSampleDetectoBorderCorrectionIntensityMax (int value)
    {
	if (value >= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMIN && value <= DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMAX)
	{
	    CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX = value;
	}
    }

    //============================================================================================

    public static void setCellDetectorMinBacteriaSize (int value)
    {
	if (value >= DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MIN && value <= DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MAX)
	{
	    CURRENT_CELL_DETECTOR_MIN_BACTERIA_SIZE = value;
	}
    }

    public static void setCellDetectorMaxCellDistance (int value)
    {
	if (value >= DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MIN && value <= DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MAX)
	{
	    CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE = value;
	}

    }

    public static void setCellDetectorAngleDifference (int value)
    {
	if (value >= DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MIN && value <= DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MAX)
	{
	    CURRENT_CELL_DETECTOR_ANGLE_DIFFERENCE = value;
	}
    }

    //============================================================================================

    public static void setFeatureExtractorBaseLengthInterval (double value)
    {
	if (value >= DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MIN && value <= DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MAX)
	{
	    CURRENT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL = value;
	}
    }

    public static void setFeatureExtractorLenghtHistogramSteps (int value)
    {
	if (value >= DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MIN && value <= DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MAX)
	{
	    CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS = value;
	}
    }
}