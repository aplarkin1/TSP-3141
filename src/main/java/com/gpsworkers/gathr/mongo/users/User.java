package com.gpsworkers.gathr.mongo.users;

import org.springframework.data.mongodb.core.index.Indexed;

import com.gpsworkers.gathr.controllers.APIController;
import com.gpsworkers.gathr.exceptions.ChannelDoesntExistException;
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.Group.LOC_SEC_SETTING;
import com.gpsworkers.gathr.mongo.groups.GroupInvitation;
import com.gpsworkers.gathr.mongo.users.FriendInvitation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Alexander Larkin
 *
 * This class is the MongoDB specification for a user record
 */
@Document(collection="users")
public class User {

    private String firstName, lastName, username;

    private Location currentLocation;

    //Helps determine if a user API Token should be expired.
    private Date dateOfLastInteraction;

    private ArrayList<String> blackList;
	private ArrayList<GroupInvitation> groupInvitations;
    private ArrayList<FriendInvitation> friendInvitations;
    private HashMap<String, LOC_SEC_SETTING> groupLocationShareSec = new HashMap<>();
    public String currentLocationGroup = "";
    
  @Id
    private String email;

  //@DBRef
    private ArrayList<String> groupNames;
    //private Collection<User> friends;



    /**
     * This constructor allows for the construction of User if and only if a first name, last name, and email are given.
     * @param firstName is the given first name
     * @param lastName is the given last name
     * @param email is the given email
     */
    public User(String firstName, String lastName, String email) {
    	setEmail(email);
    	setFirstName(firstName);
    	setLastName(lastName);
    	blackList = new ArrayList<String>();
    	groupInvitations = new ArrayList<GroupInvitation>();
    	groupNames = new ArrayList<String>();
    	//friends = new ArrayList<String>();
    	friendInvitations = new ArrayList<FriendInvitation>();
    	username = email.split("@")[0];
        updateLastInteraction();
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
    	if(currentLocation == null) {
    		currentLocation = new Location();
    	}
    	currentLocation.update(longitude, latitude, elevation, country, state, city);
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

	public void addGroup( String groupName ) {
		groupNames.add( groupName );
		groupNames = new ArrayList<String>(new HashSet<>(groupNames));
	}

	public void removeGroup ( String groupName ) {
		groupNames.remove(groupName);
		groupLocationShareSec.remove(groupName);
	}

  /*
	public boolean sendMessage(String message, String groupId, String channelName) {

		String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());

		if(message == null || message.isEmpty()) {
			System.out.println("Null or empty message passed for transmission!");
			return false;
		} else if(groupId == null || groupId.isEmpty()) {
			System.out.println("Null or empty group name passed for message transmission!");
			return false;
		} else if(channelName == null || channelName.isEmpty()) {
			System.out.println("Null or empty channel name passed for message transmission!");
			return false;
		}

		for(Group group : groups) {
			try {
				if(group.getGroupName().equals(groupId) && group.getGroupCommsNetwork().getChannel(channelName) != null) {
					group.getGroupCommsNetwork().getChannel(channelName).postMessage(message, email);
					return true;
				}
			} catch (ChannelDoesntExistException e) {
				System.out.println("Channel: " + channelName + " doesn't exist!");
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("User doesn't belong to the group: " + groupId);
		return false;
	}
	*/
	public void setLocation(Location location) {
		this.currentLocation = null;
	}

	public boolean hasBlacklistedUser(String emailToCheck) {
		if(blackList.indexOf(emailToCheck) == -1) {
			return false;
		}
		return true;
	}

	public void postGroupInvite(GroupInvitation invite) {
		groupInvitations.add(invite);
	}

	public ArrayList<GroupInvitation> getGroupInvites() {
		return groupInvitations;
	}
	/*
  public Collection<User> getFriends() {
    return friends;
  }

  public void removeFriend( User user) {
    friends.remove( user );
  }
  */
  public void sendFriendRequest( FriendInvitation invite) {
    friendInvitations.add( invite );
  }

  public ArrayList<FriendInvitation> getFriendInvites() {
    return friendInvitations;
  }

  public Collection<String> getGroupNames(){
	  return groupNames;
  }
  
  public LOC_SEC_SETTING getSecuritySettingForGroup(String groupId){
	  return groupLocationShareSec.get(groupId);
  }
  
  public void setSecuritySettingForGroup(String groupId, LOC_SEC_SETTING setting){
	  groupLocationShareSec.put(groupId, setting);
  }

  public boolean isFriendOf(String email) {
	  return false;
  }
  
}
