package com.acarast.andrey.gui.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.acarast.andrey.core.algorithm.EnumTypes;

/**
 * Class helper for the FormMain class. Sets up the components for displaying 
 * in the left panel of the main window of the application
 * @author Andrey G
 */
public class FormMainPanelLeft
{
	private JPanel panel;

	private JComboBox <String> comboboxFileName;
	private JComboBox <String> comboboxSampleName;
	private JComboBox <String> comboboxBacteriaTypeName;
	private JComboBox <String> comboboxDrugTypeName;
	private JComboBox <String> comboboxEstimator;

	private JComboBox <String> comboboxAlgorithm;

	//---- Radio buttons for selecting sample detection mode: manual or auto and buttons for adding samples
	public JRadioButton sampleRadioModeAuto;
	public JRadioButton sampleRadioModeManual;

	public JButton buttonSampleAddControl;
	public JButton buttonSampleAdd;
	public JButton buttonSampleDelete;

	//---- Textfield elements to display number of the input samples and the index of the control sample
	public JTextField textfieldSamplesCount;
	public JTextField textfieldControlSample;

	private JRadioButton estimatorSwitchON;
	private JRadioButton estimatorSwitchOFF;
	private JRadioButton estimatorScanFileName;
	private JRadioButton estimatorManualSettings;


	//----------------------------------------------------------------

