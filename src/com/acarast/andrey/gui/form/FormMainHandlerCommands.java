package com.acarast.andrey.gui.form;

public class FormMainHandlerCommands
{
    //---- Commands related to project management
    public static final String AC_NEWPROJECT = "NEWPROJECT";
    public static final String AC_ADDFILE = "ADDFILE";
    public static final String AC_ADDFOLDER = "ADDFOLDER";
    public static final String AC_REMOVEFILE = "REMOVEFILE";
    
    //---- Commands related to image view
    public static final String AC_ZOOMIN = "ZOOMIN";
    public static final String AC_ZOOMOUT = "ZOOMOUT";
    public static final String AC_RESETVIEW = "RESETVIEW";
    
    public static final String AC_SAMPLE_MOVE = "SAMPLE_MOVE";
    public static final String AC_SAMPLE_SELECT = "SAMPLE_SELECT";
    
    //---- Commands related to starting/stopping processing
    public static final String AC_PROCESS = "PROCESS";
    public static final String AC_PROCESSALL = "PROCESSALL";
    
    //---- Commands to export or save data
    public static final String AC_EXPORT_FEATUREVECTOR = "EXPORT_FEATUREVECTOR";
    public static final String AC_EXPORT_IMAGESAMPLEAREA = "EXPORT_IMAGE";
    public static final String AC_EXPORT_IMAGEBOUNDINGBOX = "EXPORT_IMAGEBOUNDINGBOX";
    public static final String AC_EXPORT_COLOREDCELLS = "EXPORT_COLOREDCELLS";
    public static final String AC_EXPORT_CHARTFREQUENCY = "EXPORT_CHARTFREQ";
    public static final String AC_EXPORT_CHARTCOUNT = "EXPORT_CHARTCOUNT";
    
    public static final String AC_SAVE_OUTPUTDATA = "SAVE_IMAGEDATA";
    public static final String AC_SAVE_OUTPUTDATAALL = "SAVE_IMAGEDATAALL";
    
    //---- Commands to observe various types of data
    public static final String AC_VIEW_CELL = "VIEW_CELL";
    public static final String AC_VIEW_IMAGE = "VIEW_IMAGE";
    public static final String AC_VIEW_CHARTFREQUENCY = "VIEW_CHARTFREQ";
    public static final String AC_VIEW_CHARTCOUNT = "VIEW_CHARTCOUNT";
    public static final String AC_VIEW_COLOREDCELL = "VIEW_COLOREDCELL";
    public static final String AC_VIEW_SAMPLES_ON = "VIEW_SAMPLES_OFF";
    public static final String AC_VIEW_SAMPLES_OFF = "VIEW_SAMPLES_ON";
    
    //---- Commands for displaying additional forms (help, about, settings)
    public static final String AC_FORM_SETTINGS = "FORM_SETTINGS";
    public static final String AC_FORM_HELP = "FORM_HELP";
    public static final String AC_FORM_ABOUT = "FORM_ABOUT";
    
    //---- Commands related to change of comboboxes and other interactions
    public static final String AC_COMBOBOX_IMAGECHANGED = "COMBOBOX_IMAGECHANGED";
    public static final String AC_COMBOBOX_SAMPLECHANGED = "COMBOBOX_SAMPLECHANGED";
    public static final String AC_CHANGED_SAMPLEDETECTIONMODE = "CHANGED_SAMPLEDETECTIONMODE";
    
    public static final String AC_CHANGED_ESTIMATOR_ON = "ESTIMATOR_ON";
    public static final String AC_CHANGED_ESTIMATOR_OFF = "ESTIMATOR_OFF";
    public static final String AC_CHANGED_ESTIMATOR_FILE = "ESTIMATOR_SET_FILE";
    public static final String AC_CHANGED_ESTIMATOR_MANUAL = "ESTIMATOR_SET_MANUAL";
    
    public static final String AC_SAMPLEDETECTION_MODE = "SAMPLEDETECTION_MODE";
    
    //---- Manual management of the samples
    public static final String AC_MANUAL_ADDSAMPLE = "MANUAL_ADDSAMPLE";
    public static final String AC_MANUAL_ADDCONTROL = "MANUAL_ADDCONTROL";
    public static final String AC_MANUAL_DELETESAMPLE = "MANUAL_DELETESAMPLE";
    
    public static final String AC_TIMELAPSE_IMPORT = "TIMELAPSE_IMPORT";
    public static final String AC_TIMELAPSE_EXPORT = "TIMELAPSE_EXPORT";
    
}
