package com.gpsworkers.gathr.exceptions;

/**
 *
 * @author Amanda Erdmann
 * This class is an exception that is used whenever a user atempts to perform an admin action
 * when not an admin
 */
public class NotAdminException extends Exception{

	/**
	 * This constructor allows for a user to set a custom error message
	 * @param message String that describes the situation that occured
	 */
	public NotAdminException(String message) {
		super(message);
	}

}
