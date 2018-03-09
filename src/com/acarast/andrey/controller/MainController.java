package com.acarast.andrey.controller;

import java.awt.Rectangle;

import com.acarast.andrey.data.DataTable;
import com.acarast.andrey.debug.DebugLogger;
import com.acarast.andrey.exception.ExceptionHandler;
import com.acarast.andrey.gui.form.FormMain;
import com.acarast.andrey.gui.form.FormMainHandler;
import com.acarast.andrey.gui.form.FormMainMouse;

/**
 * This class handles all the operations with the main data storages. 
 * @author Andrey G
 */

public class MainController
{
	private static MainController controller = null;

	//---- Main GUI form of the application and its controllers (handlers)
	private FormMain windowMain = null;
	private FormMainHandler windowMainHandler = null;
	private FormMainMouse windowMainMouse = null;

	//---- Main data table of the application
	private DataTable table = null;

	//----------------------------------------------------------------

	private MainController ()
	{
		table = new DataTable();

		DataController.init(table);
	}

	public static MainController getInstance ()
	{
		if (controller == null) { controller = new MainController(); }

		return controller;
	}

	//----------------------------------------------------------------

	public void launchMainWindow (Rectangle screenResolution)
	{
		//---- Init handlers, create window, launch.
		if (windowMain == null) 
		{ 
			//DebugLogger.switchDebugModeON();
			//DebugLogger.switchDebugMatOutputON();
			DebugLogger.logMessage("Launching app", DebugLogger.LOG_MESSAGE_TYPE.INFO);
			
			ExceptionHandler.setModeOutput(true, true, true);
			ExceptionHandler.setDataTableLink(table);

			windowMainHandler = new FormMainHandler();
			windowMainMouse = new FormMainMouse();

			windowMain = FormMain.getInstance(screenResolution, windowMainHandler, windowMainMouse); 

			windowMainHandler.init(windowMain);
		}
	}

}
