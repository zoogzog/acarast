package com.acarast.andrey.gui.form;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.acarast.andrey.gui.panel.PanelImageView;

/**
 * The central panel. Used for displaying images, currently selected.
 * @author Andrey G
 */
public class FormMainPanelCenter
{
    private JPanel panel;

    private PanelImageView panelImageViewer;

    //----------------------------------------------------------------

    public FormMainPanelCenter (FormMainMouse controllerMouse)
    {
	panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.setBackground(FormStyle.COLOR_MAIN);

	createPanel(controllerMouse);
    }

    public JPanel get ()
    {
	return panel;
    }

    //----------------------------------------------------------------

    private void createPanel (FormMainMouse controllerMouse)
    {
	JPanel ImageViewPanel = new JPanel();
	ImageViewPanel.setBackground(Color.darkGray);
	ImageViewPanel.setLayout(new BorderLayout());
	ImageViewPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

	JPanel panelMainImageViewer= new JPanel();
	panelMainImageViewer.setBackground(Color.darkGray);
	panelMainImageViewer.setLayout(new BorderLayout());
	ImageViewPanel.add(panelMainImageViewer, BorderLayout.CENTER);

	panelImageViewer = new PanelImageView();
	panelImageViewer.setBackground(FormStyle.COLOR_MAIN);
	panelImageViewer.addMouseMotionListener(controllerMouse);
	panelImageViewer.addMouseWheelListener(controllerMouse);
	panelImageViewer.addMouseListener(controllerMouse);
	ImageViewPanel.add(panelImageViewer);

	panel.add(ImageViewPanel, BorderLayout.CENTER);
    }

    //----------------------------------------------------------------

    public PanelImageView getComponentPanelImageView ()
    {
	return panelImageViewer;
    }

    /**
     * Resets this panel to default.
     */
    public void reset ()
    {
    	panelImageViewer.resetImagePosition();
    	panelImageViewer.freeImage();
    }
}
