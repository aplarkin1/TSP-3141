package com.gpsworkers.gathr.controllers.requestbodys;

/**
 * 
 * @author Alexander Larkin
 * This class is used to specify the fields of the UpdateLocationRequest request.
 * @see com.gpsworkers.gathr.controllers.APIController for more information about the controller
 * that leverages this request specification
 */
public class UpdateLocationAPIRequestBody implements BasicRequestBody {
	public String apiToken;
	public double lon, lat, elev;
	
	@Override
	public String getApiToken() {
		return apiToken;
	}
}
