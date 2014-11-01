package com.gmail.aparna14.sudokuh.algo;

import java.util.Set;
import java.util.TreeSet;


/**
 * This class outlines the backtracking algorithm used for solving sudoku board.
 * @author aparna
 *
 */
public class BackTrackingStrategy
{
	/**
	 * This function solves for the missing spaces in sudoku board
	 */
	public  boolean solve(int[][] sudokuArr)
	{
		return	solve(0,0, sudokuArr, 9);
	}
	
	/*
	 * This function tries to find value satisfying row, column and grid constraints 
	 * for particular blank cell. In case of more than one feasible value, 
	 * it uses backtracking algorithm to backtrack from false assumption.
	 * 
	 */
	private boolean solve(int row, int col, int[][] sudokuCells, int dimension)
	{
		/* Reached the end of puzzle. No blank spaces */
		if(row == dimension -1 && col == dimension -1 && sudokuCells[row][col] != 0)
		{
			return true;
		}
		else
		{
			/* If the current space is not filled,
			 * then find the values satisfying row, column and gird constraints.
			 * If no such values exist, return and backtrack to correct value.
			 * If values exist, assign value and traverse remaining cells
			 * If children traversal resulted in violation of row, column and grid constraints
			 * backtrack.
			 */
			if(isBlankSpace(row,col,sudokuCells))
			{
				Set<Integer> valuesForCell = getFeasibleValues(row, col, sudokuCells, dimension);
			
				if(valuesForCell.isEmpty())
					return false;
				for(Integer feasibleValue: valuesForCell)
				{
					sudokuCells[row][col] = feasibleValue;
					if(solve(row, col, sudokuCells, dimension))
						return true;
					
					sudokuCells[row][col] = 0;
				}
			}
			else
			{
				/**
		
				 * If it is already filled, 
				 * then increment column number.
				 * If it reaches end of column, increment row
				 * and wrap column number.
				 */
				if(col +1 == dimension)
				{
					col = (col + 1) % dimension;
					row = row +1;
					
				}
				else
					col = col+1;
				return solve(row, col, sudokuCells, dimension);
			}
			return false;
		}
	}
	
	/*
	 * checks whether the current cell is blank
	 */
	private boolean isBlankSpace(int rowIndex, int colIndex, int[][] sudokuCells)
	{
		return (sudokuCells[rowIndex][colIndex] == 0);
	}
	
	private Set<Integer> getFeasibleValues(int row, int col, int[][] sudokuCells, int dimension)
	{
		return checkRowColumnGrid(row, col, sudokuCells, dimension);
	}
	
	/*
	 * Function to obtain values for cell at (row,col) position. 
	 * It should satisfy following constraints
	 * 1. row constraint - all elements in row should have number from 1- dimension and should occur only once
	 * 2. column constraint - all element in column should have number from 1- dimension and should occur only once
	 * 3. grid constraint - all elements in a grid should    
	 */
	private Set<Integer> checkRowColumnGrid(int row, int col, int[][] sudokuCells, int dimension)
	{
		Set<Integer> feasibleValues = new TreeSet<Integer>();
		for(int i=1; i <= dimension; i++)
		{
			feasibleValues.add(i);
		}
		
		/* check row */
		for(int colIndex=0; colIndex< dimension; colIndex++)
		{
			if(sudokuCells[row][colIndex] != 0)
				feasibleValues.remove(sudokuCells[row][colIndex]);
		}
		
		/* check col */
		for(int rowIndex=0; rowIndex< dimension; rowIndex++)
		{
			if(sudokuCells[rowIndex][col] != 0)
				feasibleValues.remove(sudokuCells[rowIndex][col]);
		}
		
		/* check grid */
		int gridDimension = (int)Math.sqrt(dimension);
		int rowGridStart = ((int)(row/gridDimension))*gridDimension;
		int colGridStart = col - (col % gridDimension);
		
		for(int rowIndex = rowGridStart; rowIndex < rowGridStart + gridDimension; rowIndex++)
			for(int colIndex = colGridStart; colIndex < colGridStart + gridDimension; colIndex++)
			{
				if(sudokuCells[rowIndex][colIndex] != 0)
					feasibleValues.remove(sudokuCells[rowIndex][colIndex]);
			}
		return feasibleValues;
	}
}
