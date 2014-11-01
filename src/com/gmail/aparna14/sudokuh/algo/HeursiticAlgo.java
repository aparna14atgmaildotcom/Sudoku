package com.gmail.aparna14.sudokuh.algo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.gmail.aparna14.sudokuh.data.Entry;


/**
 * This class encapsulates the operations used by human user
 * to solve sudoku puzzle.
 * The following rules are generally checked.
 * 1. If only one value is possible for row /column or grid cell, the cell should hold that value
 * 2. If a value can only be in a row or column, then cell should hold that value.
 * 3. check for cells containing Naked pairs, Naked triples or 
 * Naked quadruples (A Naked pair is two cells in row or column, containing only 2 possible values.
 * In such scenario, remove in other grid/row/column cells these 2 possible values)
 * 4. For a value discovered in Step 1 or Step2, rearrange row and columns of that particular cell
 * to not contain the value and find cells in row or column, which satisfy 1 or 2.
 * 5. After above 4 steps, if there are missing blank spaces, then apply backtracking algorithm.
 * @author aparna
 *
 */
public class HeursiticAlgo 
{
	
	private LinkedList<Entry> determinedCellsQueue = new LinkedList<Entry>();
	private int[][] determinedCellVisited = new int[9][9];
	/**
	 * For each blank cell, the possible values for cell are calculated
	 * and stored in the form of bitmap. ith bit set means, i value is permitted for the cell.
	 * e.g if for cell 1, only 2 and 3 are permitted then bitmap stored is 110
	 * @param sudokuArr
	 * @return
	 */
	 public int[][] formBitMapofAllowedValues(int[][] sudokuArr)
	 {
		 int[][] valuesAllowedBitmap = new int[9][9];
		 for(int i=0; i<9; i++)
		 {
			 for(int j=0; j<9; j++)
			 {
				 valuesAllowedBitmap[i][j] = 0;
				 if(sudokuArr[i][j] == 0)
				 {
					Set<Integer>  allowedIntSet	= getFeasibleValues(i,j,sudokuArr,9);
					/* add the element into queue , if it is determined that cell
					 * can hold only one value
					 */
					if(allowedIntSet.size() == 1)
					{
						addToQueue(i,j, allowedIntSet.iterator().next());
					}
					
					for(Integer allowedInt : allowedIntSet)
					{
						setBitInBitmap(i,j,valuesAllowedBitmap, allowedInt);
					}
				 }
			 }
		 }
		 return valuesAllowedBitmap;
	 }
	 
	 private void addToQueue(int row, int col, int value)
	 {
		 /* already visited, hence don't add to queue */
		 if(determinedCellVisited[row][col] !=1)
		 {
			 determinedCellsQueue.addLast(new Entry(row,col,value));
			 determinedCellVisited[row][col] = 1;
		 }
	 }
	 
	 /* sets in the bitMap array, bitToBeSet position to 1 */
	 private void setBitInBitmap(int rowIdx, int colIdx, int[][] bitMapForBlankSpaces, int bitToBeSet)
	 {
		 bitMapForBlankSpaces[rowIdx][colIdx] = bitMapForBlankSpaces[rowIdx][colIdx] | ( 1<< (bitToBeSet - 1));
	 }
	 
	 /* check whether bit is set */
	 private boolean isSetBit(int value, int bitToCheck)
	 {
		 return (value & (1 << (bitToCheck - 1))) != 0;
	 }
	 
	 /* count the number of bits set */
	 private int countBitsSet(int value)
	 {
		 int bitSet = 0;
		 while(value != 0)
		 {
			 bitSet++;
			 value &= (value - 1);
		 }
		 return bitSet;
	 }
	 
	 /* Mask from the  row or column nakedTuple from remaining cells*/
	 