	public FormMainPanelLeft (FormMainHandler controllerButton)
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
		componentProjectFiles(controllerButton);
		componentSampleManager(controllerButton);
		componentSettingsProcessing(controllerButton);
		componentSettingsEstimator(controllerButton);
	}

	//----------------------------------------------------------------
	/**
	 * This panel displays two comboboxes. One for all files in the project, while the other one 
	 * shows all samples (if detection has been performed) for the currently selected file. 
	 * @param controllerButton
	 */
	private void componentProjectFiles (FormMainHandler controllerButton)
	{
		JPanel displayPanel = new JPanel();

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 90));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Label project files
		JLabel labelProject = new JLabel(" Project files");
		labelProject.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.gridwidth = 2;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelProject, layoutConstraits);

		//---- Label Image
		JLabel labelImage = new JLabel(" Image:");
		labelImage.setFont(FormStyle.DEFAULT_FONT);
		labelImage.setBorder(new EmptyBorder(0, 0, 0, 2));
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.gridwidth = 1;
		layoutConstraits.weightx = 0.2;
		displayPanel.add(labelImage, layoutConstraits);

		//---- Combo box for displaying all files int the current project
		comboboxFileName = new JComboBox <String> ();
		comboboxFileName.setFont(FormStyle.DEFAULT_FONT);
		comboboxFileName.addActionListener(controllerButton);
		comboboxFileName.setSize(new Dimension(120, 25));
		comboboxFileName.setPreferredSize(comboboxFileName.getSize());
		comboboxFileName.setActionCommand(FormMainHandlerCommands.AC_COMBOBOX_IMAGECHANGED);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 1;
		layoutConstraits.weighty = 0.2;
		comboboxFileName.addActionListener(controllerButton);
		displayPanel.add(comboboxFileName, layoutConstraits);

		ImageIcon iconButton = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_DELETE_FILE);
		JButton buttonDeleteFile = new JButton(iconButton);
		buttonDeleteFile.addActionListener (null);
		buttonDeleteFile.setSize(30, 30);
		buttonDeleteFile.setPreferredSize(new Dimension(30, 30));
		buttonDeleteFile.setToolTipText("Delete file");
		buttonDeleteFile.addActionListener(controllerButton);
		buttonDeleteFile.setActionCommand(FormMainHandlerCommands.AC_REMOVEFILE);
		buttonDeleteFile.setBackground(FormStyle.COLOR_TOOLBAR);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 1;
		layoutConstraits.weighty = 0.2;
		displayPanel.add(buttonDeleteFile, layoutConstraits);

		//---- Label Sample
		JLabel labelViewSample = new JLabel(" Sample:");
		labelViewSample.setFont(FormStyle.DEFAULT_FONT);
		labelViewSample.setBorder(new EmptyBorder(0, 0, 0, 10));
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 2;
		displayPanel.add(labelViewSample, layoutConstraits);

		//---- Combo box for displaying all samples for the currently selected file
		comboboxSampleName = new JComboBox <String> ();
		comboboxSampleName.setFont(FormStyle.DEFAULT_FONT);
		comboboxSampleName.setEnabled(false);
		comboboxSampleName.addActionListener(controllerButton);
		comboboxSampleName.setSize(new Dimension(120, 25));
		comboboxSampleName.setPreferredSize(comboboxSampleName.getSize());
		comboboxSampleName.setActionCommand(FormMainHandlerCommands.AC_COMBOBOX_SAMPLECHANGED);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 2;
		comboboxSampleName.addActionListener(controllerButton);
		displayPanel.add(comboboxSampleName, layoutConstraits);

		panel.add(displayPanel);
	}

	//----------------------------------------------------------------
	/**
	 * This panel is for specifying sample detection method and provides an interface for manual 
	 * adding control and test samples, deleting samples.
	 * @param controllerButton
	 */
	private void componentSampleManager (FormMainHandler controllerButton)
	{
		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 105));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Panel for sample detection mode (auto or manual)
		JPanel managerPanelMode = new JPanel();
		componentSampleManagerMode(controllerButton, managerPanelMode);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.anchor = GridBagConstraints.PAGE_START;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(managerPanelMode, layoutConstraits);

		//---- Panel for sample location (coordinates position X, Y, size)
		JPanel managerPanelLocation = new JPanel();
		componentSampleManagerLocation (controllerButton, managerPanelLocation);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.anchor = GridBagConstraints.CENTER;
		layoutConstraits.fill = GridBagConstraints.NONE;
		//displayPanel.add(managerPanelLocation, layoutConstraits);

		//---- Panel for buttons (add sample, delete sample etc)
		JPanel managerPanelButtons = new JPanel();
		componentSampleManagerButtons (controllerButton, managerPanelButtons);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 2;
		layoutConstraits.anchor = GridBagConstraints.CENTER;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(managerPanelButtons, layoutConstraits);

		panel.add(displayPanel);
	}

	private void componentSampleManagerMode (FormMainHandler controllerButton, JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 62));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Label for sample detection method
		JLabel labelSampleMethod = new JLabel("Sample detection method");
		labelSampleMethod.setSize(20, 20);
		labelSampleMethod.setLocation(10, 70);
		labelSampleMethod.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.gridwidth = 2;
		displayPanel.add(labelSampleMethod, layoutConstraits);

		//---- Radio button sample detection method AUTO
		sampleRadioModeAuto = new JRadioButton("Auto");
		sampleRadioModeAuto.setFont(FormStyle.DEFAULT_FONT);
		sampleRadioModeAuto.setActionCommand(FormMainHandlerCommands.AC_CHANGED_SAMPLEDETECTIONMODE);
		sampleRadioModeAuto.addActionListener(controllerButton);
		sampleRadioModeAuto.setSelected(true);
		sampleRadioModeAuto.setSize(100, 14);
		sampleRadioModeAuto.setBackground(Color.lightGray);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.gridwidth = 1;
		sampleRadioModeAuto.addActionListener(controllerButton);
		displayPanel.add(sampleRadioModeAuto, layoutConstraits);

		//---- Radio button sample detection method MANUAL
		sampleRadioModeManual = new JRadioButton("Manual");
		sampleRadioModeManual.setFont(FormStyle.DEFAULT_FONT);
		sampleRadioModeManual.setActionCommand(FormMainHandlerCommands.AC_CHANGED_SAMPLEDETECTIONMODE);
		sampleRadioModeManual.addActionListener(controllerButton);
		sampleRadioModeManual.setSelected(false);
		sampleRadioModeManual.setSize(100, 14);
		sampleRadioModeManual.setBackground(Color.lightGray);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		sampleRadioModeManual.addActionListener(controllerButton);
		displayPanel.add(sampleRadioModeManual, layoutConstraits);

		//---- Linking 2 radio buttons together
		ButtonGroup SmplExtrctMthGroup = new ButtonGroup();
		SmplExtrctMthGroup.add(sampleRadioModeManual);
		SmplExtrctMthGroup.add(sampleRadioModeAuto);
	}

	private void componentSampleManagerLocation (FormMainHandler controllerButton, JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 55));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Sample coordinate X label
		JLabel labelX = new JLabel("X:");
		labelX.setSize(20, 20);
		labelX.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.weighty = 0.1;
		layoutConstraits.weightx = 0.1;
		displayPanel.add (labelX, layoutConstraits);

		//---- Sample coordinate X text field
		JTextField sampleTXLocationX = new JTextField(4);
		sampleTXLocationX.setFont(new java.awt.Font("Times New Roman", 0, 14));
		sampleTXLocationX.setLocation(30, 70);
		sampleTXLocationX.setSize(45, 20);
		sampleTXLocationX.setEditable(false);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add (sampleTXLocationX, layoutConstraits);

		//---- Sample coordinate Y label
		JLabel labelY= new JLabel("Y:");
		labelY.setSize(20, 20);
		labelY.setLocation(80, 70);
		labelY.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add (labelY, layoutConstraits);

		//---- Sample coordinate Y text field
		JTextField sampleTXLocationY = new JTextField(4);
		sampleTXLocationY.setLocation(100, 70);
		sampleTXLocationY.setSize(45, 20);
		sampleTXLocationY.setEditable(false);
		sampleTXLocationY.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 3;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add (sampleTXLocationY, layoutConstraits);

		//---- Sample width label (2-nd row)
		JLabel labelW = new JLabel("W:");
		labelW.setSize(20, 20);
		labelW.setLocation(10, 95);
		labelW.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(labelW, layoutConstraits);

		//---- Sample width text field
		JTextField sampleTXLocationWidth = new JTextField(4);
		sampleTXLocationWidth.setLocation(30, 95);
		sampleTXLocationWidth.setSize(45, 20);
		sampleTXLocationWidth.setEditable(false);
		sampleTXLocationWidth.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(sampleTXLocationWidth, layoutConstraits);

		//---- Sample height label
		JLabel labelH = new JLabel("H:");
		labelH.setSize(20, 20);
		labelH.setLocation(80, 95);
		labelH.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(labelH, layoutConstraits);

		//---- Sample height text field 
		JTextField sampleTXLocationHeight = new JTextField(4);
		sampleTXLocationHeight.setLocation(100, 95);
		sampleTXLocationHeight.setSize(45, 20);
		sampleTXLocationHeight.setEditable(false);
		sampleTXLocationHeight.setFont(FormStyle.DEFAULT_FONT);
		layoutConstraits.gridx = 3;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(sampleTXLocationHeight, layoutConstraits);
	}

	private void componentSampleManagerButtons (FormMainHandler controllerButton, JPanel displayPanel)
	{
		Dimension DEFAULT_BUTTON_DIM = new Dimension(46, 30);

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 40));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		ImageIcon iconButtonAddControl = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SAMPLE_ADD_CONTROL);
		ImageIcon iconButtonAdd = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SAMPLE_ADD);
		ImageIcon iconButtonDelete = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICO_SAMPLE_DELETE);

		//---- Button add sample as a control sample
		buttonSampleAddControl = new JButton(iconButtonAddControl);
		buttonSampleAddControl.setMinimumSize(DEFAULT_BUTTON_DIM);
		buttonSampleAddControl.setMaximumSize(DEFAULT_BUTTON_DIM);
		buttonSampleAddControl.setFont(FormStyle.DEFAULT_FONT);
		buttonSampleAddControl.setEnabled(false);
		buttonSampleAddControl.addActionListener(controllerButton);
		buttonSampleAddControl.setToolTipText("Add control sample");
		buttonSampleAddControl.setActionCommand(FormMainHandlerCommands.AC_MANUAL_ADDCONTROL);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.weightx = 0.5;
		displayPanel.add(buttonSampleAddControl, layoutConstraits);

		//---- Button add sample as an ordinary sample
		buttonSampleAdd = new JButton(iconButtonAdd);
		buttonSampleAdd.setMinimumSize(DEFAULT_BUTTON_DIM);
		buttonSampleAdd.setMaximumSize(DEFAULT_BUTTON_DIM);
		buttonSampleAdd.setFont(FormStyle.DEFAULT_FONT);
		buttonSampleAdd.setEnabled(false);
		buttonSampleAdd.addActionListener(controllerButton);
		buttonSampleAdd.setToolTipText("Add sample");
		buttonSampleAdd.setActionCommand(FormMainHandlerCommands.AC_MANUAL_ADDSAMPLE);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(buttonSampleAdd, layoutConstraits);

		//---- Button delete sample from the sample list
		buttonSampleDelete= new JButton(iconButtonDelete);
		buttonSampleDelete.setMinimumSize(DEFAULT_BUTTON_DIM);
		buttonSampleDelete.setMaximumSize(DEFAULT_BUTTON_DIM);
		buttonSampleDelete.setFont(FormStyle.DEFAULT_FONT);
		buttonSampleDelete.setEnabled(false);
		buttonSampleDelete.addActionListener(controllerButton);
		buttonSampleDelete.setToolTipText("Delete sample");
		buttonSampleDelete.setActionCommand(FormMainHandlerCommands.AC_MANUAL_DELETESAMPLE);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(buttonSampleDelete, layoutConstraits);
	}

	//----------------------------------------------------------------
	/**
	 * Panel for providing interface for setting processing settings (number of channels, 
	 * index of the control channel and other parameters.
	 * @param controllerButton
	 */
	private void componentSettingsProcessing (FormMainHandler controllerButton)
	{
		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 85));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Cell detection task input parameters. Parameter: amount of samples
		JPanel panelParameterSamplesAmount = new JPanel();
		componentTaskParametersSampleCount(controllerButton, panelParameterSamplesAmount);		
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.weighty = 0.1;
		displayPanel.add(panelParameterSamplesAmount, layoutConstraits);

		//---- Cell detection task input parameters. Parameter: control sample index
		JPanel panelParameterControlSample = new JPanel();
		componentTaskParametersControlSample(controllerButton, panelParameterControlSample);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(panelParameterControlSample, layoutConstraits);

		JPanel panelParametersAlgorithm = new JPanel();
		componentTaskParametersAlgorithm(controllerButton, panelParametersAlgorithm);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 2;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(panelParametersAlgorithm, layoutConstraits);

		panel.add(displayPanel);
	}

	private void componentTaskParametersSampleCount (FormMainHandler controllerButton, JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (190, 25));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Parameter info label
		JLabel labelSamplesAmount = new JLabel ("Samples count: ");
		labelSamplesAmount.setFont(FormStyle.DEFAULT_FONT);
		labelSamplesAmount.setPreferredSize(new Dimension(120, 20));
		labelSamplesAmount.setSize(new Dimension(120, 20));
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(labelSamplesAmount, layoutConstraits);

		//---- Parameter textfield
		textfieldSamplesCount = new JTextField("4");
		textfieldSamplesCount.setPreferredSize(new Dimension(70, 20));
		textfieldSamplesCount.setSize(new Dimension(70, 20));
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.weightx = 0.1;
		displayPanel.add(textfieldSamplesCount, layoutConstraits);
	}

	private void componentTaskParametersControlSample (FormMainHandler controllerButton, JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (190, 25));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Parameter info label
		JLabel labelControlSample = new JLabel ("Control sample: ");
		labelControlSample.setFont(FormStyle.DEFAULT_FONT);
		labelControlSample.setPreferredSize(new Dimension(120, 20));
		labelControlSample.setSize(new Dimension(120, 20));
		labelControlSample.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(labelControlSample, layoutConstraits);

		//---- Parameter textfield
		textfieldControlSample = new JTextField("1");
		textfieldControlSample.setPreferredSize(new Dimension(70, 20));
		textfieldControlSample.setSize(new Dimension(70, 20));
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		layoutConstraits.weightx = 0.1;
		displayPanel.add(textfieldControlSample, layoutConstraits);
	}

	private void componentTaskParametersAlgorithm (FormMainHandler controllerButton, JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (220, 27));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---- Parameter algorithm label
		JLabel labelAlgorithm = new JLabel ("Algorithm: ");
		labelAlgorithm.setFont(FormStyle.DEFAULT_FONT);
		labelAlgorithm.setPreferredSize(new Dimension(70, 20));
		labelAlgorithm.setSize(new Dimension(70, 20));
		labelAlgorithm.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(labelAlgorithm, layoutConstraits);
		
		//---- Algorithm combobox 
		comboboxAlgorithm = new JComboBox<String>();
		comboboxAlgorithm.setFont(FormStyle.DEFAULT_FONT);
		comboboxAlgorithm.setSize(new Dimension(130, 25));
		comboboxAlgorithm.setPreferredSize(comboboxFileName.getSize());
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.fill = GridBagConstraints.NONE;
		displayPanel.add(comboboxAlgorithm, layoutConstraits);
		
		for (int i = 0; i < EnumTypes.AlgorithmTypeName.length; i++)
		{
			comboboxAlgorithm.addItem(EnumTypes.AlgorithmTypeName[i]);
		}
	}

	//----------------------------------------------------------------
	/**
	 * Panel for setting up parameters for estimating sensitivity of samples with SVM/criteria.
	 * @param controllerButton
	 */
	private void componentSettingsEstimator (FormMainHandler controllerButton)
	{
		JPanel displayPanel = new JPanel();

		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (240, 150));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraits = new GridBagConstraints();

		//---------------------------------------------
		//----- Estimator switcher
		JLabel labelEstimatorSwitcher = new JLabel(" Estimator");
		labelEstimatorSwitcher.setFont(FormStyle.DEFAULT_FONT);
		labelEstimatorSwitcher.setPreferredSize(new Dimension(100, 20));
		labelEstimatorSwitcher.setSize(new Dimension(100, 20));
		labelEstimatorSwitcher.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 0;
		layoutConstraits.weighty = 0.2;
		layoutConstraits.weightx = 0.2;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelEstimatorSwitcher, layoutConstraits);

		estimatorSwitchON = new JRadioButton("ON");
		estimatorSwitchON.setBackground(FormStyle.COLOR_MENU);
		estimatorSwitchON.setSelected(false);
		estimatorSwitchON.addActionListener(controllerButton);
		estimatorSwitchON.setActionCommand(FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_ON);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 0;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(estimatorSwitchON, layoutConstraits);

		estimatorSwitchOFF = new JRadioButton("OFF");
		estimatorSwitchOFF.setBackground(FormStyle.COLOR_MENU);
		estimatorSwitchOFF.setSelected(true);
		estimatorSwitchOFF.addActionListener(controllerButton);
		estimatorSwitchOFF.setActionCommand(FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_OFF);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 0;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(estimatorSwitchOFF, layoutConstraits);

		ButtonGroup estimatorSwitcherGroup = new ButtonGroup();
		estimatorSwitcherGroup.add(estimatorSwitchON);
		estimatorSwitcherGroup.add(estimatorSwitchOFF);

		//---------------------------------------------

		JLabel labelEstimatorSettings = new JLabel(" Settings");
		labelEstimatorSettings.setFont(FormStyle.DEFAULT_FONT);
		labelEstimatorSettings.setPreferredSize(new Dimension(100, 20));
		labelEstimatorSettings.setSize(new Dimension(100, 20));
		labelEstimatorSettings.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 1;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelEstimatorSettings, layoutConstraits);

		estimatorScanFileName = new JRadioButton("File");
		estimatorScanFileName.setBackground(FormStyle.COLOR_MENU);
		estimatorScanFileName.setSelected(true);
		estimatorScanFileName.setEnabled(false);
		estimatorScanFileName.addActionListener(controllerButton);
		estimatorScanFileName.setActionCommand(FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_FILE);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 1;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(estimatorScanFileName, layoutConstraits);

		estimatorManualSettings = new JRadioButton("Manual");
		estimatorManualSettings.setBackground(FormStyle.COLOR_MENU);
		estimatorManualSettings.setSelected(false);
		estimatorManualSettings.setEnabled(false);
		estimatorManualSettings.addActionListener(controllerButton);
		estimatorManualSettings.setActionCommand(FormMainHandlerCommands.AC_CHANGED_ESTIMATOR_MANUAL);
		layoutConstraits.gridx = 2;
		layoutConstraits.gridy = 1;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(estimatorManualSettings, layoutConstraits);

		ButtonGroup estimatorAlgorithmGroup = new ButtonGroup();
		estimatorAlgorithmGroup.add(estimatorScanFileName);
		estimatorAlgorithmGroup.add(estimatorManualSettings);


		//---------------------------------------------


		JLabel labelBacteriaType = new JLabel (" Bacteria type:");
		labelBacteriaType.setFont(FormStyle.DEFAULT_FONT);
		labelBacteriaType.setPreferredSize(new Dimension(100, 20));
		labelBacteriaType.setSize(new Dimension(100, 20));
		labelBacteriaType.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 2;
		layoutConstraits.anchor = GridBagConstraints.LINE_START;
		displayPanel.add(labelBacteriaType, layoutConstraits);

		comboboxBacteriaTypeName = new JComboBox<String>();
		comboboxBacteriaTypeName.setFont(FormStyle.DEFAULT_FONT);
		comboboxBacteriaTypeName.addActionListener(controllerButton);
		comboboxBacteriaTypeName.setSize(new Dimension(120, 25));
		comboboxBacteriaTypeName.setPreferredSize(comboboxFileName.getSize());
		comboboxBacteriaTypeName.setEnabled(false);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 2;
		layoutConstraits.weighty = 0.2;
		layoutConstraits.gridwidth = 2;
		displayPanel.add(comboboxBacteriaTypeName, layoutConstraits);

		for (int i = 0; i < EnumTypes.BacteriaTypeName.length; i++)
		{
			comboboxBacteriaTypeName.addItem(EnumTypes.BacteriaTypeName[i]);
		}

		JLabel labelDrugType = new JLabel (" Drug type:");
		labelDrugType.setFont(FormStyle.DEFAULT_FONT);
		labelDrugType.setPreferredSize(new Dimension(100, 20));
		labelDrugType.setSize(new Dimension(100, 20));
		labelBacteriaType.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 3;
		layoutConstraits.gridwidth = 1;
		displayPanel.add(labelDrugType, layoutConstraits);

		comboboxDrugTypeName = new JComboBox<String>();
		comboboxDrugTypeName.setFont(FormStyle.DEFAULT_FONT);
		comboboxDrugTypeName.addActionListener(controllerButton);
		comboboxDrugTypeName.setSize(new Dimension(120, 25));
		comboboxDrugTypeName.setPreferredSize(comboboxFileName.getSize());
		comboboxDrugTypeName.setEnabled(false);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 3;
		layoutConstraits.weighty = 0.2;
		layoutConstraits.gridwidth = 2;
		displayPanel.add(comboboxDrugTypeName, layoutConstraits);

		for (int i = 0; i < EnumTypes.DrugTypeName.length; i++)
		{
			comboboxDrugTypeName.addItem(EnumTypes.DrugTypeName[i]);
		}

		JLabel labelEstimatorType = new JLabel (" Estimator:");
		labelEstimatorType.setFont(FormStyle.DEFAULT_FONT);
		labelEstimatorType.setPreferredSize(new Dimension(100, 20));
		labelEstimatorType.setSize(new Dimension(100, 20));
		labelEstimatorType.setBackground(Color.white);
		layoutConstraits.gridx = 0;
		layoutConstraits.gridy = 4;
		layoutConstraits.gridwidth = 1;
		displayPanel.add(labelEstimatorType, layoutConstraits);

		comboboxEstimator = new JComboBox<String>();
		comboboxEstimator.setFont(FormStyle.DEFAULT_FONT);
		comboboxEstimator.addActionListener(controllerButton);
		comboboxEstimator.setSize(new Dimension(120, 25));
		comboboxEstimator.setPreferredSize(comboboxFileName.getSize());
		comboboxEstimator.setEnabled(false);
		layoutConstraits.gridx = 1;
		layoutConstraits.gridy = 4;
		layoutConstraits.weighty = 0.2;
		layoutConstraits.gridwidth = 2;
		displayPanel.add(comboboxEstimator, layoutConstraits);

		comboboxEstimator.addItem("Manual");
		comboboxEstimator.addItem("SVM");

		panel.add(displayPanel);
	}

	//----------------------------------------------------------------
	//---- GET COMPONENT (these methods return components of this panel)
	//----------------------------------------------------------------

	public JComboBox <String> getComponentComboboxFileName ()
	{
		return comboboxFileName;
	}

	public JComboBox <String> getComponentComboboxSampleName ()
	{ 
		return comboboxSampleName;
	}

	public JRadioButton getComponentRadioModeAuto()
	{
		return sampleRadioModeAuto;
	}

	public JRadioButton getComponentRadioModeManual()
	{
		return sampleRadioModeManual;
	}

	public JButton getComponentSampleAdd()
	{
		return buttonSampleAdd;
	}

	public JButton getComponentSampleAddControl()
	{
		return buttonSampleAddControl;
	}

	public JButton getComponentSampleDelete()
	{
		return buttonSampleDelete;
	}

	public JTextField getComponentTextfieldSamplesCount()
	{
		return textfieldSamplesCount;
	}

	public JTextField getComponentTextfieldControlSample()
	{
		return textfieldControlSample;
	}

	public JComboBox <String> getComponentComboboxAlgorithmType ()
	{
		return comboboxAlgorithm;
	}
	
	//----------------------------------------------------------------
	//---- Estimator

 	public JRadioButton getComponentRadioEstimatorON ()
	{
		return estimatorSwitchON;
	}

	public JRadioButton getComponentRadioEstimatorOFF ()
	{
		return estimatorSwitchOFF;
	}

	public JRadioButton getComponentRadioEstimatorSetFile ()
	{
		return estimatorScanFileName;
	}

	public JRadioButton getComponentRadioEstimatorSetManual ()
	{
		return estimatorManualSettings;
	}

	public JComboBox<String> getCombobobxEstimatorBacteriaType ()
	{
		return comboboxBacteriaTypeName;
	}

	public JComboBox<String> getComboboxEstimatorDrugType ()
	{
		return comboboxDrugTypeName;
	}

	public JComboBox<String> getComboboxEstimatorAlgorithmType ()
	{
		return comboboxEstimator;
	}

	//----------------------------------------------------------------

	public void reset ()
	{
		comboboxFileName.removeAllItems();
		comboboxSampleName.removeAllItems();

		textfieldSamplesCount.setText("4");
		textfieldControlSample.setText("1");
	}

}
