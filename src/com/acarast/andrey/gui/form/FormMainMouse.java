package com.acarast.andrey.gui.form;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.acarast.andrey.controller.DataController;

/**
 * Mouse movement handler for the main GUI form of the application.
 * @author Andrey G
 *
 */
public class FormMainMouse implements MouseMotionListener, MouseWheelListener, MouseListener
{
    public final static double DEFAULT_ZOOM_DELTA = 0.05;
    public final static double DEFAULT_ZOOM_MAX = 5;
    public final static double DEFAULT_ZOOM_MIN = 0.1;
    
    //----------------------------------------------------------------
    
    //---- Form to control 
    private FormMain mainFormLink;

    private Point pointStart = new Point(-1, -1);
    private Point handlerMouseListenerPoint = new Point(-1, -1);

    public static boolean isSampleSelectOn;
    public static boolean isSampleMovable;
    
    //---- Current scale of the displayed image
    public static double imageViewZoomScale = 1.0;
    
    //----------------------------------------------------------------
    
    public FormMainMouse ()
    {
	
    }
    
    public void init (FormMain mainForm)
    {
	mainFormLink = mainForm;
    }

    //----------------------------------------------------------------

    @Override
    /**
     * Handler for mouse dragging motion. If currently sample selection is turned on, then
     * allow user to chose the area of the sample. If sample selection is turned off, but moving
     * sample is turned on, then move the sample. Otherwise, move the entire image.
     */
    public void mouseDragged(MouseEvent e) 
    {
	if (!isSampleSelectOn)
	{
	    //---- Check if we need to move image or selected sample
	    if (!isSampleMovable)
	    {
		//---- Move the entire image
		Point end = e.getPoint();

		int moveX = end.x - handlerMouseListenerPoint.x;
		int moveY = end.y - handlerMouseListenerPoint.y;

		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformMove(moveX, moveY);
		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();

		handlerMouseListenerPoint = end;
	    }
	    else
	    {
		//---- Move only sample
		Point end = e.getPoint();

		Point realPointStart = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(pointStart);
		Point realPointEnd = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(end);

		int deltaX = realPointEnd.x - realPointStart.x;
		int deltaY = realPointEnd.y - realPointStart.y;

		if (mainFormLink.getComponentPanelCenter().getComponentPanelImageView().isInPolygon(realPointStart.x, realPointStart.y))
		{
		    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().setCursor(new Cursor(Cursor.HAND_CURSOR));	
		    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformMovePolygon(deltaX, deltaY);
		    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();
		}

		pointStart = end;	
	    }
	}
	else
	{
	    //---- Display sample area which is being selected
	    Point end = e.getPoint();

	    Point realPointStart = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(pointStart);
	    Point realPointEnd = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(end);

	    Point[] sampleSelectBox = new Point[4]; 
	    sampleSelectBox[0] = new Point (realPointStart.x, realPointStart.y);
	    sampleSelectBox[1] = new Point (realPointStart.x, realPointEnd.y);
	    sampleSelectBox[2] = new Point (realPointEnd.x, realPointEnd.y);
	    sampleSelectBox[3] = new Point (realPointEnd.x, realPointStart.y);

	    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().displayPolygon(sampleSelectBox);
	    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();
	}
    }

    @Override
    /**
     * Responds to mouse moving. Displays coordinates of the mouse pointer, changes cursors if it is in the are 
     * of a movable sample.
     */
    public void mouseMoved(MouseEvent e) 
    {
	pointStart = e.getPoint();

	//---- Display coordinates of the mouse mosition in the down panel of the application
	String pointX = String.valueOf(mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(e.getPoint()).x);
	String pointY = String.valueOf(mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(e.getPoint()).y);
	
	mainFormLink.getComponentPanelDown().getComponentLabelX().setText(pointX);
	mainFormLink.getComponentPanelDown().getComponentLabelY().setText(pointY);

	if (isSampleMovable && !isSampleSelectOn)
	{
	    Point pointToCheck = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().getRealPoint(pointStart);

	    if (mainFormLink.getComponentPanelCenter().getComponentPanelImageView().isInPolygon(pointToCheck.x, pointToCheck.y))
	    {
		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().setCursor(new Cursor(Cursor.HAND_CURSOR));
	    }
	    else
	    {
		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	    }
	}

    }

    @Override
    /**
     * Responds to moving the mouse wheel. Zooms out or in the displayed picture.
     */
    public void mouseWheelMoved(MouseWheelEvent e) 
    {
	int mouseWheelDirection = e.getWheelRotation();

	if (mouseWheelDirection < 0)
	{
	    int tableSize = DataController.getTable().getTableSize();
	    
	    //---- ZoomOut
	    if (tableSize != 0)
	    {
		if (imageViewZoomScale - DEFAULT_ZOOM_DELTA > DEFAULT_ZOOM_MIN)
		{
		    double exportZoom = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformZoom(-DEFAULT_ZOOM_DELTA);
		    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();

		    imageViewZoomScale = exportZoom;
		    
		    mainFormLink.getComponentPanelDown().getComponentLabelZoom().setText(String.valueOf((double)Math.round(exportZoom * 100) / 100));
		}
	    }
	}
	else
	{
	    int tableSize = DataController.getTable().getTableSize();
	    
	    //---- ZoomIn
	    if (tableSize != 0)
	    {
		if (imageViewZoomScale + DEFAULT_ZOOM_DELTA < DEFAULT_ZOOM_MAX)
		{
		    double exportZoom = mainFormLink.getComponentPanelCenter().getComponentPanelImageView().transformZoom(+DEFAULT_ZOOM_DELTA);
		    mainFormLink.getComponentPanelCenter().getComponentPanelImageView().repaint();

		    imageViewZoomScale = exportZoom;

		    mainFormLink.getComponentPanelDown().getComponentLabelZoom().setText(String.valueOf((double)Math.round(exportZoom * 100) / 100));
		}
	    }
	}
    }

    //----------------------------------------------------------------
    
    @Override
    public void mouseClicked(MouseEvent e) 
    {

    }

    @Override 
    public void mouseEntered(MouseEvent e) 
    {

    }

    @Override
    public void mouseExited(MouseEvent e) 
    { 

    }

    //----------------------------------------------------------------
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
	if (e.getButton() == MouseEvent.BUTTON1) 
	{
	    int tableSize = DataController.getTable().getTableSize();
	    
	    if (tableSize != 0 && !isSampleMovable && !isSampleSelectOn)
	    {
		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().setCursor(new Cursor(Cursor.MOVE_CURSOR)); 

		handlerMouseListenerPoint.x = e.getX();
		handlerMouseListenerPoint.y = e.getY();
	    }
	}
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
	if (e.getButton() == MouseEvent.BUTTON1) 
	{
	    int tableSize = DataController.getTable().getTableSize();
	    
	    if (tableSize != 0 && !isSampleMovable && !isSampleSelectOn)
	    {
		mainFormLink.getComponentPanelCenter().getComponentPanelImageView().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		handlerMouseListenerPoint.x = e.getX();
		handlerMouseListenerPoint.y = e.getY();
	    }
	}
    }

}