	 private void maskBits(int rowOrcolNo, boolean isRow,int[][] valuesAllowedBitmap, int nakedTupleBitmap)
	 {
		 if(isRow)
		 {
			 for(int j=0; j<9; j++)
			 {
				 if(valuesAllowedBitmap[rowOrcolNo][j] != nakedTupleBitmap)
				 {
					 valuesAllowedBitmap[rowOrcolNo][j] &= (~nakedTupleBitmap);
				 }
			 }
		 }
		 else // It is a column
		 {
			 for(int j=0; j<9; j++)
			 {
				 if(valuesAllowedBitmap[j][rowOrcolNo] != nakedTupleBitmap)
				 {
					 valuesAllowedBitmap[j][rowOrcolNo] &= (~nakedTupleBitmap);
				 }
			 }
		 }
	 }
	 
	 /**
	  * Helper function used to get the possible values not violating row/column or grid constraints 
	  * for blank cell
	  * @param row
	  * @param col
	  * @param sudokuCells - sudoku two dimensional array
	  * @param dimension - no of rows /columns in n*n board. This program takes this value as 9.
	  * @return
	  */
	 private Set<Integer> getFeasibleValues(int row, int col, int[][] sudokuCells, int dimension)
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
	 
	 /**
	  * This function is used to check if there are any cells in row or column
	  * which take number from 1-9 uniquely.
	  */
	 private void findOnlyOneValueAllowedinRowOrColumn(int rowNo, int colNo,  int[][] valuesAllowedBitmap, int[][] sudokuArr)
	 {
		 /* check if in row, any number from 1-9 occurs only once for blank cells */ 
		 for(int num = 1; num<= 9; num++)
		 {
			 int numCount = 0;
			 int pos = 0;
			 for(int i=0; i<9; i++)
			 {
				if(valuesAllowedBitmap[rowNo][i] !=0)
				{
					if(isSetBit(valuesAllowedBitmap[rowNo][i], num))
					{
						numCount++;
						pos = i;
					}
				}
			 }
			 
			 /* Means this number only occurs once in row and hence it should be in that cell */
			 if(numCount == 1)
			 {
				 addToQueue(rowNo, pos, num);
			 }
		 }
		 
		 /* check in column, any number from 1-9 occurs only once for blank cells */
		 for(int num = 1; num<= 9; num++)
		 {
			 int numCount = 0;
			 int pos = 0;
			 for(int i=0; i<9; i++)
			 {
				if(valuesAllowedBitmap[i][colNo] !=0)
				{
					if(isSetBit(valuesAllowedBitmap[i][colNo], num))
					{
						numCount++;
						pos = i;
					}
				}
			 }
			 
			 /* Means this number only occurs once in column and hence it should be in that cell */
			 if(numCount == 1)
			 {
				 addToQueue(pos, colNo, num);
			 }
		 }
	 }
	 
