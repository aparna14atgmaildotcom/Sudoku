package com.gmail.aparna14.sudokuh.data;

/*
 * This  class is used to store the coordinates and correct number for a cell.
 */
public class Entry {
	
	private int rowId;
	
	private int colId;
	
	private int element;
	
	public Entry(int row, int col, int ele)
	{
		rowId = row;
		colId = col;
		element = ele;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public int getColId() {
		return colId;
	}

	public void setColId(int colId) {
		this.colId = colId;
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
	}
}
