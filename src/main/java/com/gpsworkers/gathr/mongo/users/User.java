package com.gpsworkers.gathr.mongo.users;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.mongo.groups.Group;

@Document(collection="users")
public class User {

    private ObjectId apiToken;
    
    private String firstName, lastName, username;
    private Location currentLocation = new Location();
    private Date dateOfLastInteraction;
    private String email;
    
    @DBRef
    public ArrayList<Group> groups;
    
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        generateToken();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
    public void updateLocation(double longitude, double latitude, double elevation, String region, String state, String city) {
    	currentLocation.update(longitude, latitude, elevation, region, state, city);
    }
    
    public String generateToken() {
    	apiToken = new ObjectId();
    	return apiToken.toHexString();
    }
    
    public void updateLastInteraction() {
    	dateOfLastInteraction = new Date();
    }
    
    public Date getDateOfLastInteraction() {
    	return dateOfLastInteraction;
    }
    
}