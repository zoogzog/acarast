package com.acarast.andrey.gui.panel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.acarast.andrey.exception.ExceptionMessage;
import com.acarast.andrey.exception.ExceptionMessage.EXCEPTION_CODE;

//---- For more details check this:
//---- http://www.codejava.net/java-se/graphics/using-jfreechart-to-draw-xy-line-chart-with-xydataset

/**
 * Panel for displaying charts
 * @author Abdrey G
 */
public class PanelChart
{
	private final String DEFAULT_CHART_TITLE = null;
	private final String DEFAULT_CHART_XAXIS_LABEL = "length";
	private  String DEFAULT_CHART_YAXIS_LABEL = "frequency";
	private final PlotOrientation DEFAULT_CHART_PLOT_ORIENTATION = PlotOrientation.VERTICAL; 
	private final boolean DEFAULT_CHART_IS_SHOW_LEGEND = true;
	private final boolean DEFAULT_CHART_IS_SHOW_TOOLTIPS = false;
	private final boolean DEFAULT_CHART_IS_SHOW_URLS = false;

	private JFreeChart histogramChart;
	private ChartPanel panelHistogramChart;
	
	private XYSeriesCollection dataset;
	
	//============================================================================================
	
	public PanelChart ()
	{
		histogramChart = ChartFactory.createHistogram(DEFAULT_CHART_TITLE, DEFAULT_CHART_XAXIS_LABEL, DEFAULT_CHART_YAXIS_LABEL, null, 
				  DEFAULT_CHART_PLOT_ORIENTATION, DEFAULT_CHART_IS_SHOW_LEGEND, DEFAULT_CHART_IS_SHOW_TOOLTIPS, DEFAULT_CHART_IS_SHOW_URLS);
		
		dataset = new XYSeriesCollection();
	}
	
	//============================================================================================
	
	public void displayChart (JPanel displayPanel)
	{
		panelHistogramChart = new ChartPanel(histogramChart);
		panelHistogramChart.setPreferredSize(displayPanel.getSize());
		panelHistogramChart.setSize(displayPanel.getSize());
		panelHistogramChart.setMouseZoomable(true);
		
		displayPanel.setLayout(new BorderLayout());
		displayPanel.add(panelHistogramChart, BorderLayout.CENTER);
	}
	
	//============================================================================================
	
	public void addChartData (double[] inputDataX, double[] inputDataY, String inputDataName, boolean isFrequency)
	{
		if (isFrequency) { DEFAULT_CHART_YAXIS_LABEL = "frequency"; }
		else { DEFAULT_CHART_YAXIS_LABEL = "cell count"; }
		
		 XYSeries series = new XYSeries(inputDataName);
		 
		 for (int i = 0; i < inputDataX.length; i++)
		 {
			 series.add(inputDataX[i], inputDataY[i]);
		 }
		 
		 dataset.addSeries(series);
	}
	
	public void displayChartData ()
	{
		 XYDataset displayedDataset = dataset;
		
		histogramChart = ChartFactory.createXYLineChart(DEFAULT_CHART_TITLE, DEFAULT_CHART_XAXIS_LABEL, DEFAULT_CHART_YAXIS_LABEL, displayedDataset, 
				  DEFAULT_CHART_PLOT_ORIENTATION, DEFAULT_CHART_IS_SHOW_LEGEND, DEFAULT_CHART_IS_SHOW_TOOLTIPS, DEFAULT_CHART_IS_SHOW_URLS);

		customizeChart(histogramChart);
		
		panelHistogramChart.setChart(histogramChart);
		panelHistogramChart.repaint();
	}
	
	public void resetChartData ()
	{
		dataset.removeAllSeries();
		
		histogramChart = ChartFactory.createHistogram(DEFAULT_CHART_TITLE, DEFAULT_CHART_XAXIS_LABEL, DEFAULT_CHART_YAXIS_LABEL, null, 
				  DEFAULT_CHART_PLOT_ORIENTATION, DEFAULT_CHART_IS_SHOW_LEGEND, DEFAULT_CHART_IS_SHOW_TOOLTIPS, DEFAULT_CHART_IS_SHOW_URLS);
		
		panelHistogramChart.setChart(histogramChart);
		panelHistogramChart.repaint();
	}
	
	//============================================================================================
	
	public void saveChart (String outputFileName) throws ExceptionMessage
	{
		File outputFile = 	new File(outputFileName);	
		
		try
		{
			ChartUtilities.saveChartAsJPEG(outputFile, histogramChart, 1024, 768);
		}
		catch (Exception e) 
		{
			throw new ExceptionMessage(EXCEPTION_CODE.EXCODE_FILEWRITEFAIL);
		};
	}
	
	
	private void customizeChart(JFreeChart chart) 
	{
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.DARK_GRAY);
		
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
	}
}
