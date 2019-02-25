package com.gpsworkers.gathr.mongo.users;

public class Location {
	private String region, state, city;
	private double longitude, latitude, elevation;
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getElevation() {
		return elevation;
	}
	
	public String getState() {
		return state;
	}
	public double getLongitude() {
		return longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	
	public void update(double longitude, double latitude, double elevation, String region, String state, String city) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.region = region;
		this.state = state;
		this.city = city;
	}
	
	
}
