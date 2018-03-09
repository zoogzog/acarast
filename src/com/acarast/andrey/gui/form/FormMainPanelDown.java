package com.acarast.andrey.gui.form;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.acarast.andrey.gui.panel.PanelStatusBar;

/**
 * Bottom sub-panel of the main panel
 * @author Andrey G
 */
public class FormMainPanelDown
{
	private JPanel panel;

	//----- Label elements to display mouse pointer location and currently displayed image zoom scale
	public JLabel labelLocationValueX;
	public JLabel labelLocationValueY;
	public JLabel labelZoomScale;

	public PanelStatusBar panelProgressBar;

	//----------------------------------------------------------------

	public FormMainPanelDown (int width)
	{
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(FormStyle.COLOR_MAIN);

		createPanel(width);
	}

	public JPanel get ()
	{
		return panel;
	}

	//----------------------------------------------------------------

	private void createPanel (int width)
	{
		//---- Set current panel properties
		JPanel panelStatusBar = new JPanel();
		panelStatusBar.setLayout(new BoxLayout(panelStatusBar, BoxLayout.X_AXIS));
		panelStatusBar.setBackground(FormStyle.COLOR_MAIN);
		panelStatusBar.setSize(new Dimension(width, 50));
		panel.add(panelStatusBar, BorderLayout.PAGE_END);

		panelStatusBar.add(Box.createRigidArea(new Dimension(5,0)));

		//---- Text labels & progress bar for processing
		JPanel panelProgressBarLocation = new JPanel();
		panelProgressBar = new PanelStatusBar(panelProgressBarLocation);
		panelStatusBar.add(panelProgressBarLocation);
		//	SystemStatus.setGuiLink(panelProgressBar);

		panelStatusBar.add(Box.createRigidArea(new Dimension(5,0)));
		panelStatusBar.add(Box.createHorizontalGlue());

		//---- Display zoom scale
		JLabel labelZoom = new JLabel("zoom: ");
		labelZoom.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelZoom.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		panelStatusBar.add(labelZoom);

		panelStatusBar.add(Box.createRigidArea(new Dimension(2,0)));

		labelZoomScale = new JLabel("0.0 ");
		labelZoomScale.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelZoomScale.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		labelZoomScale.setSize(25, 20);
		labelZoomScale.setPreferredSize(labelZoomScale.getSize());
		panelStatusBar.add(labelZoomScale);

		panelStatusBar.add(Box.createRigidArea(new Dimension(20,0)));

		//---- Display cursor location
		JLabel labelLocationX = new JLabel("x:");
		labelLocationX.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelLocationX.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		panelStatusBar.add(labelLocationX);

		panelStatusBar.add(Box.createRigidArea(new Dimension(5,0)));

		labelLocationValueX = new JLabel("0  ");
		labelLocationValueX.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelLocationValueX.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		labelLocationValueX.setSize(35, 20);
		labelLocationValueX.setPreferredSize(new Dimension(35, 20));
		panelStatusBar.add(labelLocationValueX);

		panelStatusBar.add(Box.createRigidArea(new Dimension(10,0)));

		JLabel labelLocationY = new JLabel("y:");
		labelLocationY.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelLocationY.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		panelStatusBar.add(labelLocationY);

		panelStatusBar.add(Box.createRigidArea(new Dimension(5,0)));

		labelLocationValueY = new JLabel("0  ");
		labelLocationValueY.setFont(FormStyle.DEFAULT_STATUS_FONT);
		labelLocationValueY.setForeground(FormStyle.COLOR_TEXT_LIGHT);
		labelLocationValueY.setSize(35, 20);
		labelLocationValueY.setPreferredSize(new Dimension(35, 20));
		panelStatusBar.add(labelLocationValueY);

		panelStatusBar.add(Box.createRigidArea(new Dimension(30,0)));
	}

	//----------------------------------------------------------------

	public JLabel getComponentLabelX ()
	{
		return labelLocationValueX;
	}

	public JLabel getComponentLabelY ()
	{
		return labelLocationValueY;
	}

	public JLabel getComponentLabelZoom ()
	{
		return labelZoomScale;
	}

	public PanelStatusBar getComponentProgressBar ()
	{
		return panelProgressBar;
	}

	//----------------------------------------------------------------
	
	public void reset ()
	{
		labelLocationValueX.setText("0  ");
		labelLocationValueY.setText("0  ");
		labelZoomScale.setText("0.0");
	}
}
