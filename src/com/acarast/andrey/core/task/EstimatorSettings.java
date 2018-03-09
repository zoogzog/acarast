package com.acarast.andrey.core.task;

import com.acarast.andrey.core.algorithm.EnumTypes;

/**
 * Class for determining susceptibility from the extracted features/
 * @author Andrey G
 */
public class EstimatorSettings 
{
	public static enum ESTIMATOR_ALG {EMPIRIC, SVM};
	public static enum ESTIMATOR_TIMESTAMP {HOUR_2, HOUR_3};
	
	//---- Specify the type of estimating algorithm
	private ESTIMATOR_ALG algorithm;

	//---- Specify if the app will try to read settings for 
	//---- the estimator from the name of the file
	private boolean isEstimatorUseFileName;
	
	//---- Timestamp when the image of the RaST-TAS was taken
	private ESTIMATOR_TIMESTAMP timestamp;
	
	//---- Which bacteria type was used in the experiment
	private EnumTypes.BacteriaType bacteriaType;
	
	//---- Which drug was applied to the bacteria in the experiment
	private EnumTypes.DrugType drugType;
	
	//----------------------------------------------------------------
	
	public EstimatorSettings() 
	{
		isEstimatorUseFileName = true;
		
		algorithm = ESTIMATOR_ALG.SVM;
		
		timestamp = ESTIMATOR_TIMESTAMP.HOUR_3;
		
		bacteriaType = EnumTypes.BacteriaType.PAERUGINOSA;
		
		drugType = EnumTypes.DrugType.AMIKACIN;
	}
	
	//----------------------------------------------------------------
	//---- GET METHODS
	//----------------------------------------------------------------
	
	public ESTIMATOR_ALG getEstimatorAlgorithm ()
	{
		return algorithm;
	}
	
	public boolean getIsEstimatorUseFileName ()
	{
		return isEstimatorUseFileName;
	}
	
	public ESTIMATOR_TIMESTAMP getEstimatorTimestamp ()
	{
		return timestamp;
	}
	
	public EnumTypes.BacteriaType getEstimatorBacteriaType ()
	{
		return bacteriaType;
	}
	
	public EnumTypes.DrugType getEstimatorDrugType ()
	{
		return drugType;
	}
	
	//----------------------------------------------------------------
	//---- SET METHODS
	//----------------------------------------------------------------
	
	public void setEstimatorAlgorithm (ESTIMATOR_ALG value)
	{
		algorithm = value;
	}

	public void setIsEstimatorUseFileName (boolean value)
	{
		isEstimatorUseFileName = value;
	}
	
	public void setEstimatorTimestamp (ESTIMATOR_TIMESTAMP value)
	{
		timestamp = value;
	}
	
	public void setEstimatorBacteriaType (EnumTypes.BacteriaType value)
	{
		bacteriaType = value;
	}
	
	public void setEstimatorDrugType (EnumTypes.DrugType value)
	{
		drugType = value;
	}
	
}
