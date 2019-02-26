package com.gpsworkers.gathr.exceptions;

/**
 * 
 * @author Alexander Larkin
 * This class is an Exception class that is used for signaling that a Communication channel doesn't exist
 *
 */
public class ChannelDoesntExistException extends Exception {

	public ChannelDoesntExistException(String message) {
		super(message);
	}
	
}
