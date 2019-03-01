package com.gpsworkers.gathr.mongo.users;

import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class is the MongoDB specification for a user record
 */
@Document(collection="users")
public class User {
	
	//API Token used by API requests
    private ObjectId apiToken;

    private String firstName, lastName, username;
    private Location currentLocation;
    
    //Helps determine if a user API Tokn should be expired.
    private Date dateOfLastInteraction;
    
	@Id
    private String email;

    //@DBRef
    //public ArrayList<Group> groups;

    /**
     * This constructor allows for the construction of User if and only if a first name, last name, and email are given. 
     * @param firstName is the given first name
     * @param lastName is the given last name
     * @param email is the given email
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        currentLocation = new Location();
        updateLastInteraction();
        generateToken();
    }
    
    /**
     * Getter for Location
     * @return location of user
     */
    public Location getCurrentLocation() {
		return currentLocation;
	}

    
    /**
     * This method gets the stored email
     * @return String representation of the users email
     */
	public String getEmail() {
		return email;
	}

	/**
	 * This method sets the users email with the given email parameter
	 * @param email is String email to set the internal email to
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * This method returns the stored firstName
	 * @return first name stored in the object
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This method sets the internal first name to the given parameter
	 * @param firstName is the given new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This method retrieves the stored last name
	 * @return the String last name stored by this object
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * This method sets the internal last name with the given parameter
	 * @param lastName is the replacement of the old one
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * This method retrieves the stored username String
	 * @return the stored username String
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * This method sets the internal username with the one given
	 * @param username is the replacement usrname
	 */
	public void setUsername(String username) {
		this.username = username;
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
    public void updateLocation(double longitude, double latitude, double elevation, String country, String state, String city) {
    	currentLocation.update(longitude, latitude, elevation, country, state, city);
    }
    
    /**
     * This method generates a fresh user API token
     * @return a new ObjectId.
     */
    public static final ObjectId generateToken() {
    	return new ObjectId();
    }
    
    /**
     * This method returns a hex string representation of the API token.
     * @return String version of the apiToken
     */
    public String getAPIToken() {
    	if(apiToken != null) {
    		return apiToken.toHexString();
    	} else {
    		return "0";
    	}
    }
    
    /**
     * This method simply updates the date of the user's last interaction...API interaction.  This ensures that an API token will not be destroyed
     * unless a user stops utilizing our services
     */
    public void updateLastInteraction() {
    	dateOfLastInteraction = new Date();
    }
    
    /**
     * This method returns the date of the last API interaction
     * @return Date of the user's last interaction
     */
    public Date getDateOfLastInteraction() {
    	return dateOfLastInteraction;
    }

    /**
     * This method expires the user's current API token.  This is mainly used by the scheduled API Token cleaning task.
     */
    public void removeToken() {
    	apiToken = null;;
    }

    /**
     * This method allows for setting of a new API Token
     * @param apiToken the new API token to be assigned to the user.
     */
	public void setApiToken(ObjectId apiToken) {
		this.apiToken = apiToken;
	}
    
    
    
}
