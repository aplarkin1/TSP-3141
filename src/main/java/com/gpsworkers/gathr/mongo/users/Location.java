package com.gpsworkers.gathr.mongo.users;

/**
 *
 * @author Alexander Larkin
 *
 * This class handles the storage of user location data.  This is not a MongoEntity in itself.  It is used as an embedded record
 * in a user document
 */
public class Location {
	private String country, state, city;
	private double longitude, latitude, elevation;

	/**
	 * This method returns the current country
	 * @return the String representation of the user's current country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * This method sets the user's current country
	 * @param region is the new country String to be set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * This method returns the user's currently stored city
	 * @return a String representation of a user's currently stored city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * This method sets the User's current city to the parameter value
	 * @param city is the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * This method returns the user's currently stored elevation
	 * @return the user's current elevation
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * This method returns the user's currently stored state
	 * @return the user's currently stored state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * This method gets returns user's current longitude
	 * @return the user's current longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * This method returns the user's currently stored latitude.
	 * @return the user's currently stored latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * This method allows us to update a user's location.  I also decided to input the user's current city, state(medium regional area), and country,
	 * because, later on, this project will allow message broadcasts to be sent to various regions, city included...possibly state and country
	 * @param longitude is the current double longitude of the user
	 * @param latitude is the current double latitude of the user
	 * @param elevation is the current double elevation of the user
	 * @param country is the country name where the user is currently located
	 * @param region is the region(state) name where the user is currently located
	 * @param city is the city name where the user is currently located
	 */
	public void update(double longitude, double latitude, double elevation, String country, String state, String city) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.country = country;
		this.state = state;
		this.city = city;
	}

	/**
		puts all information into string with comas inbetween
		@return string with all information sepreated by comas ( country, state, city, longitude, latitude, elevation )
	*/
	public String toString() {
		return country + "," + state + "," + city + "," + Double.toString( longitude ) + "," + Double.toString( latitude ) + "," + Double.toString( elevation );
	}


}
