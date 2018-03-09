package com.acarast.andrey.gui.form;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.EmptyBorder;

/**
 * Colors, fonts and other style related settings for the main GUI window.
 * @author Andrey G
 *
 */
public class FormStyle
{
    //----------------------------------------------------------------
    //---- Main set of colors for the application
    
    public static final Color COLOR_PANEL = Color.gray;
    public static final Color COLOR_MENU = Color.lightGray;
    public static final Color COLOR_TOOLBAR = Color.lightGray;
    public static final Color COLOR_MAIN = Color.DARK_GRAY;
    public static final Color COLOR_TEXT_LIGHT = Color.white;
    
    //----------------------------------------------------------------
    //---- Default locations of the resources: html files, svm settings
    //---- icons and other graphics
    
    public static final String DEFAULT_RESOURCE_FOLDER = "data/graphics/";	
    public static final String DEFAULT_HTML_FOLDER = "data/html/";
    
    //----------------------------------------------------------------
    //---- Defaults fonts of the application
    
    public static final Font DEFAULT_FONT = new Font("Times New Roman", 0, 16);
    public static final Font DEFAULT_STATUS_FONT = new Font("Times New Roman", 0, 14);
    
    //----------------------------------------------------------------
    //---- Paths to icons and other graphic files
    
    public static final String RESOURCE_PATH_ICON = DEFAULT_RESOURCE_FOLDER + "icon.png";
    public static final String RESOURCE_PATH_ICO_SELECT_FILE = DEFAULT_RESOURCE_FOLDER + "select_file.png";
    public static final String RESOURCE_PATH_ICO_SELECT_FILE_ADD = DEFAULT_RESOURCE_FOLDER + "select_file_add.png";
    public static final String RESOURCE_PATH_ICO_SELECT_FOLDER = DEFAULT_RESOURCE_FOLDER + "select_folder.png";
    public static final String RESOURCE_PATH_ICO_ZOOMIN = DEFAULT_RESOURCE_FOLDER + "zoom_in.png";
    public static final String RESOURCE_PATH_ICO_ZOOMOUT = DEFAULT_RESOURCE_FOLDER + "zoom_out.png";
    public static final String RESOURCE_PATH_ICO_SELECT = DEFAULT_RESOURCE_FOLDER + "select.png";
    public static final String RESOURCE_PATH_ICO_SELECT_PRESSED = DEFAULT_RESOURCE_FOLDER + "select_pressed.png";
    public static final String RESOURCE_PATH_ICO_MOVE = DEFAULT_RESOURCE_FOLDER + "move.png";
    public static final String RESOURCE_PATH_ICO_MOVE_ACTIVE = DEFAULT_RESOURCE_FOLDER + "move_selected.png";
    public static final String RESOURCE_PATH_ICO_PROCESS = DEFAULT_RESOURCE_FOLDER + "process.png";
    public static final String RESOURCE_PATH_ICO_PROCESS_ALL = DEFAULT_RESOURCE_FOLDER + "process_all.png";
    public static final String RESOURCE_PATH_ICO_REFRESH = DEFAULT_RESOURCE_FOLDER + "refresh.png";
    public static final String RESOURCE_PATH_ICO_SAVE = DEFAULT_RESOURCE_FOLDER + "save.png";
    public static final String RESOURCE_PATH_ICO_SAVEALL = DEFAULT_RESOURCE_FOLDER + "save_plus.png";
    public static final String RESOURCE_PATH_ICO_SAMPLE_ADD = DEFAULT_RESOURCE_FOLDER +"add_sample.png";
    public static final String RESOURCE_PATH_ICO_SAMPLE_ADD_CONTROL = DEFAULT_RESOURCE_FOLDER + "add_c_sample.png";
    public static final String RESOURCE_PATH_ICO_SAMPLE_DELETE = DEFAULT_RESOURCE_FOLDER + "delete_sample.png";
    public static final String RESOURCE_PATH_ICO_ORIGINAL_IMAGE = DEFAULT_RESOURCE_FOLDER + "o_image.png";
    public static final String RESOURCE_PATH_ICO_CELL_IMAGE = DEFAULT_RESOURCE_FOLDER + "o_image_active.png";
    public static final String RESOURCE_PATH_ICO_DELETE_FILE = DEFAULT_RESOURCE_FOLDER + "delete_file.png";
    public static final String RESOURCE_PATH_ICO_VIEW_SAMPLES = DEFAULT_RESOURCE_FOLDER + "view_sample.png";
    public static final String RESOURCE_PATH_ICO_VIEW_SAMPLES_ON = DEFAULT_RESOURCE_FOLDER + "view_sample_on.png";
    
    
    //----------------------------------------------------------------
    //---- Main window menu styles: borders and texts
    
    public static final EmptyBorder DEFAULT_MENU_BORDER = new EmptyBorder(5, 10, 5, 50);
    public static final String MENU_FILE_NEWPROJECT = "New Project";
    public static final String MENU_FILE_OPENFILE = "Open File...";
    public static final String MENU_FILE_ADDFILE = "Remove File...";
    public static final String MENU_FILE_OPENFOLDER = "Open Folder...";
    public static final String MENU_FILE_SAVEOUTPUT = "Save output as...";
    public static final String MENU_FILE_SAVEALL = "Save all output to...";
    public static final String MENU_FILE_EXPORT = "Export...";
    public static final String MENU_FILE_EXPORT_BOUNBOX = "Image with bounding boxes";
    public static final String MENU_FILE_EXPORT_CHANNELAREA = "Image with  detected samples";
    public static final String MENU_FILE_EXPORT_COLOREDCELLS = "Image with color coded cells";
    public static final String MENU_FILE_EXPORT_LENGTHFREQCHART = "Length frequency chart";
    public static final String MENU_FILE_EXPORT_COUNTFREQCHART = "Cell count chart";
    public static final String MENU_FILE_EXPORT_FEATUREVECTOR = "Feature vector list";
    public static final String MENU_EDIT_SELECTSAMPLE = "Select sample";
    public static final String MENU_EDIT_MOVESAMPLE = "Move sample";
    public static final String MENU_VIEW_ZOOMIN = "Zoom in";
    public static final String MENU_VIEW_ZOOMOUT = "Zoom out";
    public static final String MENU_VIEW_RESETVIEWPOINT = "Fit to screen";
    public static final String MENU_VIEW_BOUNDINGBOX = "Bounding boxes";
    public static final String MENU_VIEW_COLOREDCELL = "Color coded cells";
    public static final String MENU_VIEW_ORIGINALINPUT = "Original input image";
    public static final String MENU_VIEW_FREQUENCYCHART = "Length frequency chart";
    public static final String MENU_VIEW_COUNTFREQCHART = "Cell count chart";
    public static final String MENU_RUN_RUNTHIS = "Process";
    public static final String MENU_RUN_RUNALL = "Process all";
    public static final String MENU_HELP_SETTINGS = "Settings";
    public static final String MENU_HELP_HELPMANUAL = "Help Contents";
    public static final String MENU_HELP_ABOUT = "About";
	    
}
