package com.acarast.andrey.background;

import java.awt.Toolkit;

import javax.swing.SwingWorker;

import com.acarast.andrey.controller.TaskProgressController;
import com.acarast.andrey.core.task.Task;
import com.acarast.andrey.core.task.TaskSettings;
import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.debug.DebugLogger.LOG_MESSAGE_TYPE;
import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;
import com.acarast.andrey.gui.form.FormMainHandler;

/**
 * The launcher for the background tasks. This class launches a separate thread that will
 * will process one or multiple input images.
 * @author Andrey G
 */

public class TaskBackground extends SwingWorker<Void, Void> 
{
	//---- If -1 then process all.
	private int fileToProcess = -1;

	private TaskSettings settings = null;
	private boolean isSettingsOK = false;

	private DataTable table = null;

	private FormMainHandler callbackDestination = null;

	//----------------------------------------------------------------

	public TaskBackground ()
	{
		fileToProcess = -1;

		settings = null;
		isSettingsOK = false;

		table = null;
		callbackDestination = null;
	}


	//----------------------------------------------------------------

	/**
	 * Sets the parameters for the background process. Set index to -1 if you want to process all files in the project.
	 * @param dataTable
	 * @param inputSettings
	 * @param index
	 * @param callback
	 */
	public void setSettings (DataTable dataTable, TaskSettings inputSettings, int index,  FormMainHandler callback)
	{
		settings = inputSettings;
		table = dataTable;

		callbackDestination = callback;

		isSettingsOK = inputSettings.isOK();

		fileToProcess = index;
	}

	//----------------------------------------------------------------

	@Override
	protected Void doInBackground() throws ExceptionMessage
	{
		try
		{
			/*!*/DebugLogger.logMessage("Background task has been launched", LOG_MESSAGE_TYPE.INFO);

			if (!isSettingsOK) { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSERROR); }

			//---- Send signal to the handler, that the execution has been started
			callbackDestination.callbackMessageProcessingStart();

			//---- If the file to process flag set to -1, then process all files
			//---- If not then process only a single file.
			//---- Throw error if the index is not okay
			if (fileToProcess == -1)
			{
				//---- Set up progress controller
				TaskProgressController.progressReset();
				TaskProgressController.setProgressTaskCount(table.getTableSize());
				
				Task.processImageCollection(table, settings);
			}
			else if (fileToProcess >= 0 && fileToProcess < table.getTableSize())
			{
				//---- Set up progress controller
				TaskProgressController.progressReset();
				TaskProgressController.setProgressTaskCount(1);
				
				Task.processImageSingle(table, fileToProcess, settings);
			}
			else { throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_SETTINGSERROR); }

		}
		catch (ExceptionMessage exception)
		{
			TaskProgressController.progressReset();

			callbackDestination.callbackMessageProcessingTerminated(exception);
		}

		return null;
	}

	@Override
	public void done()
	{
		Toolkit.getDefaultToolkit().beep();
		
		//---- Reset the variables 
		
		TaskProgressController.progressReset();
		
		fileToProcess = -1;

		settings = null;
		isSettingsOK = false;

		table = null;
		
		//---- Send signal to the handler, that the execution has been finished
		callbackDestination.callbackMessageProcessingStop();
		
		callbackDestination = null;
	}


}