	 /**
	  * Assumptions made: Though naked tuples can be of any size <9 , in 9*9 board, there is rare chance
	  * of seeing naked tuple of size 5. Hence limiting calculating naked tuples to of size 4.
	  * This function checks for naked tuples (pairs, triples or quadruples).
	  * A naked pair is one in which two cells in row/column should contain two values only.
	  * Hence rest of the cells in row/column will not contain these values and hence these values
	  * can be removed from the possibility in the remaining cells.
	  */
	private void checkForNakedGroups(int rowNo, int colNo,  int[][] valuesAllowedBitmap, int[][] sudokuArr) 
	{
		 /* This is used to store map of groupSize to map holding bitmaps of length groupSize 
		  * and number of occurrences of that bitmap
		  * in the row.
		  */
		Map<Integer, Map<Integer,Integer>> groupwiseBitMap = new HashMap<Integer,Map<Integer, Integer>>(); 
	 	for(int j=0; j<9; j++)
		{
			if(valuesAllowedBitmap[rowNo][j] != 0)
			{
				int bitsSet = countBitsSet(valuesAllowedBitmap[rowNo][j]);
				
				Map<Integer, Integer>  groupMap = groupwiseBitMap.get(bitsSet);
				
				if(groupMap != null)
				{
					Integer count = groupMap.get(valuesAllowedBitmap[rowNo][j]);
					
					count = (count != null)? count + 1 : 1;
					groupMap.put(valuesAllowedBitmap[rowNo][j], count);
				}
				else
				{
					/* first time, tuple of this group length is encountered */
					Map<Integer, Integer> groupTupleMap = new HashMap<Integer, Integer>();
					groupTupleMap.put(valuesAllowedBitmap[rowNo][j], 1);
					groupwiseBitMap.put(bitsSet, groupTupleMap);
				}
			}
		}
	 	
	 	for(int groupSize=2; groupSize<=4; groupSize++)
	 	{
	 		/* check in tuples of length groupsize , for any tuple occuring same number of times as groupsize */
	 		Map<Integer,Integer> groupTupleMap = groupwiseBitMap.get(groupSize);
	 		
	 		if(groupTupleMap != null)
	 		{
		 		for(Map.Entry<Integer, Integer> groupTupleEntry : groupTupleMap.entrySet())
		 		{
		 			if(groupTupleEntry.getValue() == groupSize)
		 			{
		 				/* mask these tuples values from other row elements */
		 				for(int j=0; j<9; j++)
		 				{
		 					if(valuesAllowedBitmap[rowNo][j] != groupTupleEntry.getKey())
		 					{
		 						maskBits(rowNo, true, valuesAllowedBitmap, groupTupleEntry.getKey());
		 					}
		 				}
		 			}
		 		}
	 		}
	 	}
	 	
	 	 /* This is used to store map of groupSize to map holding bitmaps of length groupSize 
		  * and number of occurrences of that bitmap
		  * in the row.
		  */
		Map<Integer, Map<Integer,Integer>> groupWiseBitColMap = new HashMap<Integer,Map<Integer, Integer>>(); 
	 	for(int j=0; j<9; j++)
		{
			if(valuesAllowedBitmap[j][colNo] != 0)
			{
				int bitsSet = countBitsSet(valuesAllowedBitmap[j][colNo]);
				
				Map<Integer, Integer>  groupMap = groupWiseBitColMap.get(bitsSet);
				
				if(groupMap != null)
				{
					Integer count = groupMap.get(valuesAllowedBitmap[j][colNo]);
					
					count = (count != null)? count + 1 : 1;
					groupMap.put(valuesAllowedBitmap[j][colNo], count);
				}
				else
				{
					/* first time, tuple of this group length is encountered */
					Map<Integer, Integer> groupTupleMap = new HashMap<Integer, Integer>();
					groupTupleMap.put(valuesAllowedBitmap[j][colNo], 1);
					groupWiseBitColMap.put(bitsSet, groupTupleMap);
				}
			}
		}
	 	
	 	for(int groupSize=2; groupSize<=4; groupSize++)
	 	{
	 		/* check in tuples of length groupsize , for any tuple occuring same number of times as groupsize */
	 		Map<Integer,Integer> groupTupleMap = groupWiseBitColMap.get(groupSize);
	 		
	 		if(groupTupleMap != null)
	 		{
		 		for(Map.Entry<Integer, Integer> groupTupleEntry : groupTupleMap.entrySet())
		 		{
		 			if(groupTupleEntry.getValue() == groupSize)
		 			{
		 				/* mask these tuples values from other row elements */
		 				for(int j=0; j<9; j++)
		 				{
		 					if(valuesAllowedBitmap[j][colNo] != groupTupleEntry.getKey())
		 					{
		 						maskBits(colNo, false, valuesAllowedBitmap, groupTupleEntry.getKey());
		 					}
		 				}
		 			}
		 		}
	 		}
	 	}
	}
	
