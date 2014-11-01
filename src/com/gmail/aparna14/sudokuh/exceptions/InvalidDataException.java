package com.gmail.aparna14.sudokuh.exceptions;

public class InvalidDataException  extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDataException(String message)
	{
		super(message);
	}
	
	public InvalidDataException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
}
