package com.gpsworkers.gathr.controllers.requestbodys;

/**
 * 
 * @author Alexander Larkin
 * 
 * This interface is used to specify a basic format that all API requests should follow, except for the user creation request.
 */
public interface BasicRequestBody {
	
	/**
	 * This method returns the API token stored that is stored in whatever implementation implements this interface
	 * @return String representation of the API Token.
	 */
	public String getApiToken();
}
