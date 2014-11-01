package com.gmail.aparna14.sudokuh.algo;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.gmail.aparna14.sudokuh.exceptions.InvalidDataException;

/**
 * This interface declares the file operations used for
 * 1. reading csv file and getting matrix (2 dimensional array)
 * 2. writing matrix to csv files
 * @author aparna
 *
 */
public interface CSVFileManager {
	
	/**
	 * Reads sudoku matrix from file and returns matrix. 
	 * @param filePath - path where file is located with filename
	 * @return matrix - two dimensional array of n^2*n^2 board.
	 */
	public int[][] readFile(String filePath) throws FileNotFoundException, IOException, InvalidDataException;
	
	/**
	 * Takes sudoku matrix and inputfileName and writes to file in current working directory
	 * Output file is stored in current working directory and output file name
	 * is input file name with date added to it.
	 * @param boardArray - sudoku matrix
	 * @param inputFilePath - input file path name
	 * @throws IOException - throws IOException ,if there is exception writing to output file.
	 */
	public void writeToFile(int[][] boardArray, String inputFilePath) throws IOException;
	
}
