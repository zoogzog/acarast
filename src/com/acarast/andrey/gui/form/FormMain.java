package com.acarast.andrey.gui.form;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main GUI form of the application.
 * @author Andrey G
 *
 */
public class FormMain
{
	private final String DEFAULT_WINDOW_TITLE = "ACA-RaST"; 
	private final String DEFAULT_WINDOW_ICON = FormStyle.RESOURCE_PATH_ICON;

	//----------------------------------------------------------------
	//---- Environment variables

	private static FormMain instance = null;

	private JFrame frameMain;

	private int windowStartX = 0;
	private int windowStartY = 0;
	private int windowWidth = 0;
	private int windowHeight = 0;

	//----------------------------------------------------------------
	//---- Components 

	private FormMainMenu menu;
	private FormMainToolbar toolbar;
	private FormMainPanelLeft panelLeft;
	private FormMainPanelRight panelRight;
	private FormMainPanelCenter panelCenter;
	private FormMainPanelDown panelDown;

	//----------------------------------------------------------------

	private FormMain (Rectangle screenResolution, FormMainHandler controllerButton, FormMainMouse controllerMouse)
	{
		windowStartX = screenResolution.x;
		windowStartY = screenResolution.y;
		windowWidth = screenResolution.width;
		windowHeight = screenResolution.height;

		controllerButton = new FormMainHandler();
		controllerMouse = new FormMainMouse();

		//---- Define all components of  the panel
		menu = new FormMainMenu(controllerButton);
		toolbar = new FormMainToolbar(controllerButton, windowWidth);
		panelLeft = new FormMainPanelLeft(controllerButton);
		panelRight = new FormMainPanelRight(controllerButton);
		panelCenter = new FormMainPanelCenter(controllerMouse);
		panelDown = new FormMainPanelDown(windowWidth);

		controllerButton.init(this);
		controllerMouse.init(this);

		//---- Create the main panel and set up all components
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		addComponents(panelMain);

		//---- Finalize creation of the main window
		frameMain = new JFrame();
		frameMain.setTitle(DEFAULT_WINDOW_TITLE);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setJMenuBar(menu.get());
		frameMain.setIconImage(FormUtils.getImageResource(DEFAULT_WINDOW_ICON));
		frameMain.getContentPane().setBackground(FormStyle.COLOR_PANEL);
		frameMain.setSize(windowWidth, windowHeight);
		frameMain.setLocation(windowStartX, windowStartY);
		frameMain.setContentPane(panelMain);

		frameMain.setVisible(true);


	}

	public static FormMain getInstance (Rectangle screenResolution, FormMainHandler controllerButton, FormMainMouse controllerMouse)
	{
		if (instance == null) { instance = new FormMain(screenResolution, controllerButton, controllerMouse); }

		return instance;
	}

	//----------------------------------------------------------------

	private void addComponents (JPanel panel)
	{
		panel.add(toolbar.get(), BorderLayout.PAGE_START);
		panel.add(panelLeft.get(), BorderLayout.LINE_START);
		panel.add(panelRight.get(), BorderLayout.LINE_END);
		panel.add(panelCenter.get(), BorderLayout.CENTER);
		panel.add(panelDown.get(), BorderLayout.PAGE_END);
	}

	//----------------------------------------------------------------
	//---- GET COMPONENTS
	//----------------------------------------------------------------

	public FormMainToolbar getComponentToolbar ()
	{
		return toolbar;
	}

	public FormMainPanelLeft getComponentPanelLeft ()
	{
		return panelLeft;
	}

	public FormMainPanelRight getComponentPanelRight ()
	{
		return panelRight;
	}

	public FormMainPanelCenter getComponentPanelCenter ()
	{
		return panelCenter;
	}

	public FormMainPanelDown getComponentPanelDown ()
	{
		return panelDown;
	}

	public JFrame getMainFrame ()
	{
		return frameMain;
	}
	//----------------------------------------------------------------

	/**
	 * Resets the GUI to its default initial state
	 */
	public void reset()
	{
		panelCenter.reset();
		panelDown.reset();
		panelLeft.reset();
		panelRight.reset();
	}


	//----------------------------------------------------------------
}
