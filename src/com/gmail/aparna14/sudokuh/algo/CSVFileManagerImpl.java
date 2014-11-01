package com.gmail.aparna14.sudokuh.algo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gmail.aparna14.sudokuh.exceptions.InvalidDataException;

/**
 * This class implements CSVFileManager interface.
 * @author aparna
 *
 */
public class CSVFileManagerImpl implements CSVFileManager {

	
	private static final String COMMA = ",";
	private static final String INEQUALCOLUMNLENGTH = "Column length does not match row length";
	private static final String NEWLINE = "\n";
	@Override
	/**
	 * Reads csv file to a matrix.
	 * CSV file is nothing but text file 
	 * with each row identified as one line
	 * and column values in row separated by comma delimiter
	 */
	
	public int[][] readFile(String fileName) throws FileNotFoundException , IOException, InvalidDataException
	{
		BufferedReader fileReader = null;
		String line = "";
		List<List<Integer>> matrix = null;
		int[][] boardArray = null;
		
		try 
		{
			 
			fileReader = new BufferedReader(new FileReader(fileName));
			matrix = new ArrayList<List<Integer>>(); 
 			
			while ((line = fileReader.readLine()) != null) 
			{
				String[] fields = line.split(COMMA);
	 			
	 			try
	 			{
	 				List<Integer> colArr = new ArrayList<Integer>();
	 				for(String field: fields)
		 			{
		 				int fieldVal = Integer.parseInt(field);
		 				colArr.add(fieldVal);
		 			}
		 			matrix.add(colArr);
	 			}
	 			catch(NumberFormatException e)
	 			{
	 				throw new InvalidDataException(e.getMessage());
	 			}
			}
	 
		}
		finally 
		{
			if (fileReader != null) 
			{
				try 
				{
					fileReader.close();
				}
				catch (IOException e) 
				{
					throw new IOException("Error while closing file reader :" + e.getCause());
				}
			}
		}
	 
		if(matrix != null)
		{
			boardArray = new int[matrix.size()][matrix.size()];
			
			int numberOfRows = matrix.size();
			
			for(int i=0; i< numberOfRows; i++)
			{
				if(matrix.get(i).size() != numberOfRows)
				{
					throw new InvalidDataException(INEQUALCOLUMNLENGTH + "in column " + (i+1) + ":" + matrix.get(i).size());
				}
				
				for(int j=0; j< numberOfRows; j++)
				{
					boardArray[i][j] = matrix.get(i).get(j);
				}
			}
		}
		return boardArray;
	}

	@Override
	public void writeToFile(int[][] boardArray, String inputFilePath) throws IOException 
	{
		
		BufferedWriter writer = null;
		String curWorkingDir = System.getProperty("user.dir");
		String inputFileName = inputFilePath.substring(inputFilePath.lastIndexOf("\\") + 1);
		inputFileName = inputFileName.substring(0,inputFileName.lastIndexOf("."));
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss"); 
		String outputFileName = curWorkingDir + "\\" + inputFileName + "_" + df.format(new Date()) + "_output.csv";
		
		int rows = boardArray.length;
		
		File file = new File(outputFileName);	
		if(!file.exists())
			file.createNewFile();
		
		try
		{
			writer = new BufferedWriter(new FileWriter(file));
		
			for(int i=0; i< rows; i++)
			{
				StringBuilder sb = new StringBuilder();
				for(int j=0; j< rows; j++)
				{
					if(sb.length() != 0)
						sb.append(COMMA + boardArray[i][j]);
					else
						sb.append(boardArray[i][j]);
				}
				writer.write(sb.toString() + NEWLINE);
			}
			writer.flush();
		}
		catch(IOException e)
		{
			throw e;
		}
		finally
		{
			if(writer != null)
			{
				try
				{
					writer.close();
				}
				catch(IOException e)
				{
					throw new IOException("Error while closing csv file writer:" + e.getCause());
				}
			}
		}
	}
}
