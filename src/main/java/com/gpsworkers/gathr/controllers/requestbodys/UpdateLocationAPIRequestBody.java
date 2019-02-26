package com.gpsworkers.gathr.controllers.requestbodys;

public class UpdateLocationAPIRequestBody implements BasicRequestBody {
	public String apiToken;
	public double lon, lat, elev;
	
	@Override
	public String getApiToken() {
		return apiToken;
	}
}
