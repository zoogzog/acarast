package com.acarast.andrey.core.task;

import java.awt.Polygon;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.acarast.andrey.controller.SettingsController;
import com.acarast.andrey.controller.TaskProgressController;
import com.acarast.andrey.controller.TaskProgressController.STATUS_ID;
import com.acarast.andrey.core.algorithm.DetectorCellLauncher;
import com.acarast.andrey.core.algorithm.DetectorChannel;
import com.acarast.andrey.core.algorithm.EstimatorSensitivity;
import com.acarast.andrey.core.algorithm.FeatureExtractor;
import com.acarast.andrey.core.imgproc.ImageDraw;
import com.acarast.andrey.core.imgproc.ImageProcessing;
import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;
import com.acarast.andrey.imgproc.ImageProcessingUtils;

/**
 * Launches execution of the image processing algorithm.
 * @author Andrey G
 */
public class Task
{
	/**
	 * Processes a single image and stores data in the data table. If the manual mode is not selected, 
	 * the sample channels are detected automatically, otherwise it is assumed that they have been specified manually.
	 * @param table
	 * @param indexImage
	 * @param isManualMode 
	 * @throws ExceptionMessage 
	 */
	public static void processImageSingle (DataTable table, int indexImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Processing file " + indexImage + "//" + table.getTableSize(), LOG_MESSAGE_TYPE.INFO);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_START);

		String filePath = table.getElement(indexImage).getDataFile().getFilePath();
		String fileName = table.getElement(indexImage).getDataFile().getFileName();

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_LOADFILE, fileName);

