package com.gpsworkers.gathr.mongo.groups;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.RandomStringUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpsworkers.gathr.exceptions.NotAdminException;
import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;

import java.util.ArrayList;
import java.util.Collection;

//import com.gpsworkers.gathr.mongo.communication.CommunicationNetwork;
import com.gpsworkers.gathr.mongo.users.User;
import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Amanda Erdmann
 *
 * This class is the MongoDB specification for a group record
 */
@Document(collection = "groups")
public class Group {

	//API Token used by API requests
  @Id
    private String groupName;

  @DBRef
    private Collection<User> users;
    private Collection<User> admins;
    //private CommunicationNetwork groupCommsNetwork;
  @Indexed ( unique = true )
    private String groupInvite;

    /**
     * This constructor allows for the construction of a new Group
     * @param groupName the new Group's name
     * @param user user that created the new group
     */
    public Group(String groupName) {
        users = new ArrayList<User>();
        admins = new ArrayList<User>();
        this.groupName = groupName;
        //System.out.println(users);
        //System.out.println(admins);
        //groupCommsNetwork = new CommunicationNetwork();

    }

    /**
     * gets group name
     * @return returns a string of the group name
     */
     public String getGroupName() {
       return groupName;
     }

     /**
      * sets group name if and only if user is a admin
      * @param user user that is trying to update group name
      * @param newName new group name
      */
      public void setGroupName( User user, String newName ) throws NotAdminException {
        if ( admins.contains( user ) ) {
          groupName = newName;
        } else {
          throw new NotAdminException( "user is not an admin");
        }
      }

      /**
       * returns all users in a group
       * @return a collection of all users in the group
       */
      public Collection<User> getUsers() {
        return users;
      }

      /**
       * adds a user to the group
       * @param newUser user to ba added
      */
      public void addUser( User newUser, String groupInvite ) {
        if ( !users.contains( newUser) && this.groupInvite == groupInvite ) {
          users.add( newUser );
        }
      }
      
      /**
       * adds a user to the group
       * @param newUser user to ba added
      */
      public void addUser( User newUser) {
        if ( !users.contains( newUser) && this.groupInvite == groupInvite ) {
          users.add( newUser );
        }
      }

      /**
       * removes a user from group if and only if user is a admin or the user is removing themself
       * @param user user atemepting to remove a user
       * @param removeUser user to be removed
       */
      public void removeUser ( User user, User removedUser ) throws NotAdminException {
        if ( user == removedUser ) {
          users.remove( removedUser );
        } else if( admins.contains( user )) {
          users.remove( user );
        } else {
          throw new NotAdminException( "user is not an admin");
        }
      }

      /**
       * returns all admins in group
       * @return collection of all user who are admins
       */
      public Collection<User> getAdmin() {
        return admins;
      }

      /**
       * makes another user admin if and only if the current user is an admin
       * @param user user atemepting to create admin
       * @param newAdmin user to be an admin
       */
      public void makeAdmin( User user, User newAdmin ) throws NotAdminException {
        if ( admins.contains( user )) {
          admins.add( newAdmin );
        } else {
          throw new NotAdminException( "user is not an admin");
        }
      }

      /**
       * Removes a user from admin if current user is admin and there is more than one admin
       * @param user user atemepting to remove admin
       * @param newAdmin user to be removed from admin
       */
      public void removeAdmin( User user, User removedAdmin ) throws NotAdminException {
        if ( admins.size() > 1 && admins.contains( user )) {
          admins.remove( removedAdmin );
        }
      }

      public String getGroupInvite( ) {
        return groupInvite;
      }

      private String newGroupInvite( ) {
        String str = RandomStringUtils.randomAlphanumeric(42);
        return str;
      }

      public boolean isAdmin(String email) {
    	  for(User user : admins) {
    		  if(user.getEmail().equals(email)) {
    			  return true;
    		  }
    	  }
    	  return false;
      }

      public void setGroupInvite() {
        groupInvite = newGroupInvite();
      }

      public String getGroupSummary() throws JsonProcessingException {
    	  HashMap<String, Object> summary = new HashMap<>();
    	  ArrayList<String> userEmails = new ArrayList<String>();
    	  ArrayList<String> adminEmails = new ArrayList<String>();
    	  for(User user : users) {
    		  userEmails.add(user.getEmail());
    	  }
    	  for(User user : admins) {
    		  userEmails.add(user.getEmail());
    	  }
    	  summary.put("name", groupName);
    	  summary.put("admins", adminEmails);
    	  summary.put("members", userEmails);
    	  summary.put("size", users.size());

    	  return GathrJSONUtils.write(summary);
      }
      /*
		public CommunicationNetwork getGroupCommsNetwork() {
			return groupCommsNetwork;
		}
       */
}
