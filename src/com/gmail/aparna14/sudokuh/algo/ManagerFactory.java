package com.gmail.aparna14.sudokuh.algo;

import com.gmail.aparna14.sudokuh.algo.CSVFileManager;
import com.gmail.aparna14.sudokuh.algo.CSVFileManagerImpl;

/**
 * This class serves as factory class to serve the manager instances/beans.
 * 
 * @author aparna
 *
 */
public class ManagerFactory 
{
	private static CSVFileManager csvFileManager = null;
	
	public static synchronized CSVFileManager getCSVFileManager()
	{
		if(csvFileManager == null)
		{
			csvFileManager = new CSVFileManagerImpl();
		}
		return csvFileManager;
	}	
}