		Mat inputImage = Imgcodecs.imread(filePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

		//---- Check if file was successfully loaded
		if (inputImage == null) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILENOTFOUND); }
		if (inputImage.empty()) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILENOTFOUND); }

		try
		{
			//---- Perform processing, depending on the selected sample detection mode
			if (settings.getIsManualMode()) { processSingleImageModeManual(table, indexImage, inputImage, settings); }
			else {  processSingleImageModeAuto(table, indexImage, inputImage, settings); }

		}
		catch (ExceptionMessage e) 
		{
			if (!DebugLogger.IS_SUPRESS_EXCEPTION) { throw e;}
		}
	}

	public static void processImageCollection (DataTable table, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Processing file collection has been initiated", LOG_MESSAGE_TYPE.INFO);


		for (int i = 0; i < table.getTableSize(); i++)
		{
			processImageSingle(table, i, settings);
		}

	}

	//----------------------------------------------------------------

	/**
	 * Processes a single image where all channels has to be automatically detected.
	 * @param table
	 * @param indexImage
	 * @param inputImage
	 * @throws ExceptionMessage 
	 */
	private static void processSingleImageModeAuto (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Processing file in automatic channel detection mode: " + indexImage + "//" + table.getTableSize(), LOG_MESSAGE_TYPE.INFO);

		if (settings.getIndexControlSample() < 0 || settings.getIndexControlSample() > settings.getSamplesCount()) 
		{ throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSERROR); }

		//---- Clear the container for samples, by removin all stored datam set the index of the control sample
		table.getElement(indexImage).removeAllSamples();
		table.getElement(indexImage).getDataDevice().setControlChannelIndex(settings.getIndexControlSample());

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_DETECTCHANNEL);

		executeDetectionChannel(table, indexImage, inputImage, settings);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_DETECTCELL);

		executeDetectionCell(table, indexImage, inputImage, settings);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_FEATUREEXTRACT);

		executeFeatureExtractor(table, indexImage, inputImage, settings);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_ESTIMATE);

		executeEstimatorSensitivity(table, indexImage, inputImage, settings);
	}

	/**
	 * Processes a single image, where all channels were manually specified.
	 * @param table
	 * @param indexImage
	 * @param inputImage
	 * @throws ExceptionMessage 
	 */
	private static void processSingleImageModeManual (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Processing file in manual channel detection mode: " + indexImage + "//" + table.getTableSize(), LOG_MESSAGE_TYPE.INFO);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_DETECTCHANNEL);

		//---- Clear the containers related to calculated data.

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_DETECTCELL);

		executeDetectionCell(table, indexImage, inputImage, settings);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_FEATUREEXTRACT);

		executeFeatureExtractor(table, indexImage, inputImage, settings);

		/*<<>>*/TaskProgressController.progressUpdate(STATUS_ID.ID_ESTIMATE);

		executeEstimatorSensitivity(table, indexImage, inputImage, settings);
	}

	//----------------------------------------------------------------

	private static void executeDetectionChannel (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Locating channels has been initiated", LOG_MESSAGE_TYPE.INFO);

		//---- Get the edge mask. This image is used for detecting sample borders. Also a binary image, where white represents
		//---- detected edges which could be cells or sample borders. 
		Mat maskEdge = ImageProcessing.getEdgeMask(inputImage);

		/*!*/DebugLogger.logMessage("Edge mask obtained", LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.debugSaveMat("debug-edgemask.jpg", maskEdge);

		//---- Detect sample channels
		Polygon[] sampleChannel = DetectorChannel.detectSamples(maskEdge, settings.getSamplesCount(), SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP);

		/*!*/DebugLogger.logMessage("Localization of channels has finished", LOG_MESSAGE_TYPE.INFO);

		//---- Check if correct number of samples has been detected. If not, throw an error message vat exception
		if (sampleChannel == null) {throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SAMPLENOTDETECTED);}
		if (sampleChannel.length != settings.getSamplesCount()) {throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SAMPLENOTDETECTED); }

		//---- Store detected samples in the table
		for (int i = 0; i < sampleChannel.length; i++)
		{
			boolean isControl = false;

			if (i == settings.getIndexControlSample()) { isControl = true; }

			table.getElement(indexImage).addSample(sampleChannel[i], isControl);
		}

		/*!*/DebugLogger.debugSaveMat("debug-locchannels.jpg", ImageDraw.drawImageSampleArea(table.getElement(indexImage)));
	}

	private static void executeDetectionCell (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Cell detection procedure has been initiated", LOG_MESSAGE_TYPE.INFO);

		//---- Cell mask, which also contains channel borders
		Mat maskCellBorder = ImageProcessing.getCellMask(inputImage);

		/*!*/DebugLogger.logMessage("Cell mask has been obtained", LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.debugSaveMat("debug-cellmask.jpg", maskCellBorder);

		//---- Cell mask, which contains only cells
		Mat maskCell =  Mat.zeros(maskCellBorder.rows(), maskCellBorder.cols(), maskCellBorder.type());

		//---- Copy only areas of the detected channels to the detection mask
		for (int i = 0; i < table.getElement(indexImage).getChannelCount(); i++)
		{
			ImageProcessingUtils.copyPolygonArea(maskCellBorder, maskCell, table.getElement(indexImage).getDataImage().getChannel(i).getChannelLocation());
		}

		/*!*/DebugLogger.logMessage("Copying sample channel areas has been finished", LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.debugSaveMat("debug-cellmaskclean.jpg", maskCell);

		DetectorCellLauncher.detectCells(table, indexImage, maskCell, settings);
	}

	private static void executeFeatureExtractor (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("Extracting features has been initiated", LOG_MESSAGE_TYPE.INFO);

		FeatureExtractor.extractFromData(table.getElement(indexImage));
	}

	private static void executeEstimatorSensitivity (DataTable table, int indexImage, Mat inputImage, TaskSettings settings) throws ExceptionMessage
	{
		if (settings.getIsEstimateSensitivity())
		{
			/*!*/DebugLogger.logMessage("Estimating sensitivity has been initiated", LOG_MESSAGE_TYPE.INFO);

			EstimatorSensitivity.estimateSensitivity(table, indexImage, settings);
		}
	}
}
