package com.acarast.andrey.gui.form;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.acarast.andrey.controller.SettingsController;

/**
 * Textfields and labels for the main form.
 * @author Andrey G
 *
 */
public class FormSettings
{
	//----------------------------------------------------------------
	//---- LABELS
	//----------------------------------------------------------------

	private final String IMAGEPROCESSING_GAUSS_KERNEL_SIZE = "Gaussian kernel size (px):";
	private final String IMAGEPROCESSING_CANNY_THRESHOLD = "Canny edge threshold (int):";
	private final String IMAGEPROCESSING_DILATE_KERNEL_SIZE ="Dilate kernel size (px):";
	private final String IMAGEPROCESSING_LOCALTHRESH_KERNEL_SIZE ="L. Threshold kernel size (px):";
	private final String IMAGEPROCESSING_LOCALTHRESH_CONDITION ="L. Threshold condition (int):";
	private final String IMAGEPROCESSING_LOCALTHRESH_DESTVALUE = "L. Threshold value (int):";
	private final String IMAGEPROCESSING_GLOBALTHREHS_DESTVALUE ="G. Threshold value (int):";

	private final String SAMPLEDETECTION_HOUGHTRANSFORM_RHO = "Hough transform rho (px):";
	private final String SAMPLEDETECTION_HOUGHTRANSFORM_THETTA = "Hough transform theta (rad):";
	private final String SAMPLEDETECTION_HOUGHTRANSFORM_LINEMIN = "Hough transform min length (px):";
	private final String SAMPLEDETECTION_HOUGHTRANSFORM_LINEGAP = "Hough transform max gap (px):";
	private final String SAMPLEDETECTION_HOUGHTRANSFORM_THRESHOLD = "Hough transform threshdold (int):";

	private final String SAMPLE_BORDERCORRECTION_SAMPLEBORDERWIDTH = "Sample border width (px):";
	private final String SAMPLE_BORDERCORRECTION_SAMPLENARROWWIDTH = "Offset to narrow sample (px):";
	private final String SAMPLE_BORDERCORRECTION_INTENSITYDROP_MIN = "Border intensity drop min (px):";
	private final String SAMPLE_BORDERCORRECTION_INTENSITYDROP_MAX = "Border intensity drop max (px):";

	private final String CELLDETECTOR_MIN_BACTERIA_SIZE = "Min bacteria size (px):";
	private final String CELLDETECTOR_MAX_CELL_DISTANCE = "Max cell distance (px):";
	private final String CELLDETECTOR_ANGLE_DIFFERENCE = "Max angle between cells (grad):";

	private final String FEATUREEXTRACTOR_HISTOGRAMSSTEPS = "Frequency histogram steps (count):";
	private final String FEATUREEXTRACTOR_INTERVAL_WEIGHT = "Confidence interval (%):";

	//----------------------------------------------------------------
	//---- TEXTFIELD
	//----------------------------------------------------------------

	private JTextField textfieldCellDetectorMinBacteriaSize;
	private JTextField textfieldCellDetectorMaxBacteriaDistance;
	private JTextField textfieldCellDetectorAngleDifference;

	private JTextField textfieldImageProcessingGaussKernelSize;
	private JTextField textfieldImageProcessingCannyThreshold;
	private JTextField textfieldImageProcessingDilateKernelSize;
	private JTextField textfieldImageProcessingLocalThresholdKernelSize;
	private JTextField textfieldImageProcessingThresholdCondition;
	private JTextField textfieldImageProcessingThresholdValue;
	private JTextField textfieldImageProcessingGlobalThreshold;

	private JTextField textfieldSampleDetectionHoughTransformRho;
	private JTextField textfieldSampleDetectionHoughTransformThetta;
	private JTextField textfieldSampleDetectionHoughTransformThreshold;
	private JTextField textfieldSampleDetectionHoughTransformMinLineLength;
	private JTextField textfieldSampleDetectionHoughTransformMaxLineGap;

	//NOT USED	private JTextField textfieldSampleDetectionBorderCorrectionPatchSize;
	private JTextField textfieldSampleDetectionBorderCorrectionNarrowSampleStrip;
	private JTextField  textfieldSampleDetectionBorderCorrectionBorderWidth;
	//NOT USED	private JTextField textfieldSampleDetectionBorderCorrectionSampleHalfWidth;
	private JTextField textfieldSampleDetectionBorderCorrectionBorderIntensityMin;
	private JTextField textfieldSampleDetectionBorderCorrectionBorderIntensityMax;

	private JTextField textfieldFeatureExtractorHistogramSteps;
	private JTextField textfieldFeatureExtractorBaseLengthWeight;

	//----------------------------------------------------------------

	private JDialog dialog;

	private int DEFAULT_FORM_WIDTH = 740;

	//----------------------------------------------------------------

	public void displayFrame(JFrame parentFrame) 
	{
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		componentMainPanel(panelMain);

		dialog = new JDialog(parentFrame, "Settings");
		dialog.setModal(true);
		dialog.setContentPane(panelMain);
		dialog.pack();
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setResizable(false);
		dialog.setVisible(true);

	}

	//----------------------------------------------------------------

