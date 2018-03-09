package com.acarast.andrey.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import com.acarast.andrey.gui.panel.PanelChart;

/**
 * Class helper for the FormMain class. Sets up the components for displaying 
 * the right panel of the application. Mainly used for displaying results.
 * @author Andrey G
 */
public class FormMainPanelRight
{
    	public static final int TABLE_SIZE = 4;
    
	private JPanel panel;

	private PanelChart panelFeatureChart;

	//---- Table to display extracted features
	private Object[][] tableData = 
	    {
		{"A. Count", "", "", "", ""}, 
		{"A. Length", "", "", "", ""},
		{"A. Density", "", "", "", ""},
		
		{"R. Count", "", "", "", ""}, 
		{"R. Length", "", "", "", ""},
		{"R. Density", "", "", "", ""},
		
		{"Sensitivity", "", "", "", ""},
	    };
	private JTable tableFeatures;

	//----------------------------------------------------------------

	public FormMainPanelRight (FormMainHandler controllerButton)
	{
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(FormStyle.COLOR_MAIN);

		createPanel(controllerButton);
	}

	public JPanel get ()
	{
		return panel;
	}

	//----------------------------------------------------------------

	private void createPanel (FormMainHandler controllerButton)
	{
		JPanel panelResultViewer = new JPanel();
		panelResultViewer.setLayout(new BoxLayout(panelResultViewer, BoxLayout.Y_AXIS));
		panelResultViewer.setBackground(Color.darkGray);

		JPanel panelResultViewerGraph = new JPanel();
		componentResultViewerGraph(panelResultViewerGraph);
		panelResultViewer.add(panelResultViewerGraph);

		JPanel panelResultViewerFeatures = new JPanel();
		componentResultViewFeatures(panelResultViewerFeatures);
		panelResultViewer.add(panelResultViewerFeatures);

		panelResultViewer.add(Box.createVerticalGlue());

		panel.add(panelResultViewer, BorderLayout.LINE_END);
	}

	//----------------------------------------------------------------

	private void componentResultViewerGraph (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (400, 280));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());

		panelFeatureChart = new PanelChart();
		panelFeatureChart.displayChart(displayPanel);

	}

	private void componentResultViewFeatures (JPanel displayPanel)
	{	
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		displayPanel.setSize(new Dimension (400, 140));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		String[] columnNames = {" Feature ", "sample 1", "sample 2", "sample 3", "sample 4"};

		tableFeatures = new JTable(tableData, columnNames);

		JScrollPane scrollPane= new  JScrollPane(tableFeatures);
		scrollPane.setBackground(FormStyle.COLOR_MENU);
		scrollPane.setSize(new Dimension(400, 135));
		scrollPane.setMinimumSize(scrollPane.getSize());
		scrollPane.setMaximumSize(scrollPane.getSize());
		scrollPane.setPreferredSize(scrollPane.getSize());
		scrollPane.setEnabled(false);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(scrollPane, layoutConstraits);
	}

	
	//----------------------------------------------------------------

	public JTable getComponentTableFeatures ()
	{
		return tableFeatures;
	}

	public PanelChart getComponentPanelChart ()
	{
		return panelFeatureChart;
	}

	//----------------------------------------------------------------
	
	public void reset ()
	{
		panelFeatureChart.resetChartData();
		
		for (int i = 0; i < TABLE_SIZE; i++)
		{
		    tableFeatures.getModel().setValueAt("", 0, i+1);
		    tableFeatures.getModel().setValueAt("", 1, i+1);
		    tableFeatures.getModel().setValueAt("", 2, i+1);
		    
		    tableFeatures.getModel().setValueAt("", 3, i+1);
		    tableFeatures.getModel().setValueAt("", 4, i+1);
		    tableFeatures.getModel().setValueAt("", 5, i+1);
		    
		    tableFeatures.getModel().setValueAt("", 6, i+1);
		}
	}

}
