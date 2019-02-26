package com.gpsworkers.gathr.exceptions;

/**
 * 
 * @author Alexander Larkin
 *
 * This class is an exception that will be raised if a Message is sent to the server without a corrsponding user id.
 */
public class MessageUserIdCannotBeEmptyException extends Exception{

	/**
	 * This constructor allows for a custom message to be set for this exception
	 * @param message string descritpion of situation.
	 */
	public MessageUserIdCannotBeEmptyException(String message) {
		super(message);
	}
	
}
