package com.acarast.andrey.gui.panel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.acarast.andrey.gui.form.FormStyle;

import java.net.URL;

public class PanelStatusBar
{
	private JProgressBar statusProgressBar;
	private JLabel statusLabel;

	//---- Progress bar parameters
	private int tickSize;
	private int tickCount;
	private int tickCurrentValue;

	private final String RESOURCE_ICO_PATH_PROCESSING = FormStyle.DEFAULT_RESOURCE_FOLDER + "loading.gif";
	private final String RESOURCE_ICO_PATH_IDLE = FormStyle.DEFAULT_RESOURCE_FOLDER + "loading_idle.png";

	private boolean isDisplayAnimation;

	private JLabel labelAnimation;
	private JPanel dd;


	//============================================================================================

	public PanelStatusBar (JPanel displayPanel)
	{
		dd = displayPanel;
		isDisplayAnimation = false;

		displayPanel.setBackground(Color.darkGray);
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));	

		tickSize = 1;
		tickCount = 100;
		tickCurrentValue = 0;

		displayPanel.add(Box.createRigidArea(new Dimension(5,0)));


		JPanel panelProgress = new JPanel();
		labelAnimation = new JLabel();
		componentPanelProgressBar(panelProgress);
		displayPanel.add(panelProgress);

		displayPanel.add(Box.createRigidArea(new Dimension(10,0)));

		JLabel labelState = new JLabel("State:");
		labelState.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelState.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		labelState.setPreferredSize(new Dimension(35, 19));
		labelState.setSize(new Dimension(35, 19));	
		displayPanel.add(labelState); 

		displayPanel.add(Box.createRigidArea(new Dimension(2,0)));

		statusLabel = new JLabel("Idle");
		statusLabel.setFont(FormStyle.DEFAULT_STATUS_FONT);
		statusLabel.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		displayPanel.add(statusLabel); 

		displayPanel.add(Box.createHorizontalGlue());
	}

	private void componentPanelProgressBar (JPanel displayPanel)
	{
		displayPanel.setPreferredSize(new Dimension(191, 35));
		displayPanel.setMaximumSize(new Dimension(191, 35));
		displayPanel.setMinimumSize(new Dimension(191, 35));
		displayPanel.setBackground(FormStyle.COLOR_MAIN);
		displayPanel.setLayout(new GridBagLayout());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		componentLabelAnimation();
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.CENTER;
		displayPanel.add(labelAnimation, layoutConstraints);



		statusProgressBar = new JProgressBar(0, tickCount);
		statusProgressBar.setValue(tickCurrentValue);
		statusProgressBar.setStringPainted(false);
		statusProgressBar.setPreferredSize(new Dimension(180, 10));
		statusProgressBar.setMaximumSize(new Dimension(180, 10));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.CENTER;
		displayPanel.add(statusProgressBar, layoutConstraints);
	}

	private void componentLabelAnimation ()
	{
		try
		{
			String filePath = "";

			if (isDisplayAnimation) { filePath = RESOURCE_ICO_PATH_PROCESSING; }
			else { filePath = RESOURCE_ICO_PATH_IDLE; }

			//---- Load icon
			File urlFile = new File(filePath);
			URL imageUrl;


			imageUrl =  urlFile.toURI().toURL();
			ImageIcon imageIcon = new ImageIcon(imageUrl);

			labelAnimation.setBorder(new EmptyBorder(0, 0, 2, 0));
			labelAnimation.setIcon(imageIcon); 	
			imageIcon.setImageObserver(labelAnimation);

		}
		catch (Exception e) {}
	}

	//============================================================================================

	public void setTickCount (int value)
	{
		tickCount = value;

		statusProgressBar.setMaximum(tickCount);
	}

	public void doTick ()
	{
		tickCurrentValue += tickSize;

		statusProgressBar.setValue(tickCurrentValue);
	}

	public void doTick (String textStatus)
	{
		tickCurrentValue += tickSize;

		statusProgressBar.setValue(tickCurrentValue);

		statusLabel.setText(textStatus);
	}

	//============================================================================================

	public void setIsDisplayAnimation (boolean value)
	{
		isDisplayAnimation = value;

		componentLabelAnimation();

		dd.repaint();
	}

	//============================================================================================

	public void reset ()
	{
		//---- Set progress bar to default values
		tickCount = 100;

		statusLabel.setText("Idle");
		
		statusProgressBar.setMaximum(tickCount);
		statusProgressBar.setValue(0);

		tickCurrentValue = 0;
	}
}