	/**
	 * Solves the sudoku array for blank spaces
	 * The steps followed are detailed in the class definition above.
	 * @param sudokuArr
	 * @return
	 */
	public boolean solveBoard(int[][] sudokuArr)
	{
		int[][] valuesAllowedBitMap = formBitMapofAllowedValues(sudokuArr);
		
		for(int idx=0; idx <9; idx++)
		{
			findOnlyOneValueAllowedinRowOrColumn(idx,idx, valuesAllowedBitMap, sudokuArr);
		}
		
		for(int idx=0; idx<9; idx++)
		{
			checkForNakedGroups(idx,idx, valuesAllowedBitMap, sudokuArr);
		}
		
		/* now remove elements in determined cells queue , one by one
		 * and assign the value in sudoku board
		 * Remove this element from its row , column and grid.
		 * and apply Step2 and step3 on the corresponding row, column.
		 */
		while(!determinedCellsQueue.isEmpty())
		{
			Entry determinedCell = determinedCellsQueue.remove();
			
			int rowId = determinedCell.getRowId();
			int colId = determinedCell.getColId();
			int ele = determinedCell.getElement();
			
			System.out.println(" Element determined is :" + ele + "at (x,y) position :" + "(" + rowId + "," + colId + ")");
			
			sudokuArr[rowId][determinedCell.getColId()] = determinedCell.getElement();
			
			removeElementFromRowColumnGrid(rowId, colId, valuesAllowedBitMap, ele);
			
			/* TODO: Is this really needed here ...
			 * Can removing this element cause something to be unique in cell with other elements.
			 * If this is case, it would have been found in the first try itself.
			 */
			// findOnlyOneValueAllowedinRowOrColumn(rowId, colId, valuesAllowedBitMap, sudokuArr);
			
			checkForNakedGroups(rowId, colId, valuesAllowedBitMap, sudokuArr);
		}
		
		/* If queue is empty and there are still blank spaces,
		 * apply backtracking algorithm 
		 */
		if(hasBlankSpaces(sudokuArr))
		{
			return new BackTrackingStrategy().solve(sudokuArr);
		}
		
		return true;
	}
	
	private boolean hasBlankSpaces(int[][] sudokuArr)
	{
		for(int i=0; i < 9; i++)
			for(int j=0; j<9; j++)
			{
				if(sudokuArr[i][j] == 0)
				{
					return true;
				}
			}
		return false;
	}
	
	/*
	 * Helper function to remove the determined element from possibility values for other blank cells
	 * in row ,column and grid.
	 */
	private void removeElementFromRowColumnGrid(int rowId, int colId, int[][] valuesAllowedBitMap, int ele)
	{
		int maskForEle = ~( 1 << (ele -1));
		/* Remove element from other row elements bitmap*/
		for(int j=0; j<9; j++)
		{
			if(valuesAllowedBitMap[rowId][j] != 0 && valuesAllowedBitMap[rowId][j] != ele)
			{
				valuesAllowedBitMap[rowId][j] &= maskForEle;
				
				if(countBitsSet(valuesAllowedBitMap[rowId][j]) == 1)
				{
					int element = (int)(Math.log(valuesAllowedBitMap[rowId][j])/Math.log(2)) + 1;
					addToQueue(rowId,j, element);
				}
			}
		}
		
		/* Remove element from other column elements bitmap */
		for(int j=0; j<9; j++)
		{
			if(valuesAllowedBitMap[j][colId] != 0 && valuesAllowedBitMap[j][colId] != ele)
			{
				valuesAllowedBitMap[j][colId] &= maskForEle;
				
				if(countBitsSet(valuesAllowedBitMap[j][colId]) == 1)
				{
					int element = (int)(Math.log(valuesAllowedBitMap[j][colId])/Math.log(2)) + 1;
					addToQueue(j,colId, element);
				}
			}
		}
		
		/* Remove element from other elements in the grid */
		int gridDimension = 3;
		int rowGridStart = ((int)(rowId/gridDimension))*gridDimension;
		int colGridStart = colId - (colId % gridDimension);
		
		for(int rowIdx = rowGridStart; rowIdx < rowGridStart + gridDimension; rowIdx++)
		{
			for(int colIdx = colGridStart; colIdx < colGridStart + gridDimension; colIdx++)
			{
				if(valuesAllowedBitMap[rowIdx][colIdx] != 0 && valuesAllowedBitMap[rowIdx][colIdx] != ele)
				{
					valuesAllowedBitMap[rowIdx][colIdx] &= maskForEle;
				}
				
				if(countBitsSet(valuesAllowedBitMap[rowIdx][colIdx]) == 1)
				{
					int element = (int)(Math.log(valuesAllowedBitMap[rowIdx][colIdx])/Math.log(2)) + 1;
					addToQueue(rowIdx,colIdx, element);
				}
			}
		}
		
		valuesAllowedBitMap[rowId][colId] = 0;
	}
}
