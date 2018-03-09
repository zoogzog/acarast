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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * Form to display information about this application.
 * @author Andrey G
 */
public class FormAbout implements ActionListener
{
	public static final String DEFAULT_APPLICATION_TITLE = "ACA-RaST";
	private static final String DEFAULT_VERSION = "v.2.0";
	private static final String DEFAULT_BUILD_DATE = "27.11.2017";
	private static final String DEFAULT_DEVELOPER1 = "Andrey G";
	private static final String DEFAULT_DEVELOPER2 = "Kikuchi K";

	public static final String HTML_BUILD_STRING = "<html>"+DEFAULT_APPLICATION_TITLE+"<br><br>Version: " + DEFAULT_VERSION + "<br>"
			+ "Build date: " + DEFAULT_BUILD_DATE + "<br>Developer information<br>*" + DEFAULT_DEVELOPER1 + "<br>*"
					+ DEFAULT_DEVELOPER2 +"<br></html>";

	//----------------------------------------------------------------

	private JDialog dialog;

	//----------------------------------------------------------------

	public void displayFrame(JFrame parentFrame) 
	{
		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		componentMainPanel(panelMain);

		dialog = new JDialog(parentFrame, "About");
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
		displayPanel.setSize(new Dimension (280, 180));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		JPanel panelAppInfo = new JPanel();
		componentAppInfo(panelAppInfo);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelAppInfo, layoutConstraints);

		JPanel panelButtons = new JPanel();
		componentButtons(panelButtons);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 1;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelButtons, layoutConstraints);

	}

	//----------------------------------------------------------------

	private void componentAppInfo (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new GridBagLayout());
		displayPanel.setSize(new Dimension (280, 150));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		GridBagConstraints layoutConstraints = new GridBagConstraints();

		JPanel panelGraphics = new JPanel();
		componentAppInfoGraphics(panelGraphics);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelGraphics, layoutConstraints);

		JPanel panelText = new JPanel();
		componentAppInfoText(panelText);
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.fill = GridBagConstraints.NONE;
		displayPanel.add(panelText, layoutConstraints);
	}

	private void componentAppInfoGraphics (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setSize(new Dimension (100, 150));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());

		ImageIcon icon = FormUtils.getIconResource(FormStyle.RESOURCE_PATH_ICON);

		JLabel iconLabel = new JLabel(icon);
		displayPanel.add(iconLabel);

	}

	private void componentAppInfoText (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setSize(new Dimension (170, 150));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

		JLabel textLabel = new JLabel(HTML_BUILD_STRING);
		textLabel.setFont(FormStyle.DEFAULT_FONT);	
		displayPanel.add(textLabel);

		displayPanel.add(Box.createVerticalGlue());
	}

	//----------------------------------------------------------------

	private void componentButtons (JPanel displayPanel)
	{
		displayPanel.setBackground(FormStyle.COLOR_MENU);
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
		displayPanel.setSize(new Dimension (280, 30));
		displayPanel.setMinimumSize(displayPanel.getSize());
		displayPanel.setMaximumSize(displayPanel.getSize());
		displayPanel.setPreferredSize(displayPanel.getSize());

		displayPanel.add(Box.createHorizontalGlue());

		JButton buttonClose = new JButton("OK");
		buttonClose.setSize(50, 30);
		buttonClose.setFont(FormStyle.DEFAULT_FONT);
		buttonClose.addActionListener(this);
		displayPanel.add(buttonClose);

		displayPanel.add(Box.createRigidArea(new Dimension(5,0)));
	}

	//----------------------------------------------------------------

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		dialog.dispose();
	}

}