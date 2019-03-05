package com.gpsworkers.gathr.mongo.groups;

import com.gpsworkers.gathr.exceptions.NotAdminException;
import java.util.Collection;
import com.gpsworkers.gathr.mongo.users.User;
import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Alexander Larkin
 *
 * This class is the MongoDB specification for a user record
 */
@Document(collection="groups")
public class Group {

	//API Token used by API requests
  @Id
    private String groupName;

  @DBRef
    private Collection<User> users;
    private Collection<User> admins;

    /**
     * This constructor allows for the construction of a new Group
     * @param groupName the new Group's name
     * @param user user that created the new group
     */
    public Group(String groupName, User user) {
        this.groupName = groupName;
        users.add( user );
        admins.add( user );
    }

    /**
     * gets group name
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
       */
      public Collection<User> getUsers() {
        return users;
      }

      /**
       * adds a user to the group
       * @param newUser user to ba added
      */
      public void addUser( User newUser) {
        if ( !users.contains( newUser) ) {
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
}
