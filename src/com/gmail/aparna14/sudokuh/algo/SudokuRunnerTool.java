package com.gmail.aparna14.sudokuh.algo;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.gmail.aparna14.sudokuh.exceptions.InvalidDataException;
import com.gmail.aparna14.sudokuh.algo.CSVFileManager;
import com.gmail.aparna14.sudokuh.algo.ManagerFactory;

public class SudokuRunnerTool {
	
	public static void main(String[] args)
	{
		int[][] sudokuArr = null;
		try
		{
			CSVFileManager csvFileManager = ManagerFactory.getCSVFileManager();
			sudokuArr  = csvFileManager.readFile(args[0]);
		
			if(sudokuArr != null)
			{
				/* solve blank spaces in sudoku board */
 				 HeursiticAlgo hr = new HeursiticAlgo();
				 if(hr.solveBoard(sudokuArr))
				 {
					 printArr(sudokuArr);
					 /* write output to file */
	 				 csvFileManager.writeToFile(sudokuArr, args[0]);
	 				
				 }
				 else
 				 {
 					System.out.println("Solution for the given Board does not exist: " + sudokuArr);
 				 }
			}
		}
		catch(FileNotFoundException fe)
		{
			System.out.println(" Unable to open file " + args[0]);
		}
		catch(IOException e)
		{
			System.out.println("Error reading from input file or writing to output file " + e.getMessage());
		}
		catch(InvalidDataException ide)
		{
			System.out.println(ide.getMessage() + ":" + ide.getCause());
		}
		
	}
	
	private static void printArr(int[][] sudokuArr)
	{
		for(int i=0; i<9; i++)
		{
			for(int j=0; j<9; j++)
				System.out.print(sudokuArr[i][j] + " ");
			System.out.println();
		}
	}

}
