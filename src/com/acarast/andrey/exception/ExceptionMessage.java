package com.acarast.andrey.exception;

import java.util.HashMap;

/**
 * Custom exception messages.
 * @author Andrey G
 */

public class ExceptionMessage extends Exception
{
	//---- AUTO GENERATED
	private static final long serialVersionUID = 1L;

	//---- Exception codes:
	//---- EXCODE_INDEXOFRANGE: index is out of range (for vectors and arrays). fatal error
	//---- EXCODE_DATACONTROLLER_INIT: data controller is not initialized. fatal error
	//---- EXCODE_HANDLER_INIT: handler for the main form was not initialized. fatal error
	//---- EXCODE_TABLEUNCOSISTENCY: number of channels in table's device and image are not the same
	//---- EXCODE_FILENOTFOUND: input image file was not found
	//---- EXCODE_FILEWRITEFAIL: fail to wrtie output data into file

	//---- EXCODE_SETTINGSERROR: processing settings are not correct. not a fatal error.
	//---- EXCODE_SETTINGSMANUALMODE: not enough samples has been added for the manual mode processing.
	//---- EXCODE_SETTINGSSAMPLESCOUNT: something wrong with the samples count parameter.
	//---- EXCODE_SETTINGSINDEXCONTROL: something wrong with the index of the control sample.
	//---- EXCODE_SAMPLENOTDETECTED: the correct number of samples has not been detected. not a fatal error
	public static enum  EXCEPTION_CODE 
	{
		EXCODE_INDEXOFRANGE, 
		EXCODE_DATACONTROLLER_INIT, 
		EXCODE_HANDLER_INIT, 
		EXCODE_TABLEUNCOSISTENCY,
		EXCODE_FILENOTFOUND,
		EXCODE_FILEWRITEFAIL,
		EXCODE_MATRIXISNULL,
		EXCODE_CELLDETECTION,


		EXCODE_SETTINGSERROR,
		EXCODE_SETTINGSMANUALMODE,
		EXCODE_SETTINGSSAMPLESCOUNT,
		EXCODE_SETTINGSINDEXCONTROL,
		EXCODE_SAMPLENOTDETECTED,
	};

	@SuppressWarnings("serial")
	private static final HashMap <EXCEPTION_CODE, String> EXCEPTION_INFO = new HashMap <EXCEPTION_CODE, String>()
	{{
		put(EXCEPTION_CODE.EXCODE_INDEXOFRANGE, "Index is out of range");
		put(EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT, "Data controler was not initialised");
		put(EXCEPTION_CODE.EXCODE_HANDLER_INIT, "Handler was not initialised");
		put(EXCEPTION_CODE.EXCODE_TABLEUNCOSISTENCY,"Data table congruency violation");
		put(EXCEPTION_CODE.EXCODE_FILENOTFOUND,"Input file was not found");
		put(EXCEPTION_CODE.EXCODE_FILEWRITEFAIL,"Fail to perform writing to file");
		put(EXCEPTION_CODE.EXCODE_MATRIXISNULL,"Matrix has a null pointer");
		put(EXCEPTION_CODE.EXCODE_CELLDETECTION, "Fatal error during cell detection");

		put(EXCEPTION_CODE.EXCODE_SETTINGSERROR, "Processing parameters are not correct");
		put(EXCEPTION_CODE.EXCODE_SETTINGSMANUALMODE, "Parameter 'Manual mode' is not correct");
		put(EXCEPTION_CODE.EXCODE_SETTINGSSAMPLESCOUNT, "Parameter 'Samples count' is not correct");
		put(EXCEPTION_CODE.EXCODE_SETTINGSINDEXCONTROL, "Parameter 'Control index' is not correct");
		put(EXCEPTION_CODE.EXCODE_SAMPLENOTDETECTED, "Can't detect specified number of channels");
	}};


	private EXCEPTION_CODE code;

	//----------------------------------------------------------------

	public ExceptionMessage (EXCEPTION_CODE i)
	{
		code = i;
	}

	public EXCEPTION_CODE getCode ()
	{
		return code;
	}

	public String getInfo ()
	{
		return EXCEPTION_INFO.get(code);
	}
}
