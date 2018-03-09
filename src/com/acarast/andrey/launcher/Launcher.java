//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//-- Project: Automatic Cell Analyser for Rapid Susceptibility Testing
//--
//-- Description: This software is designed to perform automatic 
//-- processing of microscopy images of DSTM device. 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

package com.acarast.andrey.launcher;

import java.awt.Rectangle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.acarast.andrey.controller.MainController;

/**
 * Main class. Launches the GUI of the application.
 * @author Andrey G
 */

public class Launcher
{
	public static void main(String args[])
	{
		//---- Load opencv libraries
		System.loadLibrary("opencv_java310");

		//---- Launch the main GUI window
		SwingUtilities.invokeLater(new Runnable() {public void run() {launch();}});
	}

	public static void launch () 
	{
		//---- Set default fonts for the application
		UIManager.getLookAndFeelDefaults().put("defaultFont", new java.awt.Font("Times New Roman", 0, 12));

		
		//---- Detect the maximum screen resolution
		Rectangle screenResolution = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		MainController controller = MainController.getInstance();
		controller.launchMainWindow(screenResolution);

	}
}
