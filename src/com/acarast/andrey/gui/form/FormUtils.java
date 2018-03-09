package com.acarast.andrey.gui.form;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class FormUtils
{
    /**
     * Returns icon by specifying its path
     * @param path -- path to the desired image
     */
    public static ImageIcon getIconResource(String path)
    {
	return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
    }

    /**
     * Returns image by specifying its path
     * @param path -- path to the desired image
     * @return
     */
    public static Image getImageResource(String path)
    {
	return Toolkit.getDefaultToolkit().getImage(path);
    }
}
