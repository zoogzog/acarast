package com.acarast.andrey.controller;

import java.util.HashMap;


import com.acarast.andrey.gui.panel.PanelStatusBar;

/**
 * Controller for displaying processing progress in GUI/console
 * @author Andrey G
 */

public class TaskProgressController
{
	public static enum STATUS_ID 
	{
		ID_START,
		ID_LOADFILE,
		ID_DETECTCHANNEL,
		ID_DETECTCELL,
		ID_FEATUREEXTRACT,
		ID_ESTIMATE
	};

	//---- Hashmap for storing status message 
	@SuppressWarnings("serial")
	private static final HashMap <STATUS_ID, String> STATUS_MESSAGE = new HashMap<STATUS_ID, String>()
	{{
		put(STATUS_ID.ID_START, "Starting processing ");
		put(STATUS_ID.ID_LOADFILE, "Loading file ");
		put(STATUS_ID.ID_DETECTCHANNEL, "Detecting samples ");
		put(STATUS_ID.ID_DETECTCELL, "Detecting cells ");
		put(STATUS_ID.ID_FEATUREEXTRACT, "Extracting features ");
		put(STATUS_ID.ID_ESTIMATE, "Estimating strain sensitivity ");
	}};

	private static PanelStatusBar linkPanelStatusBar = null;

	private static final int TASK_PROGRESS_SIZE = 6;
	private static int MAX_PROGRESS_ITERATION = 0;

	//----------------------------------------------------------------

	/**
	 * Binds a status panel bar to the controller.
	 * @param statusBar
	 */
	public static void establishLink (PanelStatusBar statusBar)
	{
		linkPanelStatusBar = statusBar;
	}

	//----------------------------------------------------------------

	public static void setProgressTaskCount (int taskCount)
	{
		MAX_PROGRESS_ITERATION = taskCount * TASK_PROGRESS_SIZE;

		if (linkPanelStatusBar != null)
		{
			linkPanelStatusBar.reset();
			linkPanelStatusBar.setTickCount(MAX_PROGRESS_ITERATION);
		}
	}

	//----------------------------------------------------------------

	public static void progressUpdate (STATUS_ID id)
	{
		if (linkPanelStatusBar != null)
		{
			linkPanelStatusBar.doTick(STATUS_MESSAGE.get(id));
		}
		else
		{
			System.out.println(STATUS_MESSAGE.get(id));
		}
	}

	public static void progressUpdate (STATUS_ID id, String message)
	{
		if (linkPanelStatusBar != null)
		{
			linkPanelStatusBar.doTick(STATUS_MESSAGE.get(id) + message);
		}
		else
		{
			System.out.println(STATUS_MESSAGE.get(id) + message);
		}
	}

	public static void progressReset ()
	{
		MAX_PROGRESS_ITERATION = 0;

		if (linkPanelStatusBar != null)
		{
			linkPanelStatusBar.reset();
		}
	}


}
