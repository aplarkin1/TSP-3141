package com.gpsworkers.gathr.exceptions;

/**
 * 
 * @author Alexander Larkin
 * This class is an exception that is used whenever a user attempts to create a 
 * communication channel that already has the same name as a currently established one
 * 
 */
public class ChannelAlreadyExistsException extends Exception{

	/**
	 * This constructor allows for a user to set a custom error message
	 * @param message String that describes the situation that occured
	 */
	public ChannelAlreadyExistsException(String message) {
		super(message);
	}
	
}
