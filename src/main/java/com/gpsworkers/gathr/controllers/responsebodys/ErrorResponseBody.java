package com.gpsworkers.gathr.controllers.responsebodys;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class specifies a generic error response format for GathR.
 */
public class ErrorResponseBody {
	int error;
	String desc;
	
	/**
	 * This constructor allows for a user to set an error code and description for the error that is to be generated.
	 * @param error
	 * @param desc
	 */
	public ErrorResponseBody(int error, String desc) {
		this.error = error;
		this.desc = desc;
	}
}
