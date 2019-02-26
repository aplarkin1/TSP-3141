package com.gpsworkers.gathr.exceptions;

/**
 * 
 * @author Alexander Larkin
 *
 * This class is used to pass along WebAPI related exceptions back up to the API Controller.
 * This class allows us to pass JSON errors easily back up to the API Controller
 */
public class WebApiErrorResponseException extends Exception{

	/**
	 * This constructor allows for a custom JSON string to be passed up the stack to the APIController for transmission
	 * @param message string of message
	 */
	public WebApiErrorResponseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
