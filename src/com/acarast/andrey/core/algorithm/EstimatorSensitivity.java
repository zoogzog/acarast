package com.acarast.andrey.core.algorithm;

import java.util.Arrays;

import com.acarast.andrey.core.algorithm.EnumTypes.BacteriaType;
import com.acarast.andrey.core.algorithm.EnumTypes.DrugType;
import com.acarast.andrey.core.device.MicrofluidicChannel.Sensitivity;
import com.acarast.andrey.core.task.EstimatorSettings.ESTIMATOR_TIMESTAMP;
import com.acarast.andrey.core.task.TaskSettings;
import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;

/**
 * Sensitivity estimation with SVM or empirical criteria.
 * @author Andrey G
 */
public class EstimatorSensitivity
{
	public static enum EST_ALGORITHM {EMPIRIC, SVM};

	private static final String DEFAULT_MODEL_FILE_PATH = "./data/svm-model.txt";

	public static void estimateSensitivity (DataTable table, int index, TaskSettings settings) throws ExceptionMessage
	{
		//---- Try to read settings from the name of the input file
		//---- Updates settings if successfull 
		if (settings.getIsEstimatorUseFileName()) { scanSettingsFromFileName(table.getElement(index).getDataFile().getFileName(), settings); }


		switch (settings.getEstimatorAlgorithm())
		{
		case SVM: estimateSVM(table, index, settings); break;
		case EMPIRIC: estimateEMPIRIC(table, index, settings); break;
		}
	}

	//----------------------------------------------------------------

	private static void estimateSVM (DataTable table, int index, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("(ES) Loading the svm model table", LOG_MESSAGE_TYPE.INFO);
		
		EstimatorTable svmTable = new EstimatorTable(DEFAULT_MODEL_FILE_PATH);

		//TODO
		String drugName = EnumTypes.DrugTypeName[Arrays.asList(EnumTypes.DrugTypeValue).indexOf(settings.getEstimatorDrugType())];
		
		/*!*/DebugLogger.logMessage("Loading svm weights for drug " + drugName, LOG_MESSAGE_TYPE.INFO);
		
		//---- Get hyperplane weights
		double[] weight = svmTable.getWeights(drugName);
		double thresh = svmTable.getThreshold(drugName);
		
		/*!*/DebugLogger.logMessage("(ES) Estimating data with the SVM for file: " + index, LOG_MESSAGE_TYPE.INFO);

		int samplesCount = table.getElement(index).getChannelCount();

		for (int i = 0; i < samplesCount; i++)
		{
			double value = 0;
			
			double count = table.getElement(index).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellCount();
			double length = table.getElement(index).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellLengthMean();
			
			value = count * weight[0] + length * weight[1] + thresh;
			value *= weight[weight.length - 1];
			
			/*!*/DebugLogger.logMessage("(ES) Estimated SVM value for channel " + i + " [c:" + count + " l:" + length + "]=" + value , LOG_MESSAGE_TYPE.INFO);
			
			if (value <= 0) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT); } 
			else { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }

		}
	}

	//TODO change to template mode, instead of direct
	private static void estimateEMPIRIC (DataTable table, int index, TaskSettings settings) throws ExceptionMessage
	{
		/*!*/DebugLogger.logMessage("(ES) Estimating sensitivity with the empirical model", LOG_MESSAGE_TYPE.INFO);
		
		int samplesCount = table.getElement(index).getChannelCount();

		for (int i = 0; i < samplesCount; i++)
		{
			double count = table.getElement(index).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellCount();
			double length = table.getElement(index).getDataDevice().getChannel(i).getFeatureVector().getRelativeCellLengthMean();

			switch (settings.getEstimatorDrugType())
			{
			case AMIKACIN: 
				if (count <= 0.7) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }
				else { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT); }
				break;

			case CIPROFLOXACIN: 
				if (count <= 0.7 || (count <= 0.75 && length >= 1.25)) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }
				else { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT); }
				break;

			case CEFTAZIDIME:
				if (count <= 0.7 || length >= 1.6) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }
				else { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT); }
				break;

			case MEROPENEM: 
				if (length < 1.2) {table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT);}
				if (count <= 0.7 || length >= 1.6) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }
				else {table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT);}
				break;

			case PIPERACILLIN: 
				if (length < 1.2) {table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT);}
				if (count <= 0.6 || length >= 2 || (count <= 0.7 && length >= 1.6)) { table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.SENSITIVE); }
				else {table.getElement(index).getDataDevice().getChannel(i).setSensitivity(Sensitivity.RESISTANT);}
				break;
			}

		}
	}

	//----------------------------------------------------------------

	//---- The following format of the file is supported $NAME-$BTYPE-$DRUGTYPE-$HOUR.jpg
	//---- $NAME - any combination of letters, spaces
	//---- $BTYPE - 'AR' (Aeruginosa)
	//---- $DRUGTYPE - 'A','B','C','D','E'
	//---- $HOUR - 2,3

	private static void scanSettingsFromFileName (String fileName, TaskSettings settings)
	{
		/*!*/DebugLogger.logMessage("(ES) Extracting estimator settings from the file name", LOG_MESSAGE_TYPE.INFO);
		
		//---- Remove extension
		String name = fileName.substring(0, fileName.indexOf("."));
		
		/*!*/DebugLogger.logMessage("(ES) File name: " + name, LOG_MESSAGE_TYPE.INFO);
		
		String[] splitData = name.split("-");

		//---- If the format of the file name is not supported
		if (splitData.length < 4) { return; }

		String stringHour = splitData[splitData.length - 1];
		String stringDrugType = splitData[splitData.length - 2];
		String stringBacteriaType = splitData[splitData.length - 3];
		//String stringName = splitData[splitData.length - 4];

		/*!*/DebugLogger.logMessage("(ES) Splitted into: " + stringHour + " " + stringDrugType + " " + stringBacteriaType, LOG_MESSAGE_TYPE.INFO);
		
		//---- Check hour
		if (stringHour.equals("3")){ settings.setEstimatorTimestamp(ESTIMATOR_TIMESTAMP.HOUR_3);}
		else if (stringHour.equals("2")){ settings.setEstimatorTimestamp(ESTIMATOR_TIMESTAMP.HOUR_2);}
		else { return; }

		//---TODO
		//---- Check drug type
		if (stringDrugType.equals("A")) {settings.setEstimatorDrugType(DrugType.AMIKACIN);}
		else if (stringDrugType.equals("B")) { settings.setEstimatorDrugType(DrugType.CEFTAZIDIME);}
		else if (stringDrugType.equals("C")) { settings.setEstimatorDrugType(DrugType.CIPROFLOXACIN);}
		else if (stringDrugType.equals("D")) { settings.setEstimatorDrugType(DrugType.MEROPENEM);}
		else if (stringDrugType.equals("E")) { settings.setEstimatorDrugType(DrugType.PIPERACILLIN);}
		else { return; }

		//---- Check bacteria type
		if (stringBacteriaType.equals("AR")) { settings.setEstimatorBacteriaType(BacteriaType.PAERUGINOSA);}
		else { return; }
		
		/*!*/DebugLogger.logMessage("-!- Estimator hour: " + settings.getEstimatorTimestamp(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator drug type: " + settings.getEstimatorDrugType(), LOG_MESSAGE_TYPE.INFO);
		/*!*/DebugLogger.logMessage("-!- Estimator bacteria type: " + settings.getEstimatorBacteriaType(), LOG_MESSAGE_TYPE.INFO);
	}

}
