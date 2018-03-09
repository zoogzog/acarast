package com.acarast.andrey.data;

/**
 * Stores file path and name.
 * @author Andrey G
 */

public class DataFile
{
    private String fileName;
    private String filePath;

    //----------------------------------------------------------------

    public DataFile ()
    {
	fileName = "";
	filePath = "";
    }

    //----------------------------------------------------------------

    public void setFileName (String name)
    {
	fileName = name;
    }

    public void setFilePath (String path)
    {
	filePath = path;
    }

    public String getFileName ()
    {
	return fileName;
    }

    public String getFilePath ()
    {
	return filePath;
    }

    //----------------------------------------------------------------

    /**
     * Resets the container.
     */
    public void reset ()
    {
	fileName = "";
	filePath = "";
    }

}
