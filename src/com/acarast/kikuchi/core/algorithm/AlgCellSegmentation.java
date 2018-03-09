package com.acarast.kikuchi.core.algorithm;

import java.awt.Point;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.acarast.kikuchi.core.data.ConnectedRegion;

/**
 * Performs cell segmentation
 * @author Kikuchi K, Hanada S, Andrey G
 */
public class AlgCellSegmentation 
{
	public static void  run (ConnectedRegion currentRegion, Mat workMat)
	{
		ConnectedRegion outputData = new ConnectedRegion();

		if(currentRegion.getIntersection().length != 0)
		{		
			executeIntersectionSeparation(workMat, currentRegion.getIntersection(), outputData);

			currentRegion.resetCells(outputData);

		}

	}


	private static void executeIntersectionSeparation (Mat thinImage, Point[] intersectionXY, ConnectedRegion outputData)
	{
		final int NEIGHBOR_JUNC = 155; 
		final int JUNC = 100; 

		Mat intersectionMap = Mat.zeros(thinImage.size(), thinImage.type());
		Mat intersectionMapOrg = Mat.zeros(thinImage.size(), thinImage.type());
		Mat cellMap = Mat.zeros(thinImage.size(), thinImage.type());
		Mat debugMat =  Mat.zeros(thinImage.size(), thinImage.type());

		//---- Initialise temporary matrices
		for(int i = 0; i < intersectionXY.length; i++)
		{
			int inX = intersectionXY[i].x;
			int inY = intersectionXY[i].y;

			intersectionMap.put(inX, inY, 255);
			intersectionMapOrg.put(inX, inY, 255);
			cellMap.put(inX, inY, 255);
			thinImage.put(inX, inY, 255);
			debugMat.put(inX, inY, JUNC);

		}

		int cellIndex = 1;

		int[][] mask = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};

		//------------- GLOBAL LOOP START
		for(int i = 0; i < intersectionXY.length; i++)
		{
			int X = intersectionXY[i].x;
			int Y = intersectionXY[i].y;		

			//------------- LOOP 1 START
			for (int indexMask = 0; indexMask < mask.length; indexMask++)
			{
				int nX = X + mask[indexMask][0];
				int nY = Y + mask[indexMask][1];

				try
				{
					if((thinImage.get(nX, nY)[0] > 0)&&((int)cellMap.get(nX, nY)[0] == 0))
					{
						cellMap.put(nX, nY, cellIndex);

						Point[] core = {new Point(nX, nY)};
						outputData.addNewCell(core);
						cellIndex++;
						debugMat.put(nX, nY, NEIGHBOR_JUNC);		
					}
				}
				catch(Exception e){}
			}

			//------------- LOOP 2 START
			for (int indexMask = 0; indexMask < mask.length; indexMask++)
			{
				int nX = X + mask[indexMask][0];
				int nY = Y + mask[indexMask][1];

				try
				{
					if(intersectionMap.get(nX, nY)[0] > 0)
					{
						for (int indexMask2 = 0; indexMask2 < mask.length; indexMask2++)
						{
							int nnX = nX + mask[indexMask2][0];
							int nnY = nY + mask[indexMask2][1];

							try
							{
								if((thinImage.get(nnX, nnY)[0] > 0)&&((int)cellMap.get(nnX, nnY)[0] == 0))
								{
									cellMap.put(nnX, nnY, cellIndex);

									Point[] core = {new Point(nnX, nnY)};	
									outputData.addNewCell(core);

									cellIndex++;
									debugMat.put(nnX, nnY, NEIGHBOR_JUNC);													
								}
							}
							catch(Exception e){}
						}
					}

				}
				catch(Exception e){}

			}

			//------------- LOOP 3 START
			for (int indexMask = 0; indexMask < mask.length; indexMask++)
			{
				int nX = X + mask[indexMask][0];
				int nY = Y + mask[indexMask][1];
				int currentCellIndex = 0;
				int currentAlloc = 0;
				try
				{
					currentCellIndex = (int)cellMap.get(nX, nY)[0];
					currentAlloc = (int)debugMat.get(nX, nY)[0];
				}
				catch(Exception e){currentCellIndex = 0;}

				if((currentCellIndex > 0)&&(currentAlloc == NEIGHBOR_JUNC))
				{
					int tempnX = nX;
					int tempnY = nY;			

					do
					{

						boolean loopContinue = true;
						int neighborCount0 = 0; 
						intersectionMap.put(X, Y, 0);

						for (int ind = 0; ind < mask.length; ind++)
						{
							intersectionMap.put(X + mask[ind][0] , Y + mask[ind][1], 0);
						}

						for(int k = 0; k < mask.length; k++)
						{
							try
							{

								int nnX = tempnX + mask[k][0];
								int nnY = tempnY + mask[k][1];

								if((nnX >= 0)&&(nnY >= 0))
								{

									if((int)intersectionMap.get(nnX, nnY)[0] > 200)
									{
										loopContinue = false;
									}


									if((int)thinImage.get(nnX, nnY)[0] > 0)
									{

										if((int)debugMat.get(nnX, nnY)[0] == 0)
										{
											neighborCount0++;
										}
									}
								}
							}
							catch(Exception e){}
						}


						intersectionMap.setTo(Scalar.all(0));
						for(int j = 0; j < intersectionXY.length; j++)
						{
							int intersectionRow = intersectionXY[j].x;
							int intersectionCol = intersectionXY[j].y;

							intersectionMap.put(intersectionRow, intersectionCol, 255);

						}

						if((!loopContinue)||(neighborCount0 < 1)){break;}

						int cellPix = 0;
						int label = -1;
						for(int k = 0; k < mask.length; k++)
						{
							try
							{
								cellPix = (int)thinImage.get(tempnX + mask[k][0], tempnY + mask[k][1])[0];
								label = (int)cellMap.get(tempnX + mask[k][0], tempnY + mask[k][1])[0];
							}
							catch(Exception e){}

							if((cellPix > 0)&&(label == 0))
							{
								tempnX += mask[k][0];
								tempnY += mask[k][1];

								thinImage.put(tempnX, tempnY, 40);
								outputData.addPixelsToCell(currentCellIndex-1, new Point(tempnX, tempnY));
								cellMap.put(tempnX, tempnY, currentCellIndex);
								debugMat.put(tempnX, tempnY, 255);

								break;
							}
						}
					}
					while(true);
				}
			}
			//------------- LOOP 3 END
		}
		//------------- GLOBAL LOOP END
	}

}
