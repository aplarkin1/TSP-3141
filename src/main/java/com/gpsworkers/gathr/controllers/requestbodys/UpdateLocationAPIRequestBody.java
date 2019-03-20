package com.gpsworkers.gathr.controllers.requestbodys;

/**
 * 
 * @author Alexander Larkin
 * This class is used to specify the fields of the UpdateLocationRequest request.
 * @see com.gpsworkers.gathr.controllers.APIController for more information about the controller
 * that leverages this request specification
 */
public class UpdateLocationAPIRequestBody implements BasicRequestBody {
	public double lon, lat, elev;
	
	public UpdateLocationAPIRequestBody(double lon, double lat, double elev) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.elev = elev;
	}
	
}
