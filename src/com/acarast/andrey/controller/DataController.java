package com.acarast.andrey.controller;

import java.io.File;
import java.util.Arrays;

import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.exception.ExceptionMessage;

/**
 * Static class that provides methods for all data manipulations. 
 * @author Andrey G
 */

public class DataController
{
	//---- Input file types which are allowed
	private final static String[] INPUT_FILE_TYPE = {"bmp", "jpg", "BMP", "JPG", "jpeg", "JPEG"};

	private static DataTable table = null;

	/**
	 * Initialize the data controller, set up the link to the data table.
	 */
	public static void init (DataTable tableLink)
	{
		table = tableLink;
	}

	//----------------------------------------------------------------
	/**
	 * Resets the data in the table, clears the table.
	 * @param table
	 * @throws ExceptionMessage 
	 */
	public static void scenarioNewProject () throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		table.removeAllElements();
	}

	/**
	 * Add an input file, specified by its path, to the project table.
	 * @param table
	 * @param filePath
	 * @throws ExceptionMessage 
	 */
	public static void scenarioAddFile (String filePath) throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		String fileType = filePath.substring(filePath.lastIndexOf('.') + 1); 

		boolean isTypeOK = Arrays.asList(INPUT_FILE_TYPE).contains(fileType);

		if (isTypeOK)
		{
			String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);

			//---- Add new element
			table.addElement();

			//---- Set file path and file name
			table.getElementLast().getDataFile().setFileName(fileName);
			table.getElementLast().getDataFile().setFilePath(filePath);
		}
	}

	/**
	 * Add all *.bmp and *.jpg files located in the input folder to the project table.
	 * @param table
	 * @param fileList all the files in the folder
	 * @throws ExceptionMessage 
	 */
	public static void scenarioOpenFolder (String[] fileList) throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		for (int i = 0; i < fileList.length; i++)
		{
			scenarioAddFile(fileList[i]);
		}
	}

	/**
	 * Remove a file from the table, the file is specified by the index
	 * @throws ExceptionMessage 
	 */
	public static void scenarioRemoveFile (int fileIndex) throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		table.removeElement(fileIndex);
	}

	/**
	 * Import time lapse images from a folder structure Rootdir->test$ID->processed->time-$tsd3
	 */
	public static void scenarioTimelapseImport (String filePath) throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		File[] subDirectoryList = new File(filePath).listFiles(File::isDirectory);

		for (int i = 0; i < subDirectoryList.length; i++)
		{
			String subDirectoryName = subDirectoryList[i].getName();


			if (subDirectoryName.matches("test\\d{2}"))
			{
				File directoryProcessed = new File(subDirectoryList[i].getPath() + "/processed");

				if (directoryProcessed.exists())
				{
					if (directoryProcessed.isDirectory())
					{
						File[] fileList = directoryProcessed.listFiles();
						
						for (int j = 0; j < fileList.length; j++)
						{
							String fileName = fileList[j].getName();
							
							String fileType = fileName.substring(fileName.lastIndexOf('.') + 1); 

							boolean isTypeOK = Arrays.asList(INPUT_FILE_TYPE).contains(fileType);
							
							if (isTypeOK)
							{
								String idTest = subDirectoryName.substring(4);
								String idImage = fileName.substring(5);
								
								String tableFileName = idTest + "-" +idImage;
								String tableFilePath = fileList[j].getPath();
								
								//System.out.println(tableFileName + " <--> " + tableFilePath);
								
								//---- Add new element
								table.addElement();

								//---- Set file path and file name
								table.getElementLast().getDataFile().setFileName(tableFileName);
								table.getElementLast().getDataFile().setFilePath(tableFilePath);
							}
						}
					}
				}
			}
		}

	}

	//----------------------------------------------------------------

	/**
	 * Returns all files in the project as names in an array
	 * @return
	 * @throws ExceptionMessage
	 */
	public static String[] scenarioGetFileNames () throws ExceptionMessage
	{
		if (table == null) { throw new ExceptionMessage(ExceptionMessage.EXCEPTION_CODE.EXCODE_DATACONTROLLER_INIT); }

		String[] output = new String[table.getTableSize()];

		for (int i = 0; i < output.length; i++)
		{
			output[i] = table.getElement(i).getDataFile().getFileName();
		}

		return output;
	}

	//----------------------------------------------------------------


	public static DataTable getTable ()
	{
		return table;
	}
}
