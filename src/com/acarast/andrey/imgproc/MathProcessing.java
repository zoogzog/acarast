package com.acarast.andrey.imgproc;

import java.awt.Point;
import java.awt.Polygon;

/**
 * Additional high level methods for math processing.
 * @author Andrey G
 */
public class MathProcessing
{
	/**
	 * Calculates area, occupied by the polygon.
	 * @param polygon
	 * @return
	 */
	public static double calculatePolygonArea (Polygon polygon)
	{
		int[] X = polygon.xpoints;
		int[] Y = polygon.ypoints;

		double area = 0;        

		int j = X.length -1;

		for (int i=0; i < X.length; i++)
		{ 
			area = area +  (X[j]+X[i]) * (Y[j]-Y[i]); 
			j = i;  
		}
		return area/2;
	}

	/**
	 * Calculates distance between two points.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double getDistance (Point p1, Point p2)
	{
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
}
