package com.acarast.andrey.core.algorithm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Class for storing svm model for sensitivity estimation.
 * @author Andrey G
 */
public class EstimatorTable
{
	//----------------------------------------------------------------
	//---- PRIVATE CLASS: HYPERPLANE
	
	private class Hyperplane 
	{
		public Vector <Double> weights;
		public double threshold;

		public Hyperplane ()
		{
			weights =  new Vector<Double>();
			threshold = 0;
		}

		public double[] getWeights ()
		{
			double[] output = new double[weights.size()];

			for (int i = 0; i < weights.size(); i++)
			{
				output[i] = weights.get(i);
			}

			return output;
		}

		public double getThreshold ()
		{
			return threshold;
		}

		public void setData (double[] inputData)
		{
			threshold = inputData[0];

			for (int i = 1; i < inputData.length; i++)
			{
				weights.addElement(inputData[i]);
			}
		}
	}

    //----------------------------------------------------------------
	
	//---- Lazy code, map is better
	private Vector <Hyperplane> tableWeights;
	private Vector <String> tableDrugName;

    //----------------------------------------------------------------
	
	public EstimatorTable (String fileInput)
	{
		tableWeights = new Vector<Hyperplane>();
		tableDrugName = new Vector<String>();

		//---- Lazy code, no check of the file existance
		try
		{
			FileInputStream fistream = new FileInputStream(fileInput);
			BufferedReader br = new BufferedReader(new InputStreamReader(fistream));

			String strLine;

			while ((strLine = br.readLine()) != null)  
			{
				String[] data = strLine.split(":");

				if (data.length >= 2)
				{
					String drugName = data[0];

					tableDrugName.addElement(drugName);

					String[] hyperplaneDataString = data[1].split(";");

					double[] hyperplaneData = new double[hyperplaneDataString.length]; 

					for (int i = 0; i < hyperplaneDataString.length; i++)
					{
						hyperplaneData[i] = Double.parseDouble(hyperplaneDataString[i]);
					}

					tableWeights.addElement(new Hyperplane());
					tableWeights.lastElement().setData(hyperplaneData);
				}
			}
			
			fistream.close();
			br.close();
		}
		catch (Exception e) { System.out.println("Exception caught, while loading file.");}
	}

    //----------------------------------------------------------------
	
	public double[] getWeights (String drugName)
	{
		int index = getIndexByName(drugName);

		if (index == -1) { return null; }

		return tableWeights.get(index).getWeights();
	}

	public double getThreshold (String drugName)
	{
		int index = getIndexByName(drugName);

		if (index == -1) { return 0; }

		return tableWeights.get(index).getThreshold();
	}

	private int getIndexByName (String drugName)
	{
		for (int i = 0; i < tableDrugName.size(); i++)
		{
			if (tableDrugName.get(i).equals(drugName))
			{

				return i;
			}
		}
		return -1;
	}

	public void debugPrint ()
	{
		System.out.println("Size: " + tableDrugName.size() + " " + tableWeights.size());

		for (int i = 0; i < tableDrugName.size(); i++)
		{
			System.out.print(tableDrugName.get(i) + ":");

			double[] weights = tableWeights.get(i).getWeights();
			double threshold = tableWeights.get(i).getThreshold();

			for (int j = 0; j < weights.length; j++)
			{
				System.out.print(weights[j] + " ");
			}

			System.out.println("# " + threshold);
		}
	}
}