	private void componentMainPanel (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH, 420));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		JPanel panelLeft = new JPanel ();
		panelLeft.setSize(new Dimension (DEFAULT_FORM_WIDTH, 380));
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBackground(FormStyle.COLOR_MENU);

		JPanel panelSettingsImageProcessing = new JPanel();
		componentSettingsPanelImageProcessing(panelSettingsImageProcessing);
		panelLeft.add(panelSettingsImageProcessing);

		JPanel panelSettingsCellDetector = new JPanel();
		componentSettingsPanelCellDetector(panelSettingsCellDetector);
		panelLeft.add(panelSettingsCellDetector);

		JPanel panelRight = new JPanel();
		panelRight.setLayout(new BoxLayout(panelRight , BoxLayout.Y_AXIS));
		panelRight.setSize(new Dimension (DEFAULT_FORM_WIDTH, 380));
		panelRight.setBackground(FormStyle.COLOR_MENU);

		JPanel panelSettingsSampleDetector = new JPanel();
		componentSettingsPanelSampleDetector(panelSettingsSampleDetector);
		panelRight.add(panelSettingsSampleDetector);

		JPanel panelSettingsFeatureExtractor = new JPanel();
		componentSettingsPanelFeatureExtractor(panelSettingsFeatureExtractor);
		panelRight.add(panelSettingsFeatureExtractor);


		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelLeft, layoutConstraints);

		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelRight, layoutConstraints);



		JPanel panelButtons = new JPanel();
		componentButtonsPanel(panelButtons);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.gridwidth = 2;
		displayPanel.add(panelButtons, layoutConstraints);

		setTextFieldValues();
	}	

	private void componentSettingsPanelImageProcessing (JPanel displayPanel)
	{
		String labelText = "";
		TitledBorder displayBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Image Processing Settings");
		displayBorder.setTitleFont(FormStyle.DEFAULT_FONT);

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(displayBorder);
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH / 2, 250));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING GAUSSIAN KERENEL SIZE
		JLabel labelImageProcessingGaussKernelSize = new JLabel(IMAGEPROCESSING_GAUSS_KERNEL_SIZE);
		labelImageProcessingGaussKernelSize.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingGaussKernelSize.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		layoutConstraints.weightx = 0.1;
		displayPanel.add(labelImageProcessingGaussKernelSize, layoutConstraints);	

		textfieldImageProcessingGaussKernelSize = new JTextField();
		textfieldImageProcessingGaussKernelSize.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingGaussKernelSize.setSize(50, 20);
		textfieldImageProcessingGaussKernelSize.setPreferredSize(textfieldImageProcessingGaussKernelSize.getSize());
		textfieldImageProcessingGaussKernelSize.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingGaussKernelSize, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE_MAX) + "]";

		JLabel labelImageProcessingGaussKernelSizeMinMax = new JLabel(labelText);
		labelImageProcessingGaussKernelSizeMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingGaussKernelSizeMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING CANNY EDGE THRESHOLD
		JLabel labelImageProcessingCannyThreshold = new JLabel(IMAGEPROCESSING_CANNY_THRESHOLD);
		labelImageProcessingCannyThreshold.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingCannyThreshold.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingCannyThreshold, layoutConstraints);	

		textfieldImageProcessingCannyThreshold = new JTextField();
		textfieldImageProcessingCannyThreshold.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingCannyThreshold.setSize(50, 20);
		textfieldImageProcessingCannyThreshold.setPreferredSize(textfieldImageProcessingCannyThreshold.getSize());
		textfieldImageProcessingCannyThreshold.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingCannyThreshold, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD_MAX) + "]";

		JLabel labelImageProcessingCannyThresholdMinMax = new JLabel(labelText);
		labelImageProcessingCannyThresholdMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingCannyThresholdMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING DILATE KERNEL SIZE
		JLabel labelImageProcessingDilateKernelSize = new JLabel(IMAGEPROCESSING_DILATE_KERNEL_SIZE);
		labelImageProcessingDilateKernelSize.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingDilateKernelSize.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingDilateKernelSize, layoutConstraints);	

		textfieldImageProcessingDilateKernelSize = new JTextField();
		textfieldImageProcessingDilateKernelSize.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingDilateKernelSize.setSize(50, 20);
		textfieldImageProcessingDilateKernelSize.setPreferredSize(textfieldImageProcessingDilateKernelSize.getSize());
		textfieldImageProcessingDilateKernelSize.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingDilateKernelSize, layoutConstraints);	

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE_MAX) + "]";

		JLabel labelImageProcessingDilateKernelSizeMinMax = new JLabel(labelText);
		labelImageProcessingDilateKernelSizeMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingDilateKernelSizeMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING LOCAL THRESHOLD KERNEL SIZE
		JLabel labelImageProcessingLocalThresholdKernelSize = new JLabel(IMAGEPROCESSING_LOCALTHRESH_KERNEL_SIZE);
		labelImageProcessingLocalThresholdKernelSize.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingLocalThresholdKernelSize.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholdKernelSize, layoutConstraints);	

		textfieldImageProcessingLocalThresholdKernelSize = new JTextField();
		textfieldImageProcessingLocalThresholdKernelSize.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingLocalThresholdKernelSize.setSize(50, 20);
		textfieldImageProcessingLocalThresholdKernelSize.setPreferredSize(textfieldImageProcessingDilateKernelSize.getSize());
		textfieldImageProcessingLocalThresholdKernelSize.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingLocalThresholdKernelSize, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE_MAX) + "]";

		JLabel labelImageProcessingLocalThresholdKernelSizeMinMax = new JLabel(labelText);
		labelImageProcessingLocalThresholdKernelSizeMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholdKernelSizeMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING LOCAL THREHSOLD CONDITION
		JLabel labelImageProcessingLocalThresholdCondition = new JLabel(IMAGEPROCESSING_LOCALTHRESH_CONDITION);
		labelImageProcessingLocalThresholdCondition.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingLocalThresholdCondition.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholdCondition, layoutConstraints);

		textfieldImageProcessingThresholdCondition = new JTextField();
		textfieldImageProcessingThresholdCondition.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingThresholdCondition.setSize(50, 20);
		textfieldImageProcessingThresholdCondition.setPreferredSize(textfieldImageProcessingDilateKernelSize.getSize());
		textfieldImageProcessingThresholdCondition.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingThresholdCondition, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION_MAX) + "]";

		JLabel labelImageProcessingLocalThresholdConditionMinMax = new JLabel(labelText);
		labelImageProcessingLocalThresholdConditionMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholdConditionMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING LOCAL THRESHOLD DESTVALUE
		JLabel labelImageProcessingLocalThresholDestvalue = new JLabel(IMAGEPROCESSING_LOCALTHRESH_DESTVALUE);
		labelImageProcessingLocalThresholDestvalue.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingLocalThresholDestvalue.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholDestvalue, layoutConstraints);

		textfieldImageProcessingThresholdValue = new JTextField();
		textfieldImageProcessingThresholdValue.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingThresholdValue.setSize(50, 20);
		textfieldImageProcessingThresholdValue.setPreferredSize(textfieldImageProcessingDilateKernelSize.getSize());
		textfieldImageProcessingThresholdValue.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingThresholdValue, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE_MAX) + "]";

		JLabel labelImageProcessingLocalThresholDestvalueMinMax = new JLabel(labelText);
		labelImageProcessingLocalThresholDestvalueMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingLocalThresholDestvalueMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: IMAGE PROCESSING GLOBAL THRESHOLD 
		JLabel labelImageProcessingGlobalThreshold = new JLabel (IMAGEPROCESSING_GLOBALTHREHS_DESTVALUE);
		labelImageProcessingGlobalThreshold.setFont(FormStyle.DEFAULT_FONT);
		labelImageProcessingGlobalThreshold.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingGlobalThreshold , layoutConstraints);

		textfieldImageProcessingGlobalThreshold = new JTextField();
		textfieldImageProcessingGlobalThreshold.setFont(FormStyle.DEFAULT_FONT);
		textfieldImageProcessingGlobalThreshold.setSize(50, 20);
		textfieldImageProcessingGlobalThreshold.setPreferredSize(textfieldImageProcessingDilateKernelSize.getSize());
		textfieldImageProcessingGlobalThreshold.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldImageProcessingGlobalThreshold, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_IMAGE_PROCESSING_GLOBAL_THRESHOLD_MAX) + "]";

		JLabel labelImageProcessingGlobalThresholdMinMax = new JLabel(labelText);
		labelImageProcessingGlobalThresholdMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelImageProcessingGlobalThresholdMinMax, layoutConstraints);

	}

	private void componentSettingsPanelCellDetector (JPanel displayPanel)
	{
		String labelText = "";
		TitledBorder displayBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Cell Detector Settings");
		displayBorder.setTitleFont(FormStyle.DEFAULT_FONT);

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(displayBorder);	
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH / 2, 130));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		//--------------------------------------------------------------
		//---- Settings: CELL DETECTOR MIN CELL SIZE
		JLabel labelCellDetectorMinBacteriaSize = new JLabel(CELLDETECTOR_MIN_BACTERIA_SIZE);
		labelCellDetectorMinBacteriaSize.setFont(FormStyle.DEFAULT_FONT);
		labelCellDetectorMinBacteriaSize.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		layoutConstraints.weightx = 0.1;
		displayPanel.add(labelCellDetectorMinBacteriaSize, layoutConstraints);

		textfieldCellDetectorMinBacteriaSize = new JTextField();
		textfieldCellDetectorMinBacteriaSize.setFont(FormStyle.DEFAULT_FONT);
		textfieldCellDetectorMinBacteriaSize.setSize(50, 20);
		textfieldCellDetectorMinBacteriaSize.setPreferredSize(textfieldCellDetectorMinBacteriaSize.getSize());
		textfieldCellDetectorMinBacteriaSize.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldCellDetectorMinBacteriaSize, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_MIN_BACTERIA_SIZE_MAX) + "]";

		JLabel labelCellDetectorMinBacteriaSizeMinMax = new JLabel(labelText);
		labelCellDetectorMinBacteriaSizeMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelCellDetectorMinBacteriaSizeMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: CELL DETECTOR MAX CELL SIZE
		JLabel labelCellDetectorMaxBacteriaDistance = new JLabel(CELLDETECTOR_MAX_CELL_DISTANCE);
		labelCellDetectorMaxBacteriaDistance.setFont(FormStyle.DEFAULT_FONT);
		labelCellDetectorMaxBacteriaDistance.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelCellDetectorMaxBacteriaDistance, layoutConstraints);

		textfieldCellDetectorMaxBacteriaDistance = new JTextField();
		textfieldCellDetectorMaxBacteriaDistance.setFont(FormStyle.DEFAULT_FONT);
		textfieldCellDetectorMaxBacteriaDistance.setSize(50, 20);
		textfieldCellDetectorMaxBacteriaDistance.setPreferredSize(textfieldCellDetectorMaxBacteriaDistance.getSize());
		textfieldCellDetectorMaxBacteriaDistance.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldCellDetectorMaxBacteriaDistance, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_MAX_CELL_DISTANCE_MAX) + "]";

		JLabel labelCellDetectorMaxBacteriaDistanceMinMax = new JLabel(labelText);
		labelCellDetectorMaxBacteriaDistanceMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelCellDetectorMaxBacteriaDistanceMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: CELL DETECTOR ANGLE DIFFERENCE
		JLabel labelCellDetectorAngleDifference = new JLabel(CELLDETECTOR_ANGLE_DIFFERENCE);
		labelCellDetectorAngleDifference.setFont(FormStyle.DEFAULT_FONT);
		labelCellDetectorAngleDifference.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelCellDetectorAngleDifference, layoutConstraints);

		textfieldCellDetectorAngleDifference = new JTextField();
		textfieldCellDetectorAngleDifference.setFont(FormStyle.DEFAULT_FONT);
		textfieldCellDetectorAngleDifference.setSize(50, 20);
		textfieldCellDetectorAngleDifference.setPreferredSize(textfieldCellDetectorAngleDifference.getSize());
		textfieldCellDetectorAngleDifference.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldCellDetectorAngleDifference, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_CELL_DETECTOR_ANGLE_DIFFERENCE_MAX) + "]";

		JLabel labelCellDetectorAngleDifferenceMinMax = new JLabel(labelText);
		labelCellDetectorAngleDifferenceMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelCellDetectorAngleDifferenceMinMax, layoutConstraints);


	}

	private void componentSettingsPanelSampleDetector (JPanel displayPanel)
	{
		String labelText = "";

		TitledBorder displayBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Sample Detector Settings");
		displayBorder.setTitleFont(FormStyle.DEFAULT_FONT);

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(displayBorder);	
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH / 2, 290));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		//--------------------------------------------------------------
		//---- Settings: hough transform rho
		JLabel labelSampleDetectionHoughTransformRho = new JLabel(SAMPLEDETECTION_HOUGHTRANSFORM_RHO);
		labelSampleDetectionHoughTransformRho.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionHoughTransformRho.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		layoutConstraints.weightx = 0.1;
		displayPanel.add(labelSampleDetectionHoughTransformRho, layoutConstraints);

		textfieldSampleDetectionHoughTransformRho = new JTextField(); 
		textfieldSampleDetectionHoughTransformRho.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionHoughTransformRho.setSize(50, 20);
		textfieldSampleDetectionHoughTransformRho.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionHoughTransformRho.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionHoughTransformRho, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO_MAX) + "]";

		JLabel labelSampleDetectionHoughTransformRhoMinMax = new JLabel(labelText);
		labelSampleDetectionHoughTransformRhoMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformRhoMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: hough transform theta
		JLabel labelSampleDetectionHoughTransformTheta = new JLabel(SAMPLEDETECTION_HOUGHTRANSFORM_THETTA);
		labelSampleDetectionHoughTransformTheta.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionHoughTransformTheta.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformTheta, layoutConstraints);

		textfieldSampleDetectionHoughTransformThetta = new JTextField();
		textfieldSampleDetectionHoughTransformThetta.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionHoughTransformThetta.setSize(50, 20);
		textfieldSampleDetectionHoughTransformThetta.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionHoughTransformThetta.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionHoughTransformThetta, layoutConstraints);

		labelText = "[PI/180,PI/6]";

		JLabel labelSampleDetectionHoughTransformThetaMinMax = new JLabel(labelText);
		labelSampleDetectionHoughTransformThetaMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformThetaMinMax, layoutConstraints);

		//--------------------------------------------------------------
		//---- Settings: hough transform threshold
		JLabel labelSampleDetectionHoughTransformThreshold = new JLabel(SAMPLEDETECTION_HOUGHTRANSFORM_THRESHOLD);
		labelSampleDetectionHoughTransformThreshold.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionHoughTransformThreshold.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformThreshold, layoutConstraints);

		textfieldSampleDetectionHoughTransformThreshold = new JTextField();
		textfieldSampleDetectionHoughTransformThreshold.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionHoughTransformThreshold.setSize(50, 20);
		textfieldSampleDetectionHoughTransformThreshold.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionHoughTransformThreshold.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionHoughTransformThreshold, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD_MAX) + "]";

		JLabel labelSampleDetectionHoughTransformThresholdMinMax = new JLabel(labelText);
		labelSampleDetectionHoughTransformThresholdMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 2;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformThresholdMinMax, layoutConstraints);

		//--------------------------------------------------------------

		JLabel labelSampleDetectionHoughTransformMinLineLength = new JLabel(SAMPLEDETECTION_HOUGHTRANSFORM_LINEMIN);
		labelSampleDetectionHoughTransformMinLineLength.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionHoughTransformMinLineLength.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformMinLineLength, layoutConstraints);

		textfieldSampleDetectionHoughTransformMinLineLength = new JTextField();
		textfieldSampleDetectionHoughTransformMinLineLength.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionHoughTransformMinLineLength.setSize(50, 20);
		textfieldSampleDetectionHoughTransformMinLineLength.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionHoughTransformMinLineLength.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionHoughTransformMinLineLength, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH_MAX) + "]";

		JLabel  labelSampleDetectionHoughTransformMinLineLengthMinMax = new JLabel(labelText);
		labelSampleDetectionHoughTransformMinLineLengthMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 3;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformMinLineLengthMinMax, layoutConstraints);

		//--------------------------------------------------------------
		JLabel labelSampleDetectionHoughTransformMaxLineGap = new JLabel(SAMPLEDETECTION_HOUGHTRANSFORM_LINEGAP);
		labelSampleDetectionHoughTransformMaxLineGap.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionHoughTransformMaxLineGap.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformMaxLineGap, layoutConstraints);

		textfieldSampleDetectionHoughTransformMaxLineGap = new JTextField();
		textfieldSampleDetectionHoughTransformMaxLineGap.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionHoughTransformMaxLineGap.setSize(50, 20);
		textfieldSampleDetectionHoughTransformMaxLineGap.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionHoughTransformMaxLineGap.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionHoughTransformMaxLineGap, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP_MAX) + "]";

		JLabel  labelSampleDetectionHoughTransformMaxLineGapMinMax = new JLabel(labelText);
		labelSampleDetectionHoughTransformMaxLineGapMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 4;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionHoughTransformMaxLineGapMinMax, layoutConstraints);

		//--------------------------------------------------------------
		JLabel labelSampleDetectionBorderCorrectionNarrowSampleStrip = new JLabel(SAMPLE_BORDERCORRECTION_SAMPLENARROWWIDTH);
		labelSampleDetectionBorderCorrectionNarrowSampleStrip.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionBorderCorrectionNarrowSampleStrip.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionNarrowSampleStrip, layoutConstraints);

		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip = new JTextField();
		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.setSize(50, 20);
		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionBorderCorrectionNarrowSampleStrip, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP_MAX) + "]";

		JLabel  labelSampleDetectionBorderCorrectionNarrowSampleStripMinMax = new JLabel(labelText);
		labelSampleDetectionBorderCorrectionNarrowSampleStripMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 5;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionNarrowSampleStripMinMax, layoutConstraints);

		//--------------------------------------------------------------
		JLabel labelSampleDetectionBorderCorrectionBorderWidth = new JLabel(SAMPLE_BORDERCORRECTION_SAMPLEBORDERWIDTH);
		labelSampleDetectionBorderCorrectionBorderWidth.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionBorderCorrectionBorderWidth.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionBorderWidth, layoutConstraints);

		textfieldSampleDetectionBorderCorrectionBorderWidth = new JTextField();
		textfieldSampleDetectionBorderCorrectionBorderWidth.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionBorderCorrectionBorderWidth.setSize(50, 20);
		textfieldSampleDetectionBorderCorrectionBorderWidth.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionBorderCorrectionBorderWidth.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add( textfieldSampleDetectionBorderCorrectionBorderWidth, layoutConstraints);

		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH_MAX) + "]";

		JLabel  labelSampleDetectionBorderCorrectionBorderWidthMinMax = new JLabel(labelText);
		labelSampleDetectionBorderCorrectionBorderWidthMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 6;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionBorderWidthMinMax, layoutConstraints);

		//--------------------------------------------------------------
		JLabel labelSampleDetectionBorderCorrectionIntensityMin = new JLabel(SAMPLE_BORDERCORRECTION_INTENSITYDROP_MIN);
		labelSampleDetectionBorderCorrectionIntensityMin.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionBorderCorrectionIntensityMin.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 7;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionIntensityMin, layoutConstraints);

		textfieldSampleDetectionBorderCorrectionBorderIntensityMin = new JTextField();
		textfieldSampleDetectionBorderCorrectionBorderIntensityMin.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionBorderCorrectionBorderIntensityMin.setSize(50, 20);
		textfieldSampleDetectionBorderCorrectionBorderIntensityMin.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionBorderCorrectionBorderIntensityMin.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 7;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionBorderCorrectionBorderIntensityMin, layoutConstraints);


		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN_VMAX) + "]";

		JLabel  labelSampleDetectionBorderCorrectionIntensityMinMinMax = new JLabel(labelText);
		labelSampleDetectionBorderCorrectionIntensityMinMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 7;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionIntensityMinMinMax, layoutConstraints);

		//--------------------------------------------------------------
		JLabel labelSampleDetectionBorderCorrectionIntensityMax = new JLabel(SAMPLE_BORDERCORRECTION_INTENSITYDROP_MAX);
		labelSampleDetectionBorderCorrectionIntensityMax.setFont(FormStyle.DEFAULT_FONT);
		labelSampleDetectionBorderCorrectionIntensityMax.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 8;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionIntensityMax, layoutConstraints);

		textfieldSampleDetectionBorderCorrectionBorderIntensityMax = new JTextField();
		textfieldSampleDetectionBorderCorrectionBorderIntensityMax.setFont(FormStyle.DEFAULT_FONT);
		textfieldSampleDetectionBorderCorrectionBorderIntensityMax.setSize(50, 20);
		textfieldSampleDetectionBorderCorrectionBorderIntensityMax.setPreferredSize(new Dimension(50, 20));
		textfieldSampleDetectionBorderCorrectionBorderIntensityMax.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 8;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldSampleDetectionBorderCorrectionBorderIntensityMax, layoutConstraints);


		labelText = "[" + String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMIN) + "," +
				String.valueOf(SettingsController.DEFAULT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX_VMAX) + "]";

		JLabel  labelSampleDetectionBorderCorrectionIntensityMaxMinMax = new JLabel(labelText);
		labelSampleDetectionBorderCorrectionIntensityMaxMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 8;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelSampleDetectionBorderCorrectionIntensityMaxMinMax, layoutConstraints);

		//--------------------------------------------------------------

	}

	private void componentSettingsPanelFeatureExtractor (JPanel displayPanel)
	{
		String labelText = "";

		TitledBorder displayBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Feature Extractor Settings");
		displayBorder.setTitleFont(FormStyle.DEFAULT_FONT);

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(displayBorder);	
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH / 2, 90));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		JLabel labelFeatureExtractorHistogramSteps = new JLabel(FEATUREEXTRACTOR_HISTOGRAMSSTEPS);
		labelFeatureExtractorHistogramSteps.setFont(FormStyle.DEFAULT_FONT);
		labelFeatureExtractorHistogramSteps.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		layoutConstraints.weightx = 0.1;
		displayPanel.add(labelFeatureExtractorHistogramSteps, layoutConstraints);

		textfieldFeatureExtractorHistogramSteps = new JTextField(); 
		textfieldFeatureExtractorHistogramSteps.setFont(FormStyle.DEFAULT_FONT);
		textfieldFeatureExtractorHistogramSteps.setSize(50, 20);
		textfieldFeatureExtractorHistogramSteps.setPreferredSize(new Dimension(50, 20));
		textfieldFeatureExtractorHistogramSteps.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldFeatureExtractorHistogramSteps, layoutConstraints);


		labelText = "[" + String.valueOf(SettingsController.DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS_MAX) + "]";

		JLabel labelFeatureExtractorHistogramStepsMinMax = new JLabel(labelText);
		labelFeatureExtractorHistogramStepsMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelFeatureExtractorHistogramStepsMinMax, layoutConstraints);

		//--------------------------------------------------------------

		JLabel labelFeatureExtractorHistogramBaseLengthWeight = new JLabel(FEATUREEXTRACTOR_INTERVAL_WEIGHT);
		labelFeatureExtractorHistogramBaseLengthWeight.setFont(FormStyle.DEFAULT_FONT);
		labelFeatureExtractorHistogramBaseLengthWeight.setBorder( BorderFactory.createEmptyBorder(5,5,5,8));
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelFeatureExtractorHistogramBaseLengthWeight, layoutConstraints);


		textfieldFeatureExtractorBaseLengthWeight = new JTextField(); 
		textfieldFeatureExtractorBaseLengthWeight.setFont(FormStyle.DEFAULT_FONT);
		textfieldFeatureExtractorBaseLengthWeight.setSize(50, 20);
		textfieldFeatureExtractorBaseLengthWeight.setPreferredSize(new Dimension(50, 20));
		textfieldFeatureExtractorBaseLengthWeight.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(textfieldFeatureExtractorBaseLengthWeight, layoutConstraints);


		labelText = "[" + String.valueOf(SettingsController.DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MIN) + "," +
				String.valueOf(SettingsController.DEFAULT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL_MAX) + "]";

		JLabel labelFeatureExtractorHistogramBaseLengthWeightMinMax = new JLabel(labelText);
		labelFeatureExtractorHistogramBaseLengthWeightMinMax.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraints.gridx = 2;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		layoutConstraints.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelFeatureExtractorHistogramBaseLengthWeightMinMax, layoutConstraints);


	}

	private void componentButtonsPanel (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
		displayPanel.setSize(new Dimension (DEFAULT_FORM_WIDTH, 40));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());

		displayPanel.add(Box.createHorizontalGlue());

		JButton buttonApply = new JButton("Apply");
		buttonApply.setSize(50, 30);
		buttonApply.setFont(FormStyle.DEFAULT_FONT);
		buttonApply.addActionListener(new handlerApplySettings());
		displayPanel.add(buttonApply);

		displayPanel.add(Box.createRigidArea(new Dimension(5,0)));

		JButton buttonReset = new JButton("Reset");
		buttonReset.setSize(50,30);
		buttonReset.setFont(FormStyle.DEFAULT_FONT);
		buttonReset.addActionListener(new handlerResetSettings());
		displayPanel.add(buttonReset);

		displayPanel.add(Box.createRigidArea(new Dimension(5,0)));

		JButton buttonClose = new JButton("Close");
		buttonClose.setSize(50, 30);
		buttonClose.setFont(FormStyle.DEFAULT_FONT);
		buttonClose.addActionListener(new handlerCloseForm());
		displayPanel.add(buttonClose);

		displayPanel.add(Box.createRigidArea(new Dimension(5,0)));

	}

	//----------------------------------------------------------------

	private void setTextFieldValues ()
	{
		textfieldImageProcessingGaussKernelSize.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_GAUSS_KERNEL_SIZE));
		textfieldImageProcessingCannyThreshold.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_CANNY_EDGE_THRESHOLD));
		textfieldImageProcessingDilateKernelSize.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_DILATE_KERNEL_SIZE));
		textfieldImageProcessingLocalThresholdKernelSize.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_KERNEL_SIZE));
		textfieldImageProcessingThresholdCondition.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_CONDITION));
		textfieldImageProcessingThresholdValue.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_LOCAL_THRESHOLD_DESTVALUE));
		textfieldImageProcessingGlobalThreshold.setText(String.valueOf(SettingsController.CURRENT_IMAGE_PROCESSING_GLOBAL_THRESHOLD));

		textfieldSampleDetectionHoughTransformRho.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_RHO));
		textfieldSampleDetectionHoughTransformThetta.setText(String.valueOf((double) Math.round(SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THETTA * 10000) / 10000));
		textfieldSampleDetectionHoughTransformThreshold.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_THRESHOLD));
		textfieldSampleDetectionHoughTransformMinLineLength.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MIN_LINE_LENGTH));
		textfieldSampleDetectionHoughTransformMaxLineGap.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_HOUGH_TRANSFORM_MAX_LINE_GAP));

		textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_NARROW_SAMPLESTRIP));
		textfieldSampleDetectionBorderCorrectionBorderWidth.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERWIDTH));
		textfieldSampleDetectionBorderCorrectionBorderIntensityMin.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MIN));
		textfieldSampleDetectionBorderCorrectionBorderIntensityMax.setText(String.valueOf(SettingsController.CURRENT_SAMPLE_DETECTOR_BORDERCORRECTION_BORDERINTENSITY_MAX));

		textfieldCellDetectorMinBacteriaSize.setText(String.valueOf(SettingsController.CURRENT_CELL_DETECTOR_MIN_BACTERIA_SIZE));
		textfieldCellDetectorMaxBacteriaDistance.setText(String.valueOf(SettingsController.CURRENT_CELL_DETECTOR_MAX_CELL_DISTANCE));
		textfieldCellDetectorAngleDifference.setText(String.valueOf(SettingsController.CURRENT_CELL_DETECTOR_ANGLE_DIFFERENCE));	

		textfieldFeatureExtractorHistogramSteps.setText(String.valueOf(SettingsController.CURRENT_FEATURE_EXTRACTOR_LENGTH_HISTOGRAM_STEPS));
		textfieldFeatureExtractorBaseLengthWeight.setText(String.valueOf(SettingsController.CURRENT_FEATURE_EXTRACTOR_BASELENGTH_INTERVAL));
	}

	//----------------------------------------------------------------

	private class handlerApplySettings implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{		
			int valueImageProcessingGaussKernelSize = 0;
			int valueImageProcessingCannyThreshold = 0;
			int valueImageProcessingDilateKernelSize = 0;
			int valueImageProcessingLocalThresholdKernelSize = 0;
			int valueImageProcessingThresholdCondition = 0;
			int valueImageProcessingThresholdValue = 0;
			int valueImageProcessingGlobalThreshold = 0;

			int valueSampleDetectionHoughTransformRho = 0;
			double valueSampleDetectionHoughTransformTheta = 0;
			int valueSampleDetectionHoughTransformThreshold = 0;
			int valueSampleDetectionHoughTransformMinLineLength = 0;
			int valueSampleDetectionHoughTransformMaxLineGap = 0;
			int valueSampleDetectionBorderCorrectionNarrowSampleStrip = 0;
			int valueSampleDetectionBorderCorrectionBorderWidth = 0;
			int valueSampleDetectionBorderCorrectionIntensityMin = 0;
			int valueSampleDetectionBorderCorrectionIntensityMax = 0;

			int valueCellDetectorMinBacteriaSize = 0;
			int valueCellDetectorMaxBacteriaDistance = 0;
			int valueCellDetectorAngleDifference = 0;

			int valueFeatureExtractorHistogramSteps = 0;
			double valueFeatureExtractorIntervalWeight = 0;


			//---- Parse values
			try
			{
				valueImageProcessingGaussKernelSize = Integer.parseInt(textfieldImageProcessingGaussKernelSize.getText());
				valueImageProcessingCannyThreshold = Integer.parseInt(textfieldImageProcessingCannyThreshold.getText());
				valueImageProcessingDilateKernelSize = Integer.parseInt(textfieldImageProcessingDilateKernelSize.getText());
				valueImageProcessingLocalThresholdKernelSize = Integer.parseInt(textfieldImageProcessingLocalThresholdKernelSize.getText());
				valueImageProcessingThresholdCondition = Integer.parseInt(textfieldImageProcessingThresholdCondition.getText());
				valueImageProcessingThresholdValue = Integer.parseInt( textfieldImageProcessingThresholdValue.getText());
				valueImageProcessingGlobalThreshold = Integer.parseInt(textfieldImageProcessingGlobalThreshold.getText());

				valueSampleDetectionHoughTransformRho = Integer.parseInt(textfieldSampleDetectionHoughTransformRho.getText());
				valueSampleDetectionHoughTransformTheta = Double.parseDouble(textfieldSampleDetectionHoughTransformThetta.getText());
				valueSampleDetectionHoughTransformThreshold = Integer.parseInt(textfieldSampleDetectionHoughTransformThreshold.getText());
				valueSampleDetectionHoughTransformMinLineLength = Integer.parseInt(textfieldSampleDetectionHoughTransformMinLineLength.getText());
				valueSampleDetectionHoughTransformMaxLineGap = Integer.parseInt(textfieldSampleDetectionHoughTransformMaxLineGap.getText());
				valueSampleDetectionBorderCorrectionNarrowSampleStrip = Integer.parseInt(textfieldSampleDetectionBorderCorrectionNarrowSampleStrip.getText());
				valueSampleDetectionBorderCorrectionBorderWidth = Integer.parseInt(textfieldSampleDetectionBorderCorrectionBorderWidth.getText());
				valueSampleDetectionBorderCorrectionIntensityMin = Integer.parseInt(textfieldSampleDetectionBorderCorrectionBorderIntensityMin.getText());
				valueSampleDetectionBorderCorrectionIntensityMax = Integer.parseInt(textfieldSampleDetectionBorderCorrectionBorderIntensityMax.getText());

				valueCellDetectorMinBacteriaSize = Integer.parseInt(textfieldCellDetectorMinBacteriaSize.getText());
				valueCellDetectorMaxBacteriaDistance = Integer.parseInt(textfieldCellDetectorMaxBacteriaDistance.getText());
				valueCellDetectorAngleDifference = Integer.parseInt(textfieldCellDetectorAngleDifference.getText());

				valueFeatureExtractorHistogramSteps = Integer.parseInt(textfieldFeatureExtractorHistogramSteps.getText());
				valueFeatureExtractorIntervalWeight = Double.parseDouble(textfieldFeatureExtractorBaseLengthWeight.getText());
			}
			catch (Exception e)
			{
				//FIXME
				System.out.println("WRONG");
				return;
			}

			SettingsController.setImageProcessingGaussKernelSize(valueImageProcessingGaussKernelSize);
			SettingsController.setImageProcessingCannyEdgeThreshold(valueImageProcessingCannyThreshold);
			SettingsController.setImageProcessingDilateKernelSize(valueImageProcessingDilateKernelSize);
			SettingsController.setImageProcessingLocalThresholdKernelSize(valueImageProcessingLocalThresholdKernelSize);
			SettingsController.setImageProcessingLocalThresholdCondition(valueImageProcessingThresholdCondition);
			SettingsController.setImageProcessingLocalThresholdDestvalue(valueImageProcessingThresholdValue);
			SettingsController.setImageProcessingGlobalThreshold(valueImageProcessingGlobalThreshold);

			SettingsController.setSampleDetectorHoughTransformRho(valueSampleDetectionHoughTransformRho);
			SettingsController.setSampleDetectorHoughTransformThetta(valueSampleDetectionHoughTransformTheta);
			SettingsController.setSampleDetectorHoughTransformThreshold(valueSampleDetectionHoughTransformThreshold);
			SettingsController.setSampleDetectorHoughTransformMinLineLength(valueSampleDetectionHoughTransformMinLineLength);
			SettingsController.setSampleDetectorHoughTransformMaxLineGape(valueSampleDetectionHoughTransformMaxLineGap);

			SettingsController.setSampleDetectoBorderCorrectionNarrowSampleStrip(valueSampleDetectionBorderCorrectionNarrowSampleStrip);
			SettingsController.setSampleDetectoBorderCorrectionBorderWidth(valueSampleDetectionBorderCorrectionBorderWidth);
			SettingsController.setSampleDetectoBorderCorrectionIntensityMin(valueSampleDetectionBorderCorrectionIntensityMin);
			SettingsController.setSampleDetectoBorderCorrectionIntensityMax(valueSampleDetectionBorderCorrectionIntensityMax);

			SettingsController.setCellDetectorMinBacteriaSize(valueCellDetectorMinBacteriaSize);
			SettingsController.setCellDetectorMaxCellDistance(valueCellDetectorMaxBacteriaDistance);
			SettingsController.setCellDetectorAngleDifference(valueCellDetectorAngleDifference);

			SettingsController.setFeatureExtractorLenghtHistogramSteps(valueFeatureExtractorHistogramSteps);
			SettingsController.setFeatureExtractorBaseLengthInterval(valueFeatureExtractorIntervalWeight);
		}
	}

	private class handlerResetSettings implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			SettingsController.resetToDefault();
			setTextFieldValues();
		}	
	}

	private class handlerCloseForm implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			dialog.dispose();
		}

	}
